package domain;

import nu.xom.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PatientFHIRXMLComposer {
    public static final String VALUE = "value";
    private Element root;
    private Patient patient;
    public static final String xmlns = "http://hl7.org/fhir";

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public PatientFHIRXMLComposer() {
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

    private void appendNameDetails(Element element) {
        Element name = new Element("name", xmlns);
        Element family = new Element("family", xmlns);
        family.addAttribute(new Attribute(VALUE, this.patient.family));
        Element given = new Element("given", xmlns);
        given.addAttribute(new Attribute(VALUE, this.patient.given));
        name.appendChild(family);
        name.appendChild(given);
        element.appendChild(name);
    }

    private void appendGenderDetails(Element element) {
        Element gender = new Element("gender", xmlns);
        gender.addAttribute(new Attribute(VALUE, this.patient.gender));
        element.appendChild(gender);
    }

    private void appendBirthDetails(Element element) {
        Element birthDate = new Element("birthDate", xmlns);
        birthDate.addAttribute(new Attribute(VALUE, this.patient.birthDate));

        if (this.patient.hasBirthTime()) {
            Element birthDateExtension = new Element("extension", xmlns);
            birthDateExtension.addAttribute(new Attribute("url", this.xmlns + "/StructureDefinition/patient-birthTime"));
            Element birthDateExtensionValue = new Element("valueDateTime", xmlns);
            birthDateExtensionValue.addAttribute(new Attribute(VALUE, this.patient.birthDate + "T" + this.patient.birthTime + "+05:30"));

            birthDateExtension.appendChild(birthDateExtensionValue);
            birthDate.appendChild(birthDateExtension);
        }

        element.appendChild(birthDate);
    }

    private void appendAddressDetails(Element element) {
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
        code.addAttribute(new Attribute(VALUE, patient.maritalStatusCode));
        Element display = new Element("display");
        display.addAttribute(new Attribute(VALUE, MaritalStatus.getMaritalStatus(patient.maritalStatusCode)));
        coding.appendChild(system);
        coding.appendChild(code);
        coding.appendChild(display);
        maritalStatus.appendChild(coding);
        root.appendChild(maritalStatus);
    }


    public String composePatient() throws ParsingException, IOException {
        Element entry = new Element("entry", xmlns);
        this.root.appendChild(entry);

        Element fullUrl = new Element("fullUrl", xmlns);
        fullUrl.addAttribute(new Attribute(VALUE, "urn:uuid" + UUID.randomUUID()));
        entry.appendChild(fullUrl);

        Element resource = new Element("resource", xmlns);
        entry.appendChild(resource);

        Element patient = new Element("Patient", xmlns);
        resource.appendChild(patient);

        if (this.patient.hasNameDetails()) this.appendNameDetails(patient);
        if (this.patient.hasGenderDetails()) this.appendGenderDetails(patient);
        if (this.patient.hasBirthDetails()) this.appendBirthDetails(patient);
        if (this.patient.hasAddressDetails()) this.appendAddressDetails(patient);
        Document patientDetails = new Document(root);
        Builder parser = new Builder();
        parser.build(patientDetails.toXML(), null);
        return patientDetails.toXML();

    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
    }
}

