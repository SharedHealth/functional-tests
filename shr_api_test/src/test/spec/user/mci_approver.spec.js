var request = require('request');
var User = require('../../../../src/data/user' );
var Patient = require('../../../../src/entity/patient').PatientWithHouseHold;
var Encounter = require('../../../../src/entity/encounter').DefaultEncounterFeed;
var EncounterRequest = require('../../../../src/request/encounter').EncounterRequest;
var SSORequest = require('../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../src/request/patient').PatientRequest;

describe("MCI Approver User", function () {
    var facility_user = new User('facility');
    var user = new User('mci_approver')
    var hid = "";
    var nid = "";
    var binBrn = "";
    var confidential_patient_hid = "";
    var mci_approver = it;

    before(function (done) {
        request(new SSORequest(facility_user).post(), function (err, res, body) {
            console.log(body);
            expect(res.statusCode).to.equal(200);
            facility_user.access_token = JSON.parse(res.body).access_token;
            request(new PatientRequest(facility_user, new Patient()).post(), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(201);
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

    describe("Execute all MCI APIs for mci approver user", function () {
        var patientRequest;
        var patientUpdateRequest;

        before(function (done) {
            request(new SSORequest(user).postBy(facility_user), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200);
                user.access_token = JSON.parse(res.body).access_token;
                patientRequest = new PatientRequest(user);
                patientUpdateRequest = new PatientRequest(facility_user);
                done();
            });

        });

        mci_approver("Should not be able to create patient", function (done) {
            request(new PatientRequest(user, new Patient()).post(), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(403);
                expect(body.message).to.equal("Access is denied");
                done();

            });
        });

        mci_approver("Should be able to view patient By Hid", function (done) {
            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200);
                expect(JSON.parse(body).hid).to.equal(hid);
                done();

            });
        });


        mci_approver("Should not be able to view patient By Nid", function (done) {
            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200);
                nid = JSON.parse(body).nid
                request(patientRequest.getPatientDetailsByNid(nid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();

                });
            });
        });
        mci_approver("Should not be able to view patient By BinBrn", function (done) {
            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200);
                binBrn = JSON.parse(body).bin_brn
                request(patientRequest.getPatientDetailsByBinBrn(binBrn), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();

                });
            });
        });

        mci_approver("Should not be able to view patient By houseHoldCode", function (done) {
            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200);
                houseHoldCode = JSON.parse(body).household_code
                request(patientRequest.getPatientDetailsHouseHoldCode(houseHoldCode), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();

                });
            });
        });

        mci_approver("Should not be able to view patient By Name & Location", function (done) {
            var given_name;
            var sur_name;
            var division_id;
            var district_id;
            var upazila_id;
            var address;

            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200);
                given_name = JSON.parse(body).given_name;
                sur_name = JSON.parse(body).sur_name;
                division_id = JSON.parse(body).present_address.division_id;
                district_id = JSON.parse(body).present_address.district_id;
                upazila_id = JSON.parse(body).present_address.upazila_id;
                address = "" + division_id + district_id + upazila_id;
                request(patientRequest.getPatientDetailsByNameLocation(given_name, sur_name, address), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();

                });
            });
        });

        mci_approver("Should not be able to download patients by catchment", function (done) {
            request(patientRequest.getAllPatientsByCatchment(user.catchment), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(403)
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

        mci_approver("Should not be able to update the patient", function (done) {
            request((patientRequest).updateUsingPut(hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(403);
                expect(body.message).to.equal("Access is denied");
                done();
            });
        });
        mci_approver("Should be able to view pending approval patient by catchment", function (done) {
            request(patientRequest.getAllPendingApprovalPatientsByCatchment(user.catchment), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200);
                done();
            });
        });

        mci_approver("Should be able to view pending approval details for patient by hid", function (done) {
            request(patientRequest.getAllPendingApprovalDetailsByHid(user.catchment, hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200);
                done();
            });
        });

        mci_approver("Should be able to accept pending approval for patient", function (done) {

            request((patientUpdateRequest).updateUsingPut(hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(202);
                request(patientRequest.acceptRequest("302607", hid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);
                    done();
                });
            });
        });

        mci_approver("Should be able to reject pending approval for patient", function (done) {
            request(patientUpdateRequest.multipleUpdateUsingPut(hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(202);
                request(patientRequest.multipleRequestReject("302607", hid), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(202);
                    done();
                });
            });
        });
        mci_approver("Should not be able to get the audit log details for the  patients", function (done) {
            request(patientRequest.getAuditLogsByHid(hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(403)
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

        mci_approver("Should be able to get shr feed for the  patients", function (done) {
            request(patientRequest.getUpdateFeedForSHR(hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(403);
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

        mci_approver("Should not be able to get the location details", function (done) {
            request(patientRequest.getLocationDetails(user.catchment), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200)
                done();
            });
        })
    });
});
