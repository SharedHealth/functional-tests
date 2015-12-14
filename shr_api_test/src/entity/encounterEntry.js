var et = require("elementtree");
var element = et.Element;
var subelement = et.SubElement;
var uuid = require("uuid");
var isodate = require('../../src/utility/isodate').isodate;
exports.EncounterEntry = function EncounterEntry(root, detail)
{
    var entry = subelement(root,"entry");
    var uid = uuid.v4();
    var detail = detail;
    var isoDateTime = new isodate().isoDate();

    var initialize = function()
    {
        var fullURL = subelement(entry,"fullUrl");
        fullURL.set("value", "urn:uuid:" + uid);


        addContent();

    };
    var addContent = function()
    {
        var resource = subelement(entry,"resource");
        var encounter = subelement(resource, "Encounter");
        encounter.set("xmlns", detail.clinical_standard);
        var identifier = subelement(encounter, "identifier");
        var identifierValue = subelement(identifier, "value");
        identifierValue.set("value", "urn:" + uid);
        var status = subelement(encounter, "status");
        status.set("value", "finished");
        var encounterClass = subelement(encounter, "class");
        encounterClass.set("value", "outpatient");
        var encounterType = subelement(encounter, "type");
        var encounterTypeText = subelement(encounterType, "text");
        encounterTypeText.set("value", "Consultation");
        var patient = subelement(encounter, "patient");
        var patientReference =subelement(patient, "reference");
        patientReference.set("value", detail.patient_uri)
        var subjectDisplay = subelement(patient, "display");
        subjectDisplay.set("value", detail.hid);
        var participant = subelement(encounter, "participant");
        var individual = subelement(participant, "individual");
        var participantReference = subelement(individual, "reference");
        participantReference.set("value", detail.provider_uri);
        var serviceProvider = subelement(encounter, "serviceProvider");
        var serviceProviderReference = subelement(serviceProvider, "reference");
        serviceProviderReference.set("value", detail.facility_uri);
    }

    var encounterReference = function(composition)
    {
        var encounterSection = subelement(composition, "encounter");
        var encounterReference = subelement(encounterSection, "reference");
        encounterReference.set("value", "urn:" + uid);
        //var encounterDisplay = subelement(encounterSection, "display");
        //encounterDisplay.set("value", "Encounter");

    };

    var sectionForComposition = function(parent)
    {
        var section = subelement(parent, "section");
        var entry = subelement(section, "entry");
        var reference = subelement(entry, "reference");
        reference.set("value","urn:" + uid );
        var display = subelement(entry, "display");
        display.set("value", "Encounter");
    };

    initialize();
    return {
        getSection : sectionForComposition,
        getEncounterReference : encounterReference,
        uuid : uid
    };

};

