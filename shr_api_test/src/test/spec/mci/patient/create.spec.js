var request = require('request');
var User = require('./../../../../../src/data/user');
var Patient = require('./../../../../../src/entity/patient').PatientWithUID;
var Encounter = require('./../../../../../src/entity/encounter').DefaultEncounterFeed;
var EncounterRequest = require('./../../../../../src/request/encounter').EncounterRequest;
var SSORequest = require('./../../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('./../../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('./../../../../../src/request/patient').PatientRequest;
var fs = require("fs");
var util = require("util");

describe("Patient Creation And Updatation", function () {

    var facility_user = new User('facility');
    var patient_list = null;

    before(function (done) {
         patient_list = JSON.parse(fs.readFileSync(__dirname + "/../../../../data/updatable_patient.json", "utf8"));
        request(new SSORequest(facility_user).post(), function (err, httpResponse, body) {
            util.log(body);
            facility_user.access_token = JSON.parse(httpResponse.body).access_token;
            done();
        });

    });

    it("Should create a new non confidential patient", function (done) {
        var patient = new Patient();
        request(new PatientRequest(facility_user, patient.details).post(), function (err, res, body) {
            util.log(body);
            expect(body.http_status).to.equal(201);
            done();
        });
    });

    //This test case is failing because cassandra takes time to update the contents.
    // CAP Theorem - Consistency is sacrificed for performance and availability
    it.skip("Should update an existing patient", function (done) {
        var patient = new Patient();
        request(new PatientRequest(facility_user, patient.details).post(), function (err, res, body) {
            util.log(body);
            expect(body.http_status).to.equal(201);
            request(new PatientRequest(facility_user, patient.details).post(), function (err, res, body) {
                util.log(body);
                expect(body.http_status).to.equal(202);
                done();
            });
        });
    });

    it("Should update an existing patient on all fields match", function (done) {
        var updatable_patient = patient_list["duplicate"];
        util.log(updatable_patient);
        request(new PatientRequest(facility_user, updatable_patient.details).post(), function (err, res, body) {
            util.log(body);
            expect(body.http_status).to.equal(202);
            expect(body.id).to.equal(updatable_patient.hid);
            done();
        });
    });
    it("Should update an existing patient if nid,uid and binbrn match", function(done){
        var similar_patient = patient_list["common_uid_nid_and_binbrn"];
        var patient = new Patient();
        util.log("Already Created Patient is ...");
        util.log(similar_patient);
        patient.details.nid = similar_patient.details.nid;
        patient.details.bin_brn = similar_patient.details.bin_brn;
        patient.details.uid = similar_patient.details.uid;
        util.log("New patient details are ...");
        util.log(patient);
        request(new PatientRequest(facility_user, patient.details).post(), function (err, res, body) {
            util.log(body);
            expect(body.http_status).to.equal(202);
            expect(body.id).to.equal(similar_patient.hid);
            done();
        });

    });
    it("Should update an existing patient if nid and binbrn match", function(done){
        var similar_patient = patient_list["common_nid_and_binbrn"];
        var patient = new Patient();
        util.log(similar_patient);
        patient.details.nid = similar_patient.details.nid;
        patient.details.bin_brn = similar_patient.details.bin_brn;
        util.log("New patient details are ...");
        util.log(patient);
        request(new PatientRequest(facility_user, patient.details).post(), function (err, res, body) {
            util.log(body);
            expect(body.http_status).to.equal(202);
            expect(body.id).to.equal(similar_patient.hid);
            done();
        });

    });
    it("Should update an existing patient if binbrn and uid match", function(done){
        var similar_patient = patient_list["common_uid_and_binbrn"];
        var patient = new Patient();
        util.log("Already Created Patient is ...");
        util.log(similar_patient);
        patient.details.bin_brn = similar_patient.details.bin_brn;
        patient.details.uid = similar_patient.details.uid;
        util.log("New patient details are ...");
        util.log(patient);
        request(new PatientRequest(facility_user, patient.details).post(), function (err, res, body) {
            util.log(body);
            expect(body.http_status).to.equal(202);
            expect(body.id).to.equal(similar_patient.hid);
            done();
        });

    });
    it("Should update an existing patient if nid and uid match", function(done){
        var similar_patient = patient_list["common_uid_and_nid"];
        var patient = new Patient();
        util.log("Already Created Patient is ...");
        util.log(similar_patient);
        patient.details.bin_brn = similar_patient.details.bin_brn;
        patient.details.nid = similar_patient.details.nid;
        util.log("New patient details are ...");
        util.log(patient);
        request(new PatientRequest(facility_user, patient.details).post(), function (err, res, body) {
            util.log(body);
            expect(body.http_status).to.equal(202);
            expect(body.id).to.equal(similar_patient.hid);
            done();
        });

    });
    it("Should create a new patient if only nid matches", function(done){
        var similar_patient = patient_list["common_nid"];
        var patient = new Patient();
        util.log("Already Created Patient is ...");
        util.log(similar_patient);
        patient.details.nid = similar_patient.details.nid;
        util.log("New patient details are ...");
        util.log(patient);
        request(new PatientRequest(facility_user, patient.details).post(), function (err, res, body) {
            util.log(body);
            expect(body.http_status).to.equal(201);
            done();
        });

    });
    it("Should create a new patient if only uid matches", function(done){
        var similar_patient = patient_list["common_uid"];
        var patient = new Patient();
        util.log("Already Created Patient is ...");
        util.log(similar_patient);
        patient.details.uid = similar_patient.details.uid;
        util.log("New patient details are ...");
        util.log(patient);
        request(new PatientRequest(facility_user, patient.details).post(), function (err, res, body) {
            util.log(body);
            expect(body.http_status).to.equal(201);
            done();
        });
        it("Should create a new patient if only binbrn matches", function(done){
            var similar_patient = patient_list["common_binbrn"];
            var patient = new Patient();
            util.log("Already Created Patient is ...");
            util.log(similar_patient);
            patient.details.bin_brn = similar_patient.details.bin_brn;
            util.log("New patient details are ...");
            util.log(patient);
            request(new PatientRequest(facility_user, patient.details).post(), function (err, res, body) {
                util.log(body);
                expect(body.http_status).to.equal(201);
                done();
            });

        });
    });

    it.only("Should create a new patient with HID Card status as registered and Update it to issued", function (done) {
        var patient = new Patient();
        request(new PatientRequest(facility_user, patient.details).post(), function (err, res, body) {
            expect(body.http_status).to.equal(201);
            var healthId = body.id;
            request(new PatientRequest(facility_user).getPatientDetailsByHid(healthId), function (err, res, body) {
                expect(res.statusCode).to.equal(200);
                _body = JSON.parse(body)
                expect(_body.hid).to.equal(healthId);
                expect(_body.hid_card_status).to.equal("REGISTERED");
                request(new PatientRequest(facility_user, patient.details).updateUsingPutWithGivenValues(healthId, {'hid_card_status': 'ISSUED'}), function (err, res, body) {
                    expect(res.statusCode).to.equal(202);
                    request(new PatientRequest(facility_user, patient.details).getPatientDetailsByHid(healthId), function (err, res, body) {
                        expect(res.statusCode).to.equal(200);
                        _body = JSON.parse(body)
                        expect(_body.hid).to.equal(healthId);
                        expect(_body.hid_card_status).to.equal("ISSUED");
                        done();
                    });
                });
            });
        });
    });
});

