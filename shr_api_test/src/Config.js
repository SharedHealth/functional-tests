	var config = {};
	var env = process.env.ENVIRONMENT || 'QA';
	config['QA']  = {
			'shr_server_ip' : '172.18.46.57',
			'shr_server_port' : '8081',
			'mci_dns_name' : 'bdshr-mci-qa.twhosted.com',
			'sso_server_ip' : '172.18.46.56',
			'sso_server_port' : 8080,
			'mci_protocol' : 'https'
	};


	config['SHOWCASE']  = {
		'shr_server_ip' : '172.18.46.54',
		'shr_server_port' : '8081',
		'mci_dns_name' : "bdshr-mci.twhosted.com",
		'sso_server_url' : 'http://hrmtest.dghs.gov.bd/api/1.0/sso/signin',
		'mci_protocol' : 'https'
	};

	config['STAGING']  = {
		'shr_server_url' : "http://shrstg.twhosted.com",
		'mci_dns_name' : "mcistg.twhosted.com",
		'sso_server_url' : 'http://hrmtest.dghs.gov.bd/api/1.0/sso/signin',
		'mci_protocol' : 'http'
	};


	exports.config = config[env];
	




