var et = require("elementtree");
var element = et.Element;
var subelement = et.SubElement;
var etree = et.ElementTree;
var uuid = require("uuid");
var isodate = require('../../src/utility/isodate').isodate;

exports.ImmunizationEntry = function ImmunizationEntry(root,detail, drug, encounter_reference)
{
    var entry = subelement(root, "entry");
    var uid = uuid.v4();
    var isoDateTime = new isodate().isoDate();
    var drug = drug;
    var detail = detail;
    var encounter_reference = encounter_reference;
    var get = function()
    {
        var fullUrl = subelement(entry,"fullUrl");
        fullUrl.set("value","urn:uuid:" + uid);
        var resource = subelement(entry, "resource");
        var immunization = subelement(resource, "Immunization");
        immunization.set("xmlns", detail.clinical_standard);
        var identifier = subelement(immunization, "identifier");
        var immunizationValue = subelement(identifier, "value");
        immunizationValue.set("value", "urn:uuid:" + uid);
        var immunizationStatus = subelement(immunization, "status");
        immunizationStatus.set("value", "completed");
        var vaccineType = subelement(immunization, "vaccineCode");
        var coding = subelement(vaccineType,"coding");
        var system = subelement(coding, "system");
        system.set("value", detail.entry[drug].concept_uri);
        var code = subelement(coding, "code");
        code.set("value",detail.entry[drug].concept_code );
        var codingDisplay = subelement(coding, "display");
        codingDisplay.set("value", drug);
        var patient = subelement(immunization, "patient");
        var patientReference = subelement(patient, "reference");
        patientReference.set("value", detail.patient_uri );
        var patientDisplayValue = subelement(patient, "display");
        patientDisplayValue.set("value", detail.hid);
        var refusedIndicator = subelement(immunization, "wasNotGiven");
        refusedIndicator.set("value", "false");
        var reported = subelement(immunization, "reported");
        reported.set("value", "false");
        var requester = subelement(immunization, "requester");
        var requesterReference = subelement(requester, "reference");
        requesterReference.set("value", detail.provider_uri);
        var encounter = subelement(immunization, "encounter");
        var encounterReference = subelement(encounter,"reference")
        encounterReference.set("value", "urn:uuid:" + encounter_reference);
    };

    var sectionForComposition = function(parent)
    {
        var section = subelement(parent, "section");
        var entry = subelement(section, "entry");
        var reference = subelement(entry, "reference");
        reference.set("value","urn:uuid:" + uid );
        var display = subelement(entry, "display");
        display.set("value", "Immunization");
        return section;
    };
    return {
        get : get,
        getSection : sectionForComposition
    };
};