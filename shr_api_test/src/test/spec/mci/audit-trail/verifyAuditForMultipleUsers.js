var request = require('request');
var User = require('../../../../../src/data/user');
var Patient = require('../../../../../src/entity/patient').PatientWithHouseHold;
var Encounter = require('../../../../../src/entity/encounter');
var EncounterRequest = require('../../../../../src/request/encounter').EncounterRequest;
var SSORequest = require('../../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../../src/request/patient').PatientRequest;

describe("Audit test", function () {
    var userProvider = new User('provider');
    var userFacility = new User('facility');
    var userMciAdmin = new User('mciAdmin');
    var userMciApprover = new User('mci_approver');
    var userDatasense = new User('datasense');
    var userShr = new User('shr');
    var nid = "";
    var binBrn = "";
    var houseHoldCode = "";
    var non_confidential_patient = null;


    before(function (done) {
        request(new SSORequest(userFacility).post(), function (err, httpResponse, body) {
            userFacility.access_token = JSON.parse(httpResponse.body).access_token;
        request(new SSORequest(userMciAdmin).postBy(userFacility), function (err, httpResponse, body) {
            userMciAdmin.access_token = JSON.parse(httpResponse.body).access_token;
        request(new SSORequest(userMciApprover).postBy(userFacility), function (err, httpResponse, body) {
            userMciApprover.access_token = JSON.parse(httpResponse.body).access_token;
        request(new SSORequest(userProvider).post(), function (err, httpResponse, body) {
            userProvider.access_token = JSON.parse(httpResponse.body).access_token;
            done();
        });
        });
        });
        });
    });


    after(function (done) {
        non_confidential_patient = null;
        nid = "";
        binBrn = "";
        houseHoldCode = "";
        done();
    });

    describe("MCI APIs", function () {
        var patientRequestProvider;
        var patientRequestFacility;
        var patientRequestMciAdmin;
        var patientRequestMciApprover;
        var patientRequestDatasense;
        var patientRequestShr;


        before(function (done) {
            patientRequestProvider = new PatientRequest(userProvider);
            patientRequestFacility = new PatientRequest(userFacility);
            patientRequestMciAdmin = new PatientRequest(userMciAdmin);
            patientRequestMciApprover = new PatientRequest(userMciApprover);
            patientRequestDatasense = new PatientRequest(userDatasense);
            patientRequestShr = new PatientRequest(userShr);
            done();
        });
        beforeEach(function (done) {
            non_confidential_patient = new Patient();
            request(new PatientRequest(userFacility, non_confidential_patient.details).post(), function (err, res, body) {
                non_confidential_patient.hid = body.id;
                console.log(non_confidential_patient.hid);
                done();
            });
        });

        it("Update patient same field multiple times by facility,provider and admin and then accept it", function (done) {
            request((patientRequestFacility).updateUsingPut(non_confidential_patient.hid), function (err, res, body) {
                expect(res.statusCode).to.equal(202);
                    request((patientRequestProvider).updateUsingPut(non_confidential_patient.hid), function (err, res, body) {
                        expect(res.statusCode).to.equal(202);
                            request((patientRequestMciAdmin).updateUsingPut(non_confidential_patient.hid), function (err, res, body) {
                                expect(res.statusCode).to.equal(202);
                                    request(patientRequestMciApprover.acceptRequest("3026", non_confidential_patient.hid), function (err, res, body) {
                                        expect(res.statusCode).to.equal(202);
                                        done();
                                    });
                            });
                    });
            });
        });

        it("Update patient field by facility and accept it", function (done) {


            request((patientRequestFacility).updateUsingPut(non_confidential_patient.hid), function (err, res, body) {
                expect(res.statusCode).to.equal(202);
                    request(patientRequestMciApprover.acceptRequest(userMciApprover.catchment, non_confidential_patient.hid), function (err, res, body) {
                        expect(res.statusCode).to.equal(202);
                        done();
                    });
            });
        });

        it("Update patient field by provider and accept it", function (done) {


            request((patientRequestProvider).updateUsingPut(non_confidential_patient.hid), function (err, res, body) {
            console.log(body);
                expect(res.statusCode).to.equal(202);
                    request(patientRequestMciApprover.acceptRequest(userMciApprover.catchment, non_confidential_patient.hid), function (err, res, body) {
                        expect(res.statusCode).to.equal(202);
                        console.log(body);
                        done();
                    });
            });
        });
        it("Update patient field by Mci Admin and accept it", function (done) {


            request((patientRequestMciAdmin).updateUsingPut(non_confidential_patient.hid), function (err, res, body) {

                expect(res.statusCode).to.equal(202);
                        done();

            });
        });

        it("Update multiple field of patient by facility and accept it", function (done) {


            request((patientRequestFacility).multipleUpdateUsingPut(non_confidential_patient.hid), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(202);
                    request(patientRequestMciApprover.multipleRequestAccept(userMciApprover.catchment, non_confidential_patient.hid), function (err, res, body) {
                        expect(res.statusCode).to.equal(202);
                        done();
                    });
            });
        });

        it("Update multiple field of patient by Provider and accept it", function (done) {


            request((patientRequestProvider).multipleUpdateUsingPut(non_confidential_patient.hid), function (err, res, body) {
                expect(res.statusCode).to.equal(202);
                    request(patientRequestMciApprover.multipleRequestAccept(userMciApprover.catchment, non_confidential_patient.hid), function (err, res, body) {
                        expect(res.statusCode).to.equal(202);
                        done();
                    });
            });
        });

        it("Update multiple field of patient by Admin and accept it", function (done) {


            request((patientRequestMciAdmin).multipleUpdateUsingPut(non_confidential_patient.hid), function (err, res, body) {
                expect(res.statusCode).to.equal(202);
                        done();

            });
        });

        it("Update patient multiple field multiple times by facility,provider and admin and then accept it", function (done) {


            request((patientRequestFacility).multipleUpdateUsingPut(non_confidential_patient.hid), function (err, res, body) {

            console.log(body);

                expect(res.statusCode).to.equal(202);
                    request((patientRequestProvider).multipleUpdateUsingPut(non_confidential_patient.hid), function (err, res, body) {
                    console.log(body);
                        expect(res.statusCode).to.equal(202);
                            request((patientRequestMciAdmin).multipleUpdateUsingPut(non_confidential_patient.hid), function (err, res, body) {
                            console.log(body);
                                expect(res.statusCode).to.equal(202);
                                    request(patientRequestMciApprover.multipleRequestAccept(userMciApprover.catchment, non_confidential_patient.hid), function (err, res, body) {
                                    console.log(body);
                                        expect(res.statusCode).to.equal(202);

                                        done();
                                    });
                            });
                    });
            });
        });
    });
});
