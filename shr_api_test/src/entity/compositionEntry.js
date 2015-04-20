var et = require("elementtree");
var element = et.Element;
var subelement = et.SubElement;
var etree = et.ElementTree;
var uuid = require("uuid");
var isodate = require('../../src/utility/isodate').isodate;

exports.CompositionEntry = function CompositionEntry(root, isConfidential,encounterEntry, entries,detail) {
    var entry = subelement(root, "entry");
    var encounterEntry = encounterEntry;
    var uid = uuid.v4();
    var isoDateTime = new isodate().isoDate();

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