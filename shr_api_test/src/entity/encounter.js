"use strict";
var config = require('./../Config').config;
var Concept = require('./concept').Concept;
var Feed = require('./bundle').Feed;
var pd = require("pretty-data").pd;

function EncounterConfig(hid) {
	var encounter_details = {};
	encounter_details["QA"] = {
		hid: hid,
		clinical_standard: "http://hl7.org/fhir",
		patient_uri: config.patient_uri + hid,
		facility_uri: config.facility_uri + "10000069.json",
		entry : {
	//	<vaccineCode>
	//	<coding>
	//	<system value=\"http://trstg.twhosted.com/openmrs/ws/rest/v1/tr/drugs/d2e2d3d2-878d-11e5-95dd-005056b0145c\"/>
	//<code value=\"d2e2d3d2-878d-11e5-95dd-005056b0145c\"/>
	//<display value=\"BCG\"/>
	//</coding>
	//</vaccineCode>
	//	concept_uri	http://172.18.46.56:9080/openmrs/ws/rest/v1/tr/
			//name, concept_code,concept_code_value, reference_code, reference_code_value, base_uri, type
			"BCG" : new Concept("BCG", "e90ee3ac-83b0-11e5-aa01-0050568276cf", config.concept_uri, "drugs"),
			"Temperature" : new Concept("Temperature", "a1257651-7473-4c9b-bb0a-1244c5f3c09d", config.concept_uri, "concepts"),
			"Pulse" : new Concept("Pulse", "22a952b6-cc36-45e8-8b52-ff5a90fa7c4f", config.concept_uri, "concepts"),
			"Systolic" : new Concept("Systolic", "77405a73-b915-4a93-87a7-f29fe6697fb4", config.concept_uri, "concepts"),
			"Diastolic" : new Concept("Diastolic", "af747d2f-8946-4ca2-93ec-5eb76986aff8", config.concept_uri, "concepts"),
			"Blood Pressure" : new Concept("Blood Pressure", "e69ef886-6914-4ed7-93a8-7b951dbf7139", config.concept_uri, "concepts"),
			"Vitals" : new Concept("Vitals", "44c245dd-d234-4991-a8b2-3c4a54d5092b", config.concept_uri, "concepts"),
			"Fracture in upper arm" : new Concept("Fracture in upper arm", "676ffceb-dac1-4caa-b76d-be1e6816094e", config.concept_uri, "concepts")
		},
		provider_uri: config.provider_uri + "24.json"
	};

	encounter_details["LOCAL"] = {
		hid: hid,
		clinical_standard: "http://hl7.org/fhir",
		patient_uri: config.patient_uri + hid,
		facility_uri: config.facility_uri + "10000069.json",
		entry : {
			"BCG" : new Concept("BCG", "5799c579-3c78-4133-9e02-91c1006d862d", config.concept_uri, "drugs"),
			"Temperature" : new Concept("Temperature", "a1257651-7473-4c9b-bb0a-1244c5f3c09d", config.concept_uri, "concepts"),
			"Pulse" : new Concept("Pulse", "22a952b6-cc36-45e8-8b52-ff5a90fa7c4f", config.concept_uri, "concepts"),
			"Systolic" : new Concept("Systolic", "77405a73-b915-4a93-87a7-f29fe6697fb4", config.concept_uri, "concepts"),
			"Diastolic" : new Concept("Diastolic", "af747d2f-8946-4ca2-93ec-5eb76986aff8", config.concept_uri, "concepts"),
			"Blood Pressure" : new Concept("Blood Pressure", "e69ef886-6914-4ed7-93a8-7b951dbf7139", config.concept_uri, "concepts"),
			"Vitals" : new Concept("Vitals", "44c245dd-d234-4991-a8b2-3c4a54d5092b", config.concept_uri, "concepts"),
			"Fracture in upper arm" : new Concept("Fracture in upper arm", "2218636a-0ef0-4fb1-ac7e-cf2a915b0ee4", config.concept_uri, "concepts")
		},
		provider_uri: config.provider_uri + "24.json"
	};

	encounter_details["SHOWCASE"] = {
		hid: hid,
		clinical_standard: "http://hl7.org/fhir",
		patient_uri: config.patient_uri + hid,
		facility_uri: config.facility_uri + "10019841.json",
		entry : {
			"Temperature" : new Concept("Temperature", "ed7401ef-c8ac-4af8-bf7c-1ed1d7147c78", config.concept_uri, "concepts"),
			"Pulse" : new Concept("Pulse", "ac83596d-5bb9-43b0-8db6-e90fa2a6e1e9", config.concept_uri, "concepts"),
			"Systolic" : new Concept("Systolic", "dd7d33d9-d7a5-4596-98fa-03a88ba631e1", config.concept_uri, "concepts"),
			"Diastolic" : new Concept("Diastolic", "7f6af303-e803-4d62-bbb4-e039ada4c837", config.concept_uri, "concepts"),
			"Blood Pressure" : new Concept("Blood Pressure", "c8e87979-9198-490f-8afe-4673f810825d", config.concept_uri, "concepts"),
			"Vitals" : new Concept("Vitals", "970b1e8e-ecb7-4c79-aec8-d25b5c551a66", config.concept_uri, "concepts")
		},

		provider_uri: config.provider_uri + "24.json"
	};

	encounter_details['STAGING'] = {
		hid: hid,
		clinical_standard: "http://hl7.org/fhir",
		patient_uri: config.patient_uri + hid,
		facility_uri: config.facility_uri + "10019842.json",
		entry : {
			//name, concept_code,concept_code_value, reference_code, reference_code_value, base_uri, type
			"Pulse": new Concept("Pulse", "20195aec-1a76-11e5-b5a9-00505682700b","20195aec-1a76-11e5-b5a9-00505682700b", "201adc08-1a76-11e5-b5a9-00505682700b", "78564009", config.concept_uri, "concepts"),
			"Temperature" : new Concept("Temperature", "2017b7c5-1a76-11e5-b5a9-00505682700b","2017b7c5-1a76-11e5-b5a9-00505682700b","20183c53-1a76-11e5-b5a9-00505682700b", "386725007" ,config.concept_uri, "concepts"),
			"Systolic blood pressure" : new Concept("Systolic blood pressure", "200f2911-1a76-11e5-b5a9-00505682700b","200f2911-1a76-11e5-b5a9-00505682700b","201435f6-1a76-11e5-b5a9-00505682700b", "271649006" ,config.concept_uri, "concepts"),
			"Diastolic blood pressure" : new Concept("Diastolic blood pressure", "201500c9-1a76-11e5-b5a9-00505682700b","201500c9-1a76-11e5-b5a9-00505682700b","201435f6-1a76-11e5-b5a9-00505682700b", "271649006" ,config.concept_uri, "concepts"),
			"Blood Pressure" : new Concept("Blood Pressure", "201500c9-1a76-11e5-b5a9-00505682700b","201500c9-1a76-11e5-b5a9-00505682700b","201435f6-1a76-11e5-b5a9-00505682700b", "271649006" ,config.concept_uri, "concepts"),
			"Vitals" : new Concept("Vitals", "201500c9-1a76-11e5-b5a9-00505682700b","201500c9-1a76-11e5-b5a9-00505682700b","201435f6-1a76-11e5-b5a9-00505682700b", "271649006" ,config.concept_uri, "concepts"),
			"BCG" : new Concept("BCG", "d84c3505-8bd2-4123-8885-ab9e431ef0cd", "d84c3505-8bd2-4123-8885-ab9e431ef0cd", "d84c3505-8bd2-4123-8885-ab9e431ef0cd", "BCG", config.concept_uri, "concepts"),
			"Fracture in upper arm" : new Concept("Fracture in upper arm", "201500c9-1a76-11e5-b5a9-00505682700b","201500c9-1a76-11e5-b5a9-00505682700b","201435f6-1a76-11e5-b5a9-00505682700b", "271649006" ,config.concept_uri, "concepts")
		},

		provider_uri: config.provider_uri + "113084.json"
	};

	return encounter_details[config.env];
}


exports.DefaultEncounterFeed = function(hid, isConfidential) {

	var encounter_payload = "";
	var confidentiality = isConfidential || 'No';
		var details = new EncounterConfig(hid);
		var feed = new Feed(details, confidentiality);
		feed.withImmunizationEntry("BCG");
		encounter_payload = feed.get();
	return {
		details: encounter_payload,
		pretty_details: pd.xml(encounter_payload)
	}
};


exports.EncounterFeed = function(hid, isConfidential) {
	var feed_detatails = "";
	var confidentiality = isConfidential || 'No';
	var config = new EncounterConfig(hid);
	var feed = new Feed(config, confidentiality);

	var withDiagnosisDetails = function (diagnosis)
	{
		feed.withDiagnosisEntry(diagnosis);
	};

	var withImmunizationDetails = function(immunization) {
		feed.withImmunizationEntry(immunization);
	};

	var withVitals = function()
	{
		feed.withVitalsEntry();
	};

	var get = function()
	{
		 return { details : feed.get() };
	};

	return {
		get : get,
		withVitals : withVitals,
		withDiagnosisDetails : withDiagnosisDetails,
		withImmunizationDetails : withImmunizationDetails
	}

};