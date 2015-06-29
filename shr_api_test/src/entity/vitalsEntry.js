var et = require("elementtree");
var element = et.Element;
var subelement = et.SubElement;
var etree = et.ElementTree;
var uuid = require("uuid");
var isodate = require('../../src/utility/isodate').isodate;

function VitalsSectionEntry(root,detail,concept_name, entries)
{
    var entry = subelement(root, "entry");
    var uid = uuid.v4();
    var concept_name = concept_name
    var isoDateTime = new isodate().isoDate();

    var initialize= function()
    {
        var title = subelement(entry, "title");
        title.text = concept_name;
        var id = subelement(entry, "id");
        id.text = "urn:" + uid;
        var updated = subelement(entry,"updated");
        updated.text = isoDateTime;
    };


    var addContent = function()
    {
        var content = subelement(entry,"content");
        content.set("type", "text/xml");
        var observation = subelement(content,"Observation");
        observation.set("xmlns", detail.clinical_standard);
        var observationName = subelement(observation,"name");

        var coding = subelement(observationName,"coding");
        var system = subelement(coding, "system");
        system.set("value", detail.entry[concept_name].concept_uri);
        var code = subelement(coding, "code");
        code.set("value",detail.entry[concept_name].concept_code_value );
        var codingDisplay = subelement(coding, "display");
        codingDisplay.set("value", detail.entry[concept_name].name);
        var status = subelement(observation, "status");
        status.set("value", "final");
        var reliability = subelement(observation, "reliability");
        reliability.set("value", "ok");
        var identifier = subelement(observation, "identifier");
        var identifierValue = subelement(identifier, "value");
        identifierValue.set("value", "urn:" + uid);
        var subject = subelement(observation, "subject");
        var subjectReference = subelement(subject, "reference");
        subjectReference.set("value", detail.patient_uri );
        var subjectDisplay = subelement(subject, "display");
        subjectDisplay.set("value", detail.hid);
        for(var i = 0; i < entries.length; i ++)
        {
            entries[i].getRelatedSection(observation)
        }
    };


    var sectionForComposition = function(parent)
    {
        var section = subelement(parent, "section");
        var content = subelement(section, "content");
        var reference = subelement(content, "reference");
        reference.set("value","urn:" + uid );
        var display = subelement(content, "display");
        display.set("value", concept_name);
        return section;
    }

    var get = function()
    {
        initialize();
        addContent();

    };

    var relatedSection = function(root)
    {
        var related = subelement(root, "related");
        var target = subelement(related, "target");
        var reference = subelement(target, "reference");
        reference.set("value", "urn:" + uid);
    }

    return {
        get : get,
        getSection : sectionForComposition,
        getRelatedSection : relatedSection
    };

}

function SubVitalsEntry(root,detail, concept_name, entry_value)
{
    var entry = subelement(root, "entry");
    var uid = uuid.v4();
    var concept_name = concept_name;
    var entry_value = entry_value || '0.0';
    var isoDateTime = new isodate().isoDate();

    var initialize= function()
    {
        var title = subelement(entry, "title");
        title.text = concept_name;
        var id = subelement(entry, "id");
        id.text = "urn:" + uid;
        var updated = subelement(entry,"updated");
        updated.text = isoDateTime;
    };

    var addContent = function()
    {
        var content = subelement(entry,"content");
        content.set("type", "text/xml");
        var observation = subelement(content,"Observation");
        observation.set("xmlns", detail.clinical_standard);
        var observationName = subelement(observation,"name");

        var coding = subelement(observationName,"coding");
        var system = subelement(coding, "system");
        system.set("value", detail.entry[concept_name].concept_uri);
        var code = subelement(coding, "code");
        code.set("value",detail.entry[concept_name].concept_code_value );
        var codingDisplay = subelement(coding, "display")
        codingDisplay.set("value", detail.entry[concept_name].name);

        var trcoding = subelement(observationName, "coding");
        var trsystem = subelement(trcoding, "system");
        trsystem.set("value", detail.entry[concept_name].reference_uri);
        var trcode = subelement(trcoding, "code");
        trcode.set("value", detail.entry[concept_name].reference_code_value);
        var trCodingDisplay = subelement(trcoding, "display");
        trCodingDisplay.set("value", detail.entry[concept_name].name)

        var valueDecimal = subelement(observation, "valueDecimal");
        valueDecimal.set("value", entry_value);
        var status = subelement(observation, "status");
        status.set("value", "final");
        var reliability = subelement(observation, "reliability");
        reliability.set("value", "ok");
        var identifier = subelement(observation, "identifier");
        var identifierValue = subelement(identifier, "value");
        identifierValue.set("value", "urn:" + uid);
        var subject = subelement(observation, "subject");
        var subjectReference = subelement(subject, "reference");
        subjectReference.set("value", detail.patient_uri );
        var subjectDisplay = subelement(subject, "display");
        subjectDisplay.set("value", detail.hid);

    };

    var sectionForComposition = function(parent)
    {
        var section = subelement(parent, "section");
        var content = subelement(section, "content");
        var reference = subelement(content, "reference");
        reference.set("value","urn:" + uid );
        var display = subelement(content, "display");
        display.set("value", concept_name);
        return section;
    }

    var get = function()
    {
        initialize();
        addContent();

    };

    var setValue = function(value)
    {
        entry_value =  value;
    };

    var relatedSection = function(root)
    {
        var related = subelement(root, "related");
        var target = subelement(related, "target");
        var reference = subelement(target, "reference");
        reference.set("value", "urn:" + uid);
    }

    return {
        get : get,
        getSection : sectionForComposition,
        setValue : setValue,
        getRelatedSection : relatedSection
    };
}

exports.VitalsEntry = function VitalsEntry(root,detail,entries)
{
    var vitals_entry = VitalsSectionEntry(root,detail,"Vitals", entries);
    return vitals_entry;
};

exports.BloodPressureEntry = function BloodPressureEntry(root,detail, entries)
{
    var blood_pressure_entry = VitalsSectionEntry(root, detail, "Blood Pressure", entries);
    return blood_pressure_entry;
};


exports.SystolicEntry = function SystolicEntry(root,detail)
{
    var systolic_entry = new SubVitalsEntry(root,detail,'Systolic blood pressure');

    return systolic_entry;
};

exports.DiastolicEntry = function DiastolicEntry(root,detail)
{
    var diastolic_entry = new SubVitalsEntry(root,detail,'Diastolic blood pressure');

    return diastolic_entry;
};

exports.PulseEntry = function PulseEntry(root,detail)
{
    var pulse_entry = new SubVitalsEntry(root,detail,'Pulse');
    return pulse_entry;
};

exports.TemperatureEntry = function TemperatureEntry(root,detail)
{
    var temperature_entry = new SubVitalsEntry(root,detail,'Temperature');
    return temperature_entry;
};
