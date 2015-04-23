var et = require("elementtree");
var element = et.Element;
var subelement = et.SubElement;
var etree = et.ElementTree;
var uuid = require("uuid");
var isodate = require('../../src/utility/isodate').isodate;

exports.DiagnosisEntry = function Entry(root,detail, diagnosis)
{
    var entry = subelement(root, "entry");
    var uid = uuid.v4();
    var isoDateTime = new isodate().isoDate();
    var diagnosis = diagnosis;

    var initialize= function()
    {
        var title = subelement(entry, "title");
        title.text = "Diagnosis";
        var id = subelement(entry, "id");
        id.text = "urn:" + uid;
        var updated = subelement(entry,"updated");
        updated.text = isoDateTime;
    };

    var addContent = function()
    {
        var content = subelement(entry,"content");
        content.set("type", "text/xml");
        var condition = subelement(content, "Condition");
        condition.set("xmlns", detail.clinical_standard);
        var identifier = subelement(condition, "identifier");
        var identifierValue = subelement(identifier, "value");
        identifierValue.set("value", "urn:" + uid);
        var subject = subelement(condition, "subject");
        var subjectReference = subelement(subject, "reference");
        subjectReference.set("value", detail.patient_uri );
        var subjectDisplay = subelement(subject, "display");
        subjectDisplay.set("value", detail.hid);
        var dateAsserted = subelement(condition, "dateAsserted");
        dateAsserted.set("value", isoDateTime);
        var code = subelement(condition, "code");
        var coding = subelement(code, "coding");
        var codingSystem = subelement(coding, "system");
        codingSystem.set("value", detail.entry[diagnosis].uri);
        var codingSystemCode = subelement(coding, "code");
        codingSystemCode.set("value", detail.entry[diagnosis].code);
        var codingSystemDisplay = subelement(coding, "display");
        codingSystemDisplay.set("value", detail.entry[diagnosis].name);
        var category = subelement(condition, "category");
        var categoryCoding = subelement(category, "coding");
        var categoryCodingSystem = subelement(categoryCoding, "system");
        categoryCodingSystem.set("value", detail.clinical_standard + "/condition-category");
        var categoryCodingCode = subelement(categoryCoding, "code");
        categoryCodingCode.set("value", "diagnosis");
        var categoryCodingDisplay = subelement(categoryCoding, "display");
        categoryCodingDisplay.set("value", "Diagnosis");
        var status = subelement(condition, "status");
        status.set("value", "confirmed");
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

    var withDiagnosis = function(diagnosis)
    {
        diagnosis = diagnosis;
    }

    var get = function()
    {
        initialize();
        addContent();

    };

    return {
        get : get,
        getSection : sectionForComposition,
        withDrug : diagnosis
    };
};