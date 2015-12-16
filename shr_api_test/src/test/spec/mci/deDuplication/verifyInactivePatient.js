var request = require('request');
var User = require('../../../../../src/data/user');
var Patient = require('../../../../../src/entity/patient').PatientWithHouseHold;
var Encounter = require('../../../../../src/entity/encounter').DefaultEncounterFeed;
var EncounterRequest = require('../../../../../src/request/encounter').EncounterRequest;
var SSORequest = require('../../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../../src/request/patient').PatientRequest;


describe("de duplication- test", function () {
    var userFacility = new User('facility');
    var userMciAdmin = new User('mciAdmin');
    var userMciApprover = new User('mci_approver');
    var activeHid = "";
    var inActiveHid="";
    var houseHoldCode="";
    var deDuplication = it;

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
        activeHid = "";
        inActiveHid="";
        houseHoldCode="";
        done();
    });

    describe("MCI APIs", function () {
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
            request(new PatientRequest(userFacility, new Patient()).post(), function (err, res, body) {
                console.log(body);
                activeHid = body.id;
                console.log(activeHid);
                request(new PatientRequest(userFacility, new Patient()).post(), function (err, res, body) {
                    console.log(body);
                    inActiveHid = body.id;
                    console.log(inActiveHid);
                    done();
                });
            });
        });




        deDuplication("Mark the Patient Inactive & merge with other patient", function (done) {

            request((patientRequestMciApprover).makePatientInactive(inActiveHid, activeHid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(202);
                    done();
            });
        });

        deDuplication("Mark the Patient Inactive & do not merge with any patient", function (done) {
            request((patientRequestMciApprover).makePatientInactive(inActiveHid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(202);
                request(patientRequestMciAdmin.getPatientDetailsByHid(inActiveHid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).hid).to.equal(inActiveHid);
                    expect(JSON.parse(body).active).to.be.false;
                    done();

                });
            });
        });

        deDuplication("Get the Inactive Patient data by hid", function (done) {



            request((patientRequestMciApprover).makePatientInactive(inActiveHid, activeHid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(202);
                request(patientRequestMciAdmin.getPatientDetailsByHid(inActiveHid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).hid).to.equal(inActiveHid);
                    expect(JSON.parse(body).active).to.be.false;
                    expect(JSON.parse(body).merged_with).to.equal(activeHid);
                    done();

                });
            });
        });

        deDuplication("Get the Inactive Patient data by householdcode", function (done) {


            request(patientRequestMciAdmin.getPatientDetailsByHid(inActiveHid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200)
                houseHoldCode = JSON.parse(body).household_code
                request((patientRequestMciApprover).makePatientInactive(inActiveHid, activeHid), function (err, res, body) {
                   console.log(body);
                   expect(res.statusCode).to.equal(202);
                   request(patientRequestMciAdmin.getPatientDetailsHouseHoldCode(houseHoldCode), function (err, res, body) {
                      console.log(body);
                      expect(res.statusCode).to.equal(200);
                      expect(JSON.parse(body).results[0].hid).to.equal(inActiveHid);
                      expect(JSON.parse(body).results[0].active).to.be.false;
                      expect(JSON.parse(body).results[0].merged_with).to.equal(activeHid);
                     done();
                   });
                });
            });
        });

        deDuplication("Should not be able to update the inactive patient which is already merged with other patient", function (done) {


            request((patientRequestMciApprover).makePatientInactive(inActiveHid, activeHid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(202);

                request(patientRequestMciAdmin.updateUsingPut(inActiveHid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(400);
                    expect(body.message).to.equal("Cannot update inactive patient, already merged with " + activeHid);

                    done();


                });
            });
        });

        deDuplication("Should not be able to update the inactive patient which is not merged with any patient", function (done) {


            request((patientRequestMciApprover).makePatientInactive(inActiveHid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(202);
                request(patientRequestMciAdmin.updateUsingPut(inActiveHid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(400);
                    expect(body.message).to.equal("Cannot update inactive patient");

                    done();


                });
            });
        });

        //This scenario should be treated as bad request and not forbidden. status check should be 400 and not 403
        deDuplication("Should not be able to merge with his own patient hid", function (done) {



            request((patientRequestMciApprover).makePatientInactive(inActiveHid, inActiveHid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(403);
                expect(body.message).to.equal("Cannot merge with itself");

                done();


            });
        });

        deDuplication("Should not be able to merge with invalid patient hid", function (done) {

            var invalidHid=inActiveHid+1;

            request((patientRequestMciApprover).makePatientInactive(inActiveHid, invalidHid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(404);
                expect(body.message).to.equal("Merge_with patient not found with health id: "+invalidHid);

                done();


            });
        });
        deDuplication("patient which is going to mark inactive should not have invalid id", function (done) {

            var invalidHid=inActiveHid+1;
            request((patientRequestMciApprover).makePatientInactive(invalidHid, inActiveHid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(404);
                expect(body.message).to.equal("No patient found with health id: "+invalidHid);

                done();


            });
        });

        deDuplication("Active Patient should not be merge with inactive patient", function (done) {

            request((patientRequestMciApprover).makePatientInactive(inActiveHid, activeHid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(202);
                request(new PatientRequest(userFacility, new Patient()).post(), function (err, res, body) {
                    var newActiveHid = body.id;
                    request((patientRequestMciApprover).makePatientInactive(newActiveHid, inActiveHid), function (err, res, body) {
                        console.log(body);
                        expect(res.statusCode).to.equal(403);
                        expect(body.message).to.equal("Cannot merge with inactive patient");
                        done();


                    });
                });
            });

            });

        deDuplication("Should not be able to make inactive patient active again", function (done) {


                request((patientRequestMciApprover).makePatientInactive(inActiveHid, activeHid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);
                    request(patientRequestMciAdmin.updateActiveFieldUsingPut(inActiveHid), function (err, res, body) {
                        console.log(body);
                        expect(res.statusCode).to.equal(400);
                        expect(body.message).to.equal("Cannot update active field or merge with other patient");

                        done();

                    });
                });
            });
        deDuplication("Patient should be removed from pending approval list if patient is mark inactive", function (done) {


                request((patientRequestFacility).updateUsingPut(inActiveHid), function (err, res, body) {
                    expect(res.statusCode).to.equal(202);
                    request((patientRequestMciApprover).makePatientInactive(inActiveHid, activeHid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);
                        request(patientRequestMciApprover.acceptRequest(userMciApprover.catchment, inActiveHid), function (err, res, body) {
                        console.log(body);
                        expect(res.statusCode).to.equal(403);
                        expect(body.message).to.equal("patient is already marked inactive");

                            done();
                        });
                    });
                });
            });

        deDuplication("Should not be display the pending approval details after patient mark inactive", function (done) {


                request((patientRequestFacility).updateUsingPut(inActiveHid), function (err, res, body) {
                    expect(res.statusCode).to.equal(202);
                    request((patientRequestMciApprover).makePatientInactive(inActiveHid, activeHid), function (err, res, body) {
                        console.log(body);
                        expect(res.statusCode).to.equal(202);
                        request(patientRequestMciApprover.getAllPendingApprovalDetailsByHid(userMciApprover.catchment, inActiveHid), function (err, res, body) {
                          console.log(body);
                          expect(res.statusCode).to.equal(403);
                          expect(JSON.parse(body).message).to.equal("patient is already marked inactive");
                          done();
                        });
                    });
                });
        });


        deDuplication("Get the Audit Log for Inactive Patient data by hid", function (done) {

            request((patientRequestMciApprover).makePatientInactive(inActiveHid, activeHid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(202);
                request(patientRequestMciAdmin.getPatientDetailsByHid(inActiveHid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).hid).to.equal(inActiveHid);
                    expect(JSON.parse(body).active).to.be.false;
                    expect(JSON.parse(body).merged_with).to.equal(activeHid);

                    request(patientRequestMciAdmin.getAuditLogsByHid(inActiveHid), function (err, res, body) {
                        console.log(body);
                        console.log(JSON.parse(body).updates[0].change_set.active);
                      expect(res.statusCode).to.equal(200);
//                      expect(JSON.parse(body).hid).to.equal(inActiveHid);
//                      expect(JSON.parse(body).active).to.be.false;
//                      expect(JSON.parse(body).merged_with).to.equal(activeHid);
                        done();

                    });
                });
            });
        });
    });
});
