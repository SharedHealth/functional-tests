var et = require("elementtree");
var element = et.Element;
var subelement = et.SubElement;
var etree = et.ElementTree;
var uuid = require("uuid");
var isodate = require('../../src/utility/isodate').isodate;
var CompositionEntry = require("./compositionEntry").CompositionEntry;
var EncounterEntry = require("./encounterEntry").EncounterEntry;
var VitalsEntry = require("./vitalsEntry").VitalsEntry;
var BloodPressureEntry = require("./vitalsEntry").BloodPressureEntry;
var PulseEntry = require("./vitalsEntry").PulseEntry;
var TemperatureEntry = require("./vitalsEntry").TemperatureEntry;
var SystolicEntry = require("./vitalsEntry").SystolicEntry;
var DiastolicEntry = require("./vitalsEntry").DiastolicEntry;
var ImmunizationEntry = require("./immunizationEntry").ImmunizationEntry;
var DiagnosisEntry = require("./diagnosisEntry").DiagnosisEntry;

exports.Feed = function feed(detail,confidentiality)
{
    var root = element("feed");
    var uid = uuid.v4();
    var detail = detail;
    var isoDateTime = new isodate().isoDate();
    var withImmunizationEntry = function()
    {
        var immunization = new ImmunizationEntry(root,detail,"BCG");
        var encounter = new EncounterEntry(root,detail);
        var composition =new CompositionEntry(root,confidentiality,encounter,[encounter,immunization], detail);
        initialize();
        immunization.get();
        encounter.get();
        composition.get();
        return new etree(root).write({'xml_declaration': true})
    };

    var withDiagnosisEntry = function()
    {
        var diagnosis = new DiagnosisEntry(root, detail, "Fracture in upper arm");
        var encounter = new EncounterEntry(root,detail);
        var composition =new CompositionEntry(root,confidentiality,encounter,[encounter,diagnosis], detail);
        initialize();
        diagnosis.get();
        encounter.get();
        composition.get();
        return new etree(root).write({'xml_declaration': true})
    }
    var withVitalsEntry = function()
    {
        var pulse = new PulseEntry(root,detail);
        var temperature = new TemperatureEntry(root, detail);
        var systolic = new SystolicEntry(root, detail);
        var diastolic = new DiastolicEntry(root, detail);
        var bloodPressure = new BloodPressureEntry(root, detail, [systolic, diastolic]);
        var vitals = new VitalsEntry(root, detail, [bloodPressure, pulse, temperature]);
        var encounter = new EncounterEntry(root, detail);
        var composition = new CompositionEntry(root,confidentiality,encounter,[pulse,temperature,systolic,diastolic, bloodPressure,vitals, encounter],detail);
        initialize();
        pulse.get();
        temperature.get();
        systolic.get();
        diastolic.get();
        bloodPressure.get();
        vitals.get();
        encounter.get();
        composition.get();
        return new etree(root).write({'xml_declaration': true});
    }
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
        withImmunizationEntry : withImmunizationEntry,
        withVitalsEntry : withVitalsEntry,
        withDiagnosisEntry : withDiagnosisEntry
    };
}











