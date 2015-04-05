exports.PatientRequest =     function (user_detail, patient_detail) {
        var config = require('./../Config').config;
        this.user_detail = user_detail;
        this.server = config.mci_dns_name;
        this.patient_detail = patient_detail;
        this.headers = function() {
            return {
                "Content-Type" : "application/json",
                "X-Auth-Token" : this.user_detail.access_token,
                "From" : this.user_detail.email,
                "client_id" : this.user_detail.client_id
            };
        };

        this.post = function() {
            return {
                method : 'POST',
                url : "https://" + this.server +  "/api/v1/patients",
                headers : this.headers(),
                json : true,
                body : this.patient_detail
            };
        };

        this.updatePost = function(hid) {
            return {
                url : "https://" + this.server + "/api/v1/patients/" + hid,
                headers : this.headers(),
                json : true,
                body : {"gender" : "F"}
            };
        };

        this.get = function (uri) {
            return {
                method : 'GET',
                headers : this.headers(),
                'uri' : uri || this.uri
            }
        };

        this.getPatientDetailsByHid = function(hid)
        {
            return this.get("https://" + this.server + "/api/v1/patients/" + hid);
        };
        this.getPatientDetailsByNid = function(nid)
        {
            return this.get("https://" + this.server + "/api/v1/patients/?nid=" + nid);
        };

        this.getPatientDetailsByBinBrn = function(binBrn)
        {
            return this.get("https://" + this.server + "/api/v1/patients/?bin_brn=" + binBrn);
        };
        this.getAllPatientsByCatchment = function(catchment)
        {
            return this.get("https://" + this.server + "/api/v1/catchments/"+catchment+"/patients");
        };

        this.getAllPendingApprovalPatientsByCatchment = function(catchment)
        {
            return this.get("https://" + this.server + "/api/v1/catchments/"+catchment+"/approvals");
        }


    };

exports.EncounterRequest =  function (hid,user_detail,  payload)
{
    var config = require('./../Config').config;
    this.user_detail = user_detail;
    this.hid = hid;
    this.encounter = payload || "";
    this.ip =  config.shr_server_ip;
    this.port = config.shr_server_port;
    this.uri = "http://" + this.ip + ":" + this.port + "/patients/" + this.hid + "/encounters";

    this.headers = function(content_type) {

        return {
            "Content-Type" : content_type,
            "X-Auth-Token" : this.user_detail.access_token,
            "From" : user_detail.email,
            "client_id" : user_detail.client_id
        };

    };

    this.get = function() {

        return {
            method : "GET",
            uri: this.uri ,
            headers : this.headers("application/json")
        }
    };


    this.post = function() {
        return {
            method : 'POST',
            url : this.uri,
            headers : this.headers("application/xml; charset=utf-8"),
            body : this.encounter.details,
            json : false
        };
    };
};


exports.CatchmentRequest = function (user_detail, catchment_area_code) {

    var config = require('./../Config').config;
    this.ip = config.shr_server_ip;
    this.port = config.shr_server_port;
    this.user_detail = user_detail;
    this.catchment_area_code = catchment_area_code;
    this.pad =  function (n) { return n < 10 ? '0'+n : n; };
    this.ISODateString = function() {
        var d = new Date();
        return d.getFullYear() + '-' + this.pad(d.getMonth() + 1) + '-' + this.pad(d.getDate()) + 'T' + this.pad(d.getHours()) + '%3A' + this.pad(d.getMinutes()) + '%3A' + this.pad(d.getSeconds()) + "%2B0530";

    };

    this.uri = "http://" + this.ip + ":" + this.port + "/v1/catchments/" + this.catchment_area_code + "/encounters?updatedSince=" + this.ISODateString();


    this.headers = function()
    {
        return {
            "Content-Type" : "application/json",
            "X-Auth-Token" : this.user_detail.access_token,
            "From" : this.user_detail.email,
            "client_id" : this.user_detail.client_id
        }
    }
    this.get = function ()
    {
        return {
            method : 'GET',
            uri : this.uri,
            headers : this.headers()
        }
    }
};

exports.SSORequest = function  (user_detail) {
    var config = require('./../Config').config;
    this.ip = config.sso_server_ip;
    this.port = config.sso_server_port;
    this.user_detail = user_detail;

    this.headers = function() {
        return {
            'X-Auth-Token' : this.user_detail.api_token,
            'client_id' : this.user_detail.client_id
        };
    };

    this.sso_form_data = function() {
        return {
            email : this.user_detail.email,
            password : this.user_detail.password
        };
    };

    this.post = function() {
        return {
            method : 'POST',
            url : "http://" + this.ip + ":" + this.port + "/signin",
            headers : this.headers(),
            formData : this.sso_form_data()
        };
    };

    this.postBy = function(poster)
    {
        return {
            method : 'POST',
            url : "http://" + this.ip + ":" + this.port + "/signin",
            headers :  {
                'X-Auth-Token' : poster.api_token,
                'client_id' :    poster.client_id
            },
            formData :  this.sso_form_data()
        };
    };
};







