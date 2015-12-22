"use strict"
 function Patient(confidentiality, divisionId, districtId, upazila_id) {
 	var nid = new Date().getTime().toString();
	var bin_brn =  nid.concat("0000");
	 var getCodedName = function(random_code)
	 {
		 return random_code.split("").map(convertToCharIfNumeric).join("");
	 };

	 var convertToCharIfNumeric = function(char) {
		 var codeValue = char.charCodeAt(0);
		 if ( codeValue >= 48 && codeValue <= 57) {
			 return String.fromCharCode(65 + parseInt(char));
		 }
		 else
		 {	return char;
		 }
	 };
	 var   convertNumericToChar = function(char){
		 return String.fromCharCode(65 + parseInt(char));
	 }

	 var name= "AHI " + getCodedName(Math.ceil(Math.random()*1000000).toString());

	 return {
		"nid" : nid,
		"bin_brn": bin_brn,
		"given_name" : name,
		"sur_name" : "ATEST",
		"date_of_birth" : "2000-03-01",
		"gender" : "M",
		"occupation" : "05",
		"religion" : "1",
		 "phone_number" :
        {
         "country_code": "88",
         "area_code": null,
         "number": "01678904560",
         "extension": null
         },
		"present_address" : {
			"address_line" : "Test",
			"division_id" : divisionId || "30",
			"district_id" : districtId || "26",
			"upazila_id" : upazila_id || "07"
		},
		"status" : {
			"type" : "1"

		},
		"confidential" : confidentiality || "No"
	};

};

exports.Patient = Patient;

exports.PatientWithDifferentCatchment = function(confidentiality) {
	var patient = new Patient(confidentiality || "No", "30", "33", "34");
	return patient;
}
exports.PatientWithHouseHold = function(confidentiality)
{
	var patient = new Patient(confidentiality);
	patient.uid = "9".concat(patient.nid.toString().substring(3));
	patient.household_code = "9".concat(patient.nid.toString().substring(6));
	return patient;

};
