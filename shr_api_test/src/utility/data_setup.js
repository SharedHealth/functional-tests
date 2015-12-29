var request = require('request');
var User = require('./../../src/data/user');
var Patient = require('./../../src/entity/patient').PatientWithUID;
var SSORequest = require('./../../src/request/sso').SSORequest;
var PatientRequest = require('./../../src/request/patient').PatientRequest;
var async = require('async');
var fs = require('fs');

function SuiteDataSetup() {
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
            function createPatientForDuplicateRequest(done) {
                var patient_with_duplicate_details = { details: new Patient(), hid: null};

                request(PatientRequest(facility_user, patient_with_duplicate_details.details).post(), function (err, res, body) {
                    console.log(body);
                    patient_with_duplicate_details.hid = body.id;
                    patient_list["duplicate"] = patient_with_duplicate_details;
                    done();
                });

            },
            function createPatientForCommonUID(done) {
                var patient_for_common_uid = {details: new Patient(), hid: null};

                request(PatientRequest(facility_user, patient_for_common_uid.details).post(), function (err, res, body) {
                    console.log(body);
                    patient_for_common_uid.hid = body.id;
                    patient_list["common_uid"] = patient_for_common_uid;

                    done();
                });

            },
            function createPatientForCommonNID(done) {
                var patient_for_common_nid =  {details: new Patient(), hid: null};

                request(PatientRequest(facility_user, patient_for_common_nid.details).post(), function (err, res, body) {
                    console.log(body);
                    patient_for_common_nid.hid = body.id;
                    patient_list["common_nid"] = patient_for_common_nid;
                    done();
                });

            },
            function createPatientForCommonbinbrn(done) {
                var patient_for_common_binbrn =  {details: new Patient(), hid: null};

                request(PatientRequest(facility_user, patient_for_common_binbrn.details).post(), function (err, res, body) {
                    console.log(body);
                    patient_for_common_binbrn.hid = body.id;
                    patient_list["common_binbrn"] = patient_for_common_binbrn;
                    done();
                });

            },
            function createPatientForCommonNIDAndUID(done) {
                var patient_for_common_uid_and_nid =  {details: new Patient(), hid: null};

                request(PatientRequest(facility_user, patient_for_common_uid_and_nid.details).post(), function (err, res, body) {
                    console.log(body);
                    patient_for_common_uid_and_nid.hid = body.id;
                    patient_list["common_uid_and_nid"] = patient_for_common_uid_and_nid;
                    done();
                });

            },

            function createPatientForCommonnidANDBinBrn(done) {

                var patient_for_common_nid_and_binbrn =  {details: new Patient(), hid: null};
                request(PatientRequest(facility_user, patient_for_common_nid_and_binbrn.details).post(), function (err, res, body) {
                    console.log(body);
                    patient_for_common_nid_and_binbrn.hid = body.id;
                    patient_list["common_nid_and_binbrn"] = patient_for_common_nid_and_binbrn;
                    done();
                });

            },
            function createPatientForCommonUIDAndBinBrn(done) {
                var patient_for_common_uid_and_binbrn =  {details: new Patient(), hid: null};

                request(PatientRequest(facility_user, patient_for_common_uid_and_binbrn.details).post(), function (err, res, body) {
                    console.log(body);
                    patient_for_common_uid_and_binbrn.hid = body.id;
                    patient_list["common_uid_and_binbrn"] = patient_for_common_uid_and_binbrn;
                    done();
                });

            },

            function createPatientForCommonUIDBinBrnAndNID(done) {
                var patient_for_common_uid_nid_and_binbrn =  {details: new Patient(), hid: null};
                request(PatientRequest(facility_user, patient_for_common_uid_nid_and_binbrn.details).post(), function (err, res, body) {
                    console.log(body);
                    patient_for_common_uid_nid_and_binbrn.hid = body.id;
                    patient_list["common_uid_nid_and_binbrn"] = patient_for_common_uid_nid_and_binbrn;
                    done();
                });

            },


            function updatePatientDetails(done) {
                console.log(patient_list);
                var contents = fs.writeFileSync(__dirname + "/../data/updatable_patient.json", JSON.stringify(patient_list));
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

    }

    return {
        createPatients: createPatients
    }
}


SuiteDataSetup().createPatients();