package domain;

import nu.xom.*;
import org.apache.commons.lang3.StringUtils;
import utils.FhirConstant;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static utils.FhirConstant.*;

public class PatientFHIRXMLComposer {
    public static final String VALUE = "value";
    private Element root;
    private Patient patient;
    public static final String xmlns = "http://hl7.org/fhir";
    private String mciBaseUrl;

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public PatientFHIRXMLComposer(String mciBaseUrl) {
        this.mciBaseUrl = mciBaseUrl;
        this.root = new Element("Bundle", xmlns);

        Element id = new Element("id", xmlns);
        id.addAttribute(new Attribute(VALUE, UUID.randomUUID().toString()));

        Element meta = new Element("meta", xmlns);
        Element lastUpdated = new Element("lastUpdated", xmlns);
        lastUpdated.addAttribute(new Attribute(VALUE, formatDate(new Date())));
        meta.appendChild(lastUpdated);

        Element type = new Element("type", xmlns);
        type.addAttribute(new Attribute(VALUE, "collection"));

        root.appendChild(id);
        root.appendChild(meta);
        root.appendChild(type);
    }

    private void appendNameDetails(Element element, String familyName, String givenName) {
        if (StringUtils.isBlank(familyName) && StringUtils.isBlank(givenName)) return;
        Element name = new Element("name", xmlns);
        Element family = new Element("family", xmlns);
        family.addAttribute(new Attribute(VALUE, familyName));
        Element given = new Element("given", xmlns);
        given.addAttribute(new Attribute(VALUE, givenName));
        name.appendChild(family);
        name.appendChild(given);
        element.appendChild(name);
    }

    private void appendGenderDetails(Element element, String genderValue) {
        if (StringUtils.isBlank(genderValue)) return;
        Element gender = new Element("gender", xmlns);
        gender.addAttribute(new Attribute(VALUE, genderValue));
        element.appendChild(gender);
    }

    private void appendBirthDetails(Element element, String birthDateValue, String birthTimeValue) {
        if (StringUtils.isBlank(birthDateValue)) return;
        Element birthDate = new Element("birthDate", xmlns);
        birthDate.addAttribute(new Attribute(VALUE, birthDateValue));

        if (StringUtils.isNotBlank(birthTimeValue)) {
            Element birthDateExtension = new Element("extension", xmlns);
            birthDateExtension.addAttribute(new Attribute("url", this.xmlns + "/StructureDefinition/patient-birthTime"));
            Element birthDateExtensionValue = new Element("valueDateTime", xmlns);
            birthDateExtensionValue.addAttribute(new Attribute(VALUE, birthDateValue + "T" + birthTimeValue + "+05:30"));

            birthDateExtension.appendChild(birthDateExtensionValue);
            birthDate.appendChild(birthDateExtension);
        }

        element.appendChild(birthDate);
    }

    private void appendAddressDetails(Element element) {
        if (StringUtils.isBlank(patient.addressCode)) return;
        Element address = new Element("address", xmlns);
        Element addressExtension = new Element("extension", xmlns);
        addressExtension.addAttribute(new Attribute("url", "https://sharedhealth.atlassian.net/wiki/display/docs/fhir-extensions#AddressCode"));
        Element addressExtensionValueString = new Element("valueString", xmlns);
        addressExtensionValueString.addAttribute(new Attribute(VALUE, this.patient.addressCode));
        Element addressLine = new Element("line", xmlns);
        addressLine.addAttribute(new Attribute(VALUE, this.patient.addressLine));
        Element country = new Element("country", xmlns);
        country.addAttribute(new Attribute(VALUE, this.patient.countryCode));
        addressExtension.appendChild(addressExtensionValueString);
        address.appendChild(addressExtension);
        address.appendChild(addressLine);
        address.appendChild(country);
        element.appendChild(address);

    }

    private void appendMaritalStatus() {
        Element maritalStatus = new Element("maritalStatusCode", xmlns);
        Element coding = new Element("coding", xmlns);
        Element system = new Element("system", xmlns);
        system.addAttribute(new Attribute(VALUE, xmlns + "/v3/getMaritalStatus"));
        Element code = new Element("code", xmlns);
//        code.addAttribute(new Attribute(VALUE, patient.maritalStatusCode));
        Element display = new Element("display");
//        display.addAttribute(new Attribute(VALUE, MaritalStatus.getMaritalStatus(patient.maritalStatusCode)));
        coding.appendChild(system);
        coding.appendChild(code);
        coding.appendChild(display);
        maritalStatus.appendChild(coding);
        root.appendChild(maritalStatus);
    }


    private void appendPhoneNumber(Element patientElement, String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) return;
        Element telecom = new Element("telecom", xmlns);
        patientElement.appendChild(telecom);

        Element system = new Element("system", xmlns);
        system.addAttribute(new Attribute(VALUE, "phone"));
        telecom.appendChild(system);

        Element valueElement = new Element(VALUE, xmlns);
        valueElement.addAttribute(new Attribute(VALUE, phoneNumber));
        telecom.appendChild(valueElement);
    }

    private void appendActive(Element patientElement, Boolean activeValue) {
        if (activeValue == null) return;
        Element active = new Element("active", xmlns);
        active.addAttribute(new Attribute(VALUE, String.valueOf(activeValue)));
        patientElement.appendChild(active);
    }

    private void appendDeceased(Element patientElement) {
        if (patient.isDead == null) return;
        if (StringUtils.isNotBlank(patient.dateOfDeath)) {
            Element deceasedDateTime = new Element("deceasedDateTime", xmlns);
            deceasedDateTime.addAttribute(new Attribute(VALUE, patient.dateOfDeath));
            patientElement.appendChild(deceasedDateTime);
            return;
        }
        Element deceasedBoolean = new Element("deceasedBoolean", xmlns);
        deceasedBoolean.addAttribute(new Attribute(VALUE, String.valueOf(patient.isDead)));
        patientElement.appendChild(deceasedBoolean);
    }

    private void appendExtensionWithStringValue(Element patientElement, String extensionName, String valueType, String value) {
        Element extension = new Element("extension", xmlns);
        patientElement.appendChild(extension);
        extension.addAttribute(new Attribute("url", getFhirExtensionUrl(extensionName)));

        Element valueElement = new Element(valueType, xmlns);
        valueElement.addAttribute(new Attribute(VALUE, value));
        extension.appendChild(valueElement);
    }

    private void appendIdentifier(Element element, String identifierCode, String identifierValue) {
        if (StringUtils.isBlank(identifierValue)) return;
        Element identifier = new Element("identifier", xmlns);
        element.appendChild(identifier);
        Element type = new Element("type", xmlns);

        identifier.appendChild(type);
        type.appendChild(createCodingElementFromValueSet(identifierCode, MCI_PATIENT_IDENTIFIERS_VALUESET));

        Element identifierValueElement = new Element(VALUE, xmlns);
        identifierValueElement.addAttribute(new Attribute(VALUE, identifierValue));
        identifier.appendChild(identifierValueElement);
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ").format(date);
    }

    private void appendExtensionWithCodeableConcept(Element patientElement, String extensionName, String value, String valuesetName) {
        Element extension = new Element("extension", xmlns);
        patientElement.appendChild(extension);
        extension.addAttribute(new Attribute("url", getFhirExtensionUrl(extensionName)));
        Element valueElement = new Element("valueCodeableConcept", xmlns);
        valueElement.appendChild(createCodingElementFromValueSet(value, valuesetName));
        extension.appendChild(valueElement);
    }

    private Element createCodingElementFromValueSet(String code, String valuesetName) {
        return createCodingElement(code, FhirConstant.getMCIValuesetURI(mciBaseUrl, valuesetName));
    }

    private Element createCodingElement(String code, String systemValue) {
        Element coding = new Element("coding", xmlns);

        Element system = new Element("system", xmlns);
        system.addAttribute(new Attribute(VALUE, systemValue));
        coding.appendChild(system);

        Element codeElement = new Element("code", xmlns);
        codeElement.addAttribute(new Attribute(VALUE, code));
        coding.appendChild(codeElement);

        return coding;
    }

    public String composePatient() throws ParsingException, IOException {
        Element entry = new Element("entry", xmlns);
        this.root.appendChild(entry);

        Element fullUrl = new Element("fullUrl", xmlns);
        String patientEntryFullUrlValue = "urn:uuid:" + UUID.randomUUID();
        fullUrl.addAttribute(new Attribute(VALUE, patientEntryFullUrlValue));
        entry.appendChild(fullUrl);

        Element resource = new Element("resource", xmlns);
        entry.appendChild(resource);

        Element patientElement = new Element("Patient", xmlns);
        resource.appendChild(patientElement);

        appendNameDetails(patientElement, this.patient.family, this.patient.given);
        appendGenderDetails(patientElement, patient.gender);
        appendBirthDetails(patientElement, patient.birthDate, patient.birthTime);
        appendPhoneNumber(patientElement, patient.phoneNumber);
        appendActive(patientElement, patient.active);
        appendAddressDetails(patientElement);

        appendDeceased(patientElement);
        appendIdentifier(patientElement, MCI_IDENTIFIER_NID_CODE, patient.nid);
        appendIdentifier(patientElement, MCI_IDENTIFIER_BRN_CODE, patient.binBRN);

        if (StringUtils.isNotBlank(patient.householdCode)) {
            appendExtensionWithStringValue(patientElement, HOUSE_HOLD_CODE_EXTENSION_NAME, "valueString", patient.householdCode);
        }
        if (patient.confidentiality != null) {
            appendExtensionWithStringValue(patientElement, CONFIDENTIALITY_EXTENSION_NAME, "valueBoolean", String.valueOf(patient.confidentiality));
        }
        if (StringUtils.isNotBlank(patient.education)) {
            appendExtensionWithCodeableConcept(patientElement, EDUCATION_DETAILS_EXTENSION_NAME, patient.education, MCI_PATIENT_EDUCATION_DETAILS_VALUESET);
        }
        if (StringUtils.isNotBlank(patient.occupation)) {
            appendExtensionWithCodeableConcept(patientElement, OCCUPATION_EXTENSION_NAME, patient.occupation, MCI_PATIENT_OCCUPATION_VALUESET);
        }
        if (StringUtils.isNotBlank(patient.dobType)) {
            appendExtensionWithCodeableConcept(patientElement, DOB_TYPE_EXTENSION_NAME, patient.dobType, MCI_PATIENT_DOB_TYPE_VALUESET);
        }

        for (Patient.Relation relation : patient.relations) {
            Element relationEntry = new Element("entry", xmlns);
            this.root.appendChild(relationEntry);

            Element relationEntryFullUrl = new Element("fullUrl", xmlns);
            relationEntryFullUrl.addAttribute(new Attribute(VALUE, "urn:uuid" + UUID.randomUUID()));
            relationEntry.appendChild(relationEntryFullUrl);

            Element relationResource = new Element("resource", xmlns);
            relationEntry.appendChild(relationResource);

            Element relatedPerson = new Element("RelatedPerson", xmlns);
            relationResource.appendChild(relatedPerson);

            appendExtensionWithStringValue(relatedPerson, RELATION_ID_EXTENSION_NAME, "valueString", relation.id);
            appendIdentifier(relatedPerson, MCI_IDENTIFIER_NID_CODE, relation.nid);
            appendIdentifier(relatedPerson, MCI_IDENTIFIER_BRN_CODE, relation.binBrn);
            appendIdentifier(relatedPerson, MCI_IDENTIFIER_UID_CODE, relation.uid);
            appendIdentifier(relatedPerson, MCI_IDENTIFIER_HID_CODE, relation.hid);

            appendNameDetails(relatedPerson, relation.family, relation.given);

            Element patientElementForRelation = new Element("patient", xmlns);
            Element reference = new Element("reference", xmlns);
            reference.addAttribute(new Attribute(VALUE, patientEntryFullUrlValue));
            patientElementForRelation.appendChild(reference);
            relatedPerson.appendChild(patientElementForRelation);

            Element relationship = new Element("relationship", xmlns);
            relationship.appendChild(createCodingElement(relation.type, "http://hl7.org/fhir/v3/RoleCode"));
            relatedPerson.appendChild(relationship);
        }

        Document patientDetails = new Document(root);
        Builder parser = new Builder();
        parser.build(patientDetails.toXML(), null);
        return patientDetails.toXML();

    }
}

