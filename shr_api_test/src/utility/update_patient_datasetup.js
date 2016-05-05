var request = require('request');
var User = require('./../../src/data/user');
var Patient = require('./../../src/entity/patient').PatientWithUID;
var SSORequest = require('./../../src/request/sso').SSORequest;
var PatientRequest = require('./../../src/request/patient').PatientRequest;
var async = require('async');
var fs = require('fs');
var util = require("util");
exports.PatientUpdation = function PatientUpdation() {
    var patient_list = {};
    var facility_user = new User("facility");

    var patientCreationMethodList = function (completed) {
        return [
            function getAccessToken(done) {
                request(new SSORequest(facility_user).post(), function (err, httpResponse, body) {
                    util.log(body);
                    facility_user.access_token = JSON.parse(httpResponse.body).access_token;
                    util.log(facility_user);
                    done();
                });

            },
            function createPatientForDuplicateRequest(done) {
                var patient_with_duplicate_details = new Patient();

                request(PatientRequest(facility_user, patient_with_duplicate_details.details).post(), function (err, res, body) {
                    util.log(body);
                    patient_with_duplicate_details.hid = body.id;
                    patient_list["duplicate"] = patient_with_duplicate_details;
                    util.log(patient_with_duplicate_details);
                    done();
                });

            },
            function createPatientForCommonUID(done) {
                var patient_for_common_uid = new Patient();

                request(PatientRequest(facility_user, patient_for_common_uid.details).post(), function (err, res, body) {
                    util.log(body);
                    patient_for_common_uid.hid = body.id;
                    patient_list["common_uid"] = patient_for_common_uid;

                    done();
                });

            },
            function createPatientForCommonNID(done) {
                var patient_for_common_nid = new Patient();

                request(PatientRequest(facility_user, patient_for_common_nid.details).post(), function (err, res, body) {
                    util.log(body);
                    patient_for_common_nid.hid = body.id;
                    patient_list["common_nid"] = patient_for_common_nid;
                    done();
                });

            },
            function createPatientForCommonbinbrn(done) {
                var patient_for_common_binbrn = new Patient();

                request(PatientRequest(facility_user, patient_for_common_binbrn.details).post(), function (err, res, body) {
                    util.log(body);
                    patient_for_common_binbrn.hid = body.id;
                    patient_list["common_binbrn"] = patient_for_common_binbrn;
                    done();
                });

            },
            function createPatientForCommonNIDAndUID(done) {
                var patient_for_common_uid_and_nid = new Patient();

                request(PatientRequest(facility_user, patient_for_common_uid_and_nid.details).post(), function (err, res, body) {
                    util.log(body);
                    patient_for_common_uid_and_nid.hid = body.id;
                    patient_list["common_uid_and_nid"] = patient_for_common_uid_and_nid;
                    done();
                });

            },

            function createPatientForCommonnidANDBinBrn(done) {

                var patient_for_common_nid_and_binbrn = new Patient();
                request(PatientRequest(facility_user, patient_for_common_nid_and_binbrn.details).post(), function (err, res, body) {
                    util.log(body);
                    patient_for_common_nid_and_binbrn.hid = body.id;
                    patient_list["common_nid_and_binbrn"] = patient_for_common_nid_and_binbrn;
                    done();
                });

            },
            function createPatientForCommonUIDAndBinBrn(done) {
                var patient_for_common_uid_and_binbrn = new Patient();

                request(PatientRequest(facility_user, patient_for_common_uid_and_binbrn.details).post(), function (err, res, body) {
                    util.log(body);
                    patient_for_common_uid_and_binbrn.hid = body.id;
                    patient_list["common_uid_and_binbrn"] = patient_for_common_uid_and_binbrn;
                    done();
                });

            },

            function createPatientForCommonUIDBinBrnAndNID(done) {
                var patient_for_common_uid_nid_and_binbrn = new Patient();
                request(PatientRequest(facility_user, patient_for_common_uid_nid_and_binbrn.details).post(), function (err, res, body) {
                    util.log(body);
                    patient_for_common_uid_nid_and_binbrn.hid = body.id;
                    patient_list["common_uid_nid_and_binbrn"] = patient_for_common_uid_nid_and_binbrn;
                    done();
                });

            },

            function createPatientsForMerge(done) {
                var patient_1_for_merge = new Patient();
                var patient_2_for_merge = null;
                request(PatientRequest(facility_user, patient_1_for_merge.details).post(), function (err, res, body) {
                    util.log(body);
                    patient_1_for_merge.hid = body.id;
                    patient_list["patient_1_for_merge"] = patient_1_for_merge;
                    patient_2_for_merge = new Patient();
                    patient_2_for_merge.details.nid = patient_1_for_merge.details.nid;
                    request(PatientRequest(facility_user, patient_2_for_merge.details).post(), function (err, res, body) {
                        patient_2_for_merge.hid = body.id;
                        patient_list["patient_2_for_merge"] = patient_2_for_merge;
                        done();
                    });
                });


            },
            function createPatientsForMergeWithCatchmentChange(done) {
                var patient_1 = new Patient();
                var patient_2 = null;
                request(PatientRequest(facility_user, patient_1.details).post(), function (err, res, body) {
                    util.log(body);
                    patient_1.hid = body.id;
                    patient_list["patient_1_merge_with_catchment_change"] = patient_1;
                    patient_2 = new Patient();
                    patient_2.details.nid = patient_1.details.nid;
                    request(PatientRequest(facility_user, patient_2.details).post(), function (err, res, body) {
                        patient_2.hid = body.id;
                        patient_list["patient_2_merge_with_catchment_change"] = patient_2;
                        done();
                    });
                });
            },

            function updatePatientDetails(done) {
                util.log(patient_list);
                var contents = fs.writeFileSync(__dirname + "/../data/updatable_patient.json", JSON.stringify(patient_list));
                done();
            }


        ];

    };

    return {
        setupData: patientCreationMethodList
    }
};

