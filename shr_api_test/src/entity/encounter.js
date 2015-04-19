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
			entry : {"Immunization" : new Concept("Immunization", "5799c579-3c78-4133-9e02-91c1006d862d", config.concept_uri, "drugs")},
			provider_uri: config.provider_uri + "24.json"
		};

		return encounter_details[config.env];
	}
		var details = new EncounterDetails();
		encounter_payload = new Feed(details, confidentiality).withImmunizationEntry()


	return {
		details: encounter_payload,
		pretty_details: pd.xml(encounter_payload)
	}
};


