package tests.api;

import com.google.gson.Gson;
import data.PatientFactory;
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

    Gson gson = new Gson();
    return gson.toJson(jsonPatient);
  }
}
