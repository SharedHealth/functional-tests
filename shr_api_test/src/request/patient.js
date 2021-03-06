var config = require('./../Config').config;
var EntityRequest = require('./entity').EntityRequest;
var PatientRequest = function (user_detail, patient_detail) {

    var headers = function () {
        return {
            "Content-Type": "application/json",
            "X-Auth-Token": user_detail.access_token,
            "From": user_detail.email,
            "client_id": user_detail.client_id
        }
    };

    var uri = config.mci_protocol + "://" + (config.mci_dns_name || (config.mci_server_ip + ":" + config.mci_server_port)) + "/api/v1"

    var post = function () {
        return EntityRequest({uri: uri + "/patients", body: patient_detail, headers: headers(), isJSON: true}).post()
    };

    var updatePost = function () {
        return EntityRequest({'uri': uri + "/patients", body: {'gender': 'F'}, headers: headers(), isJSON: true}).post()
    };

    var makePatientInactive = function (inActiveHid, activeHid) {
        return EntityRequest({
            'uri': uri + "/mergerequest/" + inActiveHid,
            body: {'active': false, 'merged_with': activeHid},
            headers: headers(),
            isJSON: true
        }).put()
    };

    var updateActiveFieldUsingPut = function (inActiveHid) {
        return EntityRequest({
            uri: uri + "/patients/" + inActiveHid,
            body: {'active': true},
            headers: headers(),
            isJSON: true
        }).put()
    };

    var mergeWith = function (to_be_active_patient, to_be_inactive_patient) {
        return EntityRequest(
            {
                uri: uri + "/patients/duplicates",
                body: {
                    "action": "MERGE",
                    "patient1": {
                        "hid": to_be_inactive_patient.hid,
                        "active": false,
                        "merged_with": to_be_active_patient.hid
                    },
                    "patient2": {
                        "hid": to_be_active_patient.hid,
                        "active": true
                    }
                },
                headers: headers(),
                isJSON: true

            }
        ).put();
    };

    var retainBoth = function (patient_1, patient_2) {
        return EntityRequest(
            {
                uri: uri + "/patients/duplicates",
                body: {
                    "action": "RETAIN_ALL",
                    "patient1": {
                        "hid": patient_1.hid
                    },
                    "patient2": {
                        "hid": patient_2.hid
                    }
                },
                headers: headers(),
                isJSON: true

            }
        ).put();
    };

    var mergeWithCatchmentChange = function (to_be_active_patient, to_be_inactive_patient) {
        return EntityRequest(
            {
                uri: uri + "/patients/" + inActiveHid,
                body: {
                    "action": "MERGE",
                    "patient1": {
                        "hid": to_be_inactive_patient.hid,
                        "active": false,
                        "merged_with": to_be_active_patient.hid
                    },
                    "patient2": {
                        "hid": to_be_active_patient,
                        "active": true,
                        "present_address": to_be_inactive_patient.details.present_address
                    }
                },
                headers: headers(),
                isJSON: true

            }
        ).put();
    };


    var updateUsingPut = function (hid) {
        return EntityRequest({
            uri: uri + "/patients/" + hid.toString(),
            body: {'gender': 'F'},
            headers: headers(),
            isJSON: true
        }).put()
    };

    var updateUsingPutWithGivenValues = function (hid, givenValue) {
        return EntityRequest({
            uri: uri + "/patients/" + hid.toString(),
            body: givenValue,
            headers: headers(),
            isJSON: true
        }).put()
    };

    var multipleUpdateUsingPut = function (hid) {
        return EntityRequest({
            uri: uri + "/patients/" + hid,
            body: {"occupation": "04", 'given_name': 'updatedFirstName'},
            headers: headers(),
            isJSON: true
        }).put()
    };

    var getPatientDetailsByHid = function (hid) {
        return EntityRequest({uri: uri + "/patients/" + hid, headers: headers()}).get();
    };

    var getPatientDetailsByNid = function (nid) {
        return EntityRequest({uri: uri + "/patients/?nid=" + nid, headers: headers()}).get();
    };


    var getPatientDetailsByBinBrn = function (binBrn) {
        return EntityRequest({uri: uri + "/patients/?bin_brn=" + binBrn, headers: headers()}).get();
    };

    var getPatientDetailsHouseHoldCode = function (houseHoldCode) {
        return EntityRequest({uri: uri + "/patients/?household_code=" + houseHoldCode, headers: headers()}).get();
    };

    var getAllPatientsByCatchment = function (catchment) {

        return EntityRequest({uri: uri + "/catchments/" + catchment + "/patients", headers: headers()}).get();
    };

    var getAllPendingApprovalPatientsByCatchment = function (catchment) {
        return EntityRequest({uri: uri + "/catchments/" + catchment + "/approvals", headers: headers()}).get();
    };

    var getAllPendingApprovalDetailsByHid = function (catchment, hid) {
        return EntityRequest({uri: uri + "/catchments/" + catchment + "/approvals/" + hid, headers: headers()}).get();
    };

    var acceptRequest = function (catchment, hid) {
        var url = uri + "/catchments/" + catchment + "/approvals/" + hid;
        return EntityRequest({uri: url, body: {'gender': 'F'}, headers: headers(), isJSON: true}).put();
    };

    var rejectRequest = function (catchment, hid) {
        var url = uri + "/catchments/" + catchment + "/approvals/" + hid;
        return EntityRequest({uri: url, body: {'gender': 'F'}, headers: headers(), isJSON: true}).del();
    };

    var multipleRequestAccept = function (catchment, hid) {
        var url = uri + "/catchments/" + catchment + "/approvals/" + hid;
        return EntityRequest({
            uri: url,
            body: {"occupation": "04", 'given_name': 'updatedFirstName'},
            headers: headers(),
            isJSON: true
        }).put()
    };

    var multipleRequestReject = function (catchment, hid) {
        var url = uri + "/catchments/" + catchment + "/approvals/" + hid;
        return EntityRequest({
            uri: url,
            body: {"occupation": "04", 'given_name': 'updatedFirstName'},
            headers: headers(),
            isJSON: true
        }).del();
    };


    var getAuditLogsByHid = function (hid) {
        var url = uri + "/audit/patients/" + hid;
        return EntityRequest({uri: url, headers: headers()}).get();
    };

    var getUpdateFeedForSHR = function (hid) {
        var url = uri + "/feed/patients?hid=" + hid;
        return EntityRequest({uri: url, headers: headers()}).get();
    };

    var getLocationDetails = function (catchment) {
        var url = uri + "/locations?parent=" + catchment;
        return EntityRequest({uri: url, headers: headers()}).get();

    };

    var getPatientDetailsByNameLocation = function (givenName, surName, address) {
        var url = uri + "/patients/?given_name=" + givenName + "&sur_name=" + surName + "&present_address=" + address;
        return EntityRequest({uri: url, headers: headers()}).get();
    };


    return {

        post: post,
        updatePost: updatePost,
        updateUsingPut: updateUsingPut,
        makePatientInactive: makePatientInactive,
        updateActiveFieldUsingPut: updateActiveFieldUsingPut,
        multipleUpdateUsingPut: multipleUpdateUsingPut,
        getPatientDetailsByHid: getPatientDetailsByHid,
        getPatientDetailsByNid: getPatientDetailsByNid,
        getPatientDetailsByBinBrn: getPatientDetailsByBinBrn,
        getPatientDetailsHouseHoldCode: getPatientDetailsHouseHoldCode,
        getAllPatientsByCatchment: getAllPatientsByCatchment,
        getAllPendingApprovalPatientsByCatchment: getAllPendingApprovalPatientsByCatchment,
        getAllPendingApprovalDetailsByHid: getAllPendingApprovalDetailsByHid,
        acceptRequest: acceptRequest,
        rejectRequest: rejectRequest,
        multipleRequestAccept: multipleRequestAccept,
        multipleRequestReject: multipleRequestReject,
        getAuditLogsByHid: getAuditLogsByHid,
        getUpdateFeedForSHR: getUpdateFeedForSHR,
        getLocationDetails: getLocationDetails,
        getPatientDetailsByNameLocation: getPatientDetailsByNameLocation,
        updateUsingPutWithGivenValues: updateUsingPutWithGivenValues,
        mergeWith: mergeWith,
        mergeWithCatchmentChange: mergeWithCatchmentChange,
        retainBoth: retainBoth

    }
};

exports.PatientRequest = PatientRequest;
