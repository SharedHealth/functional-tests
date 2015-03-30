module.exports = function ( hid, user_detail, payload)
{

function EncounterRequest(hid,user_detail,  payload)
{
	
	this.user_detail = user_detail;
	this.hid = hid;
	this.encounter = payload || "";
	this.ip =  "172.18.46.57";
	this.port = "8081";

	this.headers = function(content_type) {

		return {
			"Content-Type" : content_type,
			"X-Auth-Token" : this.user_detail.access_token,
			"From" : user_detail.email,
			"client_id" : user_detail.client_id
		};

	};
	
	
	

	
	this.getHeaders = function()
	{
		return {headers : this.headers("application/json") };
	};
	
	this.post = function() {
		return {
			url : "http://" + this.ip + ":" + this.port + "/patients/" + this.hid + "/encounters",
			headers : this.headers("application/xml; charset=utf-8"), 
			body : this.encounter.details,
			json : false
		};

	};
	
	this.getUrl = function(hid) {
		
		return "http://" + this.ip + ":" + this.port + "/patients/" + this.hid + "/encounters";
	};
	

	
	
}

 return new EncounterRequest(hid, user_detail, payload);

			
	};