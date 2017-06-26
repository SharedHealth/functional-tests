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
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import utils.BundleFactory;
import utils.IdpUserEnum;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static utils.IdentityLoginUtil.login;

@Category(ApiTest.class)
public class ProviderUserTests {

    ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
    private final String IDP_SERVER_BASE_URL = config.property.get(EnvironmentConfiguration.IDP_SERVER_BASE_URL);
    private final String shrBaseUrl = config.property.get(EnvironmentConfiguration.SHR_SERVER_BASE_URL_KEY);
    private final String mciBaseUrl = config.property.get(EnvironmentConfiguration.MCI_SERVER_BASE_URL_KEY);
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
                get(shrBaseUrl + "/catchments/" + catchments + "/encounters?updateSince=2017-03-01T00%3A00%3A00.000%2B0530")
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
                get(shrBaseUrl + "/catchments/" + catchments + "/encounters?updateSince=2017-03-01T00%3A00%3A00.000%2B0530")
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
                get(shrBaseUrl + "/catchments/" + catchments + "/encounters?updateSince=2017-03-01T00%3A00%3A00.000%2B0530")
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
        assertTrue(entries.length() > 0);
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
        assertEquals(0, entries.length());

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
                .body("message", equalTo("Access is denied to user " + provider.getClientId() + " for patient " + hid));

    }

    @Test
    public void providerUserShouldBeAbleToViewPatientByHid() throws Exception {
        IdpUserEnum idpUserEnum = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);
        String hid = createValidPatient();

        given().header("X-Auth-Token", accessToken).
                header("From", idpUserEnum.getEmail()).
                header("client_id", idpUserEnum.getClientId())
                .get(patientContextPath + "/" + hid)
                .then().assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body(notNullValue());
    }

    @Test
    public void providerUserShouldBeAbleToCreatePatient() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
        Patient patient = PatientFactory.validPatientWithMandatoryInformation();
        patient.gender = "M";
        String patientDetails = new PatientCCDSJSONFactory(mciBaseUrl).withValidJSON(patient);

        given().
                body(patientDetails)
                .header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .header("Content-Type", "application/json")
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_CREATED);
    }

    @Test
    public void providerUserShouldBeAbleToViewPatientByNid() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
        String hid = createValidPatient();
        JsonPath patientDetails = getPatientDetailsByHID(idpUser, accessToken, hid);
        String nid = patientDetails.get("nid");

        given()
                .header("X-Auth-Token", accessToken)
                .header("From", idpUser.getEmail())
                .header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/?nid=" + nid)
                .then().assertThat()
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body(notNullValue());
    }

    @Test
    public void providerUserShouldBeAbleToViewPatientByBinBrn() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
        Patient patient = PatientFactory.validPatientWithMandatoryInformation();
        patient.binBRN = "14893974754477445";
        String hid = createPatient(patient);
        JsonPath patientDetails = getPatientDetailsByHID(idpUser, accessToken, hid);
        String binBrn = patientDetails.get("bin_brn");

        given()
                .header("X-Auth-Token", accessToken)
                .header("From", idpUser.getEmail())
                .header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/?bin_brn=" + binBrn)
                .then().assertThat()
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body(notNullValue());
    }

    @Test
    public void providerUserShouldBeAbleToViewPatientsByHouseHoldCode() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
        Patient patient = PatientFactory.validPatientWithMandatoryInformation();
        patient.householdCode = patient.nid;
        String hid = createPatient(patient);
        JsonPath patientDetails = getPatientDetailsByHID(idpUser, accessToken, hid);
        String householdCode = patientDetails.get("household_code");

        given()
                .header("X-Auth-Token", accessToken)
                .header("From", idpUser.getEmail())
                .header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/?household_code=" + householdCode)
                .then().assertThat()
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body(notNullValue());
    }

    @Test
    public void providerUserShouldBeAbleToViewPatientByNameAndLocation() throws Exception {
        String hid = createValidPatient();
        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
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
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body(notNullValue());
    }

    @Test
    public void providerUserShouldBeAbleToDownloadPatientsByCatchment() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

        String hid = createValidPatient();
        String bundle = BundleFactory.BundleWithConditionEncounterForFever(hid);

        createEncounterForPatient(idpUser, accessToken, hid, bundle);

        String response = given().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId()).
                get("/api/v1/catchments/302607/patients")
                .then().statusCode(SC_OK)
                .extract().response().asString();

        JSONObject jsonObject = new JSONObject(response);
        JSONArray entries = new JSONArray(jsonObject.get("entries").toString());
        assertTrue(entries.length() > 0);
    }

    @Test
    public void providerUserShouldBeAbleToUpdateThePatient() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
        String hid = createValidPatient();

        given().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId()).
                body("{\"gender\":\"F\"}").
                contentType(ContentType.JSON)
                .put(patientContextPath + "/" + hid)
                .then().assertThat()
                .statusCode(SC_ACCEPTED);
    }

    @Test
    public void providerUserShouldNotBeAbleToViewPendingApprovalPatientByHID() throws Exception {
        String hid = createValidPatient();
        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

        given()
                .header("X-Auth-Token", accessToken)
                .header("From", idpUser.getEmail())
                .header("client_id", idpUser.getClientId())
                .get("/api/v1/catchments/3026/approvals/" + hid)
                .then().assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Access is denied"));
    }

    @Test
    public void providerUserShouldNotBeAbleToAcceptPendingApprovalForPatient() throws Exception {
        String hid = createValidPatient();
        updatePatient(hid);

        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
        given().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId()).
                header("Content-Type", "application/json").
                body("{\"sur_name\":\"mohammad\"}")
                .put(mciBaseUrl + "/api/v1/catchments/3026/approvals/" + hid)
                .then().assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Access is denied"));
    }

    @Test
    public void providerUserShouldNotBeAbleToRejectPendingApprovalForPatient() throws Exception {
        String hid = createValidPatient();
        updatePatient(hid);

        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
        given().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId()).
                header("Content-Type", "application/json").
                body("{\"sur_name\":\"mohammad\"}")
                .delete(mciBaseUrl + "/api/v1/catchments/3026/approvals/" + hid)
                .then().assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Access is denied"));
    }

    @Test
    public void providerShouldNotBeAbleToGetAuditLogByHID() throws Exception {
        String hid = createValidPatient();
        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

        given()
                .header("X-Auth-Token", accessToken)
                .header("From", idpUser.getEmail())
                .header("client_id", idpUser.getClientId())
                .get("/api/v1/audit/patients/" + hid)
                .then().assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Access is denied"));
    }

    @Test
    public void providerUserShouldNotBeAbleToGetShrFeedByHID() throws Exception {
        String hid = createValidPatient();
        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

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
    public void providerUserShouldNotBeAbleToGetLocationDetails() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

        given().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(mciBaseUrl + "/api/v1/locations?parent=3026")
                .then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Access is denied"));
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
        String patientDetails = new PatientCCDSJSONFactory(mciBaseUrl).withValidJSON(patient);

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

    private JsonPath getPatientDetailsByHID(IdpUserEnum idpUser, String accessToken, String hid) {
        String response = with().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId()).
                get(patientContextPath + "/" + hid)
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
                .put(patientContextPath + "/" + hid)
                .then().assertThat().statusCode(SC_ACCEPTED);
    }


}
