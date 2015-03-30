module.exports = function(user_type){
	var user = {};
		user["datasense"] = {
			email : "rappasam@thoughtworks.com",
			password : "thoughtworks",
			client_id : 18552,
			api_token : 'b7aa1f4001ac4b922dabd6a02a0dabc44cf5af74a0d1b68003ce7ccdb897a1d2',
			access_token : "",
			catchment : "30"
		};

		user["facility"] = {
			email : "dmishra@thoughtworks.com",
			password : "thoughtworks",
			client_id : 18549,
			api_token : '1c2a599423203f639dcdd8574ac5391dd67d21316ea30ee364c8a8787fb79dd3',
			access_token : "",
			catchment : ["302607"]
		};

		user["mci"] = {
			email : "ashutoks@thoughtworks.com",
			password : "thoughtworks",
			client_id : 18557,
			api_token : '',
			access_token : ""
		};

		user["provider"] = {
			email : "monikar@thoughtworks.com",
			password : "thoughtworks",
			client_id : 18556,
			api_token : 'af6d37106aa4d5fbda63c9c29b264dc4e3de6e35362fcc997659a18a58ce42ac',
			access_token : "",
			catchment : ["302607"]
		};

		user["confidential_patient"] = {
			email : "utsabban@thoughtworks.com",
			password : "thoughtworks",
			client_id : 18558,
			api_token : null,
			access_token : "",
			hid : '11214394110'

		};
	
	user["patient"] = {
			email : "hitansuj@thoughtworks.com",
			password : "thoughtworks",
			client_id : 18560,
			api_token : null,
			access_token : "",
			hid : '11246363561'

		};



	return user[user_type];

};
