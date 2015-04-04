var request = require('request');
var User = require('../../../../src/data/user' );
var SSORequest = require('../../../request/SSO');
var Patient = require('../../../../src/entity/patient');
var PatientRequest = require('../../../request/patient');
var CatchmentRequest = require('../../../request/catchment');

describe("MCI Provider User", function () {
    var user = new User('provider');
    var hid = "";
    var nid = ""
    var binBrn = "";
    var confidential_patient_hid = "";
    var mci_provider_user = it;

    before(function (done) {
        request.post(new SSORequest(user).post(), function (err, httpResponse, body) {
            user.access_token = JSON.parse(httpResponse.body).access_token;
            request.post(new PatientRequest(user, new Patient()).post(), function (err, res, body) {
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
        patientRequest = new PatientRequest(user);
        mci_provider_user("Should be able to view patient By Hid", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                expect(res.statusCode).to.equal(200);
                expect(JSON.parse(body).hid).to.equal(hid);
                done();
            });
        });


        mci_provider_user("Should be able to view patient By Nid", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                nid = JSON.parse(body).nid
                request.get(patientRequest.getPatientDetailsByNid(nid), patientRequest.getHeaders(), function (err, res, body) {
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).results[0].nid).to.equal(nid);
                    done();
                });
            });
        });
        mci_provider_user("Should be able to view patient By BinBrn", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                binBrn = JSON.parse(body).bin_brn;
                request.get(patientRequest.getPatientDetailsByBinBrn(binBrn), patientRequest.getHeaders(), function (err, res, body) {
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).results[0].bin_brn).to.equal(binBrn);
                    done();
                });
            });
        });

        mci_provider_user("Should be able to download all patient by catchment", function (done) {
            request.get(patientRequest.getAllPatientsByCatchment(user.catchment), patientRequest.getHeaders(), function (err, res, body) {
                expect(res.statusCode).to.equal(200);
                done();
            });
        });

        mci_provider_user("Should be able to update the patient", function (done) {
            request.put(patientRequest.updatePost(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(202);
                done();
            });
        });

    });
});
