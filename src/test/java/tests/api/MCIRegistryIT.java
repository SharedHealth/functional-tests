package tests.api;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.*;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.RelatedPerson;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierTypeCodesEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import utils.FhirContextHelper;
import utils.IdpUserEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.model.dstu2.valueset.LinkTypeEnum.SEE_ALSO;
import static com.jayway.restassured.RestAssured.given;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static utils.FhirConstant.*;
import static utils.IdentityLoginUtil.login;

@Category(MciApiTest.class)
public class MCIRegistryIT {
    private final IParser xmlParser = FhirContextHelper.getFhirContext().newXmlParser();
    ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();

    private final String baseUrl = config.property.get("mci_registry");
    private final String baseUrlWithoutScheme = config.property.get("mci_registry_without_scheme");
    private final String IDP_SERVER_BASE_URL = config.property.get("idp_server_base_url");
    private final String patientContextPath = "/api/v2/patients";


    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = baseUrl;
    }

    @Test
    public void createAndGetPatientWithMandatoryFields() throws Exception {
        IdpUserEnum idpUserEnum = IdpUserEnum.FACILITY;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);

        domain.Patient patient = PatientFactory.validPatientWithMandatoryInformation();
        String content = new PatientFHIRXMLFactory(baseUrl).withValidXML(patient);

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
        assertIdentifier(responsePatient.getIdentifier(), MCI_IDENTIFIER_HID_CODE, healthId, healthId);
        assertName(responsePatient, patient.given, patient.family);
        assertEquals(patient.gender, responsePatient.getGender());
        assertDOB(expectedPatient, responsePatient);
        assertAddress(expectedPatient, responsePatient);
        assertTrue(responsePatient.getActive());
        assertNull(responsePatient.getDeceased());
        assertFalse(((BooleanDt) getExtensionValue(responsePatient, CONFIDENTIALITY_EXTENSION_NAME)).getValue());
        CodeableConceptDt dobType = (CodeableConceptDt) getExtensionValue(responsePatient, DOB_TYPE_EXTENSION_NAME);
        assertEquals("1", dobType.getCodingFirstRep().getCode());
        assertLink("/api/v1/patients/" + healthId, responsePatient);
    }

    @Test
    public void shouldCreateAPatientWithoutBirthTime() throws Exception {
        IdpUserEnum idpUserEnum = IdpUserEnum.FACILITY;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);
        String content = new PatientFHIRXMLFactory(baseUrl).withValidXML(PatientFactory.validPatientWithoutBirthTime());

        Response createResponse = given()
                .header("X-Auth-Token", accessToken)
                .header("From", idpUserEnum.getEmail())
                .header("client_id", idpUserEnum.getClientId())
                .body(content).post(patientContextPath);
        assertEquals(SC_CREATED, createResponse.statusCode());
        String healthId = new JsonPath(createResponse.asString()).getString("id");
        assertNotNull(healthId);
    }

//    Commented out lines are failing because of issue #1487
    @Test
    public void shouldCreateAndGetPatientWithAllFields() throws Exception {
        IdpUserEnum idpUserEnum = IdpUserEnum.FACILITY;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);

        domain.Patient patient = PatientFactory.validPatientWithAllInformation();
        String content = new PatientFHIRXMLFactory(baseUrl).withValidXML(patient);
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
        Bundle bundle = (Bundle) resource;
        Patient responsePatient = findPatientFromBundle(bundle);

        assertNotNull(responsePatient);
        assertIdentifier(responsePatient.getIdentifier(), MCI_IDENTIFIER_HID_CODE, healthId, healthId);
//        assertIdentifier(responsePatient.getIdentifier(), MCI_IDENTIFIER_NID_CODE, patient.nid, healthId);
//        assertIdentifier(responsePatient.getIdentifier(), MCI_IDENTIFIER_BRN_CODE, patient.binBRN, healthId);

        assertPhoneNumber(patient.phoneNumber, responsePatient);
        StringDt houseHoldCode = (StringDt) getExtensionValue(responsePatient, HOUSE_HOLD_CODE_EXTENSION_NAME);
        assertEquals(patient.householdCode, houseHoldCode.getValue());
        assertFalse(((BooleanDt) getExtensionValue(responsePatient, CONFIDENTIALITY_EXTENSION_NAME)).getValue());

        CodeableConceptDt dobType = (CodeableConceptDt) getExtensionValue(responsePatient, DOB_TYPE_EXTENSION_NAME);
        CodingDt dobTypeCoding = dobType.getCodingFirstRep();
        assertEquals(dobTypeCoding.getSystem(), getMCIValuesetURI("http://"+baseUrlWithoutScheme, MCI_PATIENT_DOB_TYPE_VALUESET));
        assertEquals("3", dobTypeCoding.getCode());

        CodeableConceptDt education = (CodeableConceptDt) getExtensionValue(responsePatient, EDUCATION_DETAILS_EXTENSION_NAME);
        CodingDt educationCoding = education.getCodingFirstRep();
        assertEquals(educationCoding.getSystem(), getMCIValuesetURI("http://"+baseUrlWithoutScheme, MCI_PATIENT_EDUCATION_DETAILS_VALUESET));
        assertEquals("02", educationCoding.getCode());

        CodeableConceptDt occupation = (CodeableConceptDt) getExtensionValue(responsePatient, OCCUPATION_EXTENSION_NAME);
        CodingDt occupationCoding = occupation.getCodingFirstRep();
        assertEquals(occupationCoding.getSystem(), getMCIValuesetURI("http://"+baseUrlWithoutScheme, MCI_PATIENT_OCCUPATION_VALUESET));
        assertEquals("03", occupationCoding.getCode());

        assertName(responsePatient, patient.given, patient.family);
        assertEquals(patient.gender, responsePatient.getGender());
        assertDOB(expectedPatient, responsePatient);
        assertAddress(expectedPatient, responsePatient);
        assertTrue(responsePatient.getActive());
        assertFalse(((BooleanDt) responsePatient.getDeceased()).getValue());
        assertLink("/api/v1/patients/" + healthId, responsePatient);

        List<RelatedPerson> relatedPeople = findRelationsFromBundle(bundle);
        assertRelation(findRelationOfType(patient.relations, "MTH"), findRelatedPersonOfType(relatedPeople, "MTH"));
//        assertRelation(findRelationOfType(patient.relations, "FTH"), findRelatedPersonOfType(relatedPeople, "FTH"));
        assertRelation(findRelationOfType(patient.relations, "SPS"), findRelatedPersonOfType(relatedPeople, "SPS"));
    }

    @Test
    public void shouldCreateADeadPatientWithoutDeathOfDate() throws Exception {
        IdpUserEnum idpUserEnum = IdpUserEnum.FACILITY;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);

        domain.Patient patient = PatientFactory.validPatientWithMandatoryInformation();
        patient.isDead = true;
        String content = new PatientFHIRXMLFactory(baseUrl).withValidXML(patient);
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
        Bundle bundle = (Bundle) resource;
        Patient responsePatient = findPatientFromBundle(bundle);

        IDatatype deceased = responsePatient.getDeceased();
        assertTrue(deceased instanceof BooleanDt);
        assertTrue(((BooleanDt) deceased).getValue());
    }

    @Test
    public void shouldCreateADeadPatientWithDeathOfDate() throws Exception {
        IdpUserEnum idpUserEnum = IdpUserEnum.FACILITY;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);

        domain.Patient patient = PatientFactory.validPatientWithMandatoryInformation();
        patient.isDead = true;
        String dateOfDeath = "2016-06-14T16:50:00+05:30";
        patient.dateOfDeath = dateOfDeath;
        String content = new PatientFHIRXMLFactory(baseUrl).withValidXML(patient);
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
        Bundle bundle = (Bundle) resource;
        Patient responsePatient = findPatientFromBundle(bundle);

        IDatatype deceased = responsePatient.getDeceased();
        assertTrue(deceased instanceof DateTimeDt);
        assertEquals(dateOfDeath, ((DateTimeDt)deceased).getValueAsString());
    }

    @Test
    public void shouldFailToCreatePatientIfItHasUnknownElements() throws Exception {
        IdpUserEnum idpUserEnum = IdpUserEnum.FACILITY;
        String accessToken = login(idpUserEnum, IDP_SERVER_BASE_URL);
        String content = new PatientFHIRXMLFactory(baseUrl).withUnknownElementInXML(PatientFactory.validPatientWithoutBirthTime());

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
        String content = new PatientFHIRXMLFactory(baseUrl).withUnknownAttributeForGenderInXML(PatientFactory.validPatientWithMandatoryInformation());

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
        String content = new PatientFHIRXMLFactory(baseUrl).withMultipleGenderElementsInXML(PatientFactory.validPatientWithoutBirthTime());

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
        String content = new PatientFHIRXMLFactory(baseUrl).withInvalidGenderInXML(PatientFactory.validPatientWithoutBirthTime());

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
        String content = new PatientFHIRXMLFactory(baseUrl).withMissingRequiredDataInXML(PatientFactory.validPatientWithoutBirthTime());

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
        String content = new PatientFHIRXMLFactory(baseUrl).withDuplicateNameDataInXML(PatientFactory.validPatientWithoutBirthTime());

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

    private void assertName(Patient actual, String givenName, String familyName) {
        HumanNameDt actualName = actual.getNameFirstRep();
        assertEquals(givenName, actualName.getGivenFirstRep().getValue());
        assertEquals(familyName, actualName.getFamilyFirstRep().getValue());
    }

    private void assertIdentifier(List<IdentifierDt> identifiers, final String code, String identifierValue, String healthId) {
        IdentifierDt identifierByCode = getIdentifierByCode(identifiers, code);
        if (StringUtils.isBlank(identifierValue)) {
            assertNull(identifierByCode);
            return;
        }
        assertEquals(identifierValue, identifierByCode.getValue());
        BoundCodeableConceptDt<IdentifierTypeCodesEnum> type = identifierByCode.getType();
        CodingDt codingDt = type.getCodingFirstRep();

        assertTrue(codingDt.getSystem().contains(baseUrlWithoutScheme + "/api/v2/vs/patient-identifiers"));
        assertEquals(code, codingDt.getCode());
        assertTrue(identifierByCode.getSystem().contains( baseUrlWithoutScheme + "/api/v2/patients/" + healthId));
    }

    private IdentifierDt getIdentifierByCode(List<IdentifierDt> identifierDt, String code) {
        for (IdentifierDt dt : identifierDt) {
            if (code.equals(dt.getType().getCodingFirstRep().getCode())) {
                return dt;
            }
        }
        return null;
    }

    private Patient findPatientFromBundle(Bundle bundle) {
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (new Patient().getResourceName().equals(entry.getResource().getResourceName())) {
                return (Patient) entry.getResource();
            }
        }
        return null;
    }

    private IBaseDatatype getExtensionValue(Patient patient, String extensionName) {
        List<ExtensionDt> extension = patient.getUndeclaredExtensionsByUrl(getFhirExtensionUrl(extensionName));
        return CollectionUtils.isEmpty(extension) ? null : extension.get(0).getValue();
    }

    private void assertRelation(domain.Patient.Relation relation, RelatedPerson relatedPerson) {
        assertIdentifier(relatedPerson.getIdentifier(), MCI_IDENTIFIER_NID_CODE, relation.nid, relation.hid);
        assertIdentifier(relatedPerson.getIdentifier(), MCI_IDENTIFIER_BRN_CODE, relation.binBrn, relation.hid);
        assertIdentifier(relatedPerson.getIdentifier(), MCI_IDENTIFIER_HID_CODE, relation.hid, relation.hid);
        assertIdentifier(relatedPerson.getIdentifier(), MCI_IDENTIFIER_UID_CODE, relation.uid, relation.hid);
        assertEquals(relatedPerson.getName().getGivenFirstRep().getValue(), relation.given);
        assertEquals(relatedPerson.getName().getFamilyFirstRep().getValue(), relation.family);
    }

    private RelatedPerson findRelatedPersonOfType(List<RelatedPerson> relatedPeople, String type) {
        for (RelatedPerson relatedPerson : relatedPeople) {
            if (type.equals(relatedPerson.getRelationship().getCodingFirstRep().getCode())) return relatedPerson;
        }
        return null;
    }

    private domain.Patient.Relation findRelationOfType(List<domain.Patient.Relation> relations, String type) {
        for (domain.Patient.Relation relation : relations) {
            if (relation.type.equals(type)) return relation;
        }
        return null;
    }

    private List<RelatedPerson> findRelationsFromBundle(Bundle bundle) {
        List<RelatedPerson> relatedPersonList = new ArrayList<>();
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (new RelatedPerson().getResourceName().equals(entry.getResource().getResourceName())) {
                relatedPersonList.add((RelatedPerson) entry.getResource());
            }
        }
        return relatedPersonList;

    }

    private void assertPhoneNumber(String phoneNumber, Patient responsePatient) {
        ContactPointDt telecom = responsePatient.getTelecomFirstRep();
        assertEquals("phone", telecom.getSystem());
        assertEquals(phoneNumber, telecom.getValue());
    }
}
