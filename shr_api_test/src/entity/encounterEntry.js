var et = require("elementtree");
var element = et.Element;
var subelement = et.SubElement;
var etree = et.ElementTree;
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
        var title = subelement(entry,"title");
        title.text = "Encounter";
        var id = subelement(entry, "id");
        id.text = "urn:" + uid;
        var updated = subelement(entry, "updated");
        updated.text = isoDateTime;

    };

    var addContent = function()
    {
        var content = subelement(entry,"content");
        content.set("type", "text/xml");
        var encounter = subelement(content, "Encounter");
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
        var subject = subelement(encounter, "subject");
        var subjectReference =subelement(subject, "reference");
        subjectReference.set("value", detail.patient_uri)
        var subjectDisplay = subelement(subject, "display");
        subjectDisplay.set("value", detail.hid);
        var participant = subelement(encounter, "participant");
        var individual = subelement(participant, "individual");
        var participantReference = subelement(individual, "reference");
        participantReference.set("value", detail.provider_uri);
        var indication = subelement(encounter, "indication");
        var indicationReference = subelement(indication, "reference");
        indicationReference.set("value", "urn:" + uid);
        var indicationDisplay = subelement(indication, "display");
        indicationDisplay.set("value", "Encounter");
        var serviceProvider = subelement(encounter, "serviceProvider");
        var serviceProviderReference = subelement(serviceProvider, "reference");
        serviceProviderReference.set("value", detail.facility_uri);
    }

    var encounterReference = function(composition)
    {
        var encounterSection = subelement(composition, "encounter");
        var encounterReference = subelement(encounterSection, "reference");
        encounterReference.set("value", "urn:" + uid);
        var encounterDisplay = subelement(encounterSection, "display");
        encounterDisplay.set("value", "Encounter");

    };

    var sectionForComposition = function(parent)
    {
        var section = subelement(parent, "section");
        var content = subelement(section, "content");
        var reference = subelement(content, "reference");
        reference.set("value","urn:" + uid );
        var display = subelement(content, "display");
        display.set("value", "Encounter");
        return section;
    };

    var get = function()
    {
        initialize();
        addContent();
    };

    return {
        get : get,
        getSection : sectionForComposition,
        getEncounterReference : encounterReference
    };

};

