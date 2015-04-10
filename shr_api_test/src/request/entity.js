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

exports.EntityRequest = EntityRequest;