var config = require('./../Config').config;
exports.SSORequest = function (user_detail) {
    var config = require('./../Config').config;
    var ip = config.sso_server_ip;
    var port = config.sso_server_port;
    var url = config.sso_server_url || "http://" + ip + ":" + port + "/signin";
    var user_detail = user_detail;

    var headers = function () {
        return {
            'X-Auth-Token': user_detail.api_token,
            'client_id': user_detail.client_id
        };
    };

    var sso_form_data = function () {
        return {
            email: user_detail.email,
            password: user_detail.password
        };
    };

    var post = function () {
        return {
            method: 'POST',
            url: url,
            headers: headers(),
            formData: sso_form_data()
        };
    };

    var postBy = function (poster) {
        return {
            method: 'POST',
            url: url,
            headers: {
                'X-Auth-Token': poster.api_token,
                'client_id': poster.client_id
            },
            formData: sso_form_data()
        };
    };

    return {
        post : post,
        postBy : postBy
    }
};