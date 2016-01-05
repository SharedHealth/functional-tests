var PatientUpdation = require("./update_patient_datasetup").PatientUpdation;
var DuplicatePatientsToRetain = require("./retain_duplicates_data_setup").DuplicatePatientsToRetain;
var MergeablePatients = require("./merge_duplicates_data_setup").DuplicatePatientsToMerge;

new PatientUpdation().setupData();
new DuplicatePatientsToRetain().setupData();
new MergeablePatients().setupData();