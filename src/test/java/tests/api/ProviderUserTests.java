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
import org.junit.Ignore;
import org.junit.Test;
import utils.IdpUserEnum;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static utils.IdentityLoginUtil.login;

public class ProviderUserTests {

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
  public void providerUserShouldGetCatchmentFeedForHisCatchmentAreaCode() throws Exception {
    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String accessToken = login(provider, IDP_SERVER_BASE_URL);
    String catchments = "302607";

    given().header("X-Auth-Token", accessToken).
        header("From", provider.getEmail()).
        header("client_id", provider.getClientId()).
        get(shrBaseUrl+"/v1/catchments/"+catchments+"/encounters?updateSince=2017-03-01T00%3A00%3A00.000%2B0530")
        .then().assertThat()
        .statusCode(SC_OK);
  }

  @Test
  public void providerUserShouldNotGetCatchmentFeedDetailsWhenUpazillaIdIsMissing() throws Exception {
    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String accessToken = login(provider, IDP_SERVER_BASE_URL);
    String catchments = "3026";

    given().header("X-Auth-Token", accessToken).
        header("From", provider.getEmail()).
        header("client_id", provider.getClientId()).
        get(shrBaseUrl+"/v1/catchments/"+catchments+"/encounters?updateSince=2017-03-01T00%3A00%3A00.000%2B0530")
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied to user " + provider.getClientId() + " for catchment " + catchments));
  }

  @Test
  public void providerUserShouldGetCatchmentFeedWhenCatchmentsHasCityId() throws Exception {
    //Catchment is not validated for the presence. It should give 404 for invalid catchments.
    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String accessToken = login(provider, IDP_SERVER_BASE_URL);
    String catchments = "3026072519";

    given().header("X-Auth-Token", accessToken).
        header("From", provider.getEmail()).
        header("client_id", provider.getClientId()).
        get(shrBaseUrl+"/v1/catchments/"+catchments+"/encounters?updateSince=2017-03-01T00%3A00%3A00.000%2B0530")
        .then().assertThat()
        .statusCode(SC_OK);

  }

  @Test
  public void providerUserShouldReceiveNonConfidentialEncounter() throws Exception {
    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String accessToken = login(provider, IDP_SERVER_BASE_URL);
    String hid = createValidPatient();

    String bundle = BundleFactory.BundleWithConditionEncounterForFever(hid);
    createEncounterForPatient(provider, accessToken, hid, bundle);
    String response = getEncounterForPatient(provider, accessToken, hid);

    JSONObject jsonObject = new JSONObject(response);
    JSONArray entries = new JSONArray(jsonObject.get("entries").toString());
    assertTrue(entries.length()>0);
  }


  //Failing needs fix bug BSHR-1073
  @Ignore
  @Test
  public void providerUserShouldCreateAndNotReceiveConfidentialEncounter() throws Exception {
    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String accessToken = login(provider, IDP_SERVER_BASE_URL);
    String hid = createValidPatient();
    String bundle = BundleFactory.BundleWithConfidentialEncounter(hid);
    createEncounterForPatient(provider, accessToken, hid, bundle);
    String response = getEncounterForPatient(provider, accessToken, hid);

    JSONObject jsonObject = new JSONObject(response);
    JSONArray entries = new JSONArray(jsonObject.get("entries").toString());
    assertEquals(0,entries.length());

  }

  @Test
  public void providerUserShouldNotReceiveAnyEncounterForConfidentialPatient() throws Exception {
    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String accessToken = login(provider, IDP_SERVER_BASE_URL);
    String hid = createConfidentialPatient();
    String bundle = BundleFactory.BundleWithConfidentialEncounter(hid);
    createEncounterForPatient(provider, accessToken, hid, bundle);

    given().header("X-Auth-Token", accessToken).
        header("From", provider.getEmail()).
        header("client_id", provider.getClientId())
        .get(shrBaseUrl + "/patients/" + hid + "/encounters")
        .then().assertThat().statusCode(SC_FORBIDDEN)
        .body("message",equalTo("Access is denied to user "+provider.getClientId()+" for patient "+hid));

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
        body(patientDetails).
        header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .post(patientContextPath)
        .then().statusCode(SC_CREATED)
        .contentType(ContentType.JSON)
        .extract()
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

  private String getEncounterForPatient(IdpUserEnum provider, String accessToken, String hid) {
    return given().header("X-Auth-Token", accessToken).
        header("From", provider.getEmail()).
        header("client_id", provider.getClientId())
        .get(shrBaseUrl + "/patients/" + hid + "/encounters")
        .then().assertThat().statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .extract().response().asString();
  }

}
