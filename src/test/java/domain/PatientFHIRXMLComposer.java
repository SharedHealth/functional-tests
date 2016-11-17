package domain;

import nu.xom.*;

import java.io.IOException;

public class PatientFHIRXMLComposer {
    private Element root;
    public static final String xmlns = "http://hl7.org/fhir";
    private Patient patient;

    public PatientFHIRXMLComposer(Patient patient) {
        this.root = new Element("Patient",xmlns);
        this.patient = patient;
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

class PatientFHIRXMLData {

    private Patient patient;
    public  String xmlns;

    public PatientFHIRXMLData(Patient patient) {
        this.patient = patient;
        this.xmlns = PatientFHIRXMLComposer.xmlns;
    }

    public String withUnknownAttributeForGenderInXML() throws ParsingException, IOException {

        Builder parser = new Builder();
        Document doc = parser.build(patient.asXML(), null);
        doc.getRootElement().getFirstChildElement("gender", xmlns).addAttribute(new Attribute("newAttribute", "somevalue"));
        return doc.toXML();
    }

    public String withInvalidGenderInXML() throws ParsingException, IOException {
        Builder parser = new Builder();
        Document doc = parser.build(patient.asXML(), null);
        doc.getRootElement().getFirstChildElement("gender", xmlns).getAttribute("value").setValue("random");
        return  doc.toXML();
    }

    public String withMultipleGenderElementsInXML() throws ParsingException, IOException {
        Builder parser = new Builder();
        Document doc = parser.build(patient.asXML(), null);
        doc.getRootElement().appendChild(doc.getRootElement().getFirstChildElement("gender", xmlns).copy());
        return doc.toXML();
    }

    public String withUnknownElementInXML() throws ParsingException, IOException {
        Builder parser = new Builder();
        Document doc = parser.build(patient.asXML(), null);
        Element root = doc.getRootElement();
        Element someElement = new Element("someElement");
        Attribute someElementValue = new Attribute("value", "somevalue");
        someElement.addAttribute((someElementValue));
        root.appendChild(someElement);
        return doc.toXML();

    }

    public String withMissingRequiredDataInXML() throws ParsingException, IOException {
        Builder parser = new Builder();
        Document doc = parser.build(patient.asXML(), null);
        Element root = doc.getRootElement();
        root.removeChildren();
        Element maritalStatus = new Element("maritalStatusCode", xmlns);
        Element coding = new Element("coding", xmlns);
        Element system = new Element("system", xmlns);
        system.addAttribute(new Attribute("value", "http://hl7.org/fhir/v3/getMaritalStatus"));
        Element code = new Element("code", xmlns);
        code.addAttribute(new Attribute("value", "M"));
        Element display = new Element("display");
        display.addAttribute(new Attribute("value", "Married"));
        Element text = new Element("text");
        text.addAttribute(new Attribute("value", "Getrouwd"));
        coding.appendChild(system);
        coding.appendChild(code);
        coding.appendChild(display);
        maritalStatus.appendChild(coding);
        maritalStatus.appendChild(text);
        root.appendChild(maritalStatus);

        return doc.toXML();

    }

    public String withDuplicateNameDataInXML() throws ParsingException, IOException {
        Builder parser = new Builder();
        Document doc = parser.build(patient.asXML(), null);
        doc.getRootElement().appendChild(doc.getRootElement().getFirstChildElement("name", xmlns).copy());
        return doc.toXML();


    }

}
