package domain;

import com.jayway.restassured.http.ContentType;
import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import data.PatientFactory;
import utils.IdpUserEnum;

import java.io.FileWriter;
import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static utils.IdentityLoginUtil.login;

public class DataSetup {
  private static final ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
  PatientsCollection patientsCollection;
  String baseUrl = config.property.get("mci_registry");

  public DataSetup() {
    patientsCollection = new PatientsCollection();
  }

  public void setup() throws IOException {
    createPatients();
    updateJSON();
  }

  private void updateJSON() throws IOException {
    String myCurrentDir = System.getProperty("user.dir");
    FileWriter fileWriter = new FileWriter(myCurrentDir + "/src/test/java/data/updatablePatient.json");
    fileWriter.write(patientsCollection.getPatientsList());
    fileWriter.close();
    System.out.println("Added successfully");
  }

  private void createPatients() {
    addPatientForDuplicatePatientTest();
    addPatientForAllCommonIds();
    addPatientForCommonNIDAndBinbrn();
    addPatientForCommonUIDAndBinbrn();
    addPatientForCommonUIDAndNid();
    addPatientForCommonNid();
    addPatientForCommonUID();
    addPatientForCommonBinbrn();

  }

  private void addPatientForCommonBinbrn() {
    String patientDetails = getPatientDetailsWithAllInformation();
    String hid = createPatient(patientDetails);
    patientsCollection.addPatient("common_binbrn",patientDetails, hid);
  }

  private void addPatientForCommonUID() {
    String patientDetails = getPatientDetailsWithAllInformation();
    String hid = createPatient(patientDetails);
    patientsCollection.addPatient("common_uid",patientDetails, hid);
  }

  private void addPatientForCommonNid() {
    String patientDetails = getPatientDetailsWithAllInformation();
    String hid = createPatient(patientDetails);
    patientsCollection.addPatient("common_nid",patientDetails, hid);

  }

  private void addPatientForCommonUIDAndBinbrn() {
    String patientDetails = getPatientDetailsWithAllInformation();
    String hid = createPatient(patientDetails);
    patientsCollection.addPatient("common_uid_and_binbrn",patientDetails, hid);
  }

  private void addPatientForCommonUIDAndNid() {
    String patientDetails = getPatientDetailsWithAllInformation();
    String hid = createPatient(patientDetails);
    patientsCollection.addPatient("common_uid_and_nid",patientDetails, hid);
  }

  private void addPatientForCommonNIDAndBinbrn() {
    String patientDetails = getPatientDetailsWithAllInformation();
    String hid = createPatient(patientDetails);
    patientsCollection.addPatient("common_nid_and_binbrn",patientDetails, hid);
  }

  private void addPatientForAllCommonIds() {
    String patientDetails = getPatientDetailsWithAllInformation();
    String hid = createPatient(patientDetails);
    patientsCollection.addPatient("common_uid_nid_and_binbrn",patientDetails, hid);
  }


  private void addPatientForDuplicatePatientTest() {
    String patientDetails = getPatientDetailsWithAllInformation();
    String hid = createPatient(patientDetails);
    patientsCollection.addPatient("duplicate",patientDetails, hid);
  }

  private String getPatientDetailsWithAllInformation() {
    Patient patient = PatientFactory.validPatientWithAllInformation();
    patient.gender = "M";
    return new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);
  }

  private String createPatient(String patientDetails) {
    String baseUrl = config.property.get("mci_registry");
    String IDP_SERVER_BASE_URL = config.property.get("idp_server_base_url");
    String patientContextPath = "/api/v1/patients";

    IdpUserEnum idpUser = IdpUserEnum.FACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

    return given().
        body(patientDetails)
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .post(baseUrl+patientContextPath)
        .then().statusCode(SC_CREATED)
        .contentType(ContentType.JSON).extract()
        .response().jsonPath().getString("id");

  }

}
