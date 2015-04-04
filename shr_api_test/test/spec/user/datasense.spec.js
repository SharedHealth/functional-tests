var request = require('request');
var User = require('../../../src/user');
var Encounter = require('../../../src/type/encounter');
var EncounterRequest = require('../../../src/request/encounterRequest');

var SSORequest = require('../../../src/request/SSORequest');
var Patient = require('../../../src/type/patient');
var PatientRequest = require('../../../src/request/patientRequest');
var CatchmentRequest = require('../../../src/request/CatchmentRequest');


describe("Datasense User", function () {
    var user = new User('datasense');
    var facility_user = new User('facility');

    var hid = "";
    var confidential_patient_hid = "";

    before(function (done) {
        request.post(new SSORequest(user).post(), function (err, httpResponse, body) {
            user.access_token = JSON.parse(httpResponse.body).access_token;
            request.post(new SSORequest(facility_user).post(), function (err, httpResponse, body) {
                facility_user.access_token = JSON.parse(httpResponse.body).access_token;
                done();
            });

        });
    });

    beforeEach(function (done) {
        request.post(new PatientRequest(facility_user, new Patient()).post(), function (err, res, body) {
            hid = body.id;
            request.post(new PatientRequest(facility_user, new Patient("Yes")).post(), function (err, res, body) {
                confidential_patient_hid = body.id;
                done();
            });
        });
    });

    afterEach(function (done) {
        hid = "";
        confidential_patient_hid = "";
        done();
    });

    describe("Encounter Post and Request for non confidential patient", function () {

        var confidential_encounter_request;
        var non_confidentail_encounter_request;
        var encounter_request;

        beforeEach(function (done) {
            confidential_encounter_request = new EncounterRequest(hid, facility_user, new Encounter(hid, "Yes"));
            non_confidentail_encounter_request = new EncounterRequest(hid, facility_user, new Encounter(hid));
            encounter_request = new EncounterRequest(hid, user);
            request.post(non_confidentail_encounter_request.post(), function (post_err, post_res, post_body) {
                expect(post_res.statusCode).to.equal(200);
                done();
            });

        });

        it("Should receive non confidential encounter", function (done) {

            request.get(encounter_request.getUrl(), encounter_request.getHeaders(), function (get_err, get_res, get_body) {
                expect(get_res.statusCode).to.equal(200);
                expect(JSON.parse(get_body).entries.length).to.equal(1);
                done();

            });
        });

        it("Should receive confidential encounter", function (done) {

            request.post(confidential_encounter_request.post(), function (post_err, post_res, post_body) {
                expect(post_res.statusCode).to.equal(200);
                request.get(encounter_request.getUrl(), encounter_request.getHeaders(), function (get_err, get_res, get_body) {
                    expect(JSON.parse(get_body).entries.length).to.equal(2);
                    done();
                });
            });
        });

        it("Should not create encounter", function (done) {

            request.post(encounter_request.post(), function (post_err, post_res, post_body) {
                expect(Number(JSON.parse(post_body).httpStatus)).to.equal(403);
                // expect(JSON.parse(post_body).message).to.equal("Access for patient " + confidential_patient_hid + " data is denied");
                expect(JSON.parse(post_body).message).to.equal("Access is denied");
                request.get(encounter_request.getUrl(), encounter_request.getHeaders(), function (get_err, get_res, get_body) {
                    expect(get_res.statusCode).to.equal(200);
                    expect(JSON.parse(get_body).entries.length).to.equal(1);
                    done();
                });
            });

        });
    });

    describe("Encounter create and Post for confidential patient", function (done) {
        var confidential_encounter_request;
        var non_confidential_encounter_request;
        var encounter_request;

        beforeEach(function (done) {
            confidential_encounter_request = new EncounterRequest(confidential_patient_hid, facility_user, new Encounter(confidential_patient_hid, "Yes"));
            non_confidential_encounter_request = new EncounterRequest(confidential_patient_hid, facility_user, new Encounter(confidential_patient_hid));
            encounter_request = new EncounterRequest(confidential_patient_hid, user);
            request.post(non_confidential_encounter_request.post(), function (post_err, post_res, post_body) {
                expect(post_res.statusCode).to.equal(200);
                request.post(confidential_encounter_request.post(), function (post_err, post_res, post_body) {
                    expect(post_res.statusCode).to.equal(200);
                    done();
                });
            });

        });

        it("Should receive encounters for confidential patient", function (done) {
            request.get(encounter_request.getUrl(), encounter_request.getHeaders(), function (get_err, get_res, get_body) {
                expect(get_res.statusCode).to.equal(200);
                expect(JSON.parse(get_body).entries.length).to.equal(2);
                done();
            });
        });

    });

    describe("Catchment Feed", function () {
        it("Should receive for his catchment area code", function (done) {

            var catchment = user.catchment + "26";
            var catchment_request = new CatchmentRequest(user, catchment);
            request.get(catchment_request.getUrl(), catchment_request.getHeaders(), function (err, httpResponse, body) {
                expect(httpResponse.statusCode).to.equal(200);
                done();
            });
        });

        it("should not return catchment details for unauthorized catchments", function (done) {

            var catchment = "31" + "26";
            var catchment_request = new CatchmentRequest(user, catchment);
            request.get(catchment_request.getUrl(), catchment_request.getHeaders(), function (err, httpResponse, body) {
                expect(httpResponse.statusCode).to.equal(403);
                expect(body).to.equal('{"httpStatus":"403","message":"Access is denied to user ' + user.client_id + ' for catchment ' + catchment + '"}');
                done();
            });

        });

        it("should  return catchment details for city in case of city belongs to upazilla of facility", function (done) {
            var catchment = user.catchment + "2701";
            var catchment_request = new CatchmentRequest(user, catchment);
            request.get(catchment_request.getUrl(), catchment_request.getHeaders(), function (err, httpResponse, body) {
                expect(httpResponse.statusCode).to.equal(200);
                done();
            });

        });

    });
});
