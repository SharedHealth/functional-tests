var request = require('request');
var User = require('../../../../src/data/user');
var Patient = require('../../../../src/entity/patient').Patient;
var Encounter = require('../../../../src/entity/encounter').EncounterFeed;
var EncounterRequest = require('../../../../src/request/encounter').EncounterRequest;
var SSORequest = require('../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../src/request/patient').PatientRequest;


describe.skip("Encounter Update", function () {
    var create_user = new User('facility');
    var view_user = new User("datasense");
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
                done();
            });
        });
    });

    beforeEach(function (done) {
        request(new PatientRequest(create_user, new Patient()).post(), function (err, res, body) {
            console.log(body);
            expect(res.statusCode).to.equal(201);
            hid = body.id;
            request(new PatientRequest(create_user, new Patient("Yes")).post(), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(201);
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

        var encounter_request;
        var encounterFeed;

        beforeEach(function (done) {
            encounterFeed = new Encounter(hid, "Yes");
            encounterFeed.withDiagnosisDetails("Fracture in upper arm");
            encounter_request = new EncounterRequest(hid, create_user, encounterFeed.get());
            request(encounter_request.post(), function (err, res, body) {
                console.log(body);
                encounter_id = JSON.parse(body).encounterId;
                console.log("encounter Id is " + encounter_id);

                expect(res.statusCode).to.equal(200);
                done();
            });

        });

        it("should update encounter", function (done) {
            request(encounter_request.get(), function (err, res, body) {
                console.log(body);
                expect(res.statusCode).to.equal(200);
                //expect(JSON.parse(body).entries.length).to.equal(1);
                encounterFeed.withImmunizationDetails("BCG");
                encounter_request = new EncounterRequest(hid, create_user, encounterFeed.get());
                request(encounter_request.put(encounter_id), function (err, res, body) {
                    console.log(body);
                    expect(res.statusCode).to.equal(200);
                    request(encounter_request.get(), function (err, res, body) {
                        console.log(body);
                        encounterFeed.withVitals();
                        encounter_request = new EncounterRequest(hid, create_user, encounterFeed.get());
                        request(encounter_request.put(encounter_id), function (err, res, body) {
                            console.log(body);
                            request(encounter_request.get(), function (err, res, body) {
                                console.log(body);
                                done();
                            });

                        });
                    });

                });
            });
        });

    });
});
