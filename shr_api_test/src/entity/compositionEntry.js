var et = require("elementtree");
var element = et.Element;
var subelement = et.SubElement;
var uuid = require("uuid");
var isodate = require('../../src/utility/isodate').isodate;

exports.CompositionEntry = function CompositionEntry(root, isConfidential,encounterEntry,detail) {
    var entry = subelement(root, "entry");
    var encounterEntry = encounterEntry;
    var uid = uuid.v4();
    var isoDateTime = new isodate().isoDate();
    var composition;
    var detail = detail;
    //<fullUrl value=\"urn:uuid:5abe40a4-dea4-49db-9429-ae30cbf382cb\"/>
    var initialize = function () {
        var fullURL = subelement(entry, "fullUrl");
        fullURL.set("value", "urn:uuid:" + uid);
        addContent();
    }

    var addContent = function () {
        var resource = subelement(entry, "resource");
        composition = subelement(resource, "Composition");
        composition.set("xmlns", detail.clinical_standard);
        var identifier = subelement(composition, "identifier");
        var identifierValue = subelement(identifier, "value");
        identifierValue.set("value", "urn:uuid:" + uid);


        var compositionType = subelement(composition, "type");
        var coding = subelement(compositionType, "coding");
        var compositionTypeSystem = subelement(coding,"system");
        compositionTypeSystem.set("value", detail.clinical_standard + "/vs/doc-typecodes");
        var compoisitionCodeValue = subelement(coding, "code");
        compoisitionCodeValue.set("value", "51899-3");
        var compositionDisplay = subelement(coding, "display");
        compositionDisplay.set("value", "Details Document");
        //<title value="Patient Clinical Encounter"/>
        var compositionTitle = subelement(composition, "title");
        compositionTitle.set("value", "Patient Clinical Encounter");
        var date = subelement(composition, "date");
        date.set("value", isoDateTime);
        var status = subelement(composition, "status");
        status.set("value", "final");
        var confidentiality = subelement(composition, "confidentiality");
        if (isConfidential == 'Yes') {
            confidentiality.set("value", "V");
        }
        else
        {
            confidentiality.set("value", "N");
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
    };
    initialize();
    function addSection(entry)
    {
        entry.getSection(composition)
    }
    return {
        addSection : addSection
    };
};