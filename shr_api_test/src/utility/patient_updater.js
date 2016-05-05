//var getCodedName = getCodedName(given_name)
//{
//    return
//};
//

var async = require('async');
var request = require('request');

var User = require('./../data/user' );
var SSORequest = require('./../request/sso').SSORequest;
var PatientRequest = require('./../request/patient').PatientRequest;
var CassandraClient = require('./../misc/cassandra_client').CassandraClient;
var util = require("util");

function PatientUpdater() {
    var facility_user = new User('facility');
    var user = new User('mciAdmin');

    var convertToCharIfNumeric = function(char) {

        var codeValue = char.charCodeAt(0);
        if ( codeValue >= 48 && codeValue <= 57) {
            return String.fromCharCode(65 + parseInt(char));
        }
        else
        {	return char;
        }
    };
    var getPatients = function (result) {
        var reg = new RegExp('[0-9]+');
        patientRequest = new PatientRequest(user);
        async.each(result,function(patient_info, callback){
            //util.log(patientRequest.updateUsingPutWithGivenValues(patient_info.health_id, {'given_name' : patient_info.given_name}));
            if(reg.test(patient_info.given_name) == true)
            {
                request(patientRequest.updateUsingPutWithGivenValues(patient_info.health_id, {'given_name' : patient_info.given_name.split("").map(convertToCharIfNumeric).join("")}), function (err, res, body) {
                    util.log(body);
                    callback();
                });

                }
                    else
                {
                    callback();
                }
            });
    };
    async.waterfall([
        function getAccessTokenForFacilityUser(next) {
            request(new SSORequest(facility_user).post(), function (err, res, body) {
                facility_user.access_token = JSON.parse(res.body).access_token;
                next();
            });

        },
        function getAccessTokenForMCIAdminUser(next) {
            request(new SSORequest(user).postBy(facility_user), function (err, res, body) {
                user.access_token = JSON.parse(res.body).access_token;
                next();
            });
        },
        function getPatientList(next) {
            new CassandraClient('172.18.46.57', 'mci', 'cassandra', 'cassandra').executeQuery("select given_name, health_id from patient", getPatients);

        }

    ], function () {

    });
}

PatientUpdater();



