var request = require('request');
var User = require('../lib/user');

var SSORequest = require('../lib/SSORequest');
var Patient = require('../lib/patient');
var PatientRequest = require('../lib/patientRequest');
var CatchmentRequest = require('../lib/CatchmentRequest');

describe("Mci provider user", function() {
	var provider_user = new User('provider');
	var hid = "";
	var nid = ""
	var binBrn="";
	var confidential_patient_hid = "";

	before(function(done) {
		request.post(new SSORequest(provider_user).post(), function(err, httpResponse, body) {
			provider_user.access_token = JSON.parse(httpResponse.body).access_token;
			request.post(new PatientRequest(provider_user, new Patient()).post(), function(err, res, body) {
            hid = body.id;
      //  console.dir("hid1: "+hid);

			done();
		});

	});
});

	after(function(done) {
		hid = "";
		nid = "";
		binBrn = "";
		confidential_patient_hid = "";
		done();
	});

describe("Execute all MCI APIs for provider user", function() {

    var patientRequest;
    patientRequest=new PatientRequest(provider_user );



    it("Should be able to view patient By Hid", function(done) {
		request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function(err, res, body) {
		expect(res.statusCode).to.equal(200);
		expect(JSON.parse(body).hid).to.equal(hid);
		done();

			});
		});


	it("Should be able to view patient By Nid", function(done) {
	    request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function(err, res, body) {
	    nid=JSON.parse(body).nid

        request.get(patientRequest.getPatientDetailsByNid(nid), patientRequest.getHeaders(), function(err, res, body) {
    	expect(res.statusCode).to.equal(200);
    	expect(JSON.parse(body).results[0].nid).to.equal(nid);

    		done();

    			});
    		});
    	});
    it("Should be able to view patient By BinBrn", function(done) {
    	    request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function(err, res, body) {
    	    binBrn=JSON.parse(body).bin_brn;

            request.get(patientRequest.getPatientDetailsByBinBrn(binBrn), patientRequest.getHeaders(), function(err, res, body) {
        	//console.dir(JSON.parse(body).results)
        	expect(res.statusCode).to.equal(200);
        	expect(JSON.parse(body).results[0].bin_brn).to.equal(binBrn);

        		done();

        			});
        		});
        	});

    it("Should be able to download all patient by catchment", function(done){
            request.get(patientRequest.getAllPatientsByCatchment(provider_user.catchment), patientRequest.getHeaders(), function(err, res, body){
            expect(res.statusCode).to.equal(200);
               done();
           });
      });

    it("Should be able to update the patient", function(done){
           request.put(patientRequest.updatePost(hid),function(err, res, body){
           expect(res.statusCode).to.equal(202);
           done();
           });
          });

    });


});
