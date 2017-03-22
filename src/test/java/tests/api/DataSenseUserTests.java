package tests.api;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import data.PatientFactory;
import domain.BundleFactory;
import domain.Patient;
import nu.xom.ParsingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import utils.IdpUserEnum;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertTrue;
import static utils.IdentityLoginUtil.login;

public class DataSenseUserTests {
  ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();

  private final String IDP_SERVER_BASE_URL = config.property.get("idp_server_base_url");
  private final String shrBaseUrl = config.property.get("shr_registry");
  private final String mciBaseUrl = config.property.get("mci_registry");
  private final String patientContextPath = "/api/v1/patients";

  @Before
  public void setUp() throws Exception {
    RestAssured.baseURI = mciBaseUrl;
  }

  @Test
  public void dataSenseUserShouldReceiveNonConfidentialEncounter() throws Exception {
    IdpUserEnum datasense = IdpUserEnum.DATASENSE;
    String accessToken = login(datasense, IDP_SERVER_BASE_URL);

    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String providerAccessToken = login(provider, IDP_SERVER_BASE_URL);

    String hid = createValidPatient();
    String bundle = BundleFactory.BundleWithConditionEncounterForFever(hid);
    createEncounterForPatient(provider, providerAccessToken, hid, bundle);
    String response = getEncounterForPatient(datasense, accessToken, hid);

    JSONObject jsonObject = new JSONObject(response);
    JSONArray entries = new JSONArray(jsonObject.get("entries").toString());
    assertTrue(entries.length()>0);

  }

  @Test
  public void dataSenseUserShouldReceiveConfidentialEncounter() throws Exception {
    IdpUserEnum datasense = IdpUserEnum.DATASENSE;
    String accessToken = login(datasense, IDP_SERVER_BASE_URL);

    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String providerAccessToken = login(provider, IDP_SERVER_BASE_URL);

    String hid = createValidPatient();
    String bundle = BundleFactory.BundleWithConfidentialEncounter(hid);
    createEncounterForPatient(provider, providerAccessToken, hid, bundle);
    String response = getEncounterForPatient(datasense, accessToken, hid);

    JSONObject jsonObject = new JSONObject(response);
    JSONArray entries = new JSONArray(jsonObject.get("entries").toString());
    assertTrue(entries.length()>0);

  }

  @Test
  public void dataSenseUserShouldNotCreateEncounter() throws Exception {
    IdpUserEnum datasense = IdpUserEnum.DATASENSE;
    String accessToken = login(datasense, IDP_SERVER_BASE_URL);
    String hid = createValidPatient();
    String bundle = BundleFactory.BundleWithConditionEncounterForFever(hid);

    given().header("X-Auth-Token", accessToken).
        header("From", datasense.getEmail()).
        header("client_id", datasense.getClientId()).
        header("Content-Type", "application/xml; charset=utf-8")
        .body(bundle)
        .post(shrBaseUrl + "/patients/" + hid + "/encounters")
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message",equalTo("Access is denied"));
  }

  @Test
  public void dataSenseUserShouldReceiveEncounterForConfidentialPatient() throws Exception {
    IdpUserEnum datasense = IdpUserEnum.DATASENSE;
    String accessToken = login(datasense, IDP_SERVER_BASE_URL);

    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String providerAccessToken = login(provider, IDP_SERVER_BASE_URL);

    String hid = createConfidentialPatient();
    String bundle = BundleFactory.BundleWithConfidentialEncounter(hid);
    createEncounterForPatient(provider, providerAccessToken, hid, bundle);
    String response = getEncounterForPatient(datasense, accessToken, hid);

    JSONObject jsonObject = new JSONObject(response);
    JSONArray entries = new JSONArray(jsonObject.get("entries").toString());
    assertTrue(entries.length()>0);
  }

  @Test
  public void dataSenseUserShouldReceiveCatchmentFeedForHisCatchmentAreaCode() throws Exception {
    IdpUserEnum datasense = IdpUserEnum.DATASENSE;
    String accessToken = login(datasense, IDP_SERVER_BASE_URL);

    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String providerAccessToken = login(provider, IDP_SERVER_BASE_URL);

    String catchment = "302607";

    String hid = createValidPatient();
    String bundle = BundleFactory.BundleWithConditionEncounterForFever(hid);
    createEncounterForPatient(provider, providerAccessToken, hid, bundle);

    String response = given().header("X-Auth-Token", accessToken).
        header("From", datasense.getEmail()).
        header("client_id", datasense.getClientId()).
        get(shrBaseUrl + "/v1/catchments/" + catchment + "/encounters?updateSince=2017-03-01T00%3A00%3A00.000%2B0530")
        .then()
        .statusCode(SC_OK)
        .extract()
        .response()
        .asString();

    JSONObject jsonObject = new JSONObject(response);
    JSONArray entries = new JSONArray(jsonObject.get("entries").toString());
    assertTrue(entries.length()>0);
  }

  @Test
  public void dataSenseUserShouldNotBeAbleToGetCatchmentDetailsForUnauthorizedCatchments() throws Exception {
    IdpUserEnum datasense = IdpUserEnum.DATASENSE;
    String accessToken = login(datasense, IDP_SERVER_BASE_URL);
    String catchment = "312607";
    given().header("X-Auth-Token", accessToken).
        header("From", datasense.getEmail()).
        header("client_id", datasense.getClientId()).
        get(shrBaseUrl+"/v1/catchments/"+catchment+"/encounters?updateSince=2017-03-01T00%3A00%3A00.000%2B0530")
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message",equalTo("Access is denied to user "+datasense.getClientId()+" for catchment "+catchment));
  }

  @Test
  public void dataSenseUserShouldGetCatchmentFeedWhenCatchmentsHasCityId() throws Exception {
    IdpUserEnum datasense = IdpUserEnum.DATASENSE;
    String accessToken = login(datasense, IDP_SERVER_BASE_URL);

    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String providerAccessToken = login(provider, IDP_SERVER_BASE_URL);

    String hid = createValidPatient();
    String bundle = BundleFactory.BundleWithConditionEncounterForFever(hid);
    createEncounterForPatient(provider, providerAccessToken, hid, bundle);
    String catchment = "30260220";

    String response = given().header("X-Auth-Token", accessToken).
        header("From", datasense.getEmail()).
        header("client_id", datasense.getClientId()).
        get(shrBaseUrl + "/v1/catchments/" + catchment + "/encounters?updateSince=2017-03-01T00%3A00%3A00.000%2B0530")
        .then()
        .statusCode(SC_OK)
        .extract()
        .response()
        .asString();

    JSONObject jsonObject = new JSONObject(response);
    JSONArray entries = new JSONArray(jsonObject.get("entries").toString());
      assertTrue(entries.length()>0);

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
    IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
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

  private void createEncounterForPatient(IdpUserEnum provider, String accessToken, String hid, String bundle) {
    given().header("X-Auth-Token", accessToken).
        header("From", provider.getEmail()).
        header("client_id", provider.getClientId()).
        header("Content-Type", "application/xml; charset=utf-8")
        .body(bundle)
        .post(shrBaseUrl + "/patients/" + hid + "/encounters")
        .then().assertThat().statusCode(SC_OK);
  }

  private String getEncounterForPatient(IdpUserEnum datasense, String accessToken, String hid) {
    return given().header("X-Auth-Token", accessToken).
        header("From", datasense.getEmail()).
        header("client_id", datasense.getClientId())
        .get(shrBaseUrl + "/patients/" + hid + "/encounters")
        .then().assertThat().statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .extract().response().asString();
  }
}
