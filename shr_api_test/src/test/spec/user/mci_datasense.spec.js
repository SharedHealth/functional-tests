var request = require('request');
var User = require('../../../../src/data/user' );

var SSORequest = require('../../../request/SSO');
var Patient = require('../../../../src/entity/patient');
var PatientRequest = require('../../../request/patient');
var CatchmentRequest = require('../../../request/catchment');

describe("MCI Datasense User", function () {
    var facility_user = new User('facility');
    var user = new User('datasense')
    var hid = "";
    var nid = ""
    var binBrn = "";
    var confidential_patient_hid = "";
    var mci_datasense_user = it;

    before(function (done) {
        request(new SSORequest(facility_user).post(), function (err, httpResponse, body) {
            facility_user.access_token = JSON.parse(httpResponse.body).access_token;
            request(new SSORequest(user).postBy(facility_user), function (err, httpResponse, body) {
                user.access_token = JSON.parse(httpResponse.body).access_token;
                request(new PatientRequest(facility_user, new Patient()).post(), function (err, res, body) {
                    hid = body.id;
                    done();
                });

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

    describe("Execute all MCI APIs for mci datasense user", function () {
        var patientRequest;
        patientRequest = new PatientRequest(user);

        mci_datasense_user("Should not be able to create patient", function (done) {
            request(new PatientRequest(user, new Patient()).post(), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(body.message).to.equal("Access is denied");
                done();
            });
        });

        mci_datasense_user("Should be able to view patient By Hid", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                expect(res.statusCode).to.equal(200);
                expect(JSON.parse(body).hid).to.equal(hid);
                done();

            });
        });


        mci_datasense_user("Should not be able to view patient By Nid", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                nid = JSON.parse(body).nid
                request.get(patientRequest.getPatientDetailsByNid(nid), patientRequest.getHeaders(), function (err, res, body) {
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();

                });
            });
        });

        mci_datasense_user("Should not be able to view patient By BinBrn", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                var binBrn = JSON.parse(body).bin_brn
                request.get(patientRequest.getPatientDetailsByBinBrn(binBrn), patientRequest.getHeaders(), function (err, res, body) {
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();
                });
            });
        });

        mci_datasense_user("Should be able to download all patient by catchment", function (done) {
            request.get(patientRequest.getAllPatientsByCatchment(user.catchment), patientRequest.getHeaders(), function (err, res, body) {
                expect(res.statusCode).to.equal(200);
                done();
            });
        });

        mci_datasense_user("Should not be able to update the patient", function (done) {
            request.put(patientRequest.updatePost(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                done();
            });
        });

        mci_datasense_user("Should not be able to view pending approval patient by catchment", function (done) {
            request.get(patientRequest.getAllPendingApprovalPatientsByCatchment(user.catchment), patientRequest.getHeaders(), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                done();
            });
        });
    });
});
