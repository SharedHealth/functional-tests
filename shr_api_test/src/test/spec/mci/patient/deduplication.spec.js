var request = require('request');
var User = require('./../../../../data/user');
var Patient = require('../../../../entity/patient').PatientWithHouseHold;
var Encounter = require('../../../../entity/encounter').DefaultEncounterFeed;
var EncounterRequest = require('../../../../request/encounter').EncounterRequest;
var SSORequest = require('../../../../request/sso').SSORequest;
var CatchmentRequest = require('../../../../request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../request/patient').PatientRequest;
var fs = require("fs");

describe("Deduplication", function () {
    var userFacility = new User('facility');
    var userMciAdmin = new User('mciAdmin');
    var userMciApprover = new User('mci_approver');
    var houseHoldCode = "";
    var patient_2 = null;
    var patient_1 = null;
    var to_be_retained_patients = JSON.parse(fs.readFileSync(__dirname + "/../../../../data/duplicate_patients_to_retain.json", "utf8"));
    console.log(JSON.stringify(to_be_retained_patients));
    before(function (done) {
        request(new SSORequest(userFacility).post(), function (err, httpResponse, body) {
            userFacility.access_token = JSON.parse(httpResponse.body).access_token;
            request(new SSORequest(userMciApprover).postBy(userFacility), function (err, httpResponse, body) {
                userMciApprover.access_token = JSON.parse(httpResponse.body).access_token;
                request(new SSORequest(userMciAdmin).postBy(userFacility), function (err, httpResponse, body) {
                    userMciAdmin.access_token = JSON.parse(httpResponse.body).access_token;
                    done();
                });
            });
        });
    });

    after(function (done) {
        patient_1 = null;
        patient_2 = null;
        houseHoldCode = "";
        done();
    });

    describe("Dedup Scenarios", function () {
        var patientRequestFacility;
        var patientRequestMciAdmin;
        var patientRequestMciApprover;

        before(function (done) {
            patientRequestFacility = new PatientRequest(userFacility);
            patientRequestMciAdmin = new PatientRequest(userMciAdmin);
            patientRequestMciApprover = new PatientRequest(userMciApprover);
            done();
        });
        beforeEach(function (done) {
            done();
            //patient_1 = new Patient();
            //request(new PatientRequest(userFacility, patient_1.details).post(), function (err, res, body) {
            //    console.log(body);
            //    patient_1.hid = body.id;
            //    console.log(patient_1.hid);
            //    patient_2 = new Patient();
            //    patient_2.details.nid = patient_1.details.nid;
            //    request(new PatientRequest(userFacility, patient_2.details).post(), function (err, res, body) {
            //        console.log(body);
            //        patient_2.hid = body.id;
            //        console.log(patient_2.hid);
            //        done();
            //    });
            //});
        });

        describe("Merge", function () {
            //This test is being skipped since merge patient api is no longer used
            it.skip("Should mark a patient as inactive and merge with other patient", function (done) {
                request((patientRequestMciApprover).makePatientInactive(patient_2.hid, patient_1.hid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);
                    done();
                });
            });

            //This test is being skipped since merge patient api is no longer used
            it.skip("Should not update inactive patient status again if merged", function (done) {


                request((patientRequestMciApprover).makePatientInactive(patient_2.hid, patient_1.hid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);

                    request(patientRequestMciAdmin.updateUsingPut(patient_2.hid), function (err, res, body) {
                        console.log(body);
                        expect(res.statusCode).to.equal(400);
                        expect(body.message).to.equal("Cannot update inactive patient, already merged with " + patient_1.hid);

                        done();


                    });
                });
            });
            //This scenario should be treated as bad request and not forbidden. status check should be 400 and not 403
            //This test is being skipped since merge patient api is no longer used
            it.skip("Should fail if merged with same patient", function (done) {


                request((patientRequestMciApprover).makePatientInactive(patient_2.hid, patient_2.hid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(403);
                    expect(body.message).to.equal("Cannot merge with itself");

                    done();


                });
            });
            //This test is being skipped since merge patient api is no longer used
            it.skip("Should fail on invalid healthid for merging patient", function (done) {

                var invalidHid = patient_2.hid + 1;

                request((patientRequestMciApprover).makePatientInactive(patient_2.hid, invalidHid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(404);
                    expect(body.message).to.equal("Merge_with patient not found with health id: " + invalidHid);

                    done();


                });
            });
            //This test is being skipped since merge patient api is no longer used
            it.skip("Should fail on invalid healthid for mergeable patient", function (done) {

                var invalidHid = patient_2.hid + 1;
                request((patientRequestMciApprover).makePatientInactive(invalidHid, patient_2.hid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(404);
                    expect(body.message).to.equal("No patient found with health id: " + invalidHid);

                    done();


                });
            });
            //This test is being skipped since merge patient api is no longer used
            it.skip("Should not merge active patient with inactive patient", function (done) {

                request((patientRequestMciApprover).makePatientInactive(patient_2.hid, patient_1.hid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);
                    var newActivePatient = new Patient();
                    request(new PatientRequest(userFacility, newActivePatient.details).post(), function (err, res, body) {
                        newActivePatient.hid = body.id;
                        request((patientRequestMciApprover).makePatientInactive(newActivePatient.hid, patient_2.hid), function (err, res, body) {
                            console.log(body);
                            expect(res.statusCode).to.equal(403);
                            expect(body.message).to.equal("Cannot merge with inactive patient");
                            done();


                        });
                    });
                });

            });
        });

        describe("Retain", function () {
            it("Should retain both patients as not duplicates on match of nid", function (done) {
                console.log("Patient 1 is ..");
                console.log(to_be_retained_patients["patient_1_with_same_nid"]);
                console.log("Patient 2 is ..");
                console.log(to_be_retained_patients["patient_2_with_same_nid"]);
                request((patientRequestMciApprover).retainBoth(to_be_retained_patients["patient_2_with_same_nid"], to_be_retained_patients["patient_1_with_same_nid"]), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);
                    done();
                });
            });
            it("Should retain both patients as not duplicates on match of uid", function (done) {
                console.log("Patient 1 is ..");
                console.log(to_be_retained_patients["patient_1_with_same_uid"]);
                console.log("Patient 2 is ..");
                console.log(to_be_retained_patients["patient_2_with_same_uid"]);
                request((patientRequestMciApprover).retainBoth(to_be_retained_patients["patient_2_with_same_uid"], to_be_retained_patients["patient_1_with_same_uid"]), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);
                    done();
                });
            });
            it("Should retain both patients as not duplicates on match of binbrn", function (done) {
                console.log("Patient 1 is ..");
                console.log(to_be_retained_patients["patient_1_with_same_binbrn"]);
                console.log("Patient 2 is ..");
                console.log(to_be_retained_patients["patient_2_with_same_binbrn"]);
                request((patientRequestMciApprover).retainBoth(to_be_retained_patients["patient_2_with_same_binbrn"], to_be_retained_patients["patient_1_with_same_binbrn"]), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);
                    done();
                });
            });
            it("Should retain both patients as not duplicates on match of name and address", function (done) {
                console.log("Patient 1 is ..");
                console.log(to_be_retained_patients["patient_1_with_matching_name_and_address"]);
                console.log("Patient 2 is ..");
                console.log(to_be_retained_patients["patient_2_with_matching_name_and_address"]);
                request((patientRequestMciApprover).retainBoth(to_be_retained_patients["patient_2_with_matching_name_and_address"], to_be_retained_patients["patient_1_with_matching_name_and_address"]), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);
                    done();
                });
            });
        });

        describe("Update", function () {
            //This test is being skipped since merge patient api is no longer used
            it.skip("Should not update inactive patient status again even if not merged", function (done) {
                request((patientRequestMciApprover).makePatientInactive(patient_2.hid, patient_1.hid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);
                    request(patientRequestMciAdmin.updateUsingPut(patient_2.hid), function (err, res, body) {
                        console.log(body);
                        expect(res.statusCode).to.equal(400);
                        expect(body.message).to.equal("Cannot update inactive patient, already merged with " + patient_1.hid);

                        done();


                    });
                });
            });
        });

        describe("Deactivate without merge", function () {
            //This test is being skipped since merge patient api is no longer used
            it.skip("Should mark a patient as inactive and do not merge with any other patient", function (done) {
                //We are able to merge with undefined active patient this is a bug
                //To reproduce do not pass activePatientDetails.hid
                request((patientRequestMciApprover).makePatientInactive(patient_2.hid, patient_1.hid), function (err, res, body) {
                    console.log(body);


                    expect(res.statusCode).to.equal(202);

                    request(patientRequestMciAdmin.getPatientDetailsByHid(patient_2.hid), function (err, res, body) {
                        console.log(body);
                        expect(res.statusCode).to.equal(200);
                        expect(JSON.parse(body).hid).to.equal(patient_2.hid);
                        expect(JSON.parse(body).active).to.be.false;
                        done();

                    });
                });
            });
            //This test is being skipped since merge patient api is no longer used
            it.skip("Should not reverse inactive patient status", function (done) {


                request((patientRequestMciApprover).makePatientInactive(patient_2.hid, patient_1.hid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);
                    request(patientRequestMciAdmin.updateActiveFieldUsingPut(patient_2.hid), function (err, res, body) {
                        console.log(body);
                        expect(res.statusCode).to.equal(400);
                        expect(body.message).to.equal("Cannot update active field or merge with other patient");

                        done();

                    });
                });
            });
        });

        describe("Inactive patient Search", function () {
            //This test is being skipped since merge patient api is no longer used
            it.skip("should get the inactive patient status by household_code", function (done) {
                request(patientRequestMciAdmin.getPatientDetailsByHid(patient_2.hid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(200)
                    houseHoldCode = JSON.parse(body).household_code
                    request((patientRequestMciApprover).makePatientInactive(patient_2.hid, patient_1.hid), function (err, res, body) {
                        console.log(body);
                        expect(res.statusCode).to.equal(202);
                        request(patientRequestMciAdmin.getPatientDetailsHouseHoldCode(houseHoldCode), function (err, res, body) {
                            console.log(body);
                            expect(res.statusCode).to.equal(200);
                            expect(JSON.parse(body).results[0].hid).to.equal(patient_2.hid);
                            expect(JSON.parse(body).results[0].active).to.be.false;
                            expect(JSON.parse(body).results[0].merged_with).to.equal(patient_1.hid);
                            done();
                        });
                    });
                });
            });
            //This test is being skipped since merge patient api is no longer used
            it.skip("Should show inactive status and merged details or request", function (done) {
                request((patientRequestMciApprover).makePatientInactive(patient_2.hid, patient_1.hid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);
                    request(patientRequestMciAdmin.getPatientDetailsByHid(patient_2.hid), function (err, res, body) {
                        console.log(body);
                        expect(res.statusCode).to.equal(200);
                        expect(JSON.parse(body).hid).to.equal(patient_2.hid);
                        expect(JSON.parse(body).active).to.be.false;
                        expect(JSON.parse(body).merged_with).to.equal(patient_1.hid);
                        done();

                    });
                });
            });
        });

        describe("Pending Approval", function () {
            //This test is being skipped since merge patient api is no longer used
            it.skip("Patient should be removed from pending approval list if patient is mark inactive", function (done) {


                request((patientRequestFacility).updateUsingPut(patient_2.hid), function (err, res, body) {
                    expect(res.statusCode).to.equal(202);
                    request((patientRequestMciApprover).makePatientInactive(patient_2.hid, patient_1.hid), function (err, res, body) {
                        console.log(body);
                        expect(res.statusCode).to.equal(202);
                        request(patientRequestMciApprover.acceptRequest(userMciApprover.catchment, patient_2.hid), function (err, res, body) {
                            console.log(body);
                            expect(res.statusCode).to.equal(403);
                            expect(body.message).to.equal("patient is already marked inactive");
                            done();
                        });
                    });
                });
            });
            //This test is being skipped since merge patient api is no longer used

            it.skip("Should not be displayed in pending approval details after patient mark inactive", function (done) {


                request((patientRequestFacility).updateUsingPut(patient_2.hid), function (err, res, body) {
                    expect(res.statusCode).to.equal(202);
                    request((patientRequestMciApprover).makePatientInactive(patient_2.hid, patient_1.hid), function (err, res, body) {
                        console.log(body);
                        expect(res.statusCode).to.equal(202);
                        request(patientRequestMciApprover.getAllPendingApprovalDetailsByHid(userMciApprover.catchment, patient_2.hid), function (err, res, body) {
                            console.log(body);
                            expect(res.statusCode).to.equal(403);
                            expect(JSON.parse(body).message).to.equal("patient is already marked inactive");
                            done();
                        });
                    });
                });
            });
        });

        describe("Audit Log", function () {

            it.skip("Get the Audit Log for Inactive Patient data by hid", function (done) {

                request((patientRequestMciApprover).makePatientInactive(patient_2.hid, patient_1.hid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);
                    request(patientRequestMciAdmin.getPatientDetailsByHid(patient_2.hid), function (err, res, body) {
                        console.log(body);
                        expect(res.statusCode).to.equal(200);
                        expect(JSON.parse(body).hid).to.equal(patient_2.hid);
                        expect(JSON.parse(body).active).to.be.false;
                        expect(JSON.parse(body).merged_with).to.equal(patient_1.hid);

                        request(patientRequestMciAdmin.getAuditLogsByHid(patient_2.hid), function (err, res, body) {
                            console.log(body);
                            console.log(JSON.parse(body).updates[0].change_set.active);
                            expect(res.statusCode).to.equal(200);
//                      expect(JSON.parse(body).hid).to.equal(inactivePatientDetails.hid);
//                      expect(JSON.parse(body).active).to.be.false;
//                      expect(JSON.parse(body).merged_with).to.equal(activePatientDetails.hid);
                            done();

                        });
                    });
                });
            });
        });

    });
});
