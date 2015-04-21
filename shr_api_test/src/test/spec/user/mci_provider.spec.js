var request = require('request');
var User = require('../../../../src/data/user');
var Patient = require('../../../../src/entity/patient').PatientWithHouseHold;
var Encounter = require('../../../../src/entity/encounter').DefaultEncounterFeed;
var EncounterRequest = require('../../../../src/request/encounter').EncounterRequest;
var SSORequest = require('../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../src/request/patient').PatientRequest;

describe("MCI Provider User", function () {
    var user = new User('provider');
    var hid = "";
    var nid = "";
    var binBrn = "";
    var confidential_patient_hid = "";
    var mci_provider_user = it;

    before(function (done) {
        request(new SSORequest(user).post(), function (err, httpResponse, body) {
            console.log(body);
            user.access_token = JSON.parse(httpResponse.body).access_token;
            request(new PatientRequest(user, new Patient()).post(), function (err, res, body) {
                console.log(body);
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

    describe("Execute all MCI APIs for provider user", function () {
        var patientRequest;
        before(function (done) {
            patientRequest = new PatientRequest(user);
            done();
        })


        mci_provider_user("Should be able to view patient By Hid", function (done) {
            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200);
                expect(JSON.parse(body).hid).to.equal(hid);
                done();
            });
        });

        mci_provider_user("Should be able to create patient", function (done) {
            request(new PatientRequest(user, new Patient()).post(), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(201);
                done();

            });
        });
        mci_provider_user("Should be able to view patient By Nid", function (done) {
            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                console.log(body);
                nid = JSON.parse(body).nid
                request(patientRequest.getPatientDetailsByNid(nid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).results[0].nid).to.equal(nid);
                    done();
                });
            });
        });
        mci_provider_user("Should be able to view patient By BinBrn", function (done) {
            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                console.log(body);
                binBrn = JSON.parse(body).bin_brn;
                request(patientRequest.getPatientDetailsByBinBrn(binBrn), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).results[0].bin_brn).to.equal(binBrn);
                    done();
                });
            });
        });

        mci_provider_user("Should be able to view patient By houseHoldCode", function (done) {
            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                console.log(body);
                houseHoldCode = JSON.parse(body).household_code
                request(patientRequest.getPatientDetailsHouseHoldCode(houseHoldCode), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).results[0].hid).to.equal(hid);
                    done();

                });
            });
        });

        mci_provider_user("Should be able to view patient By Name & Location", function (done) {
            var given_name;
            var sur_name;
            var division_id;
            var district_id;
            var upazila_id;
            var address;

            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                console.log(body);
                given_name = JSON.parse(body).given_name;
                sur_name = JSON.parse(body).sur_name;
                division_id = JSON.parse(body).present_address.division_id;
                district_id = JSON.parse(body).present_address.district_id;
                upazila_id = JSON.parse(body).present_address.upazila_id;
                address = "" + division_id + district_id + upazila_id;
                request(patientRequest.getPatientDetailsByNameLocation(given_name, sur_name, address), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).results[0].hid).to.equal(hid);
                    done();

                });
            });
        });

        mci_provider_user("Should be able to download all patient by catchment", function (done) {
            request(patientRequest.getAllPatientsByCatchment(user.catchment), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200);
                done();
            });
        });

        mci_provider_user("Should be able to update the patient", function (done) {
            request((patientRequest).updateUsingPut(hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(202);
                done();
            });
        });

        mci_provider_user("Should not be able to view pending approval details for patient by hid", function (done) {
            request(patientRequest.getAllPendingApprovalDetailsByHid(user.catchment, hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(403);
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

        mci_provider_user("Should not be able to accept pending approval for patient", function (done) {
            request((patientRequest).updateUsingPut(hid), function (err, res, body) {
                console.log(body);
                request(patientRequest.acceptRequest(user.catchment, hid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(403);
                    expect(body.message).to.equal("Access is denied");
                    done();
                });
            });
        });


        mci_provider_user("Should not be able to reject pending approval for patient", function (done) {
            request(patientRequest.updatePost(hid), function (err, res, body) {
                console.log(body);
                request(patientRequest.rejectRequest(user.catchment, hid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(403);
                    expect(body.message).to.equal("Access is denied");
                    done();
                });
            });
        });

        mci_provider_user("Should not be able to get the audit log details for the  patients", function (done) {
            request(patientRequest.getAuditLogsByHid(hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(403)
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

        mci_provider_user("Should be able to get shr feed for the  patients", function (done) {
            request(patientRequest.getUpdateFeedForSHR(hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(403);
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

        mci_provider_user("Should not be able to get the location details", function (done) {
            request(patientRequest.getLocationDetails(user.catchment), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(403)
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        })
    });
});
