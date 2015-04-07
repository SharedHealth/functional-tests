module.exports = function(confidentiality) {
 	var nid = new Date().getTime();
	var bin_brn =  nid + "0000";
	return {

		"nid" : nid,
		"bin_brn": bin_brn,
		"given_name" : "A89 1812",
		"sur_name" : "ATEST",
		"date_of_birth" : "2000-03-01",
		"gender" : "M",
		"occupation" : "05",
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