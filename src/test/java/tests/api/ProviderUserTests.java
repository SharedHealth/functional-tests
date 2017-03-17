package tests.api;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import org.junit.Test;
import utils.IdpUserEnum;

import java.util.Random;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.core.IsEqual.equalTo;
import static utils.IdentityLoginUtil.login;

public class ProviderUserTests {

  ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
  private final String IDP_SERVER_BASE_URL = config.property.get("idp_server_base_url");
  private final String baseUrl = config.property.get("shr_registry");

  @Test
  public void providerUserShouldGetCatchmentFeedForHisCatchmentAreaCode() throws Exception {
    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String accessToken = login(provider, IDP_SERVER_BASE_URL);
    String catchments = "302607";

    given().header("X-Auth-Token", accessToken).
        header("From", provider.getEmail()).
        header("client_id", provider.getClientId()).
        get(baseUrl+"/v1/catchments/"+catchments+"/encounters?updateSince=2017-03-01T00%3A00%3A00.000%2B0530")
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
        get(baseUrl+"/v1/catchments/"+catchments+"/encounters?updateSince=2017-03-01T00%3A00%3A00.000%2B0530")
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
        get(baseUrl+"/v1/catchments/"+catchments+"/encounters?updateSince=2017-03-01T00%3A00%3A00.000%2B0530")
        .then().assertThat()
        .statusCode(SC_OK);

  }
}
