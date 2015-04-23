var config = require('./../Config').config;
var EntityRequest = require('./entity').EntityRequest;
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



exports.CatchmentRequest = CatchmentRequest;
