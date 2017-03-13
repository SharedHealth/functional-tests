package tests.api;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import data.PatientFactory;
import domain.Patient;
import domain.PatientFHIRXMLFactory;
import nu.xom.ParsingException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import utils.IdentityLoginUtil;
import utils.IdpUserEnum;

import java.io.IOException;
import java.util.regex.Matcher;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static utils.IdentityLoginUtil.*;

public class MCIApproverTests {
  ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
  private final String IDP_SERVER_BASE_URL = config.property.get("idp_server_base_url");
  private final String baseUrl = config.property.get("mci_registry");
  private final String patientContextPath = "/api/v1/patients";

  @Before
  public void setUp() throws Exception {
    RestAssured.baseURI = baseUrl;
  }

  @Test
  public void mciApproverUserShouldNotBeAbleToCreatePatient() throws Exception {
    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciApproverUser,facilityUser, IDP_SERVER_BASE_URL);

    Patient patient = PatientFactory.validPatientWithMandatoryInformation();
    String patientDetails = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);

    given().
        body(patientDetails).
        header("X-Auth-Token", accessToken).
        header("From", mciApproverUser.getEmail()).
        header("client_id", mciApproverUser.getClientId()).
        header("Content-Type", "application/json")
        .post(patientContextPath)
        .then()
        .assertThat()
        .statusCode(SC_FORBIDDEN)
        .contentType(ContentType.JSON);
  }

  @Test
  public void mciApproverUserShouldBeAbleToViewPatientByHid() throws Exception {
    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciApproverUser,facilityUser, IDP_SERVER_BASE_URL);
    String validHid = createValidPatient();

    given().
        header("X-Auth-Token", accessToken).
        header("From", mciApproverUser.getEmail()).
        header("client_id", mciApproverUser.getClientId()).
        get(patientContextPath+"/"+validHid)
        .then().assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(notNullValue());
  }

  @Test
  public void mciApproverUserShouldNotBeAbleToViewPatientByNid() throws Exception {
    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciApproverUser,facilityUser, IDP_SERVER_BASE_URL);
    String validHid = createValidPatient();
    JsonPath patientDetails = getPatientDetailsByHID(mciApproverUser, accessToken, validHid);
    String nid = patientDetails.get("nid");

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", mciApproverUser.getEmail())
        .header("client_id", mciApproverUser.getClientId())
        .get(patientContextPath+"/?nid="+nid)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message",equalTo("Access is denied"));
  }

  @Test
  public void mciApproverUserShouldNotBeAbleToViewPatientByBinBrn() throws Exception {
    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciApproverUser,facilityUser, IDP_SERVER_BASE_URL);

    String validHid = createValidPatient();
    JsonPath patientDetails = getPatientDetailsByHID(mciApproverUser, accessToken, validHid);
    String bin_brn = patientDetails.get("bin_brn");

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", mciApproverUser.getEmail())
        .header("client_id", mciApproverUser.getClientId())
        .get(patientContextPath+"/?bin_brn="+bin_brn)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message",equalTo("Access is denied"));
  }

  @Test
  public void mciApproverUserShouldNotBeAbleToViewPatientByHouseHoldCode() throws Exception {
    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciApproverUser,facilityUser, IDP_SERVER_BASE_URL);

    String validHid = createValidPatient();
    JsonPath patientDetails = getPatientDetailsByHID(mciApproverUser, accessToken, validHid);
    String household_code = patientDetails.get("household_code");

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", mciApproverUser.getEmail())
        .header("client_id", mciApproverUser.getClientId())
        .get(patientContextPath+"/?household_code="+household_code)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message",equalTo("Access is denied"));

  }

  @Test
  public void mciApproverUserShouldNotBeAbleToViewPatientByNameAndLocation() throws Exception {
    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciApproverUser,facilityUser, IDP_SERVER_BASE_URL);

    String validHid = createValidPatient();
    JsonPath patientDetails = getPatientDetailsByHID(mciApproverUser, accessToken, validHid);

    String givenName = patientDetails.get("given_name");
    String surName = patientDetails.get("sur_name");
    String district_id = patientDetails.get("present_address.district_id");
    String division_id = patientDetails.get("present_address.division_id");
    String upazila_id = patientDetails.get("present_address.upazila_id");
    String address = "" + division_id + district_id + upazila_id;

    given().
        header("X-Auth-Token", accessToken).
        header("From", mciApproverUser.getEmail()).
        header("client_id", mciApproverUser.getClientId()).
        get(patientContextPath + "/?given_name=" + givenName + "&sur_name=" + surName + "&present_address=" + address).
        then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void mciApproverUserShouldNotBeAbleToDownloadPatientsByCatchment() throws Exception {
    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciApproverUser,facilityUser, IDP_SERVER_BASE_URL);

    given().
        header("X-Auth-Token", accessToken).
        header("From", mciApproverUser.getEmail()).
        header("client_id", mciApproverUser.getClientId()).
        get("/api/v1/catchments/3026/patients")
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void mciApproverUserShouldNotBeAbleToUpdateThePatient() throws Exception {
    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciApproverUser,facilityUser, IDP_SERVER_BASE_URL);
    String validHid = createValidPatient();

    given().
        header("X-Auth-Token", accessToken).
        header("From", mciApproverUser.getEmail()).
        header("client_id", mciApproverUser.getClientId()).
        body("{\"sur_name\":\"mohammad\"}").
        contentType(ContentType.JSON)
        .put(patientContextPath + validHid)
        .then()
        .assertThat()
        .statusCode(SC_FORBIDDEN);
  }

  @Test
  public void mciApproverUserShouldBeAbleToViewPendingApprovalPatientByCatchment() throws Exception {
    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciApproverUser,facilityUser, IDP_SERVER_BASE_URL);

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", mciApproverUser.getEmail())
        .header("client_id", mciApproverUser.getClientId())
        .get("/api/v1/catchments/3026/approvals/")
        .then().assertThat()
        .statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .body(notNullValue());
  }

  @Test
  public void mciApproverUserShouldBeAbleToViewPendingApprovalPatientByHID() throws Exception {
    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciApproverUser, facilityUser, IDP_SERVER_BASE_URL);
    String validHid = createValidPatient();

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", mciApproverUser.getEmail())
        .header("client_id", mciApproverUser.getClientId())
        .get("/api/v1/catchments/3026/approvals/" + validHid)
        .then().assertThat()
        .statusCode(SC_OK);
  }

  @Test
  public void mciApproverUserShouldBeAbleToAcceptPendingApprovalForPatient() throws Exception {
    String validHid = createValidPatient();

    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String facilityAccessToken = login(facilityUser, IDP_SERVER_BASE_URL);

    given().
        header("X-Auth-Token", facilityAccessToken).
        header("From", facilityUser.getEmail()).
        header("client_id", facilityUser.getClientId()).
        body("{\"given_name\":\"mohammad\"}").
        contentType(ContentType.JSON)
        .put(patientContextPath + "/" +  validHid)
        .then().assertThat().statusCode(SC_ACCEPTED);

    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    String accessToken = loginFor(mciApproverUser, facilityUser, IDP_SERVER_BASE_URL);
    given().
        header("X-Auth-Token", accessToken).
        header("From", mciApproverUser.getEmail()).
        header("client_id", mciApproverUser.getClientId()).
        header("Content-Type", "application/json").
        body("{\"given_name\":\"mohammad\"}")
        .put(baseUrl+"/api/v1/catchments/3026/approvals/"+validHid)
        .then().assertThat()
        .statusCode(SC_ACCEPTED);
  }

  @Test
  public void mciApproverUserShouldBeAbleToRejectPendingApprovalForPatient() throws Exception {
    String validHid = createValidPatient();

    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String facilityAccessToken = login(facilityUser, IDP_SERVER_BASE_URL);
    given().
        header("X-Auth-Token", facilityAccessToken).
        header("From", facilityUser.getEmail()).
        header("client_id", facilityUser.getClientId()).
        body("{\"given_name\":\"mohammad\"}").
        contentType(ContentType.JSON)
        .put(patientContextPath + "/" +  validHid)
        .then().assertThat().statusCode(SC_ACCEPTED);


    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    String accessToken = loginFor(mciApproverUser, facilityUser, IDP_SERVER_BASE_URL);
    given().
        header("X-Auth-Token", accessToken).
        header("From", mciApproverUser.getEmail()).
        header("client_id", mciApproverUser.getClientId()).
        header("Content-Type", "application/json").
        body("{\"given_name\":\"mohammad\"}")
        .delete(baseUrl+"/api/v1/catchments/3026/approvals/"+validHid)
        .then().assertThat()
        .statusCode(SC_ACCEPTED);
  }

  @Test
  public void mciApproverUserShouldNotAbleToGetAuditLogByHID() throws Exception {
    String validHid = createValidPatient();
    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciApproverUser, facilityUser, IDP_SERVER_BASE_URL);

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", mciApproverUser.getEmail())
        .header("client_id", mciApproverUser.getClientId())
        .get("/api/v1/audit/patients/"+validHid)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void mciApproverUserShouldBeAbleToGetShrFeedByHID() throws Exception {
    String validHid = createValidPatient();
    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciApproverUser, facilityUser, IDP_SERVER_BASE_URL);

    given()
        .header("X-Auth-Token", accessToken)
        .header("From", mciApproverUser.getEmail())
        .header("client_id", mciApproverUser.getClientId())
        .get("/api/v1/feed/patients?hid="+validHid)
        .then().assertThat()
        .statusCode(SC_FORBIDDEN)
        .body("message", equalTo("Access is denied"));
  }

  @Test
  public void mciApproverUserShouldAbleToGetLocationDetails() throws Exception {
    IdpUserEnum mciApproverUser = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facilityUser = IdpUserEnum.FACILITY;
    String accessToken = loginFor(mciApproverUser, facilityUser, IDP_SERVER_BASE_URL);

    given().
        header("X-Auth-Token", accessToken).
        header("From", mciApproverUser.getEmail()).
        header("client_id", mciApproverUser.getClientId())
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
    patient.binBRN = "16893974754477445";
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
