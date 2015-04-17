var config = require('./../Config').config;
var EntityRequest = function(request){
    var get = function()
    {
        var payload = {
            method :'GET',
            headers : request.headers,
            uri : request.uri

        };

        console.log(payload);
        return payload;

    };

    var post = function()
    {
        var payload = {
            method: 'POST',
            'url': request.uri,
            'headers': request.headers,
            'body': request.body,
            'json': request.isJSON

        };

        console.log(payload);
        return payload;

    };

    var put = function()
    {
        var payload = {
            method: 'PUT',
            'url': request.uri,
            'headers': request.headers,
            'json': true,
            'body': request.body
        };
        console.log(payload);
        return payload;
    };

    var del = function()
    {
        var payload = {
            method: 'DELETE',
            'url': request.uri,
            'headers': request.headers,
            'json': true,
            'body': request.body
        };

        console.log(payload);
        return payload;

    }


    return {
        get : get,
        post : post,
        put : put,
        del : del
    }

};

exports.EntityRequest = EntityRequest;