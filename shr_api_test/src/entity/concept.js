"use strict"

exports.Concept = function Concept(name, concept_code,concept_code_value, reference_code, reference_code_value, base_uri, type)
{
    var name = name;
    var concept_code = concept_code;
    var concept_code_value = concept_code_value;
    var reference_code = reference_code;
    var reference_code_value = reference_code_value;
    var type = type;
    var concept_uri = base_uri + "concepts" + "/" + concept_code;
    var reference_uri = base_uri + "referenceterms" + "/" + reference_code;
    var drug_uri = base_uri + "drugs" + "/" + reference_code;


    return {
            name : name,
            concept_code: concept_code,
            reference_code : reference_code,
            concept_code_value : concept_code_value,
            reference_code_value : reference_code_value,
            concept_uri : concept_uri,
            reference_uri : reference_uri,
            drug_uri : drug_uri

        };

};

