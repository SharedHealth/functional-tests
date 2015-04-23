"use strict";
var et = require("elementtree");
var pd = require("pretty-data").pd;
var element = et.Element;
var subelement = et.SubElement;
var etree = et.ElementTree;
var uuid = require("uuid");


/*
Encounter has a Feed
Feed
    has a title
    has an id
    has a updated section
    has an author
    has multiple entry sections;

There are many types of entries. Given below are few of them;
    1. Composition
    2. Encounter
    3.Immunization

    1. Composition entry
    has a title
    has an id
    has an updated section
    has a content section

    2. Encounter entry
        has a title
        has an id
        has an updated section
        has a content section
    3. Immunization entry
        has a title
        has an id
        has an updated section
        has a content

  Content for Composition entry:
         has an identifier
         has a date
         has a status
         may have a confidentiality section
         has a subject
         has an author
         has encounter references;
         has sections for other entry references

    Content for Encounter entry:
             has Encounter section
        Content for Encounter section
                  has identifier
                  has status
                  has class
                  has type
                  has subject
                  has participant
                  has indication
                  has service-provider

    content for immunization entry
        has immunization section
        Content for immunization section
            has identifier
            has date
            has vaccineType
            has subject
            has reported section
            has requester


 */

function feed(hid, confidentiality)
{
    var root = element("feed");
    var confidentiality = confidentiality || 'No';
    var immunization = new ImmunizationEntry(root, hid);
    var encounter = new EncounterEntry(root, hid);
    var composition =new CompositionEntry(root,confidentiality,hid,encounter,[immunization]);
    var uid = uuid.v4();

    var initialize = function()
    {
        root.set("xmlns", "http://www.w3.org/2005/Atom");
        var title = subelement(root, "title");
        title.text = "Encounter";
        var id = subelement(root, "id");
        id.text = "urn:" + uid;
        var updated = subelement(root, "updated");
        updated.text = "2015-03-12T10:35:54+05:30";
        var author = subelement(root, "author");
        var uri = subelement(author, "uri");
        uri.text = "http://hrmtest.dghs.gov.bd/api/1.0/facilities/10000069.json";
    }

    var get = function()
    {

        initialize();
        immunization.get();
        encounter.get();
        composition.get();

        return new etree(root).write({'xml_declaration': true})
    }

    return {
        get : get
    };
}

function CompositionEntry(root,isConfidential, hid,encounterEntry, entries)
{
    var entry = subelement(root,"entry");
    var encounterEntry = encounterEntry;
    var uid = uuid.v4();
    var initialize = function()
    {
        var title = subelement(entry,"title");
        title.text = "Composition";
        var id = subelement(entry, "id");
        id.text = "urn:" + uid;
        var updated = subelement(entry, "updated");
        updated.text = "2015-03-12T10:36:40.187+05:30";
    }

    var addContent = function()
    {
        var content = subelement(entry, "content");
        var composition = subelement(content, "Composition");
        composition.set("xmlns", "http://hl7.org/fhir");
        var identifier = subelement(composition, "identifier");
        var identifierValue = subelement(identifier, "value");
        identifierValue.set("value", "urn:" + uid);
        var date = subelement(composition, "date");
        date.set("value", "2015-03-12T10:35:54+05:30");
        var status = subelement(composition, "status");
        status.set("value", "final");
        if(isConfidential == 'Yes')
        {
            var confidentiality = subelement(composition, "confidentiality");
            var system = subelement(confidentiality, "system");
            system.set("value", "http://hl7.org/fhir/v3/Confidentiality");
            var code = subelement(confidentiality, "code");
            code.set("value", "V");
            var confidentialityDisplay = subelement(confidentiality, "display");
            confidentialityDisplay.set("value", "very restricted");
        }
        var subject = subelement(composition, "subject");
        var subjectReference = subelement(subject, "reference");
        subjectReference.set("value", "http://172.18.46.56:8081/api/v1/patients/" + hid);
        var subjectDisplay = subelement(subject, "display");
        subjectDisplay.set("value", hid);
        var author = subelement(composition, "author");
        var authorReference = subelement(author, "reference");
        authorReference.set("value", "http://hrmtest.dghs.gov.bd/api/1.0/facilities/10000069.json");
        encounterEntry.getEncounterReference(composition);
        for(var index = 0 ; index < entries.length; index++)
        {
            entries[index].getSection(composition);
        }
    };

    var get = function()
    {
        initialize();
        addContent();
    };

    return {
        get : get
    };
}

function EncounterEntry(root, hid)
{
    var entry = subelement(root,"entry");
    var uid = uuid.v4();
    var initialize = function()
    {
        var title = subelement(entry,"title");
        title.text = "Encounter";
        var id = subelement(entry, "id");
        id.text = "urn:" + uid;
        var updated = subelement(entry, "updated");
        updated.text = "2015-03-12T10:36:40.187+05:30";

    }

    var addContent = function()
    {
        var content = subelement(entry,"content");
        content.set("type", "text/xml");
        var encounter = subelement(content, "Encounter");
        encounter.set("xmlns", "http://hl7.org/fhir");
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
        subjectReference.set("value", "http://172.18.46.56:8081/api/v1/patients/" + hid)
        var subjectDisplay = subelement(subject, "display");
        subjectDisplay.set("value", hid);
        var participant = subelement(encounter, "participant");
        var individual = subelement(participant, "individual");
        var participantReference = subelement(individual, "reference");
        participantReference.set("value", "http://hrmtest.dghs.gov.bd/api/1.0/providers/24.json");
        var indication = subelement(encounter, "indication");
        var indicationReference = subelement(indication, "reference");
        indicationReference.set("value", "urn:" + uid);
        var indicationDisplay = subelement(indication, "display");
        indicationDisplay.set("value", "Encounter");
        var serviceProvider = subelement(encounter, "serviceProvider");
        var serviceProviderReference = subelement(serviceProvider, "reference");
        serviceProviderReference.set("value", "http://hrmtest.dghs.gov.bd/api/1.0/facilities/10000069.json");
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
/*
3. Immunization entry
has a title
has an id
has an updated section (Date string need to generated currently hard coded)
has a content
*/

function ImmunizationEntry(root, hid)
{
    var entry = subelement(root, "entry");
    var hid = hid;
    var uid = uuid.v4();

    var initialize= function()
    {
        var title = subelement(entry, "title");
        title.text = "Immunization";
        var id = subelement(entry, "id");
        id.text = "urn:" + uid;
        var updated = subelement(entry,"updated");
        updated.text = "2015-03-12T10:36:40.227+05:30";
    };

    var addContent = function()
    {
        var content = subelement(entry,"content");
        content.set("type", "text/xml");
        var immunization = subelement(content, "Immunization");
        immunization.set("xmlns", "http://hl7.org/fhir");
        var identifier = subelement(immunization, "identifier");
        var immunizationValue = subelement(identifier, "value");
        immunizationValue.set("value", "urn:" + uid);
        var date = subelement(immunization, "date");
        date.set("value", "2015-02-12T00:00:00+05:30");
        var vaccineType = subelement(immunization, "vaccineType");
        var coding = subelement(vaccineType,"coding");
        var system = subelement(coding, "system");
        system.set("value", "http://172.18.46.56:9080/openmrs/ws/rest/v1/tr/drugs/5799c579-3c78-4133-9e02-91c1006d862d");
        var code = subelement(coding, "code");
        code.set("value","5799c579-3c78-4133-9e02-91c1006d862d" );
        var codingDisplay = subelement(coding, "display")
        codingDisplay.set("value", "BCG");
        var subject = subelement(immunization, "subject");
        var subjectReference = subelement(subject, "reference");
        subjectReference.set("value", "http://172.18.46.56:8081/api/v1/patients/" + hid );
        var subjectDisplay = subelement(subject, "display");
        subjectDisplay.set("value", hid);
        var reported = subelement(immunization, "reported");
        reported.set("value", "true");
        var requester = subelement(immunization, "requester");
        var requesterReference = subelement(requester, "reference");
        requesterReference.set("value", "http://hrmtest.dghs.gov.bd/api/1.0/providers/24.json");
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
}




function createSection(parent, reference_value, display_value)
{
    var section = subelement(parent, "section");
    var content = subelement(section, "content");
    var reference = subelement(content, "reference");
    reference.set("value", reference_value);
    var display = subelement(content, "display");
    display.set("value", display_value);
    return section;
}


var f = new feed();
console.log(pd.xml(f.get()));



