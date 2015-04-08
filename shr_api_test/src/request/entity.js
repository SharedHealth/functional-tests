var config = require('./../Config').config;
var EntityRequest = function() {};
EntityRequest.prototype.headers = function (content_type) {
    return {
        "Content-Type": content_type || "application/json",
        "X-Auth-Token": this.user_detail.access_token,
        "From": this.user_detail.email,
        "client_id": this.user_detail.client_id
    }
};

 EntityRequest.prototype.get = function (uri) {
     return {
         method: 'GET',
         headers: this.headers(),
         'uri': uri || this.uri()
     }
 };

EntityRequest.prototype._post = function (body, url) {
    return {
        method: 'POST',
        'url': url || this.uri(),
        'headers': this.headers(),
        'json': true,
        'body': body

    };
};

EntityRequest.prototype._put = function (body, url) {
    return {
        method: 'PUT',
        'url': url || this.uri(),
        'headers': this.headers(),
        'json': true,
        'body': body

    };
};


EntityRequest.prototype.approvalPost = function (body, url) {
    return {
        method: 'PUT',
        'url': url || this.approvalUrl(),
        'headers': this.headers(),
        'json': true,
        'body': body

    };
};


var PatientRequest = function(user_detail, patient_detail)
{
    this.user_detail = user_detail;
    this.server = config.mci_dns_name || config.mci_server_ip + ":" + config.mci_server_port;
    this.patient_detail = patient_detail;
};

PatientRequest.prototype = new EntityRequest();
PatientRequest.prototype.uri = function() {
    return "https://" + this.server + "/api/v1/patients";
};

PatientRequest.prototype.post = function() {
    return this._post(this.patient_detail);
};

PatientRequest.prototype.updatePost = function (hid) {
    return this._post({'gender': 'F'}, this.uri() + "/" + hid)
};

PatientRequest.prototype.updateUsingPut = function (hid) {
    return this._put({'gender': 'F'}, this.uri() + "/" + hid)
};


PatientRequest.prototype.getPatientDetailsByHid = function (hid) {
    return this.get("https://" + this.server + "/api/v1/patients/" + hid);
};

PatientRequest.prototype.getPatientDetailsByNid = function (nid) {
    return this.get("https://" + this.server + "/api/v1/patients/?nid=" + nid);
};

PatientRequest.prototype.getPatientDetailsByBinBrn = function (binBrn) {
    return this.get("https://" + this.server + "/api/v1/patients/?bin_brn=" + binBrn);
};

PatientRequest.prototype.getPatientDetailsHouseHoldCode = function (houseHoldCode) {
    return this.get("https://" + this.server + "/api/v1/patients/?household_code=" + houseHoldCode);
};


PatientRequest.prototype.getAllPatientsByCatchment = function (catchment) {
    return this.get("https://" + this.server + "/api/v1/catchments/" + catchment + "/patients");
};

PatientRequest.prototype.getAllPendingApprovalPatientsByCatchment = function (catchment) {
    return this.get("https://" + this.server + "/api/v1/catchments/" + catchment + "/approvals");
};

PatientRequest.prototype.getAllPendingApprovalDetailsByHid = function (catchment,hid) {
    return this.get("https://" + this.server + "/api/v1/catchments/" + catchment + "/approvals/" + hid)
};

PatientRequest.prototype.approvalUrl = function(catchment,hid) {
    return "https://" + this.server + "/api/v1/catchments/" + catchment + "/approvals/" + hid
};

PatientRequest.prototype.acceptOrRejectRequest = function (catchment,hid) {
    return this.approvalPost({'gender': 'F'}, this.approvalUrl(catchment,hid))
};

PatientRequest.prototype.getAuditLogsByHid = function (hid) {
    return this.get("https://" + this.server + "/api/v1/audit/patients/" + hid);
};

PatientRequest.prototype.getUpdateFeedForSHR = function (hid) {
    return this.get("https://" + this.server + "/api/v1/feed/patients?");
};

PatientRequest.prototype.getLocationDetails = function (catchment) {
    return this.get("https://" + this.server + "/api/v1/locations?parent="+catchment);
};

//PatientRequest.prototype.getPatientDetailsByName&PhoneNumber = function (nid) {
//    return this.get("https://" + this.server + "/api/v1/patients/?nid=" + nid);
//};

exports.PatientRequest = PatientRequest;

var EncounterRequest = function (hid, user_detail, payload) {
    var config = require('./../Config').config;
    this.user_detail = user_detail;
    this.hid = hid;
    this.encounter = payload || "";
    this.ip = config.shr_server_ip;
    this.port = config.shr_server_port;
};

EncounterRequest.prototype = new EntityRequest();

EncounterRequest.prototype.uri = function() {
    return "http://" + this.ip + ":" + this.port + "/patients/" + this.hid + "/encounters";
}

EncounterRequest.prototype.post = function () {
    var body = this.encounter.details;
    return {
        method: 'POST',
        url: this.uri(),
        headers: this.headers("application/xml; charset=utf-8"),
        body: body,
        json: false
    };
};

exports.EncounterRequest = EncounterRequest;

var CatchmentRequest = function (user_detail, catchment_area_code) {

    var config = require('./../Config').config;
    this.ip = config.shr_server_ip;
    this.port = config.shr_server_port;
    this.user_detail = user_detail;
    this.catchment_area_code = catchment_area_code;
};

CatchmentRequest.prototype = new EntityRequest();

CatchmentRequest.prototype.pad = function (n) {
    return n < 10 ? '0' + n : n;
};
CatchmentRequest.prototype.ISODateString = function () {
    var d = new Date();
    return d.getFullYear() + '-' + this.pad(d.getMonth() + 1) + '-' + this.pad(d.getDate()) + 'T' + this.pad(d.getHours()) + '%3A' + this.pad(d.getMinutes()) + '%3A' + this.pad(d.getSeconds()) + "%2B0530";

};

CatchmentRequest.prototype.uri =function() {
    return "http://" + this.ip + ":" + this.port + "/v1/catchments/" + this.catchment_area_code + "/encounters?updatedSince=" + this.ISODateString();
}

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