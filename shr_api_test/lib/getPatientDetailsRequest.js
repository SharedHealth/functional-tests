module.exports = function(hid, user_detail, confidential) {

	function getPatientDetailsRequest(hid,user_detail, confidential) {
   		var config = require('./Config').config;
		this.user_detail = user_detail;
		this.confidential = confidential || 'No';
		this.server = config.mci_dns_name;
		this.headers = function(content_type) {

			return {
				"Content-Type" : content_type,
				"X-Auth-Token" : this.user_detail.access_token,
				"From" : this.user_detail.email,
				"client_id" : this.user_detail.client_id
			};

		};

	this.getHeaders = function()
    	{
    		return {headers : this.headers("application/json") };
    	};


	this.getUrl = function(hid) {

		return "http://" + this.server + "/api/v1/patients/" + this.hid;
	};

};

};