package categories;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.composite.*;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierTypeCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.LinkTypeEnum;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParser;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Before;
import org.junit.Test;
import utils.FhirContextHelper;

import java.util.Date;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class MciApiTest implements ApiTest {
    private final IParser xmlParser = FhirContextHelper.getFhirContext().newXmlParser();

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "http://172.18.46.199:8085";
    }

    private Patient createPatientToPost() {
        Patient patientToPost = new Patient();
        patientToPost.addName(mapName("Rajia", "Shaktiman"));
        patientToPost.setGender(AdministrativeGenderEnum.OTHER);
        patientToPost.addAddress(mapAddress());

        return patientToPost;
    }

    private HumanNameDt mapName(String givenName, String surName) {
        HumanNameDt name = new HumanNameDt();
        name.addGiven(givenName);
        name.addFamily(surName);
        return name;
    }

    private AddressDt mapAddress() {
        AddressDt addressDt = new AddressDt().addLine("3rd lane");
        addressDt.setCountry("050");
        ExtensionDt addressCodeExtension = new ExtensionDt().
                setUrl("https://sharedhealth.atlassian.net/wiki/display/docs/fhir-extensions#AddressCode")
                .setValue(new StringDt("201918991101"));
        addressDt.addUndeclaredExtension(addressCodeExtension);
        return addressDt;
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
        String mciPatientURI = "http://172.18.46.199:8085/api/v2/patients/";
        healthIdIdentifierDt.setSystem(String.format("%s%s", mciPatientURI, healthId));
        return healthIdIdentifierDt;
    }


    @Test
    public void getPatientDetails() throws Exception {
        Patient patientToPost = createPatientToPost();
        //shoudl post patient and get HID
        String healthId = "98000719529"; //should get it back from post
        patientToPost.addIdentifier(mapHealthIdIdentifier(healthId));
        patientToPost.addLink(mapPatientReferenceLink(healthId));

        Response response = given().header("X-Auth-Token", "zfsTkslTvUSvjo89hZmMHkGLG6PnmpoBbJaS7wJDYl")
                .header("From", "mritunjd@thoughtworks.com")
                .header("client_id", "18550")
                .when().get("/api/v2/patients/" + healthId);
        assertEquals(200, response.getStatusCode());

        IBaseResource resource = xmlParser.parseResource(response.asString());
        assertTrue(resource instanceof Patient);

        Patient responsePatient = (Patient) resource;
        assertNotNull(responsePatient);
        assertHealthId(patientToPost, responsePatient);
        assertName(patientToPost, responsePatient);
        assertGender(patientToPost, responsePatient);
        assertAddress(patientToPost, responsePatient);
        assertLink(patientToPost, responsePatient);

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

    private void assertDOB(Patient fhirPatient, Date dateOfBirth) {
        assertEquals(dateOfBirth, fhirPatient.getBirthDate());
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
