var request = require('request');
var User = require('../../src/data/user' );
var Patient = require('../../src/entity/patient').Patient;
var Encounter = require('../../src/entity/encounter');
var EncounterRequest = require('../../src/request/encounter').EncounterRequest;
var SSORequest = require('../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../src/request/patient').PatientRequest;


describe("Create User", function () {
    var user = new User('facility');
    var confidential_patient = null;
    var non_confidential_patient = null;


    before(function (done) {
        request(new SSORequest(user).post(), function (err, httpResponse, body) {
            user.access_token = JSON.parse(httpResponse.body).access_token;
            done();
        });

    });

    beforeEach(function (done) {
        non_confidential_patient = new Patient();
        request(PatientRequest(user, non_confidential_patient.details).post(), function (err, res, body) {
            non_confidential_patient.hid = body.id;
            confidential_patient = new Patient("Yes");
            request(new PatientRequest(user, confidential_patient.details).post(), function (err, res, body) {
                confidential_patient.hid = body.id;
                done();
            });
        });
    });

it.only("should have created patients", function(done) {
    console.log("non_confidential_patient_hid " + non_confidential_patient.hid);
    console.log("confidential_patient_hid " + confidential_patient.hid);
    done();
});
});