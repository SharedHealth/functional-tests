var config = require('./../Config').config;
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