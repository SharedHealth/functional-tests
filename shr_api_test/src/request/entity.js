var config = require('./../Config').config;
var EntityRequest = function(request){
    var get = function()
    {
        return {
            method :'GET',
            headers : request.headers,
            uri : request.uri

        };

    };

    var post = function()
    {
        return {
            method: 'POST',
            'url': request.uri,
            'headers': request.headers,
            'body': request.body,
            'json': request.isJSON

        };

    };

    var put = function()
    {
        return {
            method: 'PUT',
            'url': request.uri,
            'headers': request.headers,
            'json': true,
            'body': request.body
        };
    };

    return {
        get : get,
        post : post,
        put : put
    }

};

var PatientRequest = function(user_detail, patient_detail){

    var headers = function()
    {
        return {
            "Content-Type": "application/json",
            "X-Auth-Token": user_detail.access_token,
            "From": user_detail.email,
            "client_id": user_detail.client_id
        }
    }

    var uri = config.mci_protocol + "://" +  (config.mci_dns_name || (config.mci_server_ip + ":" + config.mci_server_port)) + "/api/v1"

    var post = function(){
        return EntityRequest({uri : uri + "/patients", body : patient_detail, headers : headers(), isJSON:true}).post()
    };

    var updatePost = function(hid){
        return EntityRequest({'uri' : uri + "/patients", body : {'gender': 'F'}, headers : headers(), isJSON:true}).post()
    };

    var updateUsingPut = function (hid) {
        return EntityRequest({uri : uri + "/patients/" + hid, body : {'gender': 'F'}, headers : headers(), isJSON:true}).put()
    };

    var getPatientDetailsByHid = function (hid) {
        return EntityRequest({uri : uri + "/patients/" + hid, headers : headers()}).get();
    };

    var getPatientDetailsByNid = function (nid) {
        return EntityRequest({uri : uri + "/patients/?nid=" + nid, headers : headers()}).get();
    };


    var getPatientDetailsByBinBrn = function (binBrn) {
        return EntityRequest({uri : uri + "/patients/?bin_brn=" + binBrn, headers : headers()}).get();
    };

    var getPatientDetailsHouseHoldCode = function (houseHoldCode) {
        return EntityRequest({uri : uri + "/patients/?household_code=" + houseHoldCode, headers : headers()}).get();
    };

    var getAllPatientsByCatchment = function (catchment) {

        return EntityRequest({uri : uri + "/catchments/" + catchment + "/patients", headers : headers()}).get();
    };

    var getAllPendingApprovalPatientsByCatchment = function (catchment) {
        return EntityRequest({uri : uri + "/catchments/" + catchment  + "/approvals", headers : headers()}).get();
    };

    var getAllPendingApprovalDetailsByHid = function (catchment,hid) {
        return EntityRequest({uri : uri + "/catchments/" + catchment  + "/approvals/" + hid, headers : headers()}).get();
    };

    var acceptOrRejectUsingPut = function (catchment,hid) {
        var url = uri + "/catchments/" + catchment + "/approvals/" + hid;
        return EntityRequest({uri : url, body : {'gender': 'F'}, headers : headers(), isJSON : true }).put()
    };

    var getAuditLogsByHid = function (hid) {
        var url = uri +  "/audit/patients/" + hid;
        return EntityRequest({uri : url, headers : headers() }).get();
    };

    var getUpdateFeedForSHR = function (hid) {
        var url = uri +  "/feed/patients?hid=" + hid;
        return EntityRequest({uri : url, headers : headers() }).get();
    };

    var getLocationDetails = function (catchment) {
        var url = uri +  "/locations?parent="+catchment;
        return EntityRequest({uri : url, headers : headers() }).get();

    };

    var getPatientDetailsByNameLocation = function (givenName,surName,address) {
        var url = uri +  "/patients/?given_name="+givenName+"&sur_name="+surName+"&present_address="+address;
        return EntityRequest({uri : url, headers : headers() }).get();
    };


    return {

        post : post,
        updatePost : updatePost,
        updateUsingPut : updateUsingPut,
        getPatientDetailsByHid : getPatientDetailsByHid,
        getPatientDetailsByNid : getPatientDetailsByNid,
        getPatientDetailsByBinBrn : getPatientDetailsByBinBrn,
        getPatientDetailsHouseHoldCode : getPatientDetailsHouseHoldCode,
        getAllPatientsByCatchment : getAllPatientsByCatchment,
        getAllPendingApprovalPatientsByCatchment : getAllPendingApprovalPatientsByCatchment,
        getAllPendingApprovalDetailsByHid : getAllPendingApprovalDetailsByHid,
        acceptOrRejectUsingPut : acceptOrRejectUsingPut,
        getAuditLogsByHid : getAuditLogsByHid,
        getUpdateFeedForSHR : getUpdateFeedForSHR,
        getLocationDetails : getLocationDetails,
        getPatientDetailsByNameLocation : getPatientDetailsByNameLocation
    }
};

exports.PatientRequest = PatientRequest

var EncounterRequest = function (hid, user_detail, payload) {

    var user_detail = user_detail;
    var hid = hid;
    var encounter_payload = payload || "";
    var ip = config.shr_server_ip;
    var port = config.shr_server_port;
    var protocol = config.shr_protocol || 'http';
    var url = config.shr_server_url || (protocol + "://" + ip + ":" + port);
    var headers = function()
    {
        return {
            "Content-Type": "application/xml; charset=utf-8",
            "X-Auth-Token": user_detail.access_token,
            "From": user_detail.email,
            "client_id": user_detail.client_id
        }
    };

    var post = function()
    {
        return  EntityRequest({'uri' : url + "/patients/" + hid + "/encounters" , headers : headers(), 'body': encounter_payload.details, isJSON : false }).post();
    };

    var get = function()
    {
        return  EntityRequest({'uri' : url + "/patients/" + hid + "/encounters" , headers : headers() }).get();
    }

    return {
        EntityRequest : EntityRequest,
        post: post,
        headers: headers,
        get : get
    }

};

exports.EncounterRequest = EncounterRequest;

var CatchmentRequest = function (user_detail, catchment_area_code) {
    var ip = config.shr_server_ip;
    var port = config.shr_server_port;
    var user_detail = user_detail;
    var catchment_area_code = catchment_area_code;
    var protocol = config.shr_protocol || 'http';
    var headers = function()
    {
        return {
            "Content-Type": "application/json",
            "X-Auth-Token": user_detail.access_token,
            "From": user_detail.email,
            "client_id": user_detail.client_id
        }
    };
    var url = config.shr_server_url || (protocol + "://" + ip + ":" + port);
    var pad  = function (n) {
        return n < 10 ? '0' + n : n;
    };
    var ISODateString = function () {
        var d = new Date();
        return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) + 'T' + pad(d.getHours()) + '%3A' + pad(d.getMinutes()) + '%3A' + pad(d.getSeconds()) + "%2B0530";

    };

   var  uri =function() {
        return url + "/v1/catchments/" + catchment_area_code + "/encounters?updatedSince=" + ISODateString();
    }
    var get = function()
    {
        return  EntityRequest({'uri' : uri()  , headers : headers() }).get();
    }

    return {
        get : get
    }
};




//CatchmentRequest.prototype = new EntityRequest();
//
//CatchmentRequest.prototype.pad =

//CatchmentRequest.prototype.ISODateString = function () {
//    var d = new Date();
//    return d.getFullYear() + '-' + this.pad(d.getMonth() + 1) + '-' + this.pad(d.getDate()) + 'T' + this.pad(d.getHours()) + '%3A' + this.pad(d.getMinutes()) + '%3A' + this.pad(d.getSeconds()) + "%2B0530";
//
//};

//CatchmentRequest.prototype.uri =function() {
//    return this.url + "/v1/catchments/" + this.catchment_area_code + "/encounters?updatedSince=" + this.ISODateString();
//}

exports.CatchmentRequest = CatchmentRequest;

exports.SSORequest = function (user_detail) {
    var config = require('./../Config').config;
    this.ip = config.sso_server_ip;
    this.port = config.sso_server_port;
    this.url = config.sso_server_url || "http://" + this.ip + ":" + this.port + "/signin";
    this.user_detail = user_detail;

    this.headers = function () {
        return {
            'X-Auth-Token': this.user_detail.api_token,
            'client_id': this.user_detail.client_id
        };
    };

    this.sso_form_data = function () {
        return {
            email: this.user_detail.email,
            password: this.user_detail.password
        };
    };

    this.post = function () {
        return {
            method: 'POST',
            url: this.url,
            headers: this.headers(),
            formData: this.sso_form_data()
        };
    };

    this.postBy = function (poster) {
        return {
            method: 'POST',
            url: this.url,
            headers: {
                'X-Auth-Token': poster.api_token,
                'client_id': poster.client_id
            },
            formData: this.sso_form_data()
        };
    };
};