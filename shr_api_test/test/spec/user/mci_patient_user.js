var request = require('request');
var User = require('../../../src/user');

var SSORequest = require('../../../src/request/SSORequest');
var Patient = require('../../../src/type/patient');
var PatientRequest = require('../../../src/request/patientRequest');
var CatchmentRequest = require('../../../src/request/CatchmentRequest');

describe("Mci patient user", function () {
    var facility_user = new User('facility');
    var mciPatient_user = new User('patient')
    var hid = "";
    var nid = ""
    var binBrn = "";
    var confidential_patient_hid = "";

    before(function (done) {
        request.post(new SSORequest(facility_user).post(), function (err, httpResponse, body) {
            facility_user.access_token = JSON.parse(httpResponse.body).access_token;
            request.post(new PatientRequest(facility_user, new Patient()).post(), function (err, res, body) {
                hid = body.id;
                //   console.dir("hid: "+hid);
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

        request.post(new SSORequest(mciPatient_user).postBy(facility_user), function (err, httpResponse, body) {
            mciPatient_user.access_token = JSON.parse(httpResponse.body).access_token;

            //console.dir("M: "+mciAdmin_user.access_token)

        });

        var patientRequest;
        patientRequest = new PatientRequest(mciPatient_user);


        it("Should not be able to create patient", function (done) {
            request.post(new PatientRequest(mciPatient_user, new Patient()).post(), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(body.message).to.equal("Access is denied");
                done();

            });
        });

        it("Should not be able to view other patient By Hid", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(JSON.parse(body).message).to.equal("Access is denied to user " + mciPatient_user.client_id + " for patient with healthId : " + hid);
                done();

            });
        });

        it("Should be able to view patient By Hid", function (done) {
            var patientHid = mciPatient_user.hid;
            request.get(patientRequest.getPatientDetailsByHid(patientHid), patientRequest.getHeaders(), function (err, res, body) {
                expect(res.statusCode).to.equal(200);
                expect(JSON.parse(body).hid).to.equal(patientHid);
                done();

            });
        });


        it("Should not be able to view patient By Nid", function (done) {
            var patientHid = mciPatient_user.hid;
            request.get(patientRequest.getPatientDetailsByHid(patientHid), patientRequest.getHeaders(), function (err, res, body) {
                nid = JSON.parse(body).nid
                // console.dir(nid)

                request.get(patientRequest.getPatientDetailsByNid(nid), patientRequest.getHeaders(), function (err, res, body) {
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");

                    done();

                });
            });
        });
        it("Should not be able to view patient By BinBrn", function (done) {
            var patientHid = mciPatient_user.hid;
            request.get(patientRequest.getPatientDetailsByHid(patientHid), patientRequest.getHeaders(), function (err, res, body) {

                binBrn = JSON.parse(body).bin_brn
                //    console.dir(binBrn)

                request.get(patientRequest.getPatientDetailsByBinBrn(binBrn), patientRequest.getHeaders(), function (err, res, body) {
                    expect(res.statusCode).to.equal(403);
                    expect(JSON.parse(body).message).to.equal("Access is denied");
                    ;

                    done();

                });
            });
        });

        it("Should not be able to download patient by Catchment", function (done) {
            request.get(patientRequest.getAllPatientsByCatchment("302607"), patientRequest.getHeaders(), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                done();
            });
        });

        it("Should not be able to update the patient", function (done) {
            request.put(patientRequest.updatePost(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                done();
            });
        });

    });


});
