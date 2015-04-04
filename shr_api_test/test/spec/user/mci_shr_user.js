var request = require('request');
var User = require('../../../src/user');

var SSORequest = require('../../../src/request/SSORequest');
var Patient = require('../../../src/type/patient');
var PatientRequest = require('../../../src/request/patientRequest');
var CatchmentRequest = require('../../../src/request/CatchmentRequest');

describe("MCI SHR User", function () {
    var facility_user = new User('facility');
    var mciShr_user = new User('shr')
    var hid = "";
    var nid = ""
    var binBrn = "";
    var confidential_patient_hid = "";
    var mci_shr_user = it;
    
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
    describe("Execute all MCI APIs for mci SHR user", function () {
        request.post(new SSORequest(mciShr_user).postBy(mciShr_user), function (err, httpResponse, body) {
            mciShr_user.access_token = JSON.parse(httpResponse.body).access_token;
        });

        var patientRequest;
        patientRequest = new PatientRequest(mciShr_user);
        mci_shr_user("Should not be able to create patient", function (done) {
            request.post(new PatientRequest(mciShr_user, new Patient()).post(), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(body.message).to.equal("Access is denied");
                done();
            });
        });

        mci_shr_user("Should be able to view patient By Hid", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                expect(res.statusCode).to.equal(200);
                expect(JSON.parse(body).hid).to.equal(hid);
                done();
            });
        });

        mci_shr_user("Should not be able to view patient By Nid", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                nid = JSON.parse(body).nid
                request.get(patientRequest.getPatientDetailsByNid(nid), patientRequest.getHeaders(), function (err, res, body) {
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();
                });
            });
        });

        mci_shr_user("Should not be able to view patient By BinBrn", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                binBrn = JSON.parse(body).bin_brn
                request.get(patientRequest.getPatientDetailsByBinBrn(binBrn), patientRequest.getHeaders(), function (err, res, body) {
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();
                });
            });
        });

        mci_shr_user("Should not be able to download patients by catchment", function (done) {
            request.get(patientRequest.getAllPatientsByCatchment(mciShr_user.catchment), patientRequest.getHeaders(), function (err, res, body) {
                expect(res.statusCode).to.equal(200)
                done();
            });
        });

        mci_shr_user("Should not be able to update the patient", function (done) {
            request.put(patientRequest.updatePost(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                done();
            });
        });
    });
});
