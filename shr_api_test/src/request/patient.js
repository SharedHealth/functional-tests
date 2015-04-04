module.exports = function(user_detail, confidential) {

	function PatientRequest(user_detail, patient_detail) {
   		var config = require('./../Config').config;
		this.user_detail = user_detail;
		this.confidential = confidential || 'No';
		this.server = config.mci_dns_name;
        this.patient_detail = patient_detail;
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
                method : 'POST',
				url : "https://" + this.server +  "/api/v1/patients",
				headers : this.headers(),
				json : true,
				body : this.patient_detail
			};

		};

		this.updatePost = function(hid) {
        			return {
        				url : "https://" + this.server + "/api/v1/patients/" + hid,
        				headers : this.headers(),
        				json : true,
        				body : {"gender" : "F"}
        			};

        		};


        this.get = function (uri) {
            return {
                method : 'GET',
                headers : this.headers(),
                'uri' : uri || this.uri
            }

        }


		this.getPatientDetailsByHid = function(hid)
		{
		    return this.get("https://" + this.server + "/api/v1/patients/" + hid);
		}


		this.getPatientDetailsByNid = function(nid)
        {
        		    return this.get("https://" + this.server + "/api/v1/patients/?nid=" + nid);
  		}

        this.getPatientDetailsByBinBrn = function(binBrn)
        {
            return this.get("https://" + this.server + "/api/v1/patients/?bin_brn=" + binBrn);
        }
        this.getAllPatientsByCatchment = function(catchment)
        {
                		    return this.get("https://" + this.server + "/api/v1/catchments/"+catchment+"/patients");
        }

        this.getAllPendingApprovalPatientsByCatchment = function(catchment)
        {
                		    return this.get("https://" + this.server + "/api/v1/catchments/"+catchment+"/approvals");
        }


	}

	return new PatientRequest(user_detail, confidential);

};

