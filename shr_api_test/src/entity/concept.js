"use strict"

exports.Concept = function Concept(name, code, base_uri, type)
{
    var name = name;
    var code = code;
    var type = type;
    var uri = base_uri + type + "/" + code;

    return {
            name : name,
            code: code,
            uri : uri

        };

};

