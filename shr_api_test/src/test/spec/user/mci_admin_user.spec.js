var request = require('request');
var User = require('../../../../src/data/user' );
var Patient = require('../../../../src/entity/patient').PatientWithHouseHold;
var Encounter = require('../../../../src/entity/encounter').DefaultEncounterFeed;
var EncounterRequest = require('../../../../src/request/encounter').EncounterRequest;
var SSORequest = require('../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../src/request/patient').PatientRequest;
var util = require("util");

describe("MCI Admin User", function () {
    var facility_user = new User('facility');
    var user = new User('mciAdmin');
    var nid = "";
    var binBrn = "";
    var houseHoldCode="";
    var mci_admin_user = it;
    var non_confidential_patient = null;


    before(function (done) {
        request(new SSORequest(facility_user).post(), function (err, res, body) {
            util.log(body);
            expect(res.statusCode).to.equal(200);
            facility_user.access_token = JSON.parse(res.body).access_token;
            request(new SSORequest(user).postBy(facility_user), function (err, res, body) {
                util.log(body);
                expect(res.statusCode).to.equal(200);
                user.access_token = JSON.parse(res.body).access_token;
                non_confidential_patient = new Patient();
                request(new PatientRequest(facility_user, non_confidential_patient.details).post(), function (err, res, body) {
                    expect(res.statusCode).to.equal(201);
                    non_confidential_patient.hid = body.id;
                    done();
                });
            });
        });
    });

    after(function (done) {
        nid = "";
        binBrn = "";
        houseHoldCode="";
        non_confidential_patient = null;
        done();
    });

    describe("Execute all MCI APIs for mci admin user", function () {
        var patientRequest;
        before(function(done)
        {
            patientRequest = new PatientRequest(user);
            done();
        });


        mci_admin_user("Should not be able to create patient", function (done) {
            request(new PatientRequest(user, new Patient().details).post(), function (err, res, body) {
                util.log(body);
                expect(res.statusCode).to.equal(403);
                expect(body.message).to.equal("Access is denied");
                done();

            });
        });

        mci_admin_user("Should be able to view patient By Hid", function (done) {
            request(patientRequest.getPatientDetailsByHid(non_confidential_patient.hid), function (err, res, body) {
                util.log(body);
                expect(res.statusCode).to.equal(200);
                expect(JSON.parse(body).hid).to.equal(non_confidential_patient.hid);
                done();
            });
        });

        mci_admin_user("Should be able to view patient By Nid", function (done) {
            request(patientRequest.getPatientDetailsByHid(non_confidential_patient.hid), function (err, res, body) {
                util.log(body);
                nid = JSON.parse(body).nid;
                request(patientRequest.getPatientDetailsByNid(nid), function (err, res, body) {
                    util.log(body);
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).results[0].nid).to.equal(nid);
                    done();
                });
            });
        });

        mci_admin_user("Should be able to view patient By BinBrn", function (done) {
            request(patientRequest.getPatientDetailsByHid(non_confidential_patient.hid), function (err, res, body) {
                util.log(body);
                binBrn = JSON.parse(body).bin_brn;
                request(patientRequest.getPatientDetailsByBinBrn(binBrn), function (err, res, body) {
                    util.log(body);
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).results[0].bin_brn).to.equal(binBrn);
                    done();

                });
            });
        });

        mci_admin_user("Should be able to view patient By houseHoldCode", function (done) {
            request(patientRequest.getPatientDetailsByHid(non_confidential_patient.hid), function (err, res, body) {
                util.log(body);
                expect(res.statusCode).to.equal(200);
                houseHoldCode = JSON.parse(body).household_code;
                request(patientRequest.getPatientDetailsHouseHoldCode(houseHoldCode), function (err, res, body) {
                    util.log(body);
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).results[0].hid).to.equal(non_confidential_patient.hid);
                    done();

                });
            });
        });

        mci_admin_user("Should be able to view patient By Name & Location", function (done) {
            var given_name;
            var sur_name;
            var division_id;
            var district_id;
            var upazila_id;
            var address;
            request(patientRequest.getPatientDetailsByHid(non_confidential_patient.hid), function (err, res, body) {
                util.log(body);
                given_name = JSON.parse(body).given_name;
                sur_name = JSON.parse(body).sur_name;
                division_id = JSON.parse(body).present_address.division_id;
                district_id = JSON.parse(body).present_address.district_id;
                upazila_id = JSON.parse(body).present_address.upazila_id;
                address=""+division_id+district_id+upazila_id;
            request(patientRequest.getPatientDetailsByNameLocation(given_name,sur_name,address), function (err, res, body) {
                util.log(body);
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).results[0].hid).to.equal(non_confidential_patient.hid);
                    done();

                });
            });
        });

        mci_admin_user("Should not be able to download patients by catchment", function (done) {
           request(patientRequest.getAllPatientsByCatchment("302607"), function (err, res, body) {
               util.log(body);
               expect(res.statusCode).to.equal(403);
                done();
            });
        });

        mci_admin_user("Should be able to update the patient", function (done) {
            request(patientRequest.updateUsingPutWithGivenValues(non_confidential_patient.hid, {'gender': 'F'}), function (err, res, body) {
                util.log(body);
                expect(res.statusCode).to.equal(202);
                done();
            });
        });

        mci_admin_user("Should not be able to view pending approval patient by catchment", function (done) {
            request(patientRequest.getAllPendingApprovalPatientsByCatchment("3026"), function (err, res, body) {
                util.log(body);
                expect(res.statusCode).to.equal(403);
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

        mci_admin_user("Should not be able to view pending approval details for patient by hid", function (done) {
            request(patientRequest.getAllPendingApprovalDetailsByHid("3026", non_confidential_patient.hid), function (err, res, body) {
                util.log(body);
                expect(res.statusCode).to.equal(403);
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

        mci_admin_user("Should not be able to accept pending approval for patient", function (done) {
            request(patientRequest.updateUsingPut(non_confidential_patient.hid), function (err, res, body) {
                util.log(body);
            request(patientRequest.acceptRequest("302607", non_confidential_patient.hid), function (err, res, body) {
                util.log(body);
            expect(res.statusCode).to.equal(403);
            expect(body.message).to.equal("Access is denied");
                done();
                });
            });
        });

        mci_admin_user("Should not be able to reject pending approval for patient", function (done) {
            request(patientRequest.updateUsingPut(non_confidential_patient.hid), function (err1, res1, body1) {
                util.log(body1);
            request(patientRequest.rejectRequest("302607", non_confidential_patient.hid), function (err, res, body) {
                util.log(body);
            expect(res.statusCode).to.equal(403);
            expect(body.message).to.equal("Access is denied");
                done();
                });
            });
        });

        mci_admin_user("Should be able to get the audit log details for the  patients", function (done) {
            request(patientRequest.getAuditLogsByHid(non_confidential_patient.hid), function (err, res, body) {
                util.log(body);
                expect(res.statusCode).to.equal(200);
                done();
            });
        });
        mci_admin_user("Should be able to get shr feed for the  patients", function (done) {
            request(patientRequest.getUpdateFeedForSHR(non_confidential_patient.hid), function (err, res, body) {
                util.log(body);
            expect(res.statusCode).to.equal(403);
            expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

        mci_admin_user("Should be able to get the location details", function (done) {
            request(patientRequest.getLocationDetails(user.catchment), function (err, res, body) {
                util.log(body);
                expect(res.statusCode).to.equal(200);
                done();
            });
        })
    });
});
