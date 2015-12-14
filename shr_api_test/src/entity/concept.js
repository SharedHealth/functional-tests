"use strict"

exports.Concept = function Concept(name, reference_uuid, base_uri, concept_type)
{
    var name = name;
    var reference_uuid = reference_uuid;
    var concept_uri = base_uri + concept_type + "/" + reference_uuid;


    return {
            name : name,
            concept_code : reference_uuid,
            concept_uri : concept_uri
        };

};

