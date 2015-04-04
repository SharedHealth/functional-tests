var request = require('request');
var User = require('../../../../src/data/user' );

var SSORequest = require('../../../../src/request/SSORequest');
var Patient = require('../../../../src/entity/patient');
var PatientRequest = require('../../../../src/request/patientRequest');
var CatchmentRequest = require('../../../../src/request/CatchmentRequest');

describe("MCI Approver User", function () {
    var facility_user = new User('facility');
    var mciApprover_user = new User('mci_approver')
    var hid = "";
    var nid = ""
    var binBrn = "";
    var confidential_patient_hid = "";
    var mci_approver = it;

    before(function (done) {
        request.post(new SSORequest(facility_user).post(), function (err, httpResponse, body) {
            facility_user.access_token = JSON.parse(httpResponse.body).access_token;
            request.post(new PatientRequest(facility_user, new Patient()).post(), function (err, res, body) {
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

        request.post(new SSORequest(mciApprover_user).postBy(facility_user), function (err, httpResponse, body) {
            mciApprover_user.access_token = JSON.parse(httpResponse.body).access_token;


        });

        var patientRequest;
        patientRequest = new PatientRequest(mciApprover_user);


        mci_approver("Should not be able to create patient", function (done) {
            request.post(new PatientRequest(mciApprover_user, new Patient()).post(), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(body.message).to.equal("Access is denied");
                done();

            });
        });

        mci_approver("Should be able to view patient By Hid", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                expect(res.statusCode).to.equal(200);
                expect(JSON.parse(body).hid).to.equal(hid);
                done();

            });
        });


        mci_approver("Should not be able to view patient By Nid", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                nid = JSON.parse(body).nid
                request.get(patientRequest.getPatientDetailsByNid(nid), patientRequest.getHeaders(), function (err, res, body) {
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();

                });
            });
        });
        mci_approver("Should not be able to view patient By BinBrn", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                binBrn = JSON.parse(body).bin_brn
                request.get(patientRequest.getPatientDetailsByBinBrn(binBrn), patientRequest.getHeaders(), function (err, res, body) {
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();

                });
            });
        });

        mci_approver("Should not be able to download patients by catchment", function (done) {
            request.get(patientRequest.getAllPatientsByCatchment(mciApprover_user.catchment), patientRequest.getHeaders(), function (err, res, body) {
                expect(res.statusCode).to.equal(403)
                done();
            });
        });

        mci_approver("Should not be able to update the patient", function (done) {
            request.put(patientRequest.updatePost(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                done();
            });
        });

    });
});
