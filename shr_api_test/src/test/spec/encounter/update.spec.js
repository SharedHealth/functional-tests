var request = require('request');
var User = require('../../../../src/data/user');
var Patient = require('../../../../src/entity/patient').Patient;
var Encounter = require('../../../../src/entity/encounter').EncounterFeed;
var EncounterRequest = require('../../../../src/request/encounter').EncounterRequest;
var SSORequest = require('../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../src/request/patient').PatientRequest;


describe("Encounter Update", function () {
    var create_user = new User('facility');
    var view_user = new User("datasense");
    var another_create_user = new User('provider');
    var hid = "";
    var encounter_id = "";
    var confidential_patient_hid = "";

    before(function (done) {
        request(new SSORequest(create_user).post(), function (err, httpResponse, body) {
            console.log(body);
            expect(httpResponse.statusCode).to.equal(200);
            create_user.access_token = JSON.parse(httpResponse.body).access_token;
            request(new SSORequest(view_user).post(), function (err, httpResponse, body) {
                console.log(body);
                expect(httpResponse.statusCode).to.equal(200);
                view_user.access_token = JSON.parse(httpResponse.body).access_token;
                request(new SSORequest(another_create_user).post(), function (err, httResponse, body) {
                    console.log(body);
                    expect(httpResponse.statusCode).to.equal(200);
                    another_create_user.access_token = JSON.parse(httResponse.body).access_token;
                    done();
                })

            });
        });
    });

    describe("Encounter Post and Request for non confidential patient", function () {
        var encounter_request;
        var confidential_encounter_request = "";
        var nonconfidential_encounter_bundle;
        var confidential_encounter_bundle = "";
        var confidential_encounter_id = "";
        var confidential_encounter_bundle_content = "";


        before(function (done) {

            request(new PatientRequest(create_user, new Patient()).post(), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(201);
                hid = body.id;
                nonconfidential_encounter_bundle = new Encounter(hid, "No");
                confidential_encounter_bundle = new Encounter(hid, "yes");
                confidential_encounter_bundle_content = confidential_encounter_bundle.get();
                encounter_request = new EncounterRequest(hid, create_user, nonconfidential_encounter_bundle.get());
                confidential_encounter_request = new EncounterRequest(hid, create_user, confidential_encounter_bundle_content);

                request(encounter_request.post(), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(200);
                    encounter_id = JSON.parse(body).encounterId;
                    console.log("encounter Id is " + encounter_id);
                    request(confidential_encounter_request.post(), function (err, res, body) {
                        console.log(body);
                        expect(res.statusCode).to.equal(200);
                        confidential_encounter_id = JSON.parse(body).encounterId;
                        console.log("confidential encounter Id is " + confidential_encounter_id);
                        done();
                    })
                });
            });
        });


        it("should be able to update encounter as same user", function (done) {
            request(encounter_request.put(encounter_id), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200);
                done();
            });
        });

        it("should not be able to update encounter as different user", function (done) {
            var encounter_content = "";
            request(encounter_request.get(encounter_id), function (err, res, body) {
                encounter_content = JSON.parse(body).content;
                encounter_update_request_as_different_user = new EncounterRequest(hid, another_create_user, {details: encounter_content});
                request(encounter_update_request_as_different_user.put(encounter_id), function (err, res, body) {
                    expect(JSON.parse(body).httpStatus).to.equal('403');
                    done();
                });
            });
        });

        it("Should be able to update confidential encounter as same user", function (done) {
            var confidential_encounter_view_request = new EncounterRequest(hid, view_user, confidential_encounter_bundle_content);
            var confidential_encounter_content = "";
            request(confidential_encounter_view_request.get(confidential_encounter_id), function (err, res, body) {
                console.log(body);
                confidential_encounter_content = JSON.parse(body).content;
                request(new EncounterRequest(hid, create_user, {details: confidential_encounter_content}).put(confidential_encounter_id), function (err, res, body) {
                    console.log((body));
                    expect(JSON.parse(body).successful).to.equal(true);
                    done();
                });
            });

        });
    });

    describe("Encounter Post and Request for confidential patient", function () {
        before(function (done) {
            request(new PatientRequest(create_user, new Patient("Yes")).post(), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(201);
                confidential_patient_hid = body.id;
                nonconfidential_encounter_bundle = new Encounter(confidential_patient_hid, "No");
                encounter_request = new EncounterRequest(confidential_patient_hid, create_user, nonconfidential_encounter_bundle.get());

                request(encounter_request.post(), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(200);
                    encounter_id = JSON.parse(body).encounterId;
                    console.log("encounter Id is " + encounter_id);
                    done();
                });
            });
        });

        it("should be able to update encounter as same user", function (done) {
            request(encounter_request.put(encounter_id), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200);
                done();
            });
        });
    });
});
