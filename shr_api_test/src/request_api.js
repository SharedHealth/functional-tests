var Patient = require('./type/patient.js');
var Encounter = require('./type/encounter.js');

module.exports = function(user_detail) {

	this.user_detail = user_detail;
	
	var sso_server_ip = "172.18.46.56";
	var shr_server_ip = "172.18.46.57";
	var sso_signin_url = "http://" + sso_server_ip + ":8080/signin";
	var catchment_marker_url;
	var api_hader_info;

	this.catchment_marker_url = function(catchment_area_code) {
		return "http://" + shr_server + ":8081/v1/catchments/" + catchment_area_code + "/encounters?updatedSince=2015-03-27";
	};


	this.api_header_info = function(user_detail, content_type) {

		return {
			"Content-Type" : content_type,
			"X-Auth-Token" : user_detail.access_token,
			"From" : user_detail.email,
			"client_id" : user_detail.client_id
		};

	};
	

	
// var approval_for_single_patient = "https://bdshr-mci-qa.twhosted.com/api/v1/catchments/100409/approvals/5964931715787915265";
	


}; 
	
