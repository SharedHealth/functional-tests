package tests.api;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.composite.*;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierTypeCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.LinkTypeEnum;
import ca.uhn.fhir.parser.IParser;
import categories.MciApiTest;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import utils.FhirContextHelper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

@Category(MciApiTest.class)
public class MCIRegistryIT {
    private final IParser xmlParser = FhirContextHelper.getFhirContext().newXmlParser();
    private final String baseUrl = "http://172.18.46.199:8085";
    private final String patientContextPath = "/api/v2/patients";

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = baseUrl;
    }

    private String readFile(String fileName) {
        try {
            return Resources.toString(Resources.getResource(fileName), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Patient.Link mapPatientReferenceLink(String healthId) {
        Patient.Link link = new Patient.Link();
        link.setType(LinkTypeEnum.SEE_ALSO);
        String patientLinkUri = "http://172.18.46.199:8081/api/v1/patients/";
        ResourceReferenceDt patientReference = new ResourceReferenceDt(String.format("%s%s", patientLinkUri, healthId));
        link.setOther(patientReference);
        return link;
    }

    private IdentifierDt mapHealthIdIdentifier(String healthId) {
        IdentifierDt healthIdIdentifierDt = new IdentifierDt();
        healthIdIdentifierDt.setValue(healthId);
        healthIdIdentifierDt.setSystem(String.format("%s%s%s%s", baseUrl, patientContextPath, "/", healthId));
        return healthIdIdentifierDt;
    }

    @Test
    public void createAndGetPatient() throws Exception {
        String content = readFile("fhir/patients/valid_patient_with_mandatory_fields.xml");
        Patient expectedPatient = (Patient) xmlParser.parseResource(content);

        Response createResponse = given().body(content).post(patientContextPath);
        assertEquals(SC_CREATED, createResponse.statusCode());
        String healthId = new JsonPath(createResponse.asString()).getString("id");

        expectedPatient.addIdentifier(mapHealthIdIdentifier(healthId));
        expectedPatient.addLink(mapPatientReferenceLink(healthId));

        Response response = get(patientContextPath + "/" + healthId);
        assertEquals(SC_OK, response.getStatusCode());

        IBaseResource resource = xmlParser.parseResource(response.asString());
        assertTrue(resource instanceof Patient);

        Patient responsePatient = (Patient) resource;
        assertNotNull(responsePatient);
        assertHealthId(expectedPatient, responsePatient);
        assertName(expectedPatient, responsePatient);
        assertGender(expectedPatient, responsePatient);
        assertDOB(expectedPatient, responsePatient);
        assertAddress(expectedPatient, responsePatient);
        assertLink(expectedPatient, responsePatient);
    }

    @Test
    public void shouldCreateAPatientWithoutBirthTime() throws Exception {
        String content = readFile("fhir/patients/valid_patient_without_birth_time.xml");

        Response createResponse = given().body(content).post(patientContextPath);
        assertEquals(SC_CREATED, createResponse.statusCode());
        String healthId = new JsonPath(createResponse.asString()).getString("id");
        assertNotNull(healthId);
    }

    @Test
    public void shouldFailToCreatePatientIfItHasUnknownElements() throws Exception {
        String content = readFile("fhir/patients/patient_with_unknown_elements.xml");

        Response createResponse = given().body(content).post(patientContextPath);
        assertEquals(SC_UNPROCESSABLE_ENTITY, createResponse.statusCode());
        String message = new JsonPath(createResponse.asString()).getString("message");
        assertTrue(message.contains("Unknown element 'newElement' found during parse"));
    }

    @Test
    public void shouldFailToCreatePatientIfItHasUnknownAttributeForAnElement() throws Exception {
        String content = readFile("fhir/patients/patient_with_unknown_attributes.xml");

        Response createResponse = given().body(content).post(patientContextPath);
        assertEquals(SC_UNPROCESSABLE_ENTITY, createResponse.statusCode());
        String message = new JsonPath(createResponse.asString()).getString("message");
        assertTrue(message.contains("Unknown attribute 'newAttribute' found during parse"));
    }

    @Test
    public void shouldFailToCreatePatientIfItHasUnexpectedRepeatingElement() throws Exception {
        String content = readFile("fhir/patients/patient_with_multiple_genders.xml");

        Response createResponse = given().body(content).post(patientContextPath);
        assertEquals(SC_UNPROCESSABLE_ENTITY, createResponse.statusCode());
        String message = new JsonPath(createResponse.asString()).getString("message");
        assertTrue(message.contains("Multiple repetitions of non-repeatable element 'gender' found during parse"));
    }

    @Test
    public void shouldFailToCreatePatientIfItHasInvalidData() throws Exception {
        String content = readFile("fhir/patients/patient_with_invalid_gender.xml");

        Response createResponse = given().body(content).post(patientContextPath);
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
        String content = readFile("fhir/patients/invalid_patient_for_custom_profile.xml");

        Response createResponse = given().body(content).post(patientContextPath);
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
        String content = readFile("fhir/patients/patient_with_extra_name_fields.xml");

        Response createResponse = given().body(content).post(patientContextPath);
        assertEquals(SC_UNPROCESSABLE_ENTITY, createResponse.statusCode());
        JsonPath jsonPath = new JsonPath(createResponse.asString());
        String message = jsonPath.getString("message");
        assertEquals("Validation Failed", message);

        List<Map> errors = jsonPath.getList("errors", Map.class);

        assertEquals(1, errors.size());
        assertTrue(containsError(errors, "Element name @ /f:Patient: max allowed = 1, but found 2"));
    }

    private boolean containsError(List<Map> errors, String message) {
        for (Map error : errors) {
            assertEquals("/f:Patient", error.get("field"));
            if (error.get("reason").equals(message)) return true;
        }
        return false;
    }


    private void assertLink(Patient expectedPatient, Patient actualPatient) {
        Patient.Link expectedLink = expectedPatient.getLinkFirstRep();
        Patient.Link actualLink = actualPatient.getLinkFirstRep();
        assertEquals(expectedLink.getType(), actualLink.getType());
        assertEquals(expectedLink.getOther().getReference(), actualLink.getOther().getReference());

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
        assertEquals("http://172.18.46.199:8085/api/v2/vs/patient-identifiers", codingDt.getSystem());
        assertEquals("HID", codingDt.getCode());
    }

}
