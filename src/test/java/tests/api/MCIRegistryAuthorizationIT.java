package tests.api;

import categories.MciApiTest;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import utils.IdpUserEnum;

import java.io.IOException;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.notNullValue;
import static utils.IdentityLoginUtil.login;
import static utils.IdentityLoginUtil.loginFor;

@Category(MciApiTest.class)
public class MCIRegistryAuthorizationIT {
    private final String baseUrl = "http://172.18.46.108:8085";
    private final String patientContextPath = "/api/v2/patients";

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = baseUrl;
    }

    @Test
    public void createPatientShouldFailForInvalidFacilityAccessToken() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.FACILITY;

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        given().
                body(content).
                header("X-Auth-Token", UUID.randomUUID().toString()).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON);
    }

    @Test
    public void createPatientShouldFailForInvalidFacilityEmailId() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.FACILITY;
        String accessToken = login(idpUser);

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");

        given().
                body(content).
                header("X-Auth-Token", accessToken).
                header("From", "invalidUser@gmail.com").
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON);
    }

    @Test
    public void createPatientShouldFailForInvalidFacilityClientId() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.FACILITY;
        String accessToken = login(idpUser);
        String invalidClientId = "1000000";

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");

        given().
                body(content).
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", invalidClientId)
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON);
    }

    @Test
    public void facilityShouldCreatePatient() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.FACILITY;
        String accessToken = login(idpUser);

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        given().
                body(content).
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_CREATED)
                .contentType(ContentType.JSON);
    }

    @Test
    public void getPatientShouldFailForInvalidFacilityAccessToken() throws Exception {
        String validHid = createValidPatient();

        IdpUserEnum idpUser = IdpUserEnum.FACILITY;

        given().
                header("X-Auth-Token", UUID.randomUUID().toString()).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON)
                .body(notNullValue());
    }

    @Test
    public void getPatientShouldFailForInvalidFacilityEmailId() throws Exception {
        String validHid = createValidPatient();

        IdpUserEnum idpUser = IdpUserEnum.FACILITY;
        String accessToken = login(idpUser);

        given().
                header("X-Auth-Token", accessToken).
                header("From", "invalidUser@gmail.com").
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON)
                .body(notNullValue());
    }

    @Test
    public void getPatientShouldFailForInvalidFacilityClientId() throws Exception {
        String validHid = createValidPatient();

        IdpUserEnum idpUser = IdpUserEnum.FACILITY;
        String accessToken = login(idpUser);

        String invalidClientId = "10039499200";
        given().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", invalidClientId)
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON)
                .body(notNullValue());
    }

    @Test
    public void facilityShouldGetPatient() throws Exception {
        String validHid = createValidPatient();

        IdpUserEnum idpUser = IdpUserEnum.FACILITY;
        String accessToken = login(idpUser);

        given().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_OK)
                .contentType(ContentType.XML)
                .body(notNullValue());
    }

    @Test
    public void createPatientShouldFailForInvalidProviderAccessToken() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        given().
                body(content).
                header("X-Auth-Token", UUID.randomUUID().toString()).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON);
    }

    @Test
    public void providerShouldCreatePatient() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser);

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        given().
                body(content).
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_CREATED)
                .contentType(ContentType.JSON);
    }

    @Test
    public void getPatientShouldFailForInvalidProviderAccessToken() throws Exception {
        String validHid = createValidPatient();

        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;

        given().
                header("X-Auth-Token", UUID.randomUUID().toString()).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON)
                .body(notNullValue());
    }

    @Test
    public void providerShouldGetPatient() throws Exception {
        String validHid = createValidPatient();

        IdpUserEnum idpUser = IdpUserEnum.PROVIDER;
        String accessToken = login(idpUser);

        given().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_OK)
                .contentType(ContentType.XML)
                .body(notNullValue());
    }

    @Test
    public void createPatientShouldFailForInvalidDatasenseAccessToken() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.DATASENSE;

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        given().
                body(content).
                header("X-Auth-Token", UUID.randomUUID().toString()).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON);
    }

    @Test
    public void datasenseUserShouldNotCreatePatient() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.DATASENSE;
        String accessToken = login(idpUser);

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        given().
                body(content).
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .contentType(ContentType.JSON);
    }

    @Test
    public void getPatientShouldFailForInvalidDatasenseAccessToken() throws Exception {
        String validHid = createValidPatient();

        IdpUserEnum idpUser = IdpUserEnum.DATASENSE;

        given().
                header("X-Auth-Token", UUID.randomUUID().toString()).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON)
                .body(notNullValue());
    }

    @Test
    public void datasenseUserShouldGetPatient() throws Exception {
        String validHid = createValidPatient();

        IdpUserEnum idpUser = IdpUserEnum.DATASENSE;
        String accessToken = login(idpUser);

        given().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_OK)
                .contentType(ContentType.XML)
                .body(notNullValue());
    }

    @Test
    public void createPatientShouldFailForInvalidMCIAdminAccessToken() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.MCI_ADMIN;

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        given().
                body(content).
                header("X-Auth-Token", UUID.randomUUID().toString()).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON);
    }

    @Test
    public void mciAdminAndMciUserShouldNotCreatePatient() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.MCI_ADMIN_WITH_MCI_USER;
        String accessToken = loginFor(idpUser, IdpUserEnum.MCI_SYSTEM);

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        given().
                body(content).
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .contentType(ContentType.JSON);
    }

    @Test
    public void mciAdminUserShouldNotCreatePatient() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.MCI_ADMIN;
        String accessToken = loginFor(idpUser, IdpUserEnum.MCI_SYSTEM);

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        given().
                body(content).
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .contentType(ContentType.JSON);
    }

    @Test
    public void getPatientShouldFailForInvalidMciAdminAccessToken() throws Exception {
        String validHid = createValidPatient();

        IdpUserEnum idpUser = IdpUserEnum.MCI_ADMIN;

        given().
                header("X-Auth-Token", UUID.randomUUID().toString()).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON)
                .body(notNullValue());
    }

    @Test
    public void mciAdminAndMciUserUserShouldGetPatient() throws Exception {
        String validHid = createValidPatient();

        IdpUserEnum idpUser = IdpUserEnum.MCI_ADMIN_WITH_MCI_USER;
        String accessToken = loginFor(idpUser, IdpUserEnum.MCI_SYSTEM);

        given().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_OK)
                .contentType(ContentType.XML)
                .body(notNullValue());
    }

    @Test
    public void mciAdminUserShouldGetPatient() throws Exception {
        String validHid = createValidPatient();

        IdpUserEnum idpUser = IdpUserEnum.MCI_ADMIN;
        String accessToken = loginFor(idpUser, IdpUserEnum.MCI_SYSTEM);

        given().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_OK)
                .contentType(ContentType.XML)
                .body(notNullValue());
    }

    @Test
    public void createPatientShouldFailForInvalidMciApproverAccessToken() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.MCI_APPROVER;

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        given().
                body(content).
                header("X-Auth-Token", UUID.randomUUID().toString()).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON);
    }

    @Test
    public void mciApproverUserShouldNotCreatePatient() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.MCI_APPROVER;
        String accessToken = loginFor(idpUser, IdpUserEnum.MCI_SYSTEM);

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        given().
                body(content).
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .contentType(ContentType.JSON);
    }

    @Test
    public void getPatientShouldFailForInvalidMciApproverAccessToken() throws Exception {
        String validHid = createValidPatient();

        IdpUserEnum idpUser = IdpUserEnum.MCI_APPROVER;

        given().
                header("X-Auth-Token", UUID.randomUUID().toString()).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON)
                .body(notNullValue());
    }

    @Test
    public void mciApproverUserShouldGetPatient() throws Exception {
        String validHid = createValidPatient();

        IdpUserEnum idpUser = IdpUserEnum.MCI_APPROVER;
        String accessToken = loginFor(idpUser, IdpUserEnum.MCI_SYSTEM);

        given().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_OK)
                .contentType(ContentType.XML)
                .body(notNullValue());
    }

    @Test
    public void createPatientShouldFailForInvalidPatientAccessToken() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.PATIENT;

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        given().
                body(content).
                header("X-Auth-Token", UUID.randomUUID().toString()).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON);
    }

    @Test
    public void patientUserShouldNotCreatePatient() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.PATIENT;
        String accessToken = loginFor(idpUser, IdpUserEnum.PATIENT_JOURNAL);

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        given().
                body(content).
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .contentType(ContentType.JSON);
    }

    @Test
    public void getPatientShouldFailForInvalidPatientAccessToken() throws Exception {
        String validHid = createValidPatient();

        IdpUserEnum idpUser = IdpUserEnum.PATIENT;

        given().
                header("X-Auth-Token", UUID.randomUUID().toString()).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .contentType(ContentType.JSON)
                .body(notNullValue());
    }

    @Test
    public void patientUserShouldNotGetPatientIfHidDoesNotMatches() throws Exception {
        String validHid = createValidPatient();
        IdpUserEnum idpUser = IdpUserEnum.PATIENT;
        String accessToken = loginFor(idpUser, IdpUserEnum.PATIENT_JOURNAL);

        given().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + validHid)
                .then().assertThat()
                .statusCode(SC_FORBIDDEN)
                .contentType(ContentType.JSON)
                .body(notNullValue());
    }

    @Test
    public void patientUserShouldGetPatientIfHidMatches() throws Exception {
        IdpUserEnum idpUser = IdpUserEnum.PATIENT;
        String accessToken = loginFor(idpUser, IdpUserEnum.PATIENT_JOURNAL);

        given().
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .get(patientContextPath + "/" + idpUser.getHid())
                .then().assertThat()
                .statusCode(SC_OK)
                .contentType(ContentType.XML)
                .body(notNullValue());
    }

    private String readFile(String fileName) {
        try {
            return Resources.toString(Resources.getResource(fileName), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createValidPatient() {
        IdpUserEnum idpUser = IdpUserEnum.FACILITY;
        String accessToken = login(idpUser);

        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        return given().
                body(content).
                header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
                .post(patientContextPath)
                .then().statusCode(SC_CREATED)
                .contentType(ContentType.JSON)
                .extract()
                .response().jsonPath().getString("id");
    }
}
