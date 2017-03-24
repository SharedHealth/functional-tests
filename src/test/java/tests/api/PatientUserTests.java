package tests.api;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import data.PatientFactory;
import domain.BundleFactory;
import domain.Patient;
import nu.xom.ParsingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import utils.IdpUserEnum;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;

import static com.jayway.restassured.RestAssured.with;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static utils.IdentityLoginUtil.login;
import static utils.IdentityLoginUtil.loginFor;

public class PatientUserTests {
  ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
  private final String IDP_SERVER_BASE_URL = config.property.get("idp_server_base_url");
  private final String shrBaseUrl = config.property.get("shr_registry");
  private final String mciBaseUrl = config.property.get("mci_registry");
  private final String  patientContextPath = "/api/v1/patients";

  @Before
  public void setUp() throws Exception {
    RestAssured.baseURI = mciBaseUrl;
  }

  @Test
  public void patientUserShouldNotReceiveNonConfidentialEncounterForOtherNonConfidentialPatient() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);
    String hid = createValidPatient();
    String bundle = BundleFactory.BundleWithConditionEncounterForFever(hid);
    createEncounterForPatient(hid, bundle);

   given().header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId())
        .get(shrBaseUrl + "/patients/" + hid + "/encounters")
        .then().assertThat()
       .statusCode(SC_FORBIDDEN)
       .body("message", equalTo("Access is denied to user "+idpUser.getClientId()+" for patient "+hid));
  }

  @Test
  public void patientUserShouldNotReceiveConfidentialEncounterForOtherNonConfidentialPatient() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);
    String hid = createValidPatient();
    String bundle = BundleFactory.BundleWithConfidentialEncounter(hid);
    createEncounterForPatient(hid, bundle);

    given().header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId())
        .get(shrBaseUrl + "/patients/" + hid + "/encounters")
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied to user "+idpUser.getClientId()+" for patient "+hid));
  }

  @Test
  public void patientUserShouldNotBeAbleToCreateEncounterForOtherNonConfidentialPatient() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);
    String hid = createValidPatient();
    String bundle = BundleFactory.BundleWithConditionEncounterForFever(hid);

    given().header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId()).
        header("Content-Type", "application/xml; charset=utf-8")
        .body(bundle)
        .post(shrBaseUrl + "/patients/" + hid + "/encounters")
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message",equalTo("Access is denied"));
  }

  @Test
  public void patientUserShouldNotReceiveEncountersForConfidentialPatient() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);
    String hid = createConfidentialPatient();
    String bundle = BundleFactory.BundleWithConfidentialEncounter(hid);
    createEncounterForPatient(hid, bundle);

    given().header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId())
        .get(shrBaseUrl + "/patients/" + hid + "/encounters")
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied to user "+idpUser.getClientId()+" for patient "+hid));

  }

  @Test
  public void patientUserShouldNotBeAbleToPostEncounterFromHimselfOrHerself() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);
    String hid = idpUser.getHid();
    String bundle = BundleFactory.BundleWithConditionEncounterForFever(hid);
    String previousEncounters = getEncounterForPatient(idpUser, accessToken, hid);

    given().header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId()).
        header("Content-Type", "application/xml; charset=utf-8")
        .body(bundle)
        .post(shrBaseUrl + "/patients/" + hid + "/encounters")
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message",equalTo("Access is denied"));

    String currentEncounters = given().header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId())
        .get(shrBaseUrl + "/patients/" + hid + "/encounters")
        .then()
        .statusCode(SC_OK)
        .extract().response().asString();

    assertEquals(getLengthOfEntries(currentEncounters), getLengthOfEntries(previousEncounters));
  }

  @Test
  public void patientUserShouldBeAbleToSeeHisOrHerOwnConfidentialEncounter() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);
    String hid = idpUser.getHid();
    String bundle = BundleFactory.BundleWithConfidentialEncounter(hid);
    createEncounterForPatient(hid, bundle);
    String previousEncounters = getEncounterForPatient(idpUser, accessToken, hid);
    createEncounterForPatient(hid, bundle);
    String currentEncounters = getEncounterForPatient(idpUser, accessToken, hid);
    assertEquals(getLengthOfEntries(previousEncounters)+1, getLengthOfEntries(currentEncounters));
  }

  @Test
  public void patientUserShouldBeAbleToSeeHisOrHerOwnNonConfidentialEncounter() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);
    String hid = idpUser.getHid();
    String bundle = BundleFactory.BundleWithConditionEncounterForFever(hid);
    createEncounterForPatient(hid, bundle);
    String previousEncounters = getEncounterForPatient(idpUser, accessToken, hid);
    createEncounterForPatient(hid, bundle);
    String currentEncounters = getEncounterForPatient(idpUser, accessToken, hid);
    assertEquals(getLengthOfEntries(previousEncounters)+1, getLengthOfEntries(currentEncounters));
  }

  @Test
  public void patientUserShouldNotBeAbleToReceiveEncountersForHisCatchmentAreaCode() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);
    String catchments = "302607";

    given().header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId()).
        get(shrBaseUrl+"/v1/catchments/"+catchments+"/encounters?updateSince=2017-03-01T00%3A00%3A00.000%2B0530")
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message",equalTo("Access is denied"));
  }

  @Test
  public void patientUserShouldNotBeAbleToCreatePatient() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);
    Patient patient = PatientFactory.validPatientWithMandatoryInformation();
    patient.gender = "M";
    String  patientDetails = new PatientCCDSJSONFactory(mciBaseUrl).withValidJSON(patient);

    given().
        body(patientDetails)
        .header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .post(patientContextPath)
        .then()
        .assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void patientUserShouldNotBeAbleToViewOtherPatientByHid() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);
    String hid = createValidPatient();

    String expectedMessage = "Access is denied to user "+idpUser.getClientId()+" for patient with healthId : "+hid;

    given().header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId())
        .get(patientContextPath+"/"+hid)
        .then()
        .assertThat()
        .assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo(expectedMessage));
  }

  @Test
  public void patientUserShouldBeAbleToViewPatientByHisOrHerOwnHid() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);
    String hid = idpUser.getHid();

    given().header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId())
        .get(patientContextPath+"/"+hid)
        .then().assertThat().statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .body(notNullValue());
  }

  @Test
  public void patientUserShouldNotBeAbleToViewPatientByNid() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);
    String hid = idpUser.getHid();
    JsonPath patientDetails = getPatientDetailsByHID(idpUser, accessToken, hid);
    String nid = patientDetails.get("nid");

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .get(patientContextPath+"/?nid=" + nid)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void patientUserShouldNotBeAbleToViewPatientByBinBrn() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);

    Patient patient = PatientFactory.validPatientWithMandatoryInformation();
    patient.binBRN = "14893974754477445";
    String hid = idpUser.getHid();
    JsonPath patientDetails = getPatientDetailsByHID(idpUser, accessToken, hid);
    String binBrn = patientDetails.get("bin_brn");

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .get(patientContextPath+"/?bin_brn=" +binBrn)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void patientUserShouldNotBeAbleToViewPatientsByHouseHoldCode() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);

    Patient patient = PatientFactory.validPatientWithMandatoryInformation();
    patient.householdCode = patient.nid;
    String hid = idpUser.getHid();
    JsonPath patientDetails = getPatientDetailsByHID(idpUser, accessToken, hid);
    String householdCode = patientDetails.get("household_code");

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .get(patientContextPath+"/?household_code=" + householdCode)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void patientUserShouldBeNotAbleToViewPatientByNameAndLocation() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);
    String hid = idpUser.getHid();

    JsonPath patientDetails = getPatientDetailsByHID(idpUser, accessToken, hid);

    String givenName = patientDetails.get("given_name");
    String surName = patientDetails.get("sur_name");
    String district_id = patientDetails.get("present_address.district_id");
    String division_id = patientDetails.get("present_address.division_id");
    String upazila_id = patientDetails.get("present_address.upazila_id");
    String address = "" + division_id + district_id + upazila_id;

    given().
        header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId()).
        get(patientContextPath + "/?given_name=" + givenName + "&sur_name=" + surName + "&present_address=" + address).
        then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void patientUserShouldNotBeAbleToDownloadPatientsByCatchment() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);

    String hid = createValidPatient();
    String bundle = BundleFactory.BundleWithConditionEncounterForFever(hid);

    createEncounterForPatient(hid, bundle);

    given().
        header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId()).
        get("/api/v1/catchments/302607/patients")
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message",equalTo("Access is denied"));
  }

  @Test
  public void patientUserShouldNotBeAbleToUpdateThePatient() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);

    String hid = createValidPatient();

    given().
        header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId()).
        body("{\"gender\":\"F\"}").
        contentType(ContentType.JSON)
        .put(patientContextPath +"/"+ hid)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message",equalTo("Access is denied"));

  }

  @Test
  public void patientUserShouldNotBeAbleToViewPendingApprovalPatientByCatchment() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .get("/api/v1/catchments/3026/approvals/")
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void patientUserShouldNotBeAbleToViewPendingApprovalPatientByHID() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);
    String hid = createValidPatient();
    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .get("/api/v1/catchments/3026/approvals/"+hid)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void patinetUserShouldNotBeAbleToAcceptPendingApprovalForPatient() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);

    String hid = createValidPatient();
    updatePatient(hid);

    given().
        header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId()).
        header("Content-Type", "application/json").
        body("{\"sur_name\":\"mohammad\"}")
        .put(mciBaseUrl+"/api/v1/catchments/3026/approvals/"+hid)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void patientUserShouldNotBeAbleToRejectPendingApprovalForPatient() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);

    String hid = createValidPatient();
    updatePatient(hid);

    given().
        header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId()).
        header("Content-Type", "application/json").
        body("{\"sur_name\":\"mohammad\"}")
        .delete(mciBaseUrl+"/api/v1/catchments/3026/approvals/"+hid)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void patientShouldNotBeAbleToGetAuditLogByHID() throws Exception {
    String hid = createValidPatient();
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .get("/api/v1/audit/patients/"+hid)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void patientUserShouldNotBeAbleToGetShrFeedByHID() throws Exception {
    String hid = createValidPatient();
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .get("/api/v1/feed/patients?hid=" + hid)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void patientUserShouldNotBeAbleToGetLocationDetails() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.PATIENT;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facilityUser, IDP_SERVER_BASE_URL);

    given().
        header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId())
        .get(mciBaseUrl + "/api/v1/locations?parent=3026")
        .then()
        .assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message",equalTo("Access is denied"));
  }

  private int getLengthOfEntries(String encounters) throws JSONException {
    JSONObject jsonObject = new JSONObject(encounters);
    JSONArray entries = new JSONArray(jsonObject.get("entries").toString());
    return entries.length();
  }

  private String createConfidentialPatient() throws ParsingException, IOException {
    Patient patient = PatientFactory.validConfidentialPatientWithMandatoryInformation();
    return createPatient(patient);
  }


  private String createValidPatient() throws ParsingException, IOException {
    Patient patient = PatientFactory.validPatientWithMandatoryInformation();
    return createPatient(patient);
  }

  private String createPatient(Patient patient) {
    IdpUserEnum idpUser = IdpUserEnum.FACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
    patient.gender = "M";
    String  patientDetails = new PatientCCDSJSONFactory(mciBaseUrl).withValidJSON(patient);

    return given().
        body(patientDetails)
        .header("X-Auth-Token", accessToken).
            header("From", idpUser.getEmail()).
            header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .post(patientContextPath)
        .then().statusCode(SC_CREATED)
        .contentType(ContentType.JSON).extract()
        .response().jsonPath().getString("id");
  }

  private void createEncounterForPatient(String hid, String bundle) {
    IdpUserEnum idpUser = IdpUserEnum.FACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
    given().header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId()).
        header("Content-Type", "application/xml; charset=utf-8")
        .body(bundle)
        .post(shrBaseUrl + "/patients/" + hid + "/encounters")
        .then().assertThat().statusCode(SC_OK);
  }

  private String getEncounterForPatient(IdpUserEnum idpUser, String accessToken, String hid) {
    return given().header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId())
        .get(shrBaseUrl + "/patients/" + hid + "/encounters")
        .then().assertThat().statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .extract().response().asString();
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

  private void updatePatient(String hid) {
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String facilityAccessToken = login(facilityUser, IDP_SERVER_BASE_URL);
    given().
        header("X-Auth-Token", facilityAccessToken).
        header("From", facilityUser.getEmail()).
        header("client_id", facilityUser.getClientId()).
        body("{\"sur_name\":\"mohammad\"}").
        contentType(ContentType.JSON)
        .put(patientContextPath + "/" +  hid)
        .then().assertThat().statusCode(SC_ACCEPTED);
  }
}
