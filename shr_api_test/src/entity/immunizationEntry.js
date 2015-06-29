var et = require("elementtree");
var element = et.Element;
var subelement = et.SubElement;
var etree = et.ElementTree;
var uuid = require("uuid");
var isodate = require('../../src/utility/isodate').isodate;

exports.ImmunizationEntry = function ImmunizationEntry(root,detail, drug)
{
    var entry = subelement(root, "entry");
    var uid = uuid.v4();
    var isoDateTime = new isodate().isoDate();
    var drug = drug;

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
        console.log("-----")
        console.log(drug);
        system.set("value", detail.entry[drug].drug_uri);
        var code = subelement(coding, "code");
        code.set("value",detail.entry[drug].concept_code );
        var codingDisplay = subelement(coding, "display")
        codingDisplay.set("value", drug);
        var subject = subelement(immunization, "subject");
        var subjectReference = subelement(subject, "reference");
        subjectReference.set("value", detail.patient_uri );
        var subjectDisplay = subelement(subject, "display");
        subjectDisplay.set("value", detail.hid);
        var refusedIndicator = subelement(immunization, "refusedIndicator");
        refusedIndicator.set("value", "false");
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

    var withDrug = function(drug)
    {
        drug = drug;
    }

    var get = function()
    {
        initialize();
        addContent();

    };

    return {
        get : get,
        getSection : sectionForComposition,
        withDrug : drug
    };
};