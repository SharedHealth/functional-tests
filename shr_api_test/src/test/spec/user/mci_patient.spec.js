var request = require('request');
var User = require('../../../../src/data/user' );
var Entity = require('../../../../src/request/entity');
var SSORequest = Entity.SSORequest;
var Patient = require('../../../../src/entity/patient');
var PatientRequest = Entity.PatientRequest;
var CatchmentRequest = Entity.CatchmentRequest;

describe('MCI Patient User', function () {
    var facility_user = new User('facility');
    var mciPatient_user = new User('patient')
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

        request(new SSORequest(mciPatient_user).postBy(facility_user), function (err, httpResponse, body) {
            mciPatient_user.access_token = JSON.parse(httpResponse.body).access_token;
        });

        var patientRequest;
        patientRequest = new PatientRequest(mciPatient_user);
        mci_patient_user("Should not be able to create patient", function (done) {
            request(new PatientRequest(mciPatient_user, new Patient()).post(), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(body.message).to.equal("Access is denied");
                done();

            });
        });

        mci_patient_user("Should not be able to view other patient By Hid", function (done) {
            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(JSON.parse(body).message).to.equal("Access is denied to user " + mciPatient_user.client_id + " for patient with healthId : " + hid);
                done();
            });
        });

        mci_patient_user("Should be able to view patient By Hid", function (done) {
            var patientHid = mciPatient_user.hid;
            request.get(patientRequest.getPatientDetailsByHid(patientHid), function (err, res, body) {
                expect(res.statusCode).to.equal(200);
                expect(JSON.parse(body).hid).to.equal(patientHid);
                done();
            });
        });

        mci_patient_user("Should not be able to view patient By Nid", function (done) {
            var patientHid = mciPatient_user.hid;
            request.get(patientRequest.getPatientDetailsByHid(patientHid), function (err, res, body) {
                nid = JSON.parse(body).nid
                request(patientRequest.getPatientDetailsByNid(nid), function (err, res, body) {
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    done();
                });
            });
        });
        mci_patient_user("Should not be able to view patient By BinBrn", function (done) {
            var patientHid = mciPatient_user.hid;
            request.get(patientRequest.getPatientDetailsByHid(patientHid), function (err, res, body) {
                binBrn = JSON.parse(body).bin_brn
                request(patientRequest.getPatientDetailsByBinBrn(binBrn), function (err, res, body) {
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
            request.put(patientRequest.updatePost(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                done();
            });
        });
    });
});
