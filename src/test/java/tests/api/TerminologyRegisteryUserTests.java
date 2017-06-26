package tests.api;

import categories.ApiTest;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.PreemptiveBasicAuthScheme;
import com.jayway.restassured.response.Response;
import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static com.jayway.restassured.RestAssured.given;
import static config.EnvironmentConfiguration.TR_SERVER_BASE_URL_KEY;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Category(ApiTest.class)
public class TerminologyRegisteryUserTests {
  ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
  private final String baseUrl = config.property.get(TR_SERVER_BASE_URL_KEY);

  @Test
  public void shouldGetRecentMedicationFeed() throws Exception {
    given().get(baseUrl + "/openmrs/ws/atomfeed/medication/recent")
        .then().assertThat()
        .statusCode(SC_OK)
        .body(notNullValue());
  }

  @Test
  public void shouldNotGetMedicationFeedForInvalidUrl() throws Exception {
    given().get(baseUrl + "/openmrs/ws/atomfeed/medication/ree")
        .then().assertThat()
        .statusCode(SC_INTERNAL_SERVER_ERROR);
  }

  @Test
  public void shouldGetValueSetDetailsForProcedureOutcome() throws Exception {
    PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
    authScheme.setUserName("admin");
    authScheme.setPassword("Admin123");
    RestAssured.authentication = authScheme;

    Response response = given()
        .get(baseUrl + "/openmrs/ws/rest/v1/tr/vs/Procedure-Outcome")
        .then()
        .statusCode(SC_OK)
        .extract().response();

    JSONArray concept = new JSONObject(response.getBody().asString()).getJSONObject("codeSystem").getJSONArray("concept");
    assertTrue(concept.length()>0);
  }

  @Test
  public void shouldGetMedicationDetailsIfMedicationIdIsValid() throws Exception {
    PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
    authScheme.setUserName("admin");
    authScheme.setPassword("Admin123");
    RestAssured.authentication = authScheme;

    given()
        .get(baseUrl+"/openmrs/ws/rest/v1/tr/drugs/90b344bf-8147-11e5-b875-0050568225ca")
        .then()
        .statusCode(SC_OK)
        .body(notNullValue());
  }

  @Test
  public void shouldNotGetMedicationDetailsIfMedicationIDIsInvalid() throws Exception {
    PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
    authScheme.setUserName("admin");
    authScheme.setPassword("Admin123");
    RestAssured.authentication = authScheme;

    Response response = given()
        .get(baseUrl + "/openmrs/ws/rest/v1/tr/drugs/5799c579-3c78-4133-9e02-91c1006d862d")
        .then().assertThat()
        .statusCode(SC_NOT_FOUND)
        .extract().response();

    String message = new JSONObject(response.getBody().asString()).getJSONObject("error").get("message").toString();
    assertEquals("Concept not found",message);
  }

  @Test
  public void shouldNotGetMedicationDetailsIfUserNameIsInvalid() throws Exception {
    PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
    authScheme.setUserName("admi");
    authScheme.setPassword("Admin123");
    RestAssured.authentication = authScheme;

    Response response = given()
        .get(baseUrl + "/openmrs/ws/rest/v1/tr/drugs/5799c579-3c78-4133-9e02-91c1006d862d")
        .then().assertThat()
        .statusCode(SC_UNAUTHORIZED)
        .extract().response();

    String message = new JSONObject(response.getBody().asString()).getJSONObject("error").get("message").toString();
    assertEquals("User is not logged in",message);
  }

  @Test
  public void shouldGetRecentReferenceTermFeed() throws Exception {
    given().get(baseUrl + "/openmrs/ws/atomfeed/conceptreferenceterm/recent")
        .then().assertThat()
        .statusCode(SC_OK)
        .body(notNullValue());
  }

  @Test
  public void shouldNotGetReferenceTermFeedForInvalidUrl() throws Exception {
    given().get(baseUrl +"/openmrs/ws/atomfeed/conceptreferenceterm/rec")
        .then().assertThat()
        .statusCode(SC_INTERNAL_SERVER_ERROR);
  }

  @Test
  public void shouldGetReferenceTermIfIDIsValid() throws Exception {
    PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
    authScheme.setUserName("admin");
    authScheme.setPassword("Admin123");
    RestAssured.authentication = authScheme;

    given()
        .get(baseUrl+"/openmrs/ws/rest/v1/tr/referenceterms/dcb303ba-7c9e-11e5-b875-0050568225ca")
        .then()
        .statusCode(SC_OK)
        .body(notNullValue());
  }

  @Test
  public void shouldNotGetReferenceTermIfIDIsInvalid() throws Exception {
    PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
    authScheme.setUserName("admin");
    authScheme.setPassword("Admin123");
    RestAssured.authentication = authScheme;

    Response response = given()
        .get(baseUrl + "/openmrs/ws/rest/v1/tr/referenceterms/5799c579-3c78-4133-9e02-91c1006d862d")
        .then().assertThat()
        .statusCode(SC_NOT_FOUND)
        .extract().response();

    String message = new JSONObject(response.getBody().asString()).getJSONObject("error").get("message").toString();
    assertEquals("Reference term not found",message);
  }

  @Test
  public void shouldNotGetReferenceTermIfUserNameIsInvalid() throws Exception {
    PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
    authScheme.setUserName("admn");
    authScheme.setPassword("Admin123");
    RestAssured.authentication = authScheme;

    Response response = given()
        .get(baseUrl + "/openmrs/ws/rest/v1/tr/referenceterms/5799c579-3c78-4133-9e02-91c1006d862d")
        .then().assertThat()
        .statusCode(SC_UNAUTHORIZED)
        .extract().response();

    String message = new JSONObject(response.getBody().asString()).getJSONObject("error").get("message").toString();
    assertEquals("User is not logged in",message);
  }

  @Test
  public void shouldGetConceptFeedDetails() throws Exception {
    given().get(baseUrl + "/openmrs/ws/atomfeed/concept/recent")
        .then().assertThat()
        .statusCode(SC_OK)
        .body(notNullValue());
  }

  @Test
  public void shouldNotGetConceptFeedDetailsForInvalidUrl() throws Exception {
    given().get(baseUrl +"/openmrs/ws/atomfeed/concept/rec")
        .then().assertThat()
        .statusCode(SC_INTERNAL_SERVER_ERROR);
  }

  @Test
  public void shouldGetConceptDetailsIfIDIsValid() throws Exception {
    PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
    authScheme.setUserName("admin");
    authScheme.setPassword("Admin123");
    RestAssured.authentication = authScheme;

    given()
        .get(baseUrl+"/openmrs/ws/rest/v1/tr/concepts/d6971a0c-82f3-11e5-b875-0050568225ca")
        .then()
        .statusCode(SC_OK)
        .body(notNullValue());
  }

  @Test
  public void shouldNotGetConceptDetailsIfIDIsInvalid() throws Exception {
    PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
    authScheme.setUserName("admin");
    authScheme.setPassword("Admin123");
    RestAssured.authentication = authScheme;

    Response response = given()
        .get(baseUrl + "/openmrs/ws/rest/v1/tr/concepts/5799c579-3c78-4133-9e02-91c1006d862d")
        .then().assertThat()
        .statusCode(SC_NOT_FOUND)
        .extract().response();

    String message = new JSONObject(response.getBody().asString()).getJSONObject("error").get("message").toString();
    assertEquals("Concept not found",message);
  }

  @Test
  public void shouldNotGetConceptDetailsIfUserNameIsInvalid() throws Exception {
    PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
    authScheme.setUserName("admn");
    authScheme.setPassword("Admin123");
    RestAssured.authentication = authScheme;

    Response response = given()
        .get(baseUrl + "/openmrs/ws/rest/v1/tr/concepts/5799c579-3c78-4133-9e02-91c1006d862d")
        .then().assertThat()
        .statusCode(SC_UNAUTHORIZED)
        .extract().response();

    String message = new JSONObject(response.getBody().asString()).getJSONObject("error").get("message").toString();
    assertEquals("User is not logged in",message);
  }

}
