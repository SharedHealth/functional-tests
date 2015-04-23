var request = require('request');
var User = require('../../../../src/data/user' );
var Patient = require('../../../../src/entity/patient').Patient;
var Encounter = require('../../../../src/entity/encounter').DefaultEncounterFeed;
var EncounterRequest = require('../../../../src/request/encounter').EncounterRequest;
var SSORequest = require('../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../src/request/patient').PatientRequest;

describe("Datasense User", function () {
    var user = new User('datasense');
    var facility_user = new User('facility');

    var hid = "";
    var confidential_patient_hid = "";
    var datasense_user = it;

    before(function (done) {
        request(new SSORequest(user).post(), function (err, httpResponse, body) {
            console.log(body);
            expect(httpResponse.statusCode).to.equal(200)
            user.access_token = JSON.parse(httpResponse.body).access_token;
            request(new SSORequest(facility_user).post(), function (err, httpResponse, body) {
                console.log(body)
                expect(httpResponse.statusCode).to.equal(200)
                facility_user.access_token = JSON.parse(httpResponse.body).access_token;
                done();
            });

        });
    });

    beforeEach(function (done) {
        request(new PatientRequest(facility_user, new Patient()).post(), function (err, res, body) {
            console.log(body)
            expect(res.statusCode).to.equal(201)
            hid = body.id;
            request(new PatientRequest(facility_user, new Patient("Yes")).post(), function (err, res, body) {
                console.log(body)
                expect(res.statusCode).to.equal(201)
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
            encounter_request = new EncounterRequest(hid, user, new Encounter(hid));
            request(non_confidentail_encounter_request.post(), function (post_err, post_res, post_body) {
                console.log(post_body);
                expect(post_res.statusCode).to.equal(200);
                done();
            });

        });

        datasense_user("Should receive non confidential encounter", function (done) {
            request(encounter_request.get(), function (get_err, get_res, get_body) {
                console.log(get_body);
                expect(get_res.statusCode).to.equal(200);
                expect(JSON.parse(get_body).entries.length).to.equal(1);
                done();

            });
        });

        datasense_user("Should receive confidential encounter", function (done) {

            request(confidential_encounter_request.post(), function (post_err, post_res, post_body) {
                console.log(post_body);
                expect(post_res.statusCode).to.equal(200);
                request(encounter_request.get(), function (get_err, get_res, get_body) {
                    console.log(get_body);
                    expect(JSON.parse(get_body).entries.length).to.equal(2);
                    done();
                });
            });
        });

        datasense_user("Should not create encounter", function (done) {

            request(encounter_request.post(), function (post_err, post_res, post_body) {
                console.log(post_body);
                expect(Number(JSON.parse(post_body).httpStatus)).to.equal(403);
                expect(JSON.parse(post_body).message).to.equal("Access is denied");
                request(encounter_request.get(), function (get_err, get_res, get_body) {
                    console.log(get_body);
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
            request(non_confidential_encounter_request.post(), function (post_err, post_res, post_body) {
                console.log(post_body);
                expect(post_res.statusCode).to.equal(200);
                request(confidential_encounter_request.post(), function (post_err, post_res, post_body) {
                    console.log(post_body);
                    expect(post_res.statusCode).to.equal(200);
                    done();
                });
            });

        });

        datasense_user("Should receive encounters for confidential patient", function (done) {
            request(encounter_request.get(), function (get_err, get_res, get_body) {
                console.log(get_body);
                expect(get_res.statusCode).to.equal(200);
                expect(JSON.parse(get_body).entries.length).to.equal(2);
                done();
            });
        });

    });

    describe("Catchment Feed", function () {
        datasense_user("Should receive for his catchment area code", function (done) {

            var catchment = user.catchment + "26";
            var catchment_request = new CatchmentRequest(user, catchment);
            request(catchment_request.get(), function (err, httpResponse, body) {
                console.log(body);
                expect(httpResponse.statusCode).to.equal(200);
                done();
            });
        });

        datasense_user("should not return catchment details for unauthorized catchments", function (done) {

            var catchment = "31" + "26";
            var catchment_request = new CatchmentRequest(user, catchment);
            request(catchment_request.get(), function (err, httpResponse, body) {
                console.log(body);
                expect(httpResponse.statusCode).to.equal(403);
                expect(body).to.equal('{"httpStatus":"403","message":"Access is denied to user ' + user.client_id + ' for catchment ' + catchment + '"}');
                done();
            });

        });

        datasense_user("should  return catchment details for city in case of city belongs to upazilla of facility", function (done) {
            var catchment = user.catchment + "2701";
            var catchment_request = new CatchmentRequest(user, catchment);
            request(catchment_request.get(), function (err, httpResponse, body) {
                console.log(body);
                expect(httpResponse.statusCode).to.equal(200);
                done();
            });

        });

    });
});

