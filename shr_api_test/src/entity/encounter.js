"use strict";
module.exports = function(hid, isConfidential) {
	var config = require('./../Config').config;
	var Concept = require('./concept').Concept;
	var Feed = require('./feed').Feed;
	var pd = require("pretty-data").pd;
	var encounter_payload = "";
	var confidentiality = isConfidential || 'No';

	function EncounterDetails() {
		var encounter_details = {};
		encounter_details["QA"] = {
			hid: hid,
			clinical_standard: "http://hl7.org/fhir",
			patient_uri: config.patient_uri + hid,
			facility_uri: config.facility_uri + "10000069.json",
			entry : {
			"Immunization" : new Concept("Immunization", "5799c579-3c78-4133-9e02-91c1006d862d", config.concept_uri, "drugs"),
			"Temperature" : new Concept("Temperature", "a1257651-7473-4c9b-bb0a-1244c5f3c09d", config.concept_uri, "concepts"),
			"Pulse" : new Concept("Pulse", "22a952b6-cc36-45e8-8b52-ff5a90fa7c4f", config.concept_uri, "concepts"),
			"Systolic" : new Concept("Systolic", "77405a73-b915-4a93-87a7-f29fe6697fb4", config.concept_uri, "concepts"),
			"Diastolic" : new Concept("Diastolic", "af747d2f-8946-4ca2-93ec-5eb76986aff8", config.concept_uri, "concepts"),
			"Blood Pressure" : new Concept("Blood Pressure", "e69ef886-6914-4ed7-93a8-7b951dbf7139", config.concept_uri, "concepts"),
			"Vitals" : new Concept("Vitals", "44c245dd-d234-4991-a8b2-3c4a54d5092b", config.concept_uri, "concepts")
			},
			provider_uri: config.provider_uri + "24.json"
		};

		encounter_details["SHOWCASE"] = {
			hid: hid,
			clinical_standard: "http://hl7.org/fhir",
			patient_uri: config.patient_uri + hid,
			facility_uri: config.facility_uri + "10019841.json",
			entry : {"Immunization" : new Concept("Immunization", "5799c579-3c78-4133-9e02-91c1006d862d", config.concept_uri, "drugs")},
			provider_uri: config.provider_uri + "24.json"
		};


		return encounter_details[config.env];
	}
		var details = new EncounterDetails();
		encounter_payload = new Feed(details, confidentiality).withVitalsEntry();


	return {
		details: encounter_payload,
		pretty_details: pd.xml(encounter_payload)
	}
};


