exports.Patient = function(confidentiality) {
 	var nid = new Date().getTime().toString();
	var bin_brn =  nid.concat("0000");
	var name=Math.ceil(Math.random()*1000000);
	return {
		"nid" : nid,
		"bin_brn": bin_brn,
		"given_name" : "A89 " + name,
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
			"division_id" : "30",
			"district_id" : "26",
			"upazila_id" : "07"
		},
		"status" : {
			"type" : "1"

		},
		"confidential" : confidentiality || "No"
	};

};

exports.PatientWithHouseHold = function(confidentiality)
{
	var nid = new Date().getTime().toString();
	var bin_brn =  nid.concat("0000");
	var name=Math.ceil(Math.random()*1000000);
	var uid = "9".concat(nid.toString().substring(3));
	var houseHoldCode = "9".concat(nid.toString().substring(6));
	return {

		"nid" : nid,
		"bin_brn": bin_brn,
		"uid": uid,
		"given_name" : "A89 " + name,
		"sur_name" : "ATEST",
		"date_of_birth" : "2000-03-01",
		"gender" : "M",
		"occupation" : "05",
		"religion": "1",
		"phone_number":
		{
			"country_code": "88",
			"area_code": null,
			"number": "01678904560",
			"extension": null
		},
		"present_address" : {
			"address_line" : "Test",
			"division_id" : "30",
			"district_id" : "26",
			"upazila_id" : "07"
		},

		"household_code": houseHoldCode,
		"status" : {
			"type" : "1"

		},
		"confidential" : confidentiality || "No"
	};

};
