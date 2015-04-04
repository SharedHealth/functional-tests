var request = require('request');
var User = require('../../../src/user');

var SSORequest = require('../../../src/request/SSORequest');
var Patient = require('../../../src/type/patient');
var PatientRequest = require('../../../src/request/patientRequest');
var CatchmentRequest = require('../../../src/request/CatchmentRequest');

describe("Mci admin user", function () {
    var facility_user = new User('facility');
    var mciAdmin_user = new User('mciAdmin')
    var hid = "";
    var nid = ""
    var binBrn = "";
    var confidential_patient_hid = "";

    before(function (done) {
        request.post(new SSORequest(facility_user).post(), function (err, httpResponse, body) {
            facility_user.access_token = JSON.parse(httpResponse.body).access_token;
            //console.dir("f: "+facility_user.access_token)
            request.post(new PatientRequest(facility_user, new Patient()).post(), function (err, res, body) {
                hid = body.id;
                // console.dir("hid: "+hid);
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

    describe("Execute all MCI APIs for mci admin user", function () {

        request.post(new SSORequest(mciAdmin_user).postBy(facility_user), function (err, httpResponse, body) {
            mciAdmin_user.access_token = JSON.parse(httpResponse.body).access_token;

            //console.dir("M: "+mciAdmin_user.access_token)

        });

        var patientRequest;
        patientRequest = new PatientRequest(mciAdmin_user);


        it("Should not be able to create patient", function (done) {
            request.post(new PatientRequest(mciAdmin_user, new Patient()).post(), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(body.message).to.equal("Access is denied");
                done();

            });
        });

        it("Should be able to view patient By Hid", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                expect(res.statusCode).to.equal(200);
                expect(JSON.parse(body).hid).to.equal(hid);
                done();

            });
        });


        it("Should be able to view patient By Nid", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {
                nid = JSON.parse(body).nid
                //console.dir(nid)

                request.get(patientRequest.getPatientDetailsByNid(nid), patientRequest.getHeaders(), function (err, res, body) {
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).results[0].nid).to.equal(nid);

                    done();

                });
            });
        });
        it("Should be able to view patient By BinBrn", function (done) {
            request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function (err, res, body) {

                binBrn = JSON.parse(body).bin_brn
                //  console.dir(binBrn)

                request.get(patientRequest.getPatientDetailsByBinBrn(binBrn), patientRequest.getHeaders(), function (err, res, body) {
                    //console.dir(JSON.parse(body).results)
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).results[0].bin_brn).to.equal(binBrn);

                    done();

                });
            });
        });

        it("Should not be able to download patients by catchment", function (done) {
            request.get(patientRequest.getAllPatientsByCatchment("302607"), patientRequest.getHeaders(), function (err, res, body) {
                expect(res.statusCode).to.equal(403)
                done();
            });
        });

        it("Should be able to update the patient", function (done) {
            request.put(patientRequest.updatePost(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(202);
                done();
            });
        });

    });


});
