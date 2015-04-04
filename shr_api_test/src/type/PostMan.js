module.exports = function(requestSetFilePath) {
	var fs = require('fs');
	var pd = require("pretty-data").pd;
	var uuid = require('node-uudi');
	// console.log("I am here");
	// console.log(requestSetFilePath);
	// console.log(requestSetFilePath);
	function PostmanRequest(requestSetFilePath) {
		this.content = fs.readFileSync(requestSetFilePath, 'utf8').toString();
		// this.content = this.content.replace(/\\n/g, "").replace(/\\"/g,'"');
		json.parse(pd.json(this.content));
		
		this.request = function()
		{
			return {
  "id" : uuid,
  "name" : "postman_collection_name",
  "timestamp" : new Date().getTime(),
  "order" : [uuid1, uuid2],
  "requests" : [
    {
    	"collectionId" : uuid,
    	"id" : uuid,
    	"name" : "name of  request",
    	"description" : "some description",
    	"url" : "given url",
    	"method" : "GET",
    	"headers" : "Content-Type: application/json\nX-Auth-Token: {{x_auth_token}}\nFrom: {{sso_email}}\nclient_id: {{sso_client_id}}\n",
    	"data":[{"key":"email","value":"{{sso_email}}","type":"text"},{"key":"password","value":"{{sso_password_2}}","type":"text"}],
    	"dataMode":"params",
    	"timestamp":0,
		"responses":[],
		"version":2
    	
    }
  ]
};

				headers =  {
					"Content-Type" : "application/json",
					"X-Auth-Token" : "this.user_detail.access_token",
					"From" : "this.user_detail.email",
					"client_id" : "this.user_detail.client_id"
				};



function Request(request_name, description, url, method, headers, dataMode, data)
{
	
	this.name = request_name;
	this.description = description;
	this.url = url;
	this.method = method;
	this.headers = this.formateHeaders(headers);
	this.dataMode = dataMode;
	this.data = this.formatdata(data);
	this.formateHeaders = function(headers)
	{
		return JSON.stringify(headers);		
	};	
	
	
}

function FinalRequest(collection_name, requests)
{
	this.collection_id = uuid.v1();
	this.collection_name = collection_name;
	this.timestamp = new Date().getTime();
	this.requests = this.getRequests();
	
	
	return 
	{
		"id" : this.collection_id,
		"name" : this.collection_name,
		"timestamp" : this.timestamp,
		"order" : this.getRequestsOrder(),
		"requests" : this.requests
		
	};
	
	
	
	
}


		};
		
	};
	
	
	
	

	return new PostmanRequest(requestSetFilePath);
};
