module.exports = function ( hid, user_detail, payload)
{



    function EncounterRequest(hid,user_detail,  payload)
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
    }
    return new EncounterRequest(hid, user_detail, payload);
};