var request = require('request');
var User = require('../../../../src/data/user' );
var Patient = require('../../../../src/entity/patient');
var Encounter = require('../../../../src/entity/encounter');
var SSORequest = require('../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../src/request/patient').PatientRequest;

describe('MCI Patient User', function () {
    var facility_user = new User('facility');
    var user = new User('patient')
    var hid = "";
    var nid = ""
    var binBrn = "";
    var confidential_patient_hid = "";
    var mci_patient_user = it;

    before(function (done) {
        request(new SSORequest(facility_user).post(), function (err, httpResponse, body) {
            facility_user.access_token = JSON.parse(httpResponse.body).access_token;
            request(new PatientRequest(facility_user, new Patient()).post(), function (err, res, body) {
                hid = body.id;
                done();
            });
        });
    });

    after(function (done) {
        hid = "";
        nid = "";
        binBrn = "";
        confidential_patient_hid = "";
        done();
    });

    describe("Execute all MCI APIs for mci patient user", function () {
        var patientRequest;
        var patientUpdateRequest;

        before(function(done){
            request(new SSORequest(user).postBy(facility_user), function (err, httpResponse, body) {
                user.access_token = JSON.parse(httpResponse.body).access_token;
                done();
            });
            patientRequest = new PatientRequest(user);
            patientUpdateRequest = new PatientRequest(facility_user);

        })


        mci_patient_user("Should not be able to create patient", function (done) {
            request(new PatientRequest(user, new Patient()).post(), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(body.message).to.equal("Access is denied");
                done();

            });
        });

        mci_patient_user("Should not be able to view other patient By Hid", function (done) {
            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(JSON.parse(body).message).to.equal("Access is denied to user " + user.client_id + " for patient with healthId : " + hid);
                done();
            });
        });

        mci_patient_user("Should be able to view patient By his own Hid", function (done) {
            var patientHid = user.hid;
            request(patientRequest.getPatientDetailsByHid(patientHid), function (err, res, body) {
                expect(res.statusCode).to.equal(200);
                expect(JSON.parse(body).hid).to.equal(patientHid);
                done();
            });
        });

        mci_patient_user("Should not be able to view patient By Nid", function (done) {
            var patientHid = user.hid;
            request(patientRequest.getPatientDetailsByHid(patientHid), function (err, res, body) {
                nid = JSON.parse(body).nid
                request(patientRequest.getPatientDetailsByNid(nid), function (err, res, body) {
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();
                });
            });
        });
        mci_patient_user("Should not be able to view patient By BinBrn", function (done) {
            var patientHid = user.hid;
            request(patientRequest.getPatientDetailsByHid(patientHid), function (err, res, body) {
                binBrn = JSON.parse(body).bin_brn
                request(patientRequest.getPatientDetailsByBinBrn(binBrn), function (err, res, body) {
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();
                });
            });
        });

        mci_patient_user("Should be able to view patient By houseHoldCode", function (done) {
            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                houseHoldCode = JSON.parse(body).household_code
                request(patientRequest.getPatientDetailsHouseHoldCode(houseHoldCode), function (err, res, body) {
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();

                });
            });
        });

        mci_patient_user("Should not be able to view patient By Name & Location", function (done) {
            var given_name;
            var sur_name;
            var division_id;
            var district_id;
            var upazila_id;
            var address;
            var patientHid = user.hid;

            request(patientRequest.getPatientDetailsByHid(patientHid), function (err, res, body) {
                given_name = JSON.parse(body).given_name;
                sur_name = JSON.parse(body).sur_name;
                division_id = JSON.parse(body).present_address.division_id;
                district_id = JSON.parse(body).present_address.district_id;
                upazila_id = JSON.parse(body).present_address.upazila_id;
                address=""+division_id+district_id+upazila_id;
            request(patientRequest.getPatientDetailsByNameLocation(given_name,sur_name,address), function (err, res, body) {
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();

                });
            });
        });

        mci_patient_user("Should not be able to download patient by Catchment", function (done) {
           request(patientRequest.getAllPatientsByCatchment("302607"), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                done();
            });
        });

        mci_patient_user("Should not be able to update the patient", function (done) {
            request((patientRequest).updateUsingPut(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                done();
            });
        });

        mci_patient_user("Should not be able to view pending approval patient by catchment", function (done) {
            request(patientRequest.getAllPendingApprovalPatientsByCatchment("3026"), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

        mci_patient_user("Should not be able to view pending approval details for patient by hid", function (done) {
            request(patientRequest.getAllPendingApprovalDetailsByHid("3026", hid), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

        mci_patient_user("Should not be able to accept pending approval for patient", function (done) {
            request((patientUpdateRequest).updateUsingPut(hid), function (err, res, body) {
            request(patientRequest.acceptRequest("302607", hid), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(body.message).to.equal("Access is denied");
                done();
                });
            });
        });


      mci_patient_user("Should not be able to reject pending approval for patient", function (done) {
          request(patientUpdateRequest.updatePost(hid), function (err, res, body) {
          request(patientRequest.rejectRequest("302607", hid), function (err, res, body) {
              expect(res.statusCode).to.equal(403);
              expect(body.message).to.equal("Access is denied");
                done();
                });
            });
        });

        mci_patient_user("Should not be able to get the audit log details for the  patients", function (done) {
            request(patientRequest.getAuditLogsByHid(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(403)
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

        mci_patient_user("Should be able to get shr feed for the  patients", function (done) {
            request(patientRequest.getUpdateFeedForSHR(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

        mci_patient_user("Should not be able to get the location details", function (done) {
            request(patientRequest.getLocationDetails(user.catchment), function (err, res, body) {
                expect(res.statusCode).to.equal(403)
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        })
    });
});
