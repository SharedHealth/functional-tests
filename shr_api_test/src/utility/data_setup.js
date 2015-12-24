var request = require('request');
var User = require('./../../src/data/user');
var Patient = require('./../../src/entity/patient').Patient;
var SSORequest = require('./../../src/request/sso').SSORequest;
var PatientRequest = require('./../../src/request/patient').PatientRequest;
var async = require('async');
var fs = require('fs');

function SuiteDataSetup() {
    var updatable_patient = {details: new Patient(), hid: null};
    var facility_user = new User("facility");

    var createPatients = function() {
        async.series([
            function getAccessToken(done) {
                request(new SSORequest(facility_user).post(), function (err, httpResponse, body) {
                    console.log(body)
                    facility_user.access_token = JSON.parse(httpResponse.body).access_token;
                    done();
                });

            },
            function createPatient(done) {
                request(PatientRequest(facility_user, updatable_patient.details).post(), function(err,res,body){
                    console.log(body);
                    updatable_patient.hid = body.id;
                    done();
                });

            },
            function updatePatientDetails(done){
                console.log();
                var contents = fs.writeFileSync(__dirname + "/../data/updatable_patient.json", JSON.stringify(updatable_patient));
              done();
            },
            function (err,results){
                if(err == null)
                {
                    console.log("There is an error during datasetup");
                    console.log(err);
                }
                else
                {
                    console.log("All required patients created");

                }
            }


        ]);

    }

    return {
        createPatients : createPatients
    }
}




SuiteDataSetup().createPatients();