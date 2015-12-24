var request = require('request');
var User = require('../../../../src/data/user');
var Patient = require('../../../../src/entity/patient').Patient;
var Encounter = require('../../../../src/entity/encounter').DefaultEncounterFeed;
var EncounterRequest = require('../../../../src/request/encounter').EncounterRequest;
var SSORequest = require('../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../src/request/patient').PatientRequest;
var fs = require("fs");
describe("Patient", function () {

    var facility_user = new User('facility');

    before(function (done) {
        request(new SSORequest(facility_user).post(), function (err, httpResponse, body) {
            console.log(body);
            facility_user.access_token = JSON.parse(httpResponse.body).access_token;
            done();
        });

    });

    it("Should create a new non confidential patient", function (done) {
        var patient = new Patient();
        request(new PatientRequest(facility_user, patient).post(), function (err, res, body) {
            console.log(body);
            expect(body.http_status).to.equal(201);
            done();
        });
    });

    //This test case is failing because cassandra takes time to update the contents.
    // CAP Theorem - Consistency is sacrificied for performance and avalaibility
    it.skip("Should update an existing patient", function (done) {
        var patient = new Patient();
        request(new PatientRequest(facility_user, patient).post(), function (err, res, body) {
            console.log(body);
            //user.access_token = JSON.parse(httpResponse.body).access_token;
            expect(body.http_status).to.equal(201);
            request(new PatientRequest(facility_user, patient).post(), function (err, res, body) {
                console.log(body);
                expect(body.http_status).to.equal(202);
                done();
            });
        });
    });

    it("Should update an existing patient on all fields match", function (done) {
        var updatable_patient = JSON.parse(fs.readFileSync(__dirname + "/../../../data/updatable_patient.json", "utf8"));
        console.log(updatable_patient);
        request(new PatientRequest(facility_user, updatable_patient.details).post(), function (err, res, body) {
            console.log(body);
            expect(body.http_status).to.equal(202);
            expect(body.id).to.equal(updatable_patient.hid);
            done();
        });
    });
});

