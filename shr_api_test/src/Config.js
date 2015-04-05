	var config = {};
	var env = process.env.ENVIRONMENT || 'QA';
	config['QA']  = {
			'shr_server_ip' : '172.18.46.57',
			'shr_server_port' : '8081',
			'mci_dns_name' : 'bdshr-mci-qa.twhosted.com',
			'sso_server_ip' : '172.18.46.56',
			'sso_server_port' : 8080
	};		
	
	 exports.config = config[env];
	




