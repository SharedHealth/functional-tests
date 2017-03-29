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
import nu.xom.ParsingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import utils.IdpUserEnum;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utils.IdentityLoginUtil.login;
import static utils.IdentityLoginUtil.loginFor;

@Category(ApiTest.class)
public class MCIAdminUserTests {

    ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
    private final String IDP_SERVER_BASE_URL = config.property.get("idp_server_base_url");
    private final String baseUrl = config.property.get("mci_registry");
    private final String patientContextPath = "/api/v1/patients";

    @Before
    public void setUp() throws Exception {
      RestAssured.baseURI = baseUrl;
    }

  @Test
  public void mciAdminUserShouldNotBeAbleToCreatePatient() throws Exception {
    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciAdminUser,facilityUser, IDP_SERVER_BASE_URL);

    Patient patient = PatientFactory.validPatientWithMandatoryInformation();
    String patientDetails = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);

    given().
        body(patientDetails).
        header("X-Auth-Token", accessToken).
        header("From", mciAdminUser.getEmail()).
        header("client_id", mciAdminUser.getClientId()).
        header("Content-Type", "application/json")
        .post(patientContextPath)
        .then()
        .assertThat()
        .statusCode(SC_FORBIDDEN)
        .contentType(ContentType.JSON);
  }

  @Test
  public void mciAdminUserShouldBeAbleToViewPatientByHid() throws Exception {
    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciAdminUser,facilityUser, IDP_SERVER_BASE_URL);
    String validHid = createValidPatient();

    given().
        header("X-Auth-Token", accessToken).
        header("From", mciAdminUser.getEmail()).
        header("client_id", mciAdminUser.getClientId()).
        get(patientContextPath+"/"+validHid)
        .then().assertThat()
        .statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .body(notNullValue());
  }

  @Test
  public void mciAdminUserShouldBeAbleToViewPatientByNid() throws Exception {
    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciAdminUser,facilityUser, IDP_SERVER_BASE_URL);
    String validHid = createValidPatient();
    JsonPath patientDetails = getPatientDetailsByHID(mciAdminUser, accessToken, validHid);
    String nid = patientDetails.get("nid");

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", mciAdminUser.getEmail())
        .header("client_id", mciAdminUser.getClientId())
        .get(patientContextPath+"/?nid="+nid)
        .then().assertThat()
        .statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .body(notNullValue());
  }

  @Test
  public void mciAdminUserShouldBeAbleToViewPatientByBinBrn() throws Exception {
    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciAdminUser,facilityUser, IDP_SERVER_BASE_URL);

    String validHid = createValidPatient();
    JsonPath patientDetails = getPatientDetailsByHID(mciAdminUser, accessToken, validHid);
    String bin_brn = patientDetails.get("bin_brn");

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", mciAdminUser.getEmail())
        .header("client_id", mciAdminUser.getClientId())
        .get(patientContextPath+"/?bin_brn="+bin_brn)
        .then().assertThat()
        .statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .body(notNullValue());
  }

  @Test
  public void mciAdminUserShouldBeAbleToViewPatientByHouseHoldCode() throws Exception {
    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciAdminUser,facilityUser, IDP_SERVER_BASE_URL);

    String validHid = createValidPatient();
    JsonPath patientDetails = getPatientDetailsByHID(mciAdminUser, accessToken, validHid);
    String household_code = patientDetails.get("household_code");

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", mciAdminUser.getEmail())
        .header("client_id", mciAdminUser.getClientId())
        .get(patientContextPath+"/?household_code="+household_code)
        .then().assertThat()
        .statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .body(notNullValue());
  }

  @Test
  public void mciAdminUserShouldBeAbleToViewPatientByNameAndLocation() throws Exception {
    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciAdminUser,facilityUser, IDP_SERVER_BASE_URL);

    String validHid = createValidPatient();
    JsonPath patientDetails = getPatientDetailsByHID(mciAdminUser, accessToken, validHid);

    String givenName = patientDetails.get("given_name");
    String surName = patientDetails.get("sur_name");
    String district_id = patientDetails.get("present_address.district_id");
    String division_id = patientDetails.get("present_address.division_id");
    String upazila_id = patientDetails.get("present_address.upazila_id");
    String address = "" + division_id + district_id + upazila_id;

    given().
        header("X-Auth-Token", accessToken).
        header("From", mciAdminUser.getEmail()).
        header("client_id", mciAdminUser.getClientId()).
        get(patientContextPath + "/?given_name=" + givenName + "&sur_name=" + surName + "&present_address=" + address).
        then().assertThat()
        .statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .body(notNullValue());
  }

  @Test
  public void mciAdminUserShouldNotBeAbleToDownloadPatientsByCatchment() throws Exception {
    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciAdminUser,facilityUser, IDP_SERVER_BASE_URL);

    given().
        header("X-Auth-Token", accessToken).
        header("From", mciAdminUser.getEmail()).
        header("client_id", mciAdminUser.getClientId()).
        get("/api/v1/catchments/302607/patients")
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void mciAdminUserShouldBeAbleToUpdateThePatient() throws Exception {
    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciAdminUser,facilityUser, IDP_SERVER_BASE_URL);
    String validHid = createValidPatient();

    given().
        header("X-Auth-Token", accessToken).
        header("From", mciAdminUser.getEmail()).
        header("client_id", mciAdminUser.getClientId()).
        body("{\"gender\":\"F\"}").
        contentType(ContentType.JSON)
        .put(patientContextPath +"/"+ validHid)
        .then().assertThat()
        .statusCode(SC_ACCEPTED);
  }

  @Test
  public void mciAdminUserShouldNotBeAbleToViewPendingApprovalPatientByCatchment() throws Exception {
    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciAdminUser,facilityUser, IDP_SERVER_BASE_URL);

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", mciAdminUser.getEmail())
        .header("client_id", mciAdminUser.getClientId())
        .get("/api/v1/catchments/3026/approvals/")
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void mciAdminUserShouldNotBeAbleToViewPendingApprovalPatientByHID() throws Exception {
    String validHid = createValidPatient();
    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciAdminUser,facilityUser, IDP_SERVER_BASE_URL);

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", mciAdminUser.getEmail())
        .header("client_id", mciAdminUser.getClientId())
        .get("/api/v1/catchments/3026/approvals/"+validHid)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void mciAdminUserShouldNotBeAbleToAcceptPendingApprovalForPatient() throws Exception {
    String validHid = createValidPatient();

    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String facilityAccessToken = login(facilityUser, IDP_SERVER_BASE_URL);
    given().
        header("X-Auth-Token", facilityAccessToken).
        header("From", facilityUser.getEmail()).
        header("client_id", facilityUser.getClientId()).
        body("{\"sur_name\":\"mohammad\"}").
        contentType(ContentType.JSON)
        .put(patientContextPath + "/" +  validHid)
        .then().assertThat().statusCode(SC_ACCEPTED);

    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    String accessToken = loginFor(mciAdminUser,facilityUser, IDP_SERVER_BASE_URL);
    given().
        header("X-Auth-Token", accessToken).
        header("From", mciAdminUser.getEmail()).
        header("client_id", mciAdminUser.getClientId()).
        header("Content-Type", "application/json").
        body("{\"sur_name\":\"mohammad\"}")
        .put(baseUrl+"/api/v1/catchments/3026/approvals/"+validHid)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void mciAdminUserShouldNotBeAbleToRejectPendingApprovalForPatient() throws Exception {
    String validHid = createValidPatient();

    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String facilityAccessToken = login(facilityUser, IDP_SERVER_BASE_URL);
    given().
        header("X-Auth-Token", facilityAccessToken).
        header("From", facilityUser.getEmail()).
        header("client_id", facilityUser.getClientId()).
        body("{\"sur_name\":\"mohammad\"}").
        contentType(ContentType.JSON)
        .put(patientContextPath + "/" +  validHid)
        .then().assertThat().statusCode(SC_ACCEPTED);

    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    String accessToken = loginFor(mciAdminUser,facilityUser, IDP_SERVER_BASE_URL);
    given().
        header("X-Auth-Token", accessToken).
        header("From", mciAdminUser.getEmail()).
        header("client_id", mciAdminUser.getClientId()).
        header("Content-Type", "application/json").
        body("{\"sur_name\":\"mohammad\"}")
        .delete(baseUrl+"/api/v1/catchments/3026/approvals/"+validHid)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void mciAdminUserShouldBeAbleToGetAuditLogByHID() throws Exception {
    String validHid = createValidPatient();
    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciAdminUser, facilityUser, IDP_SERVER_BASE_URL);

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", mciAdminUser.getEmail())
        .header("client_id", mciAdminUser.getClientId())
        .get("/api/v1/audit/patients/"+validHid)
        .then().assertThat()
        .statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .body(notNullValue());
  }

  @Test
  public void mciAdminUserShouldNotBeAbleToGetShrFeedByHID() throws Exception {
    String validHid = createValidPatient();
    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciAdminUser, facilityUser, IDP_SERVER_BASE_URL);

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", mciAdminUser.getEmail())
        .header("client_id", mciAdminUser.getClientId())
        .get("/api/v1/feed/patients?hid="+validHid)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void mciAdminUserShouldAbleToGetLocationDetails() throws Exception {
    IdpUserEnum mciAdminUser = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciAdminUser, facilityUser, IDP_SERVER_BASE_URL);

    given().
        header("X-Auth-Token", accessToken).
        header("From", mciAdminUser.getEmail()).
        header("client_id", mciAdminUser.getClientId())
        .get(baseUrl + "/api/v1/locations?parent=3026")
        .then()
        .assertThat()
        .statusCode(SC_OK);
  }

  private String createValidPatient() throws ParsingException, IOException {
    IdpUserEnum idpUser = IdpUserEnum.FACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
    Patient patient = PatientFactory.validPatientWithMandatoryInformation();
    patient.gender = "M";
    patient.binBRN = "15893974754477445";
    patient.householdCode = patient.nid;
    String  patientDetails = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);

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

  private JsonPath getPatientDetailsByHID(IdpUserEnum idpUser, String accessToken, String validHid) {
    String response = with().
        header("X-Auth-Token", accessToken).
        header("From", idpUser.getEmail()).
        header("client_id", idpUser.getClientId()).
        get(patientContextPath + "/" + validHid).asString();

    return new JsonPath(response);
  }

}
