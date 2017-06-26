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
import static org.apache.http.HttpStatus.SC_OK;
import static utils.IdentityLoginUtil.login;

public class DataSetup {
  private static final ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
  private PatientsCollection patientsCollection;
  private final String IDP_SERVER_BASE_URL = config.property.get(EnvironmentConfiguration.IDP_SERVER_BASE_URL);
  private final String baseUrl = config.property.get(EnvironmentConfiguration.MCI_SERVER_BASE_URL_KEY);

  public DataSetup() {
    patientsCollection = new PatientsCollection();
  }

  public void setup() throws IOException {
    createPatients();
    updateJSON("updatablePatient.json");
  }

  public void deDuplicateSetup() throws IOException {
    createDeduplicationPatients();
    updateJSON("duplicationPatients.json");
  }

  private void createDeduplicationPatients() {
    addPatientsWithSameNid();
    addPatientWithSameUid();
    addPatientWithSameBinbrn();
    addPatientWithMatchingNameAndAddress();
  }

  private void updateJSON(String fileName) throws IOException {
    String myCurrentDir = System.getProperty("user.dir");
    FileWriter fileWriter = new FileWriter(myCurrentDir + "/src/test/java/data/"+fileName);
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

  private void addPatientsWithSameNid() {
    Patient patientToRetain = new PatientFactory().validPatientWithMandatoryInformation();
    Patient patientForMerge = new PatientFactory().validPatientWithMandatoryInformation();

    patientToRetain.gender = patientForMerge.gender = "M";
    patientForMerge.nid = patientToRetain.nid;

    String patientDetailToRetain = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patientToRetain);
    String hidOfRetainPatient = createPatient(patientDetailToRetain);

    String patientDetailForMerge = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patientForMerge);
    String hidOfMergePatient = createPatient(patientDetailForMerge);
    patientsCollection.addPatient("patient_1_with_same_nid",getPatientDetails(hidOfRetainPatient), hidOfRetainPatient);
    patientsCollection.addPatient("patient_2_with_same_nid",getPatientDetails(hidOfMergePatient), hidOfMergePatient);
  }

  private void addPatientWithSameUid() {
    Patient patientToRetain = new PatientFactory().validPatientWithAllInformation();
    patientToRetain.gender = "M";
    String patientDetailToRetain = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patientToRetain);
    String hidOfRetainPatient = createPatient(patientDetailToRetain);

    Patient patientForMerge = new PatientFactory().validPatientWithAllInformation();
    patientForMerge.gender = "M";
    patientForMerge.uid = patientToRetain.uid;
    String patientDetailForMerge = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patientForMerge);
    String hidOfMergePatient = createPatient(patientDetailForMerge);

    patientsCollection.addPatient("patient_1_with_same_uid",getPatientDetails(hidOfRetainPatient), hidOfRetainPatient);
    patientsCollection.addPatient("patient_2_with_same_uid",getPatientDetails(hidOfMergePatient), hidOfMergePatient);
  }

  private void addPatientWithMatchingNameAndAddress() {
    Patient patientToRetain = new PatientFactory().validPatientWithAllInformation();
    patientToRetain.gender = "M";
    String patientDetailToRetain = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patientToRetain);
    String hidOfRetainPatient = createPatient(patientDetailToRetain);

    Patient patientForMerge = new PatientFactory().validPatientWithAllInformation();
    patientForMerge.gender = "M";
    patientForMerge.given = patientToRetain.given;
    patientForMerge.family = patientToRetain.family;
    patientForMerge.addressLine = patientToRetain.addressLine;
    String patientDetailForMerge = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patientForMerge);
    String hidOfMergePatient = createPatient(patientDetailForMerge);

    patientsCollection.addPatient("patient_1_with_matching_name_and_address",getPatientDetails(hidOfRetainPatient), hidOfRetainPatient);
    patientsCollection.addPatient("patient_2_with_matching_name_and_address",getPatientDetails(hidOfMergePatient), hidOfMergePatient);

  }

  private String getPatientDetails(String hid) {
    IdpUserEnum idpUser = IdpUserEnum.FACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
    return given().header("X-Auth-Token",accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .get(baseUrl + "/api/v1/patients/" + hid)
        .then().statusCode(SC_OK)
        .extract().response().asString();
  }

  private void addPatientWithSameBinbrn() {
    Patient patientToRetain = new PatientFactory().validPatientWithAllInformation();
    patientToRetain.gender = "M";

    String patientDetailToRetain = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patientToRetain);
    String hidOfRetainPatient = createPatient(patientDetailToRetain);

    Patient patientForMerge = new PatientFactory().validPatientWithAllInformation();
    patientForMerge.gender = "M";
    patientForMerge.binBRN = patientToRetain.binBRN;
    String patientDetailForMerge = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patientForMerge);
    String hidOfMergePatient = createPatient(patientDetailForMerge);

    patientsCollection.addPatient("patient_1_with_same_binbrn",getPatientDetails(hidOfRetainPatient), hidOfRetainPatient);
    patientsCollection.addPatient("patient_2_with_same_binbrn",getPatientDetails(hidOfMergePatient), hidOfMergePatient);
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
    String IDP_SERVER_BASE_URL = config.property.get(EnvironmentConfiguration.IDP_SERVER_BASE_URL);
    String  patientContextPath = config.property.get(EnvironmentConfiguration.MCI_PATIENT_CONTEXT_PATH_KEY);

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
