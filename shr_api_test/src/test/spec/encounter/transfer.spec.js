var request = require('request');
var User = require('../../../../src/data/user' );
var Patient = require('../../../../src/entity/patient').Patient;
var PatientWithDifferentCatchment = require('../../../../src/entity/patient').PatientWithDifferentCatchment;
var Encounter = require('../../../../src/entity/encounter').DefaultEncounterFeed;
var EncounterRequest = require('../../../../src/request/encounter').EncounterRequest;
var SSORequest = require('../../../../src/request/sso').SSORequest;
var CatchmentRequest = require('../../../../src/request/catchment').CatchmentRequest;
var PatientRequest = require('../../../../src/request/patient').PatientRequest;

//describe("Encounter Merge", function () {
//    var user = new User('datasense');
//    var facility_user = new User('facility');
//    var hid = "";
//    var confidential_patient_hid = "";
//    var datasense_user = it;
//}
