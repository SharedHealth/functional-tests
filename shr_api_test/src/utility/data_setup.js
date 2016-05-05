var PatientUpdation = require("./update_patient_datasetup").PatientUpdation;
var DuplicatePatientsToRetain = require("./retain_duplicates_data_setup").DuplicatePatientsToRetain;
var MergeablePatients = require("./merge_duplicates_data_setup").DuplicatePatientsToMerge;
var async = require('async');

var error_method = [
    function (err, results) {
            if (err == null) {
            util.log("There is an error during datasetup");
            util.log(err);
        }
        else {
            util.log("All required patients created");

        }
    }
];


function setupData(){
    var series_methods = (new PatientUpdation().setupData());
    series_methods.concat(new MergeablePatients().setupData());
    series_methods.concat(new DuplicatePatientsToRetain().setupData());
    series_methods.concat(error_method);
    async.series(series_methods);
}

setupData();

