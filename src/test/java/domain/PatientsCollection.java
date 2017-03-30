package domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.HashMap;

public class PatientsCollection {
  private final HashMap<String, HashMap<String, String>> patientsList;

  public PatientsCollection() {
    this.patientsList = new HashMap<>();
  }

  public void addPatient(String name, String details, String hid){
    HashMap<String, String> patient = new HashMap<>();
    patient.put("detail", details);
    patient.put("hid",hid);
    this.patientsList.put(name, patient);
  }

  public String getPatientsList(){
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonParser jsonParser = new JsonParser();
    JsonElement parse = jsonParser.parse(this.patientsList.toString());
    return gson.toJson(parse);

  }
}
