var EntityRequest = function(request){
    var get = function()
    {
        var request_content = {
            method :'GET',
            headers : request.headers,
            uri : request.uri

        };
        console.log(request_content);
        return request_content;

    };

    var post = function()
    {
        var request_content = {
            method: 'POST',
            'url': request.uri,
            'headers': request.headers,
            'body': request.body,
            'json': request.isJSON

        };
        console.log(request_content);
        return request_content;

    };

    var put = function()
    {
        var request_content = {
            method: 'PUT',
            'url': request.uri,
            'headers': request.headers,
            'json': request.isJSON,
            'body': request.body
        };
        console.log(request_content);
        return request_content;
    };

    var del = function()
    {
        var request_content = {
            method: 'DELETE',
            'url': request.uri,
            'headers': request.headers,
            'json': true,
            'body': request.body
        };
        console.log(request_content);
        return request_content;

    };


    return {
        get : get,
        post : post,
        put : put,
        del : del
    }

};

exports.EntityRequest = EntityRequest;