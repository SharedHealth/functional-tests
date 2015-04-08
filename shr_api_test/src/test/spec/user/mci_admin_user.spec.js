var request = require('request');
var Entity = require('../../../../src/request/entity');
var User = require('../../../../src/data/user' );

var SSORequest = Entity.SSORequest;
var Patient = require('../../../../src/entity/patient');
var PatientRequest = Entity.PatientRequest;
var CatchmentRequest = Entity.CatchmentRequest;

describe("MCI Admin User", function () {
    var facility_user = new User('facility');
    var user = new User('mciAdmin')
    var hid = "";
    var nid = ""
    var binBrn = "";
    var confidential_patient_hid = "";
    var mci_admin_user = it;

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

    describe("Execute all MCI APIs for mci admin user", function () {
        var patientRequest;
        patientRequest = new PatientRequest(user);

        mci_admin_user("Should not be able to create patient", function (done) {
            request(new PatientRequest(user, new Patient()).post(), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(body.message).to.equal("Access is denied");
                done();

            });
        });

        mci_admin_user("Should be able to view patient By Hid", function (done) {
            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(200);
                expect(JSON.parse(body).hid).to.equal(hid);
                done();
            });
        });

        mci_admin_user("Should be able to view patient By Nid", function (done) {
            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                nid = JSON.parse(body).nid
                request(patientRequest.getPatientDetailsByNid(nid), function (err, res, body) {
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).results[0].nid).to.equal(nid);
                    done();
                });
            });
        });

        mci_admin_user("Should be able to view patient By BinBrn", function (done) {
            request(patientRequest.getPatientDetailsByHid(hid), function (err, res, body) {
                binBrn = JSON.parse(body).bin_brn
                request(patientRequest.getPatientDetailsByBinBrn(binBrn), function (err, res, body) {
                    expect(res.statusCode).to.equal(200);
                    expect(JSON.parse(body).results[0].bin_brn).to.equal(binBrn);
                    done();

                });
            });
        });

        mci_admin_user("Should not be able to download patients by catchment", function (done) {
           request(patientRequest.getAllPatientsByCatchment("302607"), function (err, res, body) {
                expect(res.statusCode).to.equal(403)
                done();
            });
        });

        mci_admin_user("Should be able to update the patient", function (done) {
            request.put(patientRequest.updatePost(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(202);
                done();
            });
        });

<<<<<<< Updated upstream
        mci_admin_user("Should be able to update the patient", function (done) {
            request.put(patientRequest.updatePost(hid), function (err, res, body) {
                expect(res.statusCode).to.equal(202);
                
                done();
            });
        });
=======
        mci_admin_user("Should not be able to view pending approval patient by catchment", function (done) {
            request.get(patientRequest.getAllPendingApprovalPatientsByCatchment("3026"), function (err, res, body) {
                expect(res.statusCode).to.equal(403);
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

>>>>>>> Stashed changes
    });


});
