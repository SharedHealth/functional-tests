var request = require('request');
var User = require('../../../../src/data/user' );
var Patient = require('../../../../src/entity/patient').Patient;
var Encounter = require('../../../../src/entity/encounter').DefaultEncounterFeed;
var EncounterRequest = require('../../../../src/request/encounter').EncounterRequest;
var SSORequest = require('../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../src/request/patient').PatientRequest;
var util = require("util");


describe("Facility User", function () {
    var user = new User('facility');
    var facility_user = it;
    var non_confidential_patient = null;
    var confidential_patient = null;

    var provider_user = new User('provider');

    var catchment_user = new User();
    before(function (done) {
        request(new SSORequest(user).post(), function (err, httpResponse, body) {
            util.log(body);
            expect(httpResponse.statusCode).to.equal(200);
            user.access_token = JSON.parse(httpResponse.body).access_token;
            request(new SSORequest(provider_user).post(), function(err, httpResponse, body){
                util.log(body);
                expect(httpResponse.statusCode).to.equal(200);
                provider_user.access_token = JSON.parse(httpResponse.body).access_token;
                done();
            });
        });
    });

    beforeEach(function (done) {
        non_confidential_patient = new Patient();
        
        request(PatientRequest(user, non_confidential_patient.details).post(), function (err, res, body) {
            util.log(body);
            non_confidential_patient.hid = body.id;
            confidential_patient = new Patient("Yes");
            request(new PatientRequest(user, confidential_patient.details).post(), function (err, res, body) {
                util.log(body);
                expect(res.statusCode).to.equal(201);
                confidential_patient.hid = body.id;
                done();
            });
        });
    });

    afterEach(function () {
        non_confidential_patient = null;
        confidential_patient = null;
    });

    describe("Encounter Post and Request for non confidential patient", function () {

        var confidential_encounter_request;
        var non_confidential_encounter_request;

        beforeEach(function (done) {
            confidential_encounter_request = EncounterRequest(non_confidential_patient.hid, user,  Encounter(non_confidential_patient.hid, "Yes"));
            non_confidential_encounter_request = EncounterRequest(non_confidential_patient.hid, user, Encounter(non_confidential_patient.hid));
            request(non_confidential_encounter_request.post(), function (post_err, post_res, post_body) {
                util.log(post_body);
                expect(post_res.statusCode).to.equal(200);
                done();
            });

        });

        facility_user("Should receive non confidential encounter", function (done) {
            request(non_confidential_encounter_request.get(), function (get_err, get_res, get_body) {
                util.log(get_body);
                expect(get_res.statusCode).to.equal(200);
                expect(JSON.parse(get_body).entries.length).to.equal(1);
                done();

            });
        });

        //Failing needs fix bug BSHR-1073
        facility_user.skip("Should create and not receive confidential encounter", function (done) {
            request(confidential_encounter_request.post(), function (post_err, post_res, post_body) {
                util.log(post_body);
                expect(post_res.statusCode).to.equal(200);
                request(confidential_encounter_request.get(), function (get_err, get_res, get_body) {
                    util.log(get_body);
                    expect(get_res.statusCode).to.equal(200);
                    expect(JSON.parse(get_body).entries.length).to.equal(1);
                    done();
                });
            });

        });
    });

    describe("Encounter create and Post for confidential patient", function () {
        var confidential_encounter_request;
        var non_confidential_encounter_request;

        beforeEach(function (done) {
            confidential_encounter_request = new EncounterRequest(confidential_patient.hid, user, new Encounter(confidential_patient.hid, "Yes"));
            non_confidential_encounter_request = new EncounterRequest(confidential_patient.hid, user, new Encounter(confidential_patient.hid));
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

        facility_user("Should not receive any encounter for confidential patient", function (done) {
            request(confidential_encounter_request.get(), function (get_err, get_res, res_body) {
                util.log(res_body);
                expect(get_res.statusCode).to.equal(403);
                expect(Number(JSON.parse(res_body).httpStatus)).to.equal(403);
                expect(JSON.parse(res_body).message).to.equal("Access is denied to user " + user.client_id + " for patient " + confidential_patient.hid);
                done();
            });
        });

    });

    describe("Catchment Feed", function () {
        facility_user("Should receive for his catchment area code", function (done) {
            var catchment = user.catchment[0];
            var catchment_request =  CatchmentRequest(user, catchment);
            request(catchment_request.get(), function (err, httpResponse, body) {
                util.log(body);
                expect(httpResponse.statusCode).to.equal(200);
                done();
            });
        });

        facility_user("should not return catchment details for district in case catchment_code correspondes to upazilla belongs to upazilla", function (done) {
            var catchment = provider_user.catchment[0];
            var district_catchment = catchment.substring(0, catchment.length - 2);
            var catchment_request = new CatchmentRequest(provider_user, district_catchment);
            request(catchment_request.get(), function (err, httpResponse, body) {
                util.log(body);
                expect(httpResponse.statusCode).to.equal(403);
                expect(body).to.equal('{"httpStatus":"403","message":"Access is denied to user ' + provider_user.client_id + ' for catchment ' + district_catchment + '"}');
                done();
            });

        });

        facility_user("should  return catchment details for city in case of city belongs to upazilla of facility", function (done) {
            var catchment = user.catchment[0] + "01";
            var catchment_request = new CatchmentRequest(user, catchment);
            request(catchment_request.get(), function (err, httpResponse, body) {
                util.log(body);
                expect(httpResponse.statusCode).to.equal(200);
                done();
            });

        });

    });
});
