"use strict";
var CassandraClient = require('./../misc/cassandra_client').CassandraClient;


//mci_admin_user("Should be able to update the patient", function (done) {
//    request(patientRequest.updateUsingPut(hid), function (err, res, body) {
//        console.log(body);
//        expect(res.statusCode).to.equal(202);
//        done();
//    });
//});

function PatientUpdater(given_name, patient){

}

var getCodedName = getCodedName(given_name)
{
    return random_code.split("").map(convertToCharIfNumeric).join("");
};

var convertToCharIfNumeric = function(char) {
    var codeValue = char.charCodeAt(0);
    if ( codeValue >= 48 && codeValue <= 57) {
        return String.fromCharCode(65 + parseInt(char));
    }
    else
    {	return char;
    }
}
var   convertNumericToChar = function(char){
    return String.fromCharCode(65 + parseInt(char));
}


function updateRecords(result){
    console.log(result);
}

new CassandraClient('172.18.46.57','mci','cassandra','cassandra').executeQuery("select given_name, health_id from patient", writeResults);


