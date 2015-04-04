module.exports = function(user_detail, catchment_area_code) {
	


	function CatchmentRequest(user_detail, catchment_area_code) {
		var config = require('./../Config').config;
		this.ip = config.shr_server_ip;
		this.port = config.shr_server_port;
		this.user_detail = user_detail;
		this.catchment_area_code = catchment_area_code;
		this.pad =  function (n) { return n < 10 ? '0'+n : n; };
		this.ISODateString = function() {
			var d = new Date();
			return d.getFullYear() + '-' + this.pad(d.getMonth() + 1) + '-' + this.pad(d.getDate()) + 'T' + this.pad(d.getHours()) + '%3A' + this.pad(d.getMinutes()) + '%3A' + this.pad(d.getSeconds()) + "%2B0530";
			
		};

		this.getUrl = function() {
			return "http://" + this.ip + ":" + this.port + "/v1/catchments/" + this.catchment_area_code + "/encounters?updatedSince=" + this.ISODateString();
		};

		this.getHeaders = function() {

			return {
				headers : {
					"Content-Type" : "application/json",
					"X-Auth-Token" : this.user_detail.access_token,
					"From" : this.user_detail.email,
					"client_id" : this.user_detail.client_id
				}
			};

		};

	};

	return new CatchmentRequest(user_detail, catchment_area_code);
};
