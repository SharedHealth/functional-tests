var request = require('request');
var User = require('./../../src/data/user');
var Patient = require('./../../src/entity/patient').PatientWithUID;
var SSORequest = require('./../../src/request/sso').SSORequest;
var PatientRequest = require('./../../src/request/patient').PatientRequest;
var async = require('async');
var fs = require('fs');

exports.DuplicatePatientsToMerge = function () {
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

            function createPatientsToMergeWithMatchingNID(done){
                var patient_1_with_same_nid = new Patient();
                var patient_2_with_same_nid = null;
                request(PatientRequest(facility_user, patient_1_with_same_nid.details).post(), function (err, res, body) {
                    console.log(body);
                    patient_1_with_same_nid.hid = body.id;
                    patient_list["patient_1_with_same_nid"] = patient_1_with_same_nid;
                    patient_2_with_same_nid = new Patient();
                    patient_2_with_same_nid.details.nid = patient_1_with_same_nid.details.nid;
                    request(PatientRequest(facility_user,patient_2_with_same_nid.details).post(), function(err,res,body){
                        patient_2_with_same_nid.hid = body.id;
                        patient_list["patient_2_with_same_nid"] = patient_2_with_same_nid;
                        done();
                    });
                });


            },
            function createPatientsToMergeWithMatchingUID(done){
                var patient_1_with_same_uid = new Patient();
                var patient_2_with_same_uid = null;
                request(PatientRequest(facility_user, patient_1_with_same_uid.details).post(), function (err, res, body) {
                    console.log(body);
                    patient_1_with_same_uid.hid = body.id;
                    patient_list["patient_1_with_same_uid"] = patient_1_with_same_uid;
                    patient_2_with_same_uid = new Patient();
                    patient_2_with_same_uid.details.uid = patient_1_with_same_uid.details.uid;
                    request(PatientRequest(facility_user,patient_2_with_same_uid.details).post(), function(err,res,body){
                        patient_2_with_same_uid.hid = body.id;
                        patient_list["patient_2_with_same_uid"] = patient_2_with_same_uid;
                        done();
                    });
                });


            },
            function createPatientsToMergeWithMatchingBinBrn(done){
                var patient_1_with_same_binbrn = new Patient();
                var patient_2_with_same_binbrn = null;
                request(PatientRequest(facility_user, patient_1_with_same_binbrn.details).post(), function (err, res, body) {
                    console.log(body);
                    patient_1_with_same_binbrn.hid = body.id;
                    patient_list["patient_1_with_same_binbrn"] = patient_1_with_same_binbrn;
                    patient_2_with_same_binbrn = new Patient();
                    patient_2_with_same_binbrn.details.bin_brn = patient_1_with_same_binbrn.details.bin_brn;
                    request(PatientRequest(facility_user,patient_2_with_same_binbrn.details).post(), function(err,res,body){
                        patient_2_with_same_binbrn.hid = body.id;
                        patient_list["patient_2_with_same_binbrn"] = patient_2_with_same_binbrn;
                        done();
                    });
                });


            },
            function createPatientsToMergeWithMatchingNameAndAddress(done){
                var patient_1_with_matching_name_and_address = new Patient();
                var patient_2_with_matching_name_and_address = null;
                request(PatientRequest(facility_user, patient_1_with_matching_name_and_address.details).post(), function (err, res, body) {
                    console.log(body);
                    patient_1_with_matching_name_and_address.hid = body.id;
                    patient_list["patient_1_with_matching_name_and_address"] = patient_1_with_matching_name_and_address;
                    patient_2_with_matching_name_and_address = new Patient();
                    patient_2_with_matching_name_and_address.details.given_name = patient_1_with_matching_name_and_address.details.given_name;
                    patient_2_with_matching_name_and_address.details.present_address = patient_1_with_matching_name_and_address.details.present_address;
                    request(PatientRequest(facility_user,patient_2_with_matching_name_and_address.details).post(), function(err,res,body){
                        patient_2_with_matching_name_and_address.hid = body.id;
                        patient_list["patient_2_with_matching_name_and_address"] = patient_2_with_matching_name_and_address;
                        done();
                    });
                });


            },
            function writeDetailsToFile(done) {
                console.log(patient_list);
                var contents = fs.writeFileSync(__dirname + "/../data/duplicate_patients_for_merge.json", JSON.stringify(patient_list));
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

