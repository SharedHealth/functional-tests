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

exports.Feed = function Bundle(detail, confidentiality)
{
    var root = element("Bundle");
    var uid = uuid.v4();
    var detail = detail;
    var isoDateTime = new isodate().isoDate();
    var encounter = new EncounterEntry(root, detail);
    var composition = new CompositionEntry(root,confidentiality, encounter, detail);

    var initialize = function()
    {
        root.set("xmlns", "http://hl7.org/fhir");
        //var title = subelement(root, "title");
        //title.text = "Encounter";
        var id = subelement(root, "id");
        id.set("value","urn:" + uid);
        var meta = subelement(root, "meta");
        var lastUpdated = subelement(meta, "lastUpdated");
        lastUpdated.set("value", isoDateTime);
        var contentType = subelement(root, "type");
        contentType.set("value", "collection");
        composition.addSection(encounter);
    };
    initialize();
    var addEntry = function(entry)
    {
        entry.get();
        composition.addSection(entry);
    };

    var addEntries = function(entries)
    {
        for(var i = 0; i < entries.length; i++)
        {
            entries[i].get();
            composition.addSection(entries[i]);
        }
    };
    var get = function()
    {
        return new etree(root).write({'xml_declaration': false});
    };

    var withImmunizationEntry = function(immunizationCode)
    {
        addEntry(new ImmunizationEntry(root,detail,immunizationCode,encounter.uuid));
    };

    var withDiagnosisEntry = function(diagnosis)
    {
        addEntry(new DiagnosisEntry(root,detail, diagnosis));
    };
    var withVitalsEntry = function()
    {
        var pulse = new PulseEntry(root,detail);
        var temperature = new TemperatureEntry(root, detail);
        var systolic = new SystolicEntry(root, detail);
        var diastolic = new DiastolicEntry(root, detail);
        var bloodPressure = new BloodPressureEntry(root, detail, [systolic, diastolic]);
        var vitals = new VitalsEntry(root, detail, [bloodPressure, pulse, temperature]);
        addEntries([pulse,temperature,systolic,diastolic,bloodPressure,vitals]);
    };
    return {
        get : get,
        withImmunizationEntry: withImmunizationEntry,
        withDiagnosisEntry : withDiagnosisEntry,
        withVitalsEntry : withVitalsEntry
    }
};
