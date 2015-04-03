var request = require('request');
var User = require('../lib/user');

var SSORequest = require('../lib/SSORequest');
var Patient = require('../lib/patient');
var PatientRequest = require('../lib/patientRequest');
var CatchmentRequest = require('../lib/CatchmentRequest');
var getPatientDetailsRequest= require('../lib/getPatientDetailsRequest')

describe.only("mci facility user", function() {
	var facility_user = new User('facility');
	var hid = "";
	var nid = ""
	var confidential_patient_hid = "";

	before(function(done) {
		request.post(new SSORequest(facility_user).post(), function(err, httpResponse, body) {
			facility_user.access_token = JSON.parse(httpResponse.body).access_token;
			request.post(new PatientRequest(facility_user, new Patient()).post(), function(err, res, body) {
            hid = body.id;


			done();
		});

	});
});

describe("Execute all MCI APIs", function() {

    var patientRequest;
 //   beforeEach(function(done) {
    patientRequest=new PatientRequest(facility_user );
//    done();

 //  });

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
	    console.dir(nid)

        request.get(patientRequest.getPatientDetailsByNid(nid), patientRequest.getHeaders(), function(err, res, body) {
    	expect(res.statusCode).to.equal(200);
    	expect(JSON.parse(body).results[0].nid).to.equal(nid);

    		done();

    			});
    		});
    	});
    it("Should be able to view patient By Uid", function(done) {
    	    request.get(patientRequest.getPatientDetailsByHid(hid), patientRequest.getHeaders(), function(err, res, body) {
    	    nid=JSON.parse(body).nid
    	    console.dir(nid)

            request.get(patientRequest.getPatientDetailsByNid(nid), patientRequest.getHeaders(), function(err, res, body) {
        	expect(res.statusCode).to.equal(200);
        	expect(JSON.parse(body).results[0].nid).to.equal(nid);

        		done();

        			});
        		});
        	});





    });


});
