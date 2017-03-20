package tests.api;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import data.PatientFactory;
import domain.BundleFactory;
import domain.Patient;
import domain.PatientFHIRXMLFactory;
import nu.xom.ParsingException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import utils.IdpUserEnum;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.core.IsEqual.equalTo;
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
    String validHid = createValidPatient();

    String bundle = BundleFactory.BundleWithConditionEncounterForFever(validHid);

       given().header("X-Auth-Token", accessToken).
        header("From", provider.getEmail()).
        header("client_id", provider.getClientId()).
        header("Content-Type", "application/xml; charset=utf-8")
        .body(bundle)
        .post(shrBaseUrl + "/patients/" + validHid + "/encounters")
        .then().assertThat().statusCode(SC_OK);


    JsonPath jsonPath = given().header("X-Auth-Token", accessToken).
        header("From", provider.getEmail()).
        header("client_id", provider.getClientId())
        .get(shrBaseUrl + "/patients/" + validHid + "/encounters")
        .then().statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .extract()
        .response().jsonPath();

  }

  private String createValidPatient() throws ParsingException, IOException {
    IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
    Patient patient = PatientFactory.validPatientWithMandatoryInformation();
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
}
