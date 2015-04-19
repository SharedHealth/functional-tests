var et = require("elementtree");
var pd = require("pretty-data").pd;
var element = et.Element;
var subelement = et.SubElement;
var etree = et.ElementTree;
var uuid = require("uuid");
var isodate = require('../../src/utility/isodate').isodate;
var isoDateTime = new isodate().isoDate();

exports.Feed = function feed(detail,confidentiality)
{
    var root = element("feed");
    var uid = uuid.v4();
    var detail = detail;
    console.log(detail);
    var withImmunizationEntry = function()
    {
        var immunization = new ImmunizationEntry(root,detail);
        var encounter = new EncounterEntry(root,detail);
        var composition =new CompositionEntry(root,confidentiality,encounter,[encounter,immunization], detail);
        initialize();
        immunization.get();
        encounter.get();
        composition.get();
        return new etree(root).write({'xml_declaration': true})
    };

    var initialize = function()
    {
        root.set("xmlns", "http://www.w3.org/2005/Atom");
        var title = subelement(root, "title");
        title.text = "Encounter";
        var id = subelement(root, "id");
        id.text = "urn:" + uid;
        var updated = subelement(root, "updated");
        updated.text = isoDateTime;
        var author = subelement(root, "author");
        var uri = subelement(author, "uri");
        uri.text = detail.facility_uri;
    }

    return {
        withImmunizationEntry : withImmunizationEntry
    };
}

function EncounterEntry(root, detail)
{
    var entry = subelement(root,"entry");
    var uid = uuid.v4();
    var detail = detail;

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
        encounterType.set("value", "Consultation");
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

}

function ImmunizationEntry(root,detail)
{
    var entry = subelement(root, "entry");
    var uid = uuid.v4();

    var initialize= function()
    {
        var title = subelement(entry, "title");
        title.text = "Immunization";
        var id = subelement(entry, "id");
        id.text = "urn:" + uid;
        var updated = subelement(entry,"updated");
        updated.text = isoDateTime;
    };

    var addContent = function()
    {
        var content = subelement(entry,"content");
        content.set("type", "text/xml");
        var immunization = subelement(content, "Immunization");
        immunization.set("xmlns", detail.clinical_standard);
        var identifier = subelement(immunization, "identifier");
        var immunizationValue = subelement(identifier, "value");
        immunizationValue.set("value", "urn:" + uid);
        var date = subelement(immunization, "date");
        date.set("value", isoDateTime);
        var vaccineType = subelement(immunization, "vaccineType");
        var coding = subelement(vaccineType,"coding");
        var system = subelement(coding, "system");
        system.set("value", detail.entry["Immunization"].uri);
        var code = subelement(coding, "code");
        code.set("value",detail.entry["Immunization"].code );
        var codingDisplay = subelement(coding, "display")
        codingDisplay.set("value", "BCG");
        var subject = subelement(immunization, "subject");
        var subjectReference = subelement(subject, "reference");
        subjectReference.set("value", detail.patient_uri );
        var subjectDisplay = subelement(subject, "display");
        subjectDisplay.set("value", detail.hid);
        var reported = subelement(immunization, "reported");
        reported.set("value", "true");
        var requester = subelement(immunization, "requester");
        var requesterReference = subelement(requester, "reference");
        requesterReference.set("value", detail.provider_uri);
    };

    var sectionForComposition = function(parent)
    {
        var section = subelement(parent, "section");
        var content = subelement(section, "content");
        var reference = subelement(content, "reference");
        reference.set("value","urn:" + uid );
        var display = subelement(content, "display");
        display.set("value", "Immunization");
        return section;
    }

    var get = function()
    {
        initialize();
        addContent();

    };

    return {
        get : get,
        getSection : sectionForComposition
    };
};

function CompositionEntry(root, isConfidential,encounterEntry, entries,detail) {
    var entry = subelement(root, "entry");
    var encounterEntry = encounterEntry;
    var uid = uuid.v4();

    var initialize = function () {
        var title = subelement(entry, "title");
        title.text = "Composition";
        var id = subelement(entry, "id");
        id.text = "urn:" + uid;
        var updated = subelement(entry, "updated");
        updated.text = isoDateTime;
    }

    var addContent = function () {
        var content = subelement(entry, "content");
        var composition = subelement(content, "Composition");
        composition.set("xmlns", detail.clinical_standard);
        var identifier = subelement(composition, "identifier");
        var identifierValue = subelement(identifier, "value");
        identifierValue.set("value", "urn:" + uid);
        var date = subelement(composition, "date");
        date.set("value", isoDateTime);
        var status = subelement(composition, "status");
        status.set("value", "final");
        if (isConfidential == 'Yes') {
            var confidentiality = subelement(composition, "confidentiality");
            var system = subelement(confidentiality, "system");
            system.set("value", detail.clinical_standard + "/v3/Confidentiality");
            var code = subelement(confidentiality, "code");
            code.set("value", "V");
            var confidentialityDisplay = subelement(confidentiality, "display");
            confidentialityDisplay.set("value", "very restricted");
        }
        var subject = subelement(composition, "subject");
        var subjectReference = subelement(subject, "reference");


        subjectReference.set("value", detail.patient_uri);
        var subjectDisplay = subelement(subject, "display");
        subjectDisplay.set("value", detail.hid);
        var author = subelement(composition, "author");
        var authorReference = subelement(author, "reference");
        authorReference.set("value", detail.facility_uri);
        encounterEntry.getEncounterReference(composition);
        for (var index = 0; index < entries.length; index++) {
            entries[index].getSection(composition);
        }
    };

    var get = function () {
        initialize();
        addContent();
    };

    return {
        get: get
    };
};







