package domain;

import nu.xom.*;

import java.io.IOException;

public class PatientFHIRXMLComposer {
    private Element root;
    private Patient patient;
    public static final String xmlns = "http://hl7.org/fhir";

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public PatientFHIRXMLComposer() {
        this.root = new Element("Patient",xmlns);
    }

    private void appendNameDetails()
    {

        Element name = new Element("name", xmlns);
        Element family = new Element("family", xmlns);
        family.addAttribute(new Attribute("value", patient.family));
        Element given = new Element("given", xmlns);
        given.addAttribute(new Attribute("value", patient.given));
        name.appendChild(family);
        name.appendChild(given);
        root.appendChild(name);
    }

    private void appendGenderDetails()
    {
        Element gender = new Element("gender", xmlns);
        gender.addAttribute(new Attribute("value", patient.gender));
        root.appendChild(gender);
    }

    private void appendBirthDetails()
    {
        Element birthDate = new Element("birthDate", xmlns);
        birthDate.addAttribute(new Attribute("value", patient.birthDate));

        if(patient.hasBirthTime())
        {
            Element birthDateExtension = new Element("extension", xmlns);
            birthDateExtension.addAttribute(new Attribute("url", this.xmlns + "/StructureDefinition/patient-birthTime"));
            Element birthDateExtensionValue = new Element("valueDateTime", xmlns);
            birthDateExtensionValue.addAttribute(new Attribute("value", patient.birthDate + "T" + patient.birthTime  + "+05:30"));

            birthDateExtension.appendChild(birthDateExtensionValue);
            birthDate.appendChild(birthDateExtension);
        }

        root.appendChild(birthDate);
    }

    private void appendAddressDetails(){
        Element address = new Element("address", xmlns);
        Element addressExtension = new Element("extension", xmlns);
        addressExtension.addAttribute(new Attribute("url", "https://sharedhealth.atlassian.net/wiki/display/docs/fhir-extensions#AddressCode"));
        Element addressExtensionValueString = new Element("valueString", xmlns);
        addressExtensionValueString.addAttribute(new Attribute("value", patient.addressCode));
        Element addressLine = new Element("line", xmlns);
        addressLine.addAttribute(new Attribute("value", patient.addressLine));
        Element country = new Element("country", xmlns);
        country.addAttribute(new Attribute("value", patient.countryCode));
        addressExtension.appendChild(addressExtensionValueString);
        address.appendChild(addressExtension);
        address.appendChild(addressLine);
        address.appendChild(country);
        root.appendChild(address);

    }

    private void appendMaritalStatus() {
        Element maritalStatus = new Element("maritalStatusCode", xmlns);
        Element coding = new Element("coding", xmlns);
        Element system = new Element("system", xmlns);
        system.addAttribute(new Attribute("value", xmlns + "/v3/getMaritalStatus"));
        Element code = new Element("code", xmlns);
        code.addAttribute(new Attribute("value", patient.maritalStatusCode));
        Element display = new Element("display");
        display.addAttribute(new Attribute("value", MaritalStatus.getMaritalStatus(patient.maritalStatusCode)));
        coding.appendChild(system);
        coding.appendChild(code);
        coding.appendChild(display);
        maritalStatus.appendChild(coding);
        root.appendChild(maritalStatus);
    }





    public String compose() throws ParsingException, IOException {

        if (patient.hasNameDetails()) this.appendNameDetails();
        if (patient.hasGenderDetails()) this.appendGenderDetails();
        if (patient.hasBirthDetails()) this.appendBirthDetails();
        if (patient.hasAddressDetails()) this.appendAddressDetails();
        Document patientDetails = new Document(root);
        Builder parser = new Builder();
        parser.build(patientDetails.toXML(), null);
        return patientDetails.toXML();

    }
}

