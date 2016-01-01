var PatientUpdation = require("./update_patient_datasetup").PatientUpdation;
var DuplicatePatientsToRetain = require("./retain_duplicates_data_setup").DuplicatePatientsToRetain;

new PatientUpdation().setupData();
new DuplicatePatientsToRetain().setupData();