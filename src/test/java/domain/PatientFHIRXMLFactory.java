package domain;

import nu.xom.*;

import java.io.IOException;

/**
 * Created by rajeshvaran on 11/18/16.
 */
public class PatientFHIRXMLFactory {

    private PatientFHIRXMLComposer composer;
    public  String xmlns;



    public PatientFHIRXMLFactory() {
        this.xmlns = PatientFHIRXMLComposer.xmlns;
        this.composer = new PatientFHIRXMLComposer();
    }

    public String withValidXML(Patient patient) throws ParsingException, IOException {
        Document doc = getDocument(patient);
        return doc.toXML();
    }
    public String withUnknownAttributeForGenderInXML(Patient patient) throws ParsingException, IOException {

        Document doc = getDocument(patient);
        doc.getRootElement().getFirstChildElement("gender", xmlns).addAttribute(new Attribute("newAttribute", "somevalue"));
        return doc.toXML();
    }

    public String withInvalidGenderInXML(Patient patient) throws ParsingException, IOException {
        Document doc = getDocument(patient);
        doc.getRootElement().getFirstChildElement("gender", xmlns).getAttribute("value").setValue("random");
        return  doc.toXML();
    }

    public String withMultipleGenderElementsInXML(Patient patient) throws ParsingException, IOException {
        Document doc = getDocument(patient);
        doc.getRootElement().appendChild(doc.getRootElement().getFirstChildElement("gender", xmlns).copy());
        return doc.toXML();
    }

    public String withUnknownElementInXML(Patient patient) throws ParsingException, IOException {
        Document doc = getDocument(patient);
        Element root = doc.getRootElement();
        Element someElement = new Element("someElement");
        Attribute someElementValue = new Attribute("value", "somevalue");
        someElement.addAttribute((someElementValue));
        root.appendChild(someElement);
        return doc.toXML();

    }

    public String withMissingRequiredDataInXML(Patient patient) throws ParsingException, IOException {
        Document doc = getDocument(patient);
        Element root = doc.getRootElement();
        root.removeChildren();
        Element maritalStatus = new Element("maritalStatus", xmlns);
        Element coding = new Element("coding", xmlns);
        Element system = new Element("system", xmlns);
        system.addAttribute(new Attribute("value", "http://hl7.org/fhir/v3/MaritalStatus"));
        Element code = new Element("code", xmlns);
        code.addAttribute(new Attribute("value", "M"));
        Element display = new Element("display", xmlns);
        display.addAttribute(new Attribute("value", "Married"));
        Element text = new Element("text", xmlns);
        text.addAttribute(new Attribute("value", "Getrouwd"));
        coding.appendChild(system);
        coding.appendChild(code);
        coding.appendChild(display);
        maritalStatus.appendChild(coding);
        maritalStatus.appendChild(text);
        root.appendChild(maritalStatus);

        return doc.toXML();

    }

    public String withDuplicateNameDataInXML(Patient patient) throws ParsingException, IOException {
        Document doc = getDocument(patient);
        doc.getRootElement().appendChild(doc.getRootElement().getFirstChildElement("name", xmlns).copy());
        return doc.toXML();


    }

    private Document getDocument(Patient patient) throws ParsingException, IOException {
        this.composer.setPatient(patient);
        Builder parser = new Builder();
        return parser.build(this.composer.compose(), null);
    }

}
