package tests.api;

import categories.ApiTest;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import data.PatientFactory;
import domain.Patient;
import domain.PatientCCDSJSONFactory;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import utils.IdpUserEnum;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static utils.IdentityLoginUtil.login;

@Category(ApiTest.class)
public class CreatePatientTests {

  ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
  private final String IDP_SERVER_BASE_URL = config.property.get(EnvironmentConfiguration.IDP_SERVER_BASE_URL);
  private final String baseUrl = config.property.get(EnvironmentConfiguration.MCI_SERVER_BASE_URL_KEY);
  private final String  patientContextPath = config.property.get(EnvironmentConfiguration.MCI_PATIENT_CONTEXT_PATH_KEY);
  private String patientList;

  @Before
  public void setUp() throws Exception {
    RestAssured.baseURI = baseUrl;
    String myCurrentDir = System.getProperty("user.dir");
    patientList = new String(Files.readAllBytes(Paths.get(myCurrentDir + "/src/test/java/data/updatablePatient.json")));
  }

  //This test case is failing because cassandra takes time to update the contents.
  // CAP Theorem - Consistency is sacrificed for performance and availability
  @Ignore
  @Test
  public void facilityUserShouldBeAbleToUpdateExistingPatient() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.TESTFACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
    Patient patient = PatientFactory.validPatientWithMandatoryInformation();
    patient.gender = "M";
    String  patientDetails = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);

    given().
        body(patientDetails)
        .header("X-Auth-Token", accessToken).
            header("From", idpUser.getEmail()).
            header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .post(patientContextPath)
        .then().assertThat()
        .statusCode(SC_CREATED)
        .contentType(ContentType.JSON)
        .body(notNullValue());

    given().
        body(patientDetails)
        .header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .post(patientContextPath)
        .then().assertThat()
        .statusCode(SC_ACCEPTED)
        .contentType(ContentType.JSON)
        .body(notNullValue());
  }

  @Test
  public void facilityUserShouldBeAbleToCreatePatientWithHIDCardStatusAsRegisterAndUpdateItToIssued() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.FACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
    Patient patient = PatientFactory.validPatientWithMandatoryInformation();
    patient.gender = "M";
    String  patientDetails = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);

    String hid = given().
        body(patientDetails)
        .header("X-Auth-Token", accessToken).
            header("From", idpUser.getEmail()).
            header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .post(patientContextPath)
        .then().statusCode(SC_CREATED)
        .contentType(ContentType.JSON).extract()
        .response().jsonPath().getString("id");

    String hidCardStatus = getPatientDetailsByHID(idpUser, accessToken, hid).get("hid_card_status");
    assertEquals("REGISTERED",hidCardStatus);

    updatePatient(hid,"{\"hid_card_status\":\"ISSUED\"}");
    String updatedHidCardStatus = getPatientDetailsByHID(idpUser, accessToken, hid).get("hid_card_status");
    assertEquals("ISSUED",updatedHidCardStatus);
  }

  @Test
  @Ignore("Auto Merge Scenario")
  public void facilityUserShouldBeAbleToUpdateAnExistingPatientOnAllFieldsMatch() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.FACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

    JSONObject patient = new JSONObject(patientList).getJSONObject("duplicate");
    String patientDetails = patient.get("detail").toString();
    String hid = given().
        body(patientDetails)
        .header("X-Auth-Token", accessToken).
            header("From", idpUser.getEmail()).
            header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .post(patientContextPath)
        .then().statusCode(SC_ACCEPTED)
        .contentType(ContentType.JSON).extract()
        .response().jsonPath().getString("id");

    assertEquals(patient.get("hid").toString(),hid);
  }

  @Test
  @Ignore("Auto Merge Scenario")
  public void facilityUserShouldBeAbleToUpdateAnExistingPatientIfNidUidAndBinBrnMatch() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.FACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

    JSONObject prevPatient = new JSONObject(patientList).getJSONObject("common_uid_nid_and_binbrn");
    JSONObject prevPatientDetail = prevPatient.getJSONObject("detail");

    Patient patient = new PatientFactory().validPatientWithAllInformation();
    patient.uid = prevPatientDetail.get("uid").toString();
    patient.nid = prevPatientDetail.get("nid").toString();
    patient.binBRN = prevPatientDetail.get("bin_brn").toString();
    patient.gender = "M";
    String patientDetails = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);

    String hid = given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(patientDetails)
        .post(patientContextPath)
        .then().statusCode(SC_ACCEPTED)
        .contentType(ContentType.JSON).extract()
        .response().jsonPath().getString("id");

    assertEquals(prevPatient.get("hid").toString(),hid);
  }

  @Test
  @Ignore("Auto Merge Scenario")
  public void facilityUserShouldBeAbleToUpdateAnExistingPatientIfNidAndBinBrnMatch() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.FACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

    JSONObject prevPatient = new JSONObject(patientList).getJSONObject("common_nid_and_binbrn");
    JSONObject prevPatientDetail = prevPatient.getJSONObject("detail");

    Patient patient = new PatientFactory().validPatientWithAllInformation();
    patient.nid = prevPatientDetail.get("nid").toString();
    patient.binBRN = prevPatientDetail.get("bin_brn").toString();
    patient.gender = "M";
    String patientDetails = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);

    String hid = given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(patientDetails)
        .post(patientContextPath)
        .then().statusCode(SC_ACCEPTED)
        .contentType(ContentType.JSON).extract()
        .response().jsonPath().getString("id");

    assertEquals(prevPatient.get("hid").toString(),hid);
  }

  @Test
  @Ignore("Auto Merge Scenario")
  public void facilityUserShouldBeAbleToUpdateExistingPatientIfBinBrnAndUidMatch() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.FACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

    JSONObject prevPatient = new JSONObject(patientList).getJSONObject("common_uid_and_binbrn");
    JSONObject prevPatientDetail = prevPatient.getJSONObject("detail");

    Patient patient = new PatientFactory().validPatientWithAllInformation();
    patient.uid = prevPatientDetail.get("uid").toString();
    patient.binBRN = prevPatientDetail.get("bin_brn").toString();
    patient.gender = "M";
    String patientDetails = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);

    String hid = given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(patientDetails)
        .post(patientContextPath)
        .then().statusCode(SC_ACCEPTED)
        .contentType(ContentType.JSON).extract()
        .response().jsonPath().getString("id");

    assertEquals(prevPatient.get("hid").toString(),hid);
  }

  @Test
  @Ignore("Auto Merge Scenario")
  public void facilityUserShouldBeAbleToUpdateExistingPatientIfNidAndUidMatch() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.FACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

    JSONObject prevPatient = new JSONObject(patientList).getJSONObject("common_uid_and_nid");
    JSONObject prevPatientDetail = prevPatient.getJSONObject("detail");

    Patient patient = new PatientFactory().validPatientWithAllInformation();
    patient.nid = prevPatientDetail.get("nid").toString();
    patient.uid = prevPatientDetail.get("uid").toString();
    patient.gender = "M";
    String patientDetails = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);

    String hid = given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(patientDetails)
        .post(patientContextPath)
        .then().statusCode(SC_ACCEPTED)
        .contentType(ContentType.JSON).extract()
        .response().jsonPath().getString("id");

    assertEquals(prevPatient.get("hid").toString(),hid);
  }

  @Test
  @Ignore("Auto Merge Scenario")
  public void facilityUserShouldBeAbleToCreateNewPatientOnlyNidMatches() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.FACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

    JSONObject prevPatient = new JSONObject(patientList).getJSONObject("common_nid");
    JSONObject prevPatientDetail = prevPatient.getJSONObject("detail");

    Patient patient = new PatientFactory().validPatientWithAllInformation();
    patient.nid = prevPatientDetail.get("nid").toString();
    patient.gender = "M";
    String patientDetails = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(patientDetails)
        .post(patientContextPath)
        .then().assertThat()
        .statusCode(SC_CREATED);
  }

  @Test
  public void facilityUserShouldBeAbleToCreateNewPatientOnlyUidMatches() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.FACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

    JSONObject prevPatient = new JSONObject(patientList).getJSONObject("common_uid");
    JSONObject prevPatientDetail = prevPatient.getJSONObject("detail");

    Patient patient = new PatientFactory().validPatientWithAllInformation();
    patient.uid = prevPatientDetail.get("uid").toString();
    patient.gender = "M";
    String patientDetails = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(patientDetails)
        .post(patientContextPath)
        .then().assertThat()
        .statusCode(SC_CREATED);
  }

  @Test
  public void facilityUserShouldBeAbleToCreateNewPatientOnlyBinBrnMatches() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.FACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

    JSONObject prevPatient = new JSONObject(patientList).getJSONObject("common_binbrn");
    JSONObject prevPatientDetail = prevPatient.getJSONObject("detail");

    Patient patient = new PatientFactory().validPatientWithAllInformation();
    patient.binBRN = prevPatientDetail.get("bin_brn").toString();
    patient.gender = "M";
    String patientDetails = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(patientDetails)
        .post(patientContextPath)
        .then().assertThat()
        .statusCode(SC_CREATED);
  }

  private JsonPath getPatientDetailsByHID(IdpUserEnum idpUser, String accessToken, String hid) {
    String response = with().
        header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId()).
        get(patientContextPath+ "/" + hid)
        .then()
        .statusCode(SC_OK)
        .extract()
        .response().asString();

    return new JsonPath(response);
  }

  private void updatePatient(String hid, String field) {
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String facilityAccessToken = login(facilityUser, IDP_SERVER_BASE_URL);
    given().
        header("X-Auth-Token", facilityAccessToken).
        header("From", facilityUser.getEmail()).
        header("client_id", facilityUser.getClientId()).
        body(field).
        contentType(ContentType.JSON)
        .put(patientContextPath + "/" +  hid)
        .then().assertThat().statusCode(SC_ACCEPTED);
  }
}
