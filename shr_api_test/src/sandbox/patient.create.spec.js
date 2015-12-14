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
    var confidential_patient_hid = "";
    var non_confidential_patient_hid = "";


    before(function (done) {
        request(new SSORequest(user).post(), function (err, httpResponse, body) {
            user.access_token = JSON.parse(httpResponse.body).access_token;
            done();
        });

    });

    beforeEach(function (done) {
        request(PatientRequest(user, new Patient()).post(), function (err, res, body) {
            non_confidential_patient_hid = body.id;
            request(new PatientRequest(user, new Patient("Yes")).post(), function (err, res, body) {
                confidential_patient_hid = body.id;
                done();
            });
        });
    });

it.only("should have created patients", function(done) {
    console.log("non_confidential_patient_hid " + non_confidential_patient_hid);
    console.log("confidential_patient_hid " + confidential_patient_hid);
    done();
});
});