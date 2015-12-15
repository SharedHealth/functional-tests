"use strict"

exports.ConceptDrug = function ConceptDrug(name, reference_uuid, base_uri)
{
    var name = name;
    var reference_uuid = reference_uuid;

    var concept_uri = base_uri + "drugs" + "/" + reference_uuid;


    return {
            name : name,
            concept_code : reference_uuid,
            concept_uri : concept_uri
        };

};

//exports.ObservationConcept = function Observation(name, concept_uuid, base_uri, ) {
//
//}

