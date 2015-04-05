module.exports = function(confidentiality) {

	return {

		"nid" : new Date().getTime(),
		"bin_brn": "16343220123456780",
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