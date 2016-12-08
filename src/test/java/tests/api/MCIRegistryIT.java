package tests.api;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.composite.*;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierTypeCodesEnum;
import ca.uhn.fhir.parser.IParser;
import categories.MciApiTest;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import data.PatientFactory;
import domain.PatientFHIRXMLFactory;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import utils.FhirContextHelper;
import utils.IdpUserEnum;

import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.model.dstu2.valueset.LinkTypeEnum.SEE_ALSO;
import static com.jayway.restassured.RestAssured.given;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static utils.IdentityLoginUtil.login;

@Category(MciApiTest.class)
public class MCIRegistryIT {
    private final IParser xmlParser = FhirContextHelper.getFhirContext().newXmlParser();
    ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();

    private final String baseUrl = config.property.get("mci_registry");
    private final String IDP_SERVER_BASE_URL = config.property.get("idp_server_base_url");
    private final String patientContextPath = "/api/v2/patients";


    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = baseUrl;
    }

    @Test
    public void createAndGetPatient() throws Exception {
        IdpUserEnum idpUserEnum = IdpUserEnum.FACILITY;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);

        String content = new PatientFHIRXMLFactory().withValidXML(PatientFactory.validPatientWithMandatoryInformation());

        Patient expectedPatient = findPatientFromBundle((Bundle) xmlParser.parseResource(content));

        Response createResponse = given()
                .body(content)
                .header("X-Auth-Token", accessToken)
                .header("From", idpUserEnum.getEmail())
                .header("client_id", idpUserEnum.getClientId())
                .post(patientContextPath)
                .then()
                .assertThat()
                .statusCode(SC_CREATED)
                .contentType(ContentType.JSON)
                .body("httpStatus", equalTo(SC_CREATED))
                .body("id", notNullValue())
                .extract().response();

        String healthId = new JsonPath(createResponse.asString()).getString("id");

        expectedPatient.addIdentifier(mapHealthIdIdentifier(healthId));
        Response response = given()
                .header("X-Auth-Token", accessToken)
                .header("From", idpUserEnum.getEmail())
                .header("client_id", idpUserEnum.getClientId())
                .get(patientContextPath + "/" + healthId)
                .then()
                .assertThat().contentType(ContentType.XML).statusCode(SC_OK)
                .extract().response();

        IBaseResource resource = xmlParser.parseResource(response.asString());
        assertTrue(resource instanceof Bundle);

        Patient responsePatient = findPatientFromBundle((Bundle) resource);

        assertNotNull(responsePatient);
        assertHealthId(expectedPatient, responsePatient);
        assertName(expectedPatient, responsePatient);
        assertGender(expectedPatient, responsePatient);
        assertDOB(expectedPatient, responsePatient);
        assertAddress(expectedPatient, responsePatient);
        assertLink("/api/v1/patients/" + healthId, responsePatient);
    }

    @Test
    public void shouldCreateAPatientWithoutBirthTime() throws Exception {
        IdpUserEnum idpUserEnum = IdpUserEnum.FACILITY;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);
        String content = new PatientFHIRXMLFactory().withValidXML(PatientFactory.validPatientWithoutBirthTime());

        Response createResponse = given()
                .header("X-Auth-Token", accessToken)
                .header("From", idpUserEnum.getEmail())
                .header("client_id", idpUserEnum.getClientId())
                .body(content).post(patientContextPath);
        assertEquals(SC_CREATED, createResponse.statusCode());
        String healthId = new JsonPath(createResponse.asString()).getString("id");
        assertNotNull(healthId);
    }

    @Test
    public void shouldFailToCreatePatientIfItHasUnknownElements() throws Exception {
        IdpUserEnum idpUserEnum = IdpUserEnum.FACILITY;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);
        String content = new PatientFHIRXMLFactory().withUnknownElementInXML(PatientFactory.validPatientWithoutBirthTime());

        Response createResponse = given()
                .header("X-Auth-Token", accessToken)
                .header("From", idpUserEnum.getEmail())
                .header("client_id", idpUserEnum.getClientId())
                .body(content).post(patientContextPath);
        assertEquals(SC_UNPROCESSABLE_ENTITY, createResponse.statusCode());
        String message = new JsonPath(createResponse.asString()).getString("message");
        assertTrue(message.contains("Unknown element 'someElement' found during parse"));
    }

    @Test
    public void shouldFailToCreatePatientIfItHasUnknownAttributeForAnElement() throws Exception {
        IdpUserEnum idpUserEnum = IdpUserEnum.FACILITY;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);
        String content = new PatientFHIRXMLFactory().withUnknownAttributeForGenderInXML(PatientFactory.validPatientWithMandatoryInformation());

        Response createResponse = given().body(content)
                .header("X-Auth-Token", accessToken)
                .header("From", idpUserEnum.getEmail())
                .header("client_id", idpUserEnum.getClientId())
                .post(patientContextPath);
        assertEquals(SC_UNPROCESSABLE_ENTITY, createResponse.statusCode());
        String message = new JsonPath(createResponse.asString()).getString("message");
        assertTrue(message.contains("Unknown attribute 'newAttribute' found during parse"));
    }

    @Test
    public void shouldFailToCreatePatientIfItHasUnexpectedRepeatingElement() throws Exception {
        String content = new PatientFHIRXMLFactory().withMultipleGenderElementsInXML(PatientFactory.validPatientWithoutBirthTime());

        IdpUserEnum idpUserEnum = IdpUserEnum.FACILITY;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);

        Response createResponse = given()
                .header("X-Auth-Token", accessToken)
                .header("From", idpUserEnum.getEmail())
                .header("client_id", idpUserEnum.getClientId())
                .body(content).post(patientContextPath);
        assertEquals(SC_UNPROCESSABLE_ENTITY, createResponse.statusCode());
        String message = new JsonPath(createResponse.asString()).getString("message");
        assertTrue(message.contains("Multiple repetitions of non-repeatable element 'gender' found during parse"));
    }

    @Test
    public void shouldFailToCreatePatientIfItHasInvalidData() throws Exception {
        IdpUserEnum idpUserEnum = IdpUserEnum.FACILITY;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);
        String content = new PatientFHIRXMLFactory().withInvalidGenderInXML(PatientFactory.validPatientWithoutBirthTime());

        Response createResponse = given()
                .header("X-Auth-Token", accessToken)
                .header("From", idpUserEnum.getEmail())
                .header("client_id", idpUserEnum.getClientId())
                .body(content)
                .post(patientContextPath);
        assertEquals(SC_UNPROCESSABLE_ENTITY, createResponse.statusCode());
        JsonPath jsonPath = new JsonPath(createResponse.asString());
        String message = jsonPath.getString("message");
        assertEquals("Validation Failed", message);

        String errorMessage = "The value provided is not in the value set http://hl7.org/fhir/ValueSet/administrative-gender (http://hl7.org/fhir/ValueSet/administrative-gender, and a code is required from this value set";
        List<Map> errors = jsonPath.getList("errors", Map.class);
        assertEquals(1, errors.size());
        Map error = errors.get(0);
        assertEquals("/f:Patient/f:gender", error.get("field"));
        assertEquals("error", error.get("type"));
        assertEquals(errorMessage, error.get("reason"));
    }

    @Test
    public void shouldFailToCreatePatientHasNotRequiredDataForMCIProfile() throws Exception {
        String content = new PatientFHIRXMLFactory().withMissingRequiredDataInXML(PatientFactory.validPatientWithoutBirthTime());

        IdpUserEnum idpUserEnum = IdpUserEnum.FACILITY;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);

        Response createResponse = given()
                .header("X-Auth-Token", accessToken)
                .header("From", idpUserEnum.getEmail())
                .header("client_id", idpUserEnum.getClientId())
                .body(content)
                .post(patientContextPath);
        assertEquals(SC_UNPROCESSABLE_ENTITY, createResponse.statusCode());
        JsonPath jsonPath = new JsonPath(createResponse.asString());
        String message = jsonPath.getString("message");
        assertEquals("Validation Failed", message);

        List<Map> errors = jsonPath.getList("errors", Map.class);

        assertEquals(4, errors.size());
        assertTrue(containsError(errors, "Element '/f:Patient.name': minimum required = 1, but only found 0"));
        assertTrue(containsError(errors, "Element '/f:Patient.gender': minimum required = 1, but only found 0"));
        assertTrue(containsError(errors, "Element '/f:Patient.birthDate': minimum required = 1, but only found 0"));
        assertTrue(containsError(errors, "Element '/f:Patient.address': minimum required = 1, but only found 0"));
    }

    @Test
    public void shouldFailToCreatePatientHasUnwantedDuplicateDataForMCIProfile() throws Exception {
        IdpUserEnum idpUserEnum = IdpUserEnum.FACILITY;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);
        String content = new PatientFHIRXMLFactory().withDuplicateNameDataInXML(PatientFactory.validPatientWithoutBirthTime());

        Response createResponse = given()
                .header("X-Auth-Token", accessToken)
                .header("From", idpUserEnum.getEmail())
                .header("client_id", idpUserEnum.getClientId())
                .body(content).post(patientContextPath);
        assertEquals(SC_UNPROCESSABLE_ENTITY, createResponse.statusCode());
        JsonPath jsonPath = new JsonPath(createResponse.asString());
        String message = jsonPath.getString("message");
        assertEquals("Validation Failed", message);

        List<Map> errors = jsonPath.getList("errors", Map.class);

        assertEquals(1, errors.size());
        assertTrue(containsError(errors, "Element name @ /f:Patient: max allowed = 1, but found 2"));
    }

    private IdentifierDt mapHealthIdIdentifier(String healthId) {
        IdentifierDt healthIdIdentifierDt = new IdentifierDt();
        healthIdIdentifierDt.setValue(healthId);
        healthIdIdentifierDt.setSystem(String.format("%s%s%s%s", baseUrl, patientContextPath, "/", healthId));
        return healthIdIdentifierDt;
    }

    private boolean containsError(List<Map> errors, String message) {
        for (Map error : errors) {
            assertEquals("/f:Patient", error.get("field"));
            if (error.get("reason").equals(message)) return true;
        }
        return false;
    }


    private void assertLink(String linkEndsWith, Patient actualPatient) {
        Patient.Link actualLink = actualPatient.getLinkFirstRep();
        assertEquals(SEE_ALSO.getCode(), actualLink.getType());
        assertTrue(actualLink.getOther().getReference().getValue().endsWith(linkEndsWith));
    }

    private void assertAddress(Patient expected, Patient actual) {
        AddressDt actualAddress = actual.getAddressFirstRep();
        AddressDt expectedAddress = expected.getAddressFirstRep();
        assertEquals(expectedAddress.getCountry(), actualAddress.getCountry());
        assertEquals(expectedAddress.getLine(), actualAddress.getLine());
        ExtensionDt expectedExtensionDt = expectedAddress.getUndeclaredExtensions().get(0);
        ExtensionDt actualExtensionDt = actualAddress.getUndeclaredExtensions().get(0);
        assertEquals(expectedExtensionDt.getUrl(), actualExtensionDt.getUrl());
        assertEquals(expectedExtensionDt.getValue(), actualExtensionDt.getValue());
    }

    private void assertDOB(Patient expected, Patient actual) {
        assertEquals(expected.getBirthDateElement(), actual.getBirthDateElement());
    }

    private void assertGender(Patient expected, Patient actual) {
        assertEquals(expected.getGender(), actual.getGender());
    }

    private void assertName(Patient expected, Patient actual) {
        HumanNameDt actualName = actual.getNameFirstRep();
        HumanNameDt expectedName = expected.getNameFirstRep();
        assertEquals(expectedName.getGivenFirstRep(), actualName.getGivenFirstRep());
        assertEquals(expectedName.getFamilyFirstRep(), actualName.getFamilyFirstRep());
    }

    private void assertHealthId(Patient expectedPatient, Patient actualPatient) {
        IdentifierDt actualIdentifier = actualPatient.getIdentifier().get(0);
        IdentifierDt expectedIdentifier = expectedPatient.getIdentifier().get(0);
        assertEquals(expectedIdentifier.getValue(), actualIdentifier.getValue());
        assertEquals(expectedIdentifier.getSystem(), actualIdentifier.getSystem());
        BoundCodeableConceptDt<IdentifierTypeCodesEnum> type = actualIdentifier.getType();
        CodingDt codingDt = type.getCodingFirstRep();
        assertEquals(baseUrl + "/api/v2/vs/patient-identifiers", codingDt.getSystem());
        assertEquals("Health Id", codingDt.getCode());
    }

    private Patient findPatientFromBundle(Bundle bundle) {
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (new Patient().getResourceName().equals(entry.getResource().getResourceName())) {
                return (Patient) entry.getResource();
            }
        }
        return null;
    }
}
