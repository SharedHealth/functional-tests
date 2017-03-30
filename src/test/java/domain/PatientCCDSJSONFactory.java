package domain;

import com.google.gson.Gson;
import domain.Patient;
import domain.PatientForJSON;

public class PatientCCDSJSONFactory {
  public PatientCCDSJSONFactory(String baseUrl) {
  }

  public String withValidJSON(Patient patient) {
    PatientForJSON jsonPatient = new PatientForJSON();
    jsonPatient.given_name = patient.given;
    jsonPatient.sur_name = patient.family;
    jsonPatient.gender = patient.gender;
    jsonPatient.nid = patient.nid;
    jsonPatient.date_of_birth = patient.birthDate+"T"+patient.birthTime+".000+05:30";
    jsonPatient.present_address.address_line = patient.addressLine;
    jsonPatient.present_address.division_id = patient.division_id;
    jsonPatient.present_address.district_id = patient.district_id;
    jsonPatient.present_address.upazila_id = patient.upazila_id;
    jsonPatient.present_address.country_code = '0'+patient.countryCode;
    jsonPatient.present_address.city_corporation_id = patient.city_corporation_id;
    jsonPatient.uid = patient.uid;
    jsonPatient.household_code = patient.householdCode;
    jsonPatient.bin_brn = patient.binBRN;
    jsonPatient.confidential = (patient.confidentiality != null && patient.confidentiality == true) ? "Yes" : "No";

    Gson gson = new Gson();
    return gson.toJson(jsonPatient);
  }
}
