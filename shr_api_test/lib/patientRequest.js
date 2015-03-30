module.exports = function(user_detail, confidential)
{


	
function PatientRequest(user_detail, patient_detail)
{
	
	this.user_detail = user_detail;
	this.confidential = confidential || 'No';
	
		this.headers = function() {

		return {
			"Content-Type" : "application/json",
			"X-Auth-Token" : this.user_detail.access_token,
			"From" : this.user_detail.email,
			"client_id" : this.user_detail.client_id
		};

	};

	this.post = function() {		
		return {
			url : "https://bdshr-mci-qa.twhosted.com/api/v1/patients",
			headers : this.headers(this.user_detail, "application/json"),
			json : true,
			body : patient_detail
		};
	
};
}

return new PatientRequest(user_detail, confidential);

	
};


