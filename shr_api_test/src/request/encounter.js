var config = require('./../Config').config;
var EntityRequest = require('./entity').EntityRequest;
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
    var isEncounterId = function(){
        (encounter_id != "") ? true : false;
    };
    var post = function()
    {
        return  EntityRequest({'uri' : url + "/patients/" + hid + "/encounters" , headers : headers(), 'body': encounter_payload.details, isJSON : false }).post();
    };

    var get = function(encounter_id)
    {
        return  (encounter_id != undefined) ? EntityRequest({'uri' : url + "/patients/" + hid + "/encounters/" + encounter_id , headers : headers() }).get() : EntityRequest({'uri' : url + "/patients/" + hid + "/encounters" , headers : headers() }).get();
    }

    var put = function(encounterId)
    {
        return  EntityRequest({'uri' : url + "/patients/" + hid + "/encounters/" + encounterId , headers : headers(), 'body': encounter_payload.details, isJSON : false }).put();
    }

    var updateEncounterDetails = function(details)
    {
        payload = details;
    }

    return {
        EntityRequest : EntityRequest,
        post: post,
        headers: headers,
        get : get,
        put : put,
        updateEncounterDetails : updateEncounterDetails

    }

};

exports.EncounterRequest = EncounterRequest;



