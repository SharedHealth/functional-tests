package tests.api;

import categories.ApiTest;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import data.PatientFactory;
import domain.Patient;
import domain.PatientCCDSJSONFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import utils.IdpUserEnum;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_CREATED;
import static utils.IdentityLoginUtil.login;
import static utils.IdentityLoginUtil.loginFor;

@Category(ApiTest.class)
public class MCIAuditForMultipleUsersTests {
  ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
  private final String IDP_SERVER_BASE_URL = config.property.get(EnvironmentConfiguration.IDP_SERVER_BASE_URL);
  private final String baseUrl = config.property.get(EnvironmentConfiguration.MCI_SERVER_BASE_URL_KEY);
  private final String  patientContextPath = config.property.get(EnvironmentConfiguration.MCI_PATIENT_CONTEXT_PATH_KEY);

  @Before
  public void setUp() throws Exception {
    RestAssured.baseURI = baseUrl;
  }

  @Test
  public void shouldUpdatePatientSameFieldMultipleTimesByFacilityProviderAndAdminAndAccept() throws Exception {
    String hid = createPatient();

    IdpUserEnum facility = IdpUserEnum.FACILITY;
    String facilityAccessToken = login(facility, IDP_SERVER_BASE_URL);

    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String providerAccessToken = login(provider, IDP_SERVER_BASE_URL);

    IdpUserEnum mciAdmin = IdpUserEnum.MCI_ADMIN;
    String mciAdminAccessToken = loginFor(mciAdmin,facility, IDP_SERVER_BASE_URL);

    updatePatientWithSingleField(facility, facilityAccessToken, hid);
    updatePatientWithSingleField(provider, providerAccessToken, hid);
    updatePatientWithSingleField(mciAdmin, mciAdminAccessToken, hid);

    acceptRequestWithSingleField("3026",hid);
  }

  @Test
  public void shouldUpdatePatientFieldByFacilityAndAccept() throws Exception {
    String hid = createPatient();
    IdpUserEnum facility = IdpUserEnum.FACILITY;
    String facilityAccessToken = login(facility, IDP_SERVER_BASE_URL);

    updatePatientWithSingleField(facility, facilityAccessToken, hid);
    acceptRequestWithSingleField("3026",hid);
  }

  @Test
  public void shouldUpdatePatientFieldByProviderAndAccept() throws Exception {
    String hid = createPatient();
    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String providerAccessToken = login(provider, IDP_SERVER_BASE_URL);

    updatePatientWithSingleField(provider, providerAccessToken, hid);
    acceptRequestWithSingleField("3026",hid);
  }

  @Test
  public void shouldUpdatePatientFieldByMciAdminAndAccept() throws Exception {
    String hid = createPatient();
    IdpUserEnum mciAdmin = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facility = IdpUserEnum.FACILITY;
    String mciAdminAccessToken = loginFor(mciAdmin,facility, IDP_SERVER_BASE_URL);

    updatePatientWithSingleField(mciAdmin, mciAdminAccessToken, hid);
  }

  @Test
  public void shouldUpdateMultipleFieldsOfPatientByFacilityAndAccept() throws Exception {
    String hid = createPatient();
    IdpUserEnum facility = IdpUserEnum.FACILITY;
    String facilityAccessToken = login(facility, IDP_SERVER_BASE_URL);

    updatePatientWithMultipleFields(facility, facilityAccessToken, hid);
    acceptRequestWithMultipleFields("3026",hid);
  }

  @Test
  public void shouldUpdateMultipleFieldsOfPatientByProviderAndAccept() throws Exception {
    String hid = createPatient();
    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String providerAccessToken = login(provider, IDP_SERVER_BASE_URL);

    updatePatientWithMultipleFields(provider, providerAccessToken, hid);
    acceptRequestWithMultipleFields("3026",hid);
  }

  @Test
  public void shouldUpdateMultipleFieldsOfPatientByMciAdminAndAccept() throws Exception {
    String hid = createPatient();
    IdpUserEnum mciAdmin = IdpUserEnum.MCI_ADMIN;
    IdpUserEnum facility = IdpUserEnum.FACILITY;
    String mciAdminAccessToken = loginFor(mciAdmin,facility, IDP_SERVER_BASE_URL);

    updatePatientWithMultipleFields(mciAdmin, mciAdminAccessToken, hid);
  }

  @Test
  public void shouldUpdateMultipleFieldsOfPatientMultipleTimesByFacilityProviderAndAdminAndAccept() throws Exception {
    String hid = createPatient();

    IdpUserEnum facility = IdpUserEnum.FACILITY;
    String facilityAccessToken = login(facility, IDP_SERVER_BASE_URL);

    IdpUserEnum provider = IdpUserEnum.PROVIDER;
    String providerAccessToken = login(provider, IDP_SERVER_BASE_URL);

    IdpUserEnum mciAdmin = IdpUserEnum.MCI_ADMIN;
    String mciAdminAccessToken = loginFor(mciAdmin,facility, IDP_SERVER_BASE_URL);

    updatePatientWithMultipleFields(facility, facilityAccessToken, hid);
    updatePatientWithMultipleFields(provider, providerAccessToken, hid);
    updatePatientWithMultipleFields(mciAdmin, mciAdminAccessToken, hid);

    acceptRequestWithMultipleFields("3026",hid);
  }

  private void updatePatientWithSingleField(IdpUserEnum user, String accessToken, String hid) {
    String field = "{\"gender\":\"F\"}";
    updatePatient(user, accessToken, hid, field);
  }

  private void updatePatientWithMultipleFields(IdpUserEnum user, String accessToken, String hid) {
    String field ="{\"occupation\":\"04\",\"given_name\":\"updateFirstName\"}";
    updatePatient(user, accessToken, hid, field);
  }

  private void acceptRequestWithSingleField(String catchment, String hid) {
    String field = "{\"gender\":\"F\"}";
    acceptRequest(catchment, hid, field);
  }

  private void acceptRequestWithMultipleFields(String catchment, String hid) {
    String field ="{\"occupation\":\"04\",\"given_name\":\"updateFirstName\"}";
    acceptRequest(catchment, hid, field);
  }

  private void acceptRequest(String catchment, String hid, String field) {
    IdpUserEnum mciApprover = IdpUserEnum.MCI_APPROVER;
    IdpUserEnum facility = IdpUserEnum.FACILITY;
    String mciApproverAccessToken = loginFor(mciApprover, facility, IDP_SERVER_BASE_URL);

    given().
        header("X-Auth-Token", mciApproverAccessToken).
        header("From", mciApprover.getEmail()).
        header("client_id", mciApprover.getClientId()).
        header("Content-Type", "application/json").
        body(field)
        .put(baseUrl +"/api/v1/catchments/"+catchment+"/approvals/"+hid)
        .then().assertThat()
        .statusCode(SC_ACCEPTED);
  }

  private String createPatient() {
    IdpUserEnum idpUser = IdpUserEnum.TESTFACILITY;
    String accessToken = login(idpUser, IDP_SERVER_BASE_URL);
    Patient patient = PatientFactory.validPatientWithMandatoryInformation();
    patient.gender = "M";
    String  patientDetails = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);

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

  private void updatePatient(IdpUserEnum user, String accessToken,String hid, String field) {
    given().
        header("X-Auth-Token", accessToken).
        header("From", user.getEmail()).
        header("client_id", user.getClientId()).
        body(field).
        contentType(ContentType.JSON)
        .put(patientContextPath + "/" +  hid)
        .then().assertThat().statusCode(SC_ACCEPTED);
  }
}
