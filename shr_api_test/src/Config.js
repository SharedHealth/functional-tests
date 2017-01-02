	var config = {};
	var env = process.env.ENVIRONMENT || 'QA';


	config['QA']  = {
			'env' : env,
			'shr_server_ip' : '172.18.46.57',
			'shr_server_port' : '8083',
			'mci_dns_name' : 'bdshr-mci-qa.twhosted.com',
			'sso_server_ip' : '172.18.46.55',
			'sso_server_port' : 8084,
			'mci_protocol' : 'https',
			patient_uri : "http://172.18.46.56:8081/api/default/patients/",
			facility_uri : "http://hrmtest.dghs.gov.bd/api/1.0/facilities/",
			concept_uri : "http://172.18.46.56:9080/openmrs/ws/rest/v1/tr/",
			provider_uri : "http://hrmtest.dghs.gov.bd/api/1.0/providers/"

	};


	config['SHOWCASE']  = {
		'env' : env,
		'shr_server_ip' : '172.18.46.54',
		'shr_server_port' : '8081',
		'mci_dns_name' : "bdshr-mci.twhosted.com",
		'sso_server_url' : 'http://hrmtest.dghs.gov.bd/api/1.0/sso/signin',
		'mci_protocol' : 'https',
		patient_uri : "http://172.18.46.53:8081/api/v1/patients/",
		facility_uri : "http://hrmtest.dghs.gov.bd/api/1.0/facilities/",
		concept_uri : "http://172.18.46.53:9080/openmrs/ws/rest/v1/tr/",
		provider_uri : "http://hrmtest.dghs.gov.bd/api/1.0/providers/"

	};

	config['STAGING']  = {
		'env' : env,
		'shr_server_url' : "http://shrstg.twhosted.com",
		'mci_dns_name' : "mcistg.twhosted.com",
		'sso_server_url' : 'http://hrmtest.dghs.gov.bd/api/1.0/sso/signin',
		'mci_protocol' : 'http',
		patient_uri : "http://mcistg.twhosted.com/api/v1/patients/",
		facility_uri : "http://hrmtest.dghs.gov.bd/api/1.0/facilities/",
		concept_uri : "http://trstg.twhosted.com/openmrs/ws/rest/v1/tr/",
		provider_uri : "http://hrmtest.dghs.gov.bd/api/1.0/providers/"

	};


	config['LOCAL']  = {
		'env' : env,
		'shr_server_ip' : '192.168.33.10',
		'shr_server_port' : '8081',
		'mci_dns_name' : 'bdshr-mci-qa.twhosted.com',
		'sso_server_ip' : '172.18.46.56',
		'sso_server_port' : 8084,
		'mci_protocol' : 'https',
		patient_uri : "http://172.18.46.56:8081/api/default/patients/",
		facility_uri : "http://hrmtest.dghs.gov.bd/api/1.0/facilities/",
		concept_uri : "http://172.18.46.56:9080/openmrs/ws/rest/v1/tr/",
		provider_uri : "http://hrmtest.dghs.gov.bd/api/1.0/providers/"

	};


	exports.config = config[env];
	




