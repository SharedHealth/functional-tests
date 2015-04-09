module.exports = function(confidentiality) {
 	var nid = new Date().getTime();
	var bin_brn =  nid + "0000";
	var houseHoldCode=Math.ceil(Math.random()*100000000);
	var name=Math.ceil(Math.random()*1000000)

	return {

		"nid" : nid,
		"bin_brn": bin_brn,
		"given_name" : "A89 "+name,
		"sur_name" : "ATEST",
		"date_of_birth" : "2000-03-01",
		"gender" : "M",
		"occupation" : "05",
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