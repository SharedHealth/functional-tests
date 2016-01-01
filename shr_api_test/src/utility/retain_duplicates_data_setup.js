var request = require('request');
var User = require('./../../src/data/user');
var Patient = require('./../../src/entity/patient').PatientWithUID;
var SSORequest = require('./../../src/request/sso').SSORequest;
var PatientRequest = require('./../../src/request/patient').PatientRequest;
var async = require('async');
var fs = require('fs');

exports.DuplicatePatientsToRetain = function () {
    var patient_list = {};
    var facility_user = new User("facility");

    var createPatients = function () {
        async.series([
            function getAccessToken(done) {
                request(new SSORequest(facility_user).post(), function (err, httpResponse, body) {
                    console.log(body);
                    facility_user.access_token = JSON.parse(httpResponse.body).access_token;
                    done();
                });

            },

            function createPatientsToRetain(done){
                var patient_1_to_retain = new Patient();
                var patient_2_to_retain = null;
                request(PatientRequest(facility_user, patient_1_to_retain.details).post(), function (err, res, body) {
                    console.log(body);
                    patient_1_to_retain.hid = body.id;
                    patient_list["patient_1_to_retain"] = patient_1_to_retain;
                    patient_2_to_retain = new Patient();
                    patient_2_to_retain.details.nid = patient_1_to_retain.details.nid;
                    request(PatientRequest(facility_user,patient_2_to_retain.details).post(), function(err,res,body){
                        patient_2_to_retain.hid = body.id;
                        patient_list["patient_2_to_retain"] = patient_2_to_retain;
                        done();
                    });
                });


            },

            function writeDetailsToFile(done) {
                console.log(patient_list);
                var contents = fs.writeFileSync(__dirname + "/../data/duplicate_patients_to_retain.json", JSON.stringify(patient_list));
                done();
            },
            function (err, results) {
                if (err == null) {
                    console.log("There is an error during datasetup");
                    console.log(err);
                }
                else {
                    console.log("All required patients created");

                }
            }


        ]);

    };

    return {
        setupData: createPatients
    }
};

