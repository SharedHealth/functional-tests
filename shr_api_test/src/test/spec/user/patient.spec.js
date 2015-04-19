var request = require('request');
var User = require('../../../../src/data/user' );
var Patient = require('../../../../src/entity/patient').Patient;
var Encounter = require('../../../../src/entity/encounter');
var EncounterRequest = require('../../../../src/request/encounter').EncounterRequest;
var SSORequest = require('../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../src/request/patient').PatientRequest;

describe('Patient User', function () {
    var user = new User('patient');
    var facility_user = new User('facility');
    var confidential_user = new User('confidential_patient');
    var datasense_user = new User('datasense');
    var hid = "";
    var confidential_patient_hid = "";
    var patient_user = it;

    before(function (done) {
        request(new SSORequest(user).postBy(facility_user), function (err, httpResponse, body) {
            user.access_token = JSON.parse(httpResponse.body).access_token;
            request(new SSORequest(confidential_user).postBy(facility_user), function (err, httpResponse, body) {
                confidential_user.access_token = JSON.parse(httpResponse.body).access_token;
                request(new SSORequest(facility_user).post(), function (err, httpResponse, body) {
                    facility_user.access_token = JSON.parse(httpResponse.body).access_token;
                    request(new SSORequest(datasense_user).post(), function (err, httpResponse, body) {
                        datasense_user.access_token = JSON.parse(httpResponse.body).access_token;
                        done();
                    });

                });
            });
        });
    });

    beforeEach(function (done) {
        request(new PatientRequest(facility_user, new Patient()).post(), function (err, res, body) {
            hid = body.id;
            request(new PatientRequest(facility_user, new Patient("Yes")).post(), function (err, res, body) {
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

    describe("Encounter Post and Request for other non confidential patient", function () {

        var confidential_encounter_request;
        var non_confidentail_encounter_request;
        var encounter_request;

        beforeEach(function (done) {
            confidential_encounter_request = new EncounterRequest(hid, facility_user, new Encounter(hid, "Yes"));
            non_confidentail_encounter_request = new EncounterRequest(hid, facility_user, new Encounter(hid));
            encounter_request = new EncounterRequest(hid, user, new Encounter(hid));
            request(non_confidentail_encounter_request.post(), function (post_err, post_res, post_body) {
                expect(post_res.statusCode).to.equal(200);
                done();
            });

        });

        patient_user("Should not receive non confidential encounter", function (done) {

            request(encounter_request.get(), function (get_err, get_res, res_body) {
                expect(get_res.statusCode).to.equal(403);
                expect(Number(JSON.parse(res_body).httpStatus)).to.equal(403);
                expect(JSON.parse(res_body).message).to.equal("Access is denied to user " + user.client_id + " for patient " + hid);
                done();

            });
        });

        patient_user("Should not receive confidential encounters", function (done) {

            request(confidential_encounter_request.post(), function (post_err, post_res, post_body) {
                expect(post_res.statusCode).to.equal(200);
                request(encounter_request.get(), function (get_err, get_res, res_body) {
                    expect(get_res.statusCode).to.equal(403);
                    expect(Number(JSON.parse(res_body).httpStatus)).to.equal(403);
                    expect(JSON.parse(res_body).message).to.equal("Access is denied to user " + user.client_id + " for patient " + hid);
                    done();
                });
            });
        });

        patient_user("Should not create encounter", function (done) {

            request(encounter_request.post(), function (post_err, post_res, post_body) {
                expect(Number(JSON.parse(post_body).httpStatus)).to.equal(403);
                expect(JSON.parse(post_body).message).to.equal("Access is denied");
                done();
            });

        });
    });

    describe("Encounter create and Post for confidential patient", function () {
        var confidential_encounter_request;
        var non_confidential_encounter_request;
        var encounter_request;

        beforeEach(function (done) {
            confidential_encounter_request = new EncounterRequest(confidential_patient_hid, facility_user, new Encounter(confidential_patient_hid, "Yes"));
            non_confidential_encounter_request = new EncounterRequest(confidential_patient_hid, facility_user, new Encounter(confidential_patient_hid));
            encounter_request = new EncounterRequest(confidential_patient_hid, user);
            request(non_confidential_encounter_request.post(), function (post_err, post_res, post_body) {
                expect(post_res.statusCode).to.equal(200);
                request(confidential_encounter_request.post(), function (post_err, post_res, post_body) {
                    expect(post_res.statusCode).to.equal(200);
                    done();
                });
            });

        });

        patient_user("Should not receive encounters for confidential patient", function (done) {
            request(encounter_request.get(), function (get_err, get_res, get_body) {

                expect(get_res.statusCode).to.equal(403);
                expect(Number(JSON.parse(get_body).httpStatus)).to.equal(403);
                expect(JSON.parse(get_body).message).to.equal("Access is denied to user " + user.client_id + " for patient " + confidential_patient_hid);
                done();
            });
        });

    });

    describe("Encounter post request from patient himself", function () {
        var confidential_encounter_request;
        var non_confidential_encounter_request;
        var encounter_request;
        var user_encounter_count = 0;
        var confidential_user_encounter_count = 0;
        var datasense_user_encounter_get = null;
        beforeEach(function (done) {
            confidential_encounter_request = new EncounterRequest(confidential_user.hid, datasense_user);
            non_confidential_encounter_request = new EncounterRequest(user.hid, datasense_user);

            request(non_confidential_encounter_request.get(), function (get_err, get_res, get_body) {
                user_encounter_count = JSON.parse(get_body).entries.length;
                request(confidential_encounter_request.get(), function (get_err, get_res, get_body) {
                    confidential_user_encounter_count = JSON.parse(get_body).entries.length;
                    done();

                });

            });
        });

        patient_user("Should not accept post request from confidential patient", function (done) {
            var confidential_user_encounter_request = new EncounterRequest(confidential_user.hid, confidential_user, new Encounter(confidential_user.hid, "Yes"));

            request(confidential_user_encounter_request.post(), function (post_err, post_res, post_body) {
                expect(post_res.statusCode).to.equal(403);
                request(confidential_user_encounter_request.get(), function (get_err, get_res, get_body) {
                    expect(JSON.parse(get_body).entries.length).to.equal(confidential_user_encounter_count);
                    done();
                });
            });
        });

        patient_user("Should not accept post request from non confidential patient", function (done) {
            var non_confidential_user_encounter_request = new EncounterRequest(user.hid, user, new Encounter(user.hid, "No"));
            request(non_confidential_user_encounter_request.post(), function (post_err, post_res, post_body) {
                expect(post_res.statusCode).to.equal(403);
                request(non_confidential_user_encounter_request.get(), function (get_err, get_res, get_body) {
                    expect(JSON.parse(get_body).entries.length).to.equal(user_encounter_count);
                    done();
                });
            });
        });

    });

    describe("Encounter request from patient himself", function () {
        var confidential_encounter_request;
        var non_confidential_encounter_request;
        var encounter_request;
        var user_encounter_count = 0;
        var confidential_user_encounter_count = 0;
        beforeEach(function (done) {
            confidential_encounter_request = new EncounterRequest(confidential_user.hid, datasense_user);
            non_confidential_encounter_request = new EncounterRequest(user.hid, datasense_user);

            request(non_confidential_encounter_request.get(), function (get_err, get_res, get_body) {
                user_encounter_count = JSON.parse(get_body).entries.length;
                request(confidential_encounter_request.get(), function (get_err, get_res, get_body) {
                    confidential_user_encounter_count = JSON.parse(get_body).entries.length;
                    done();

                });

            });
        });

        patient_user("should be able to see his own confidential encounters", function (done) {
            var confidential_encounter_post = new EncounterRequest(confidential_user.hid, facility_user, new Encounter(confidential_user.hid, "Yes"));
            var encounter_request = new EncounterRequest(confidential_user.hid, confidential_user);
            request(confidential_encounter_post.post(), function (post_err, post_res, post_body) {
                expect(post_res.statusCode).to.equal(200);
                request(encounter_request.get(), function (get_err, get_res, get_body) {
                    expect(JSON.parse(get_body).entries.length).to.equal(Number(confidential_user_encounter_count) + 1);
                    done();
                });
            });
        });

        patient_user("should be able to see his own non confidential encounters", function (done) {
            var encounter_post = new EncounterRequest(user.hid, facility_user, new Encounter(user.hid, "Yes"));
            var encounter_request = new EncounterRequest(user.hid, user);
            request(encounter_post.post(), function (post_err, post_res, post_body) {
                expect(post_res.statusCode).to.equal(200);
                request(encounter_request.get(), function (get_err, get_res, get_body) {
                    expect(JSON.parse(get_body).entries.length).to.equal(Number(user_encounter_count) + 1);
                    done();
                });
            });
        });

    });

    describe("Catchment Feed", function () {
        patient_user("Should not receive encounters for his catchment area code", function (done) {

            var catchment = "302607";
            var catchment_request = new CatchmentRequest(user, catchment);
            request(catchment_request.get(), function (err, httpResponse, body) {
                expect(httpResponse.statusCode).to.equal(403);
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

    });
});

