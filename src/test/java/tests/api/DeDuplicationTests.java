package tests.api;

import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import utils.IdpUserEnum;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static utils.IdentityLoginUtil.login;
import static utils.IdentityLoginUtil.loginFor;

public class DeDuplicationTests {
  ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
  private final String IDP_SERVER_BASE_URL = config.property.get(EnvironmentConfiguration.IDP_SERVER_BASE_URL);
  private final String mciBaseUrl = config.property.get(EnvironmentConfiguration.MCI_SERVER_BASE_URL_KEY);
  private final String  patientContextPath = config.property.get(EnvironmentConfiguration.MCI_PATIENT_CONTEXT_PATH_KEY);
  private String patientList;

  @Before
  public void setUp() throws Exception {
    RestAssured.baseURI = mciBaseUrl;
    String myCurrentDir = System.getProperty("user.dir");
    patientList = new String(Files.readAllBytes(Paths.get(myCurrentDir + "/src/test/java/data/duplicationPatients.json")));
  }

  @Test
  public void mciApproverShouldBeAbleToMarkAPatientAsInactiveAndMergeWithDuplicatePatient() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facility = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facility, IDP_SERVER_BASE_URL);

    JSONObject patientToRetain = new JSONObject(patientList).getJSONObject("patient_1_with_same_nid");
    JSONObject patientForMerge = new JSONObject(patientList).getJSONObject("patient_2_with_same_nid");

    String retainPatientHid = patientToRetain.get("hid").toString();
    String mergedPatientHid = patientForMerge.get("hid").toString();
    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(new Gson().toJson(getMergeJson(retainPatientHid, mergedPatientHid)))
        .put(patientContextPath+"/duplicates")
        .then().assertThat().statusCode(SC_ACCEPTED);

    Response response = given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(new Gson().toJson(getMergeJson(retainPatientHid, mergedPatientHid)))
        .put(patientContextPath+"/duplicates")
        .then()
        .statusCode(SC_BAD_REQUEST)
        .extract().response();

    String body = response.getBody().asString();
        body.contains("invalid.payload. Duplicates don't exist for health IDs");
        body.contains("in db. Cannot merge.");
        body.contains(patientToRetain.get("hid").toString());
        body.contains(patientForMerge.get("hid").toString());

    JsonPath retainPatientJson = new JsonPath( getPatientDetailsByHid(idpUser, accessToken,patientToRetain));
    JsonPath mergePatientJson = new JsonPath( getPatientDetailsByHid(idpUser, accessToken,patientForMerge));

    assertEquals(patientToRetain.get("hid").toString(),retainPatientJson.get("hid"));
    assertEquals(null, retainPatientJson.get("provider"));

    assertEquals(false, mergePatientJson.get("active"));
    assertEquals(patientToRetain.get("hid").toString(),mergePatientJson.get("merged_with"));

    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String facilityAccessToken = login(facilityUser, IDP_SERVER_BASE_URL);

    given().
        header("X-Auth-Token", facilityAccessToken).
        header("From", facilityUser.getEmail()).
        header("client_id", facilityUser.getClientId()).
        body("{\"sur_name\":\"mohammad\"}").
        contentType(ContentType.JSON)
        .put(patientContextPath + "/" +  mergedPatientHid)
        .then().assertThat().statusCode(SC_BAD_REQUEST)
        .body("message", equalTo("Cannot update inactive patient, already merged with "+retainPatientHid));
  }

  @Test
  public void shouldFailIfMergedWithSamePatient() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facility = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facility, IDP_SERVER_BASE_URL);
    JSONObject patient = new JSONObject(patientList).getJSONObject("patient_2_with_same_nid");
    String hid = patient.get("hid").toString();

    Response response = given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(new Gson().toJson(getMergeJson(hid, hid)))
        .put(patientContextPath+"/duplicates")
        .then()
        .statusCode(SC_BAD_REQUEST)
        .extract().response();

    String body = response.getBody().asString();
    body.contains("invalid.payload. Duplicates don't exist for health IDs");
    body.contains("in db. Cannot merge.");
    body.contains(hid+" & "+ hid);
  }

  @Test
  public void shouldFailOnInvalidHealthIdForMerginPatient() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facility = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facility, IDP_SERVER_BASE_URL);

    JSONObject patientToRetain = new JSONObject(patientList).getJSONObject("patient_1_with_same_nid");
    JSONObject patientForMerge = new JSONObject(patientList).getJSONObject("patient_2_with_same_nid");

    String retainPatientHid = patientToRetain.get("hid").toString() + "1";
    String mergedPatientHid = patientForMerge.get("hid").toString();

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(new Gson().toJson(getMergeJson(retainPatientHid, mergedPatientHid)))
        .put(patientContextPath+"/duplicates")
        .then().assertThat().statusCode(SC_NOT_FOUND)
        .body("message", equalTo("No patient found with health id: "+retainPatientHid));
  }

  @Test
  public void shouldRetainBothPatientsAsNotDuplicatesOnMatchOfNID() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facility = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facility, IDP_SERVER_BASE_URL);

    JSONObject patient_1 = new JSONObject(patientList).getJSONObject("patient_1_with_same_nid");
    JSONObject patient_2 = new JSONObject(patientList).getJSONObject("patient_2_with_same_nid");

    String retainPatientHid = patient_1.get("hid").toString();
    String mergedPatientHid = patient_2.get("hid").toString();

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(new Gson().toJson(getRetainJson(retainPatientHid, mergedPatientHid)))
        .put(patientContextPath+"/duplicates")
        .then().assertThat()
        .statusCode(SC_ACCEPTED);
  }

  @Test
  public void shouldRetainBothPatientsAsNotDuplicatesOnMatchOfUID() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facility = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facility, IDP_SERVER_BASE_URL);

    JSONObject patient_1 = new JSONObject(patientList).getJSONObject("patient_1_with_same_uid");
    JSONObject patient_2 = new JSONObject(patientList).getJSONObject("patient_2_with_same_uid");

    String retainPatientHid = patient_1.get("hid").toString();
    String mergedPatientHid = patient_2.get("hid").toString();

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(new Gson().toJson(getRetainJson(retainPatientHid, mergedPatientHid)))
        .put(patientContextPath+"/duplicates")
        .then().assertThat()
        .statusCode(SC_ACCEPTED);
  }

  @Test
  public void shouldRetainBothPatientsAsNotDuplicatesOnMatchOfBinbrn() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facility = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facility, IDP_SERVER_BASE_URL);

    JSONObject patient_1 = new JSONObject(patientList).getJSONObject("patient_1_with_same_binbrn");
    JSONObject patient_2 = new JSONObject(patientList).getJSONObject("patient_2_with_same_binbrn");

    String retainPatientHid = patient_1.get("hid").toString();
    String mergedPatientHid = patient_2.get("hid").toString();

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(new Gson().toJson(getRetainJson(retainPatientHid, mergedPatientHid)))
        .put(patientContextPath+"/duplicates")
        .then().assertThat()
        .statusCode(SC_ACCEPTED);
  }

  @Test
  public void shouldRetainBothPatientsAsNotDuplicatesOnMatchOfNameAndAddress() throws Exception {
    IdpUserEnum idpUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facility = IdpUserEnum.FACILITY;
    String accessToken = loginFor(idpUser, facility, IDP_SERVER_BASE_URL);

    JSONObject patient_1 = new JSONObject(patientList).getJSONObject("patient_1_with_matching_name_and_address");
    JSONObject patient_2 = new JSONObject(patientList).getJSONObject("patient_2_with_matching_name_and_address");

    String retainPatientHid = patient_1.get("hid").toString();
    String mergedPatientHid = patient_2.get("hid").toString();

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", idpUser.getEmail())
        .header("client_id", idpUser.getClientId())
        .header("Content-Type", "application/json")
        .body(new Gson().toJson(getRetainJson(retainPatientHid, mergedPatientHid)))
        .put(patientContextPath+"/duplicates")
        .then().assertThat()
        .statusCode(SC_ACCEPTED);
  }

  private String getPatientDetailsByHid(IdpUserEnum idpUser, String accessToken, JSONObject patient) throws JSONException {
    return with().
        header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId()).
        get(patientContextPath+ "/" + patient.get("hid"))
        .then()
        .statusCode(SC_OK)
        .extract()
        .response().asString();
  }

  private HashMap<String, Object> getMergeJson(String retainPatientHid, String mergedPatientHid) throws JSONException {
    HashMap<String, Object> payload = new HashMap<>();
    payload.put("action", "MERGE");

    HashMap<String, Object> patient1Map = new HashMap<>();
    patient1Map.put("hid", mergedPatientHid);
    patient1Map.put("active", false);
    patient1Map.put("merged_with", retainPatientHid);
    payload.put("patient1", patient1Map);


    HashMap<String, Object> patient2Map = new HashMap<>();
    patient2Map.put("hid", retainPatientHid);
    patient2Map.put("active", true);
    payload.put("patient2", patient2Map);
    return payload;
  }

  private HashMap<String, Object> getRetainJson(String retainPatientHid, String mergedPatientHid) {
    HashMap<String, Object> payload = new HashMap<>();
    payload.put("action", "RETAIN_ALL");

    HashMap<String, Object> patient1Map = new HashMap<>();
    patient1Map.put("hid", mergedPatientHid);
    payload.put("patient1", patient1Map);

    HashMap<String, Object> patient2Map = new HashMap<>();
    patient2Map.put("hid", retainPatientHid);
    payload.put("patient2", patient2Map);

    return payload;
  }
}
