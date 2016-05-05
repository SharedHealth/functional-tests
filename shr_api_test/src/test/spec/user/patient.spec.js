var request = require('request');
var User = require('../../../../src/data/user');
var Patient = require('../../../../src/entity/patient').Patient;
var Encounter = require('../../../../src/entity/encounter').DefaultEncounterFeed;
var EncounterRequest = require('../../../../src/request/encounter').EncounterRequest;
var SSORequest = require('../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../src/request/patient').PatientRequest;
var util = require("util");
describe('Patient User', function () {
    var user = new User('patient');
    var facility_user = new User('facility');
    var confidential_user = new User('confidential_patient');
    var datasense_user = new User('datasense');
    var non_confidential_patient = null;
    var confidential_patient = null;
    var patient_user = it;

    before(function (done) {
        request(new SSORequest(user).postBy(facility_user), function (err, httpResponse, body) {
            util.log(body);
            user.access_token = JSON.parse(httpResponse.body).access_token;
            request(new SSORequest(confidential_user).postBy(facility_user), function (err, httpResponse, body) {
                util.log(body);
                confidential_user.access_token = JSON.parse(httpResponse.body).access_token;
                request(new SSORequest(facility_user).post(), function (err, httpResponse, body) {
                    util.log(body);
                    facility_user.access_token = JSON.parse(httpResponse.body).access_token;
                    request(new SSORequest(datasense_user).post(), function (err, httpResponse, body) {
                        util.log(body);
                        datasense_user.access_token = JSON.parse(httpResponse.body).access_token;
                        done();
                    });

                });
            });
        });
    });

    describe("Encounter Post and Request for other non confidential patient", function () {

        var confidential_encounter_request;
        var non_confidentail_encounter_request;
        var encounter_request;


        before(function (done) {
            non_confidential_patient = new Patient();
            request(new PatientRequest(facility_user, non_confidential_patient.details).post(), function (err, res, body) {
                util.log(body);
                non_confidential_patient.hid = body.id;
                confidential_encounter_request = new EncounterRequest(non_confidential_patient.hid, facility_user, new Encounter(non_confidential_patient.hid, "Yes"));
                non_confidentail_encounter_request = new EncounterRequest(non_confidential_patient.hid, facility_user, new Encounter(non_confidential_patient.hid));
                encounter_request = new EncounterRequest(non_confidential_patient.hid, user, new Encounter(non_confidential_patient.hid));
                request(non_confidentail_encounter_request.post(), function (post_err, post_res, post_body) {
                    util.log(post_body);
                    expect(post_res.statusCode).to.equal(200);
                    done();
                });

            });
        });

        patient_user("Should not receive non confidential encounter", function (done) {

            request(encounter_request.get(), function (get_err, get_res, res_body) {
                util.log(res_body);
                expect(get_res.statusCode).to.equal(403);
                expect(Number(JSON.parse(res_body).httpStatus)).to.equal(403);
                expect(JSON.parse(res_body).message).to.equal("Access is denied to user " + user.client_id + " for patient " + non_confidential_patient.hid);
                done();

            });
        });

        patient_user("Should not receive confidential encounters", function (done) {

            request(confidential_encounter_request.post(), function (post_err, post_res, post_body) {
                util.log(post_body);
                expect(post_res.statusCode).to.equal(200);
                request(encounter_request.get(), function (get_err, get_res, res_body) {
                    util.log(res_body);
                    expect(get_res.statusCode).to.equal(403);
                    expect(Number(JSON.parse(res_body).httpStatus)).to.equal(403);
                    expect(JSON.parse(res_body).message).to.equal("Access is denied to user " + user.client_id + " for patient " + non_confidential_patient.hid);
                    done();
                });
            });
        });

        patient_user("Should not create encounter", function (done) {

            request(encounter_request.post(), function (post_err, post_res, post_body) {
                util.log(post_body);
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

        before(function (done) {
            confidential_patient = new Patient("Yes");
            request(new PatientRequest(facility_user, confidential_patient.details).post(), function (err, res, body) {
                util.log(body);
                confidential_patient.hid = body.id;
                confidential_encounter_request = new EncounterRequest(confidential_patient.hid, facility_user, new Encounter(confidential_patient.hid, "Yes"));
                non_confidential_encounter_request = new EncounterRequest(confidential_patient.hid, facility_user, new Encounter(confidential_patient.hid));
                encounter_request = new EncounterRequest(confidential_patient.hid, user);
                request(non_confidential_encounter_request.post(), function (post_err, post_res, post_body) {
                    util.log(post_body);
                    expect(post_res.statusCode).to.equal(200);
                    request(confidential_encounter_request.post(), function (post_err, post_res, post_body) {
                        util.log(post_body);
                        expect(post_res.statusCode).to.equal(200);
                        done();
                    });
                });
            });
        });

        patient_user("Should not receive encounters for confidential patient", function (done) {
            request(encounter_request.get(), function (get_err, get_res, get_body) {
                util.log(get_body);

                expect(get_res.statusCode).to.equal(403);
                expect(Number(JSON.parse(get_body).httpStatus)).to.equal(403);
                expect(JSON.parse(get_body).message).to.equal("Access is denied to user " + user.client_id + " for patient " + confidential_patient.hid);
                done();
            });
        });

    });

    describe("Encounter post request from patient himself", function () {
        var confidential_encounter_request;
        var non_confidential_encounter_request;
        var user_encounter_count = 0;
        var confidential_user_encounter_count = 0;

        before(function (done) {
            confidential_encounter_request = new EncounterRequest(confidential_user.hid, datasense_user);
            non_confidential_encounter_request = new EncounterRequest(user.hid, datasense_user);
            request(non_confidential_encounter_request.get(), function (get_err, get_res, get_body) {
                util.log(get_body);
                user_encounter_count = JSON.parse(get_body).entries.length;
                request(confidential_encounter_request.get(), function (get_err, get_res, get_body) {
                    util.log(get_body);
                    confidential_user_encounter_count = JSON.parse(get_body).entries.length;
                    done();
                });

            });
        });

        patient_user("Should not accept post request from confidential patient", function (done) {
            var confidential_user_encounter_request = new EncounterRequest(confidential_user.hid, confidential_user, new Encounter(confidential_user.hid, "Yes"));

            request(confidential_user_encounter_request.post(), function (post_err, post_res, post_body) {
                util.log(post_body);
                expect(post_res.statusCode).to.equal(403);
                request(confidential_user_encounter_request.get(), function (get_err, get_res, get_body) {
                    util.log(get_body);
                    expect(JSON.parse(get_body).entries.length).to.equal(confidential_user_encounter_count);
                    done();
                });
            });
        });

        patient_user("Should not accept post request from non confidential patient", function (done) {
            var non_confidential_user_encounter_request = new EncounterRequest(user.hid, user, new Encounter(user.hid, "No"));
            request(non_confidential_user_encounter_request.post(), function (post_err, post_res, post_body) {
                util.log(post_body);
                expect(post_res.statusCode).to.equal(403);
                request(non_confidential_user_encounter_request.get(), function (get_err, get_res, get_body) {
                    util.log(get_body);
                    expect(JSON.parse(get_body).entries.length).to.equal(user_encounter_count);
                    done();
                });
            });
        });

    });

    describe("Encounter request from patient himself", function () {
        var confidential_encounter_request;
        var non_confidential_encounter_request;
        var user_encounter_count = 0;
        var confidential_user_encounter_count = 0;
        before(function (done) {
            confidential_encounter_request = new EncounterRequest(confidential_user.hid, datasense_user);
            non_confidential_encounter_request = new EncounterRequest(user.hid, datasense_user);
            request(non_confidential_encounter_request.get(), function (get_err, get_res, get_body) {
                util.log(get_body);
                user_encounter_count = JSON.parse(get_body).entries.length;
                request(confidential_encounter_request.get(), function (get_err, get_res, get_body) {
                    util.log(get_body);
                    confidential_user_encounter_count = JSON.parse(get_body).entries.length;
                    done();
                });
            });
        });
        // Skipped because of bug     98000121486 - patient 98000196090 - confidential patient - BSHR 1084
        patient_user.skip("should be able to see his own confidential encounters", function (done) {
            var confidential_encounter_post = new EncounterRequest(confidential_user.hid, facility_user, new Encounter(confidential_user.hid, "Yes"));
            var encounter_request = new EncounterRequest(confidential_user.hid, confidential_user);
            request(confidential_encounter_post.post(), function (post_err, post_res, post_body) {
                util.log(post_body);
                expect(post_res.statusCode).to.equal(200);
                request(encounter_request.get(), function (get_err, get_res, get_body) {
                    util.log(get_body);
                    expect(JSON.parse(get_body).entries.length).to.equal(Number(confidential_user_encounter_count) + 1);
                    done();
                });
            });
        });

        // Skipped because of bug     98000121486 - patient 98000196090 - confidential patient - BSHR 1084
        patient_user.skip("should be able to see his own non confidential encounters", function (done) {
            var encounter_post = new EncounterRequest(user.hid, facility_user, new Encounter(user.hid, "Yes"));
            var encounter_request = new EncounterRequest(user.hid, user);
            request(encounter_post.post(), function (post_err, post_res, post_body) {
                util.log(post_body);
                expect(post_res.statusCode).to.equal(200);
                request(encounter_request.get(), function (get_err, get_res, get_body) {
                    util.log(JSON.parse(get_body).entries.length);
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
                util.log(body);
                expect(httpResponse.statusCode).to.equal(403);
                expect(JSON.parse(body).message).to.equal("Access is denied");
                done();
            });
        });

    });
});

