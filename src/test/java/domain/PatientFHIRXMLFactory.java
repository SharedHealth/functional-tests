package domain;

//import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
//import ca.uhn.fhir.model.primitive.InstantDt;
import nu.xom.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

class MetaInfo {
    String lastUpdated;

//    public MetaInfo() {
//        this.lastUpdated = new InstantDt(new Date(), TemporalPrecisionEnum.MILLI).toString();
//    }
}

class Bundle {
    String id;
    MetaInfo metaInfo;
    String type;
    ArrayList <Entry> entry;

    public Bundle() {
        this.id = UUID.randomUUID().toString();
        this.metaInfo = new MetaInfo();
        this.type = "collection";
        this.entry = entry;
    }

    public void createBundle(){
//        Entry entry = new Entry();
//        Composition composition = new Composition("Composition");
//        String compose = composition.compose();
//        System.out.println("hello"+compose);
    }

    public static void main(String[] args) {
        String encounterId = UUID.randomUUID().toString();
        String codeId = UUID.randomUUID().toString();
        String complaintId = UUID.randomUUID().toString();
        Composition composition = new Composition("Composition", "98000106461", "10019842", encounterId,complaintId);
//        Condition composition = new Condition("Condition","98000106461",codeId,uuid);
        Entry entry = new Entry(encounterId, composition);
        Element element1 = entry.create();
        Document document = new Document(element1);
        System.out.println("document"+document.toXML());

    }

}


 class BundleFactory {

}

/**
 * dated by rajeshvaran on 11/18/16.
 */
public class PatientFHIRXMLFactory {

    private PatientFHIRXMLComposer composer;
    public String xmlns;


    public PatientFHIRXMLFactory(String baseUrl) {
        this.xmlns = PatientFHIRXMLComposer.xmlns;
        this.composer = new PatientFHIRXMLComposer(baseUrl);
    }

    public String withValidXML(Patient patient) throws ParsingException, IOException {
        Document doc = getDocument(patient);
        return doc.toXML();
    }

    public String withUnknownAttributeForGenderInXML(Patient patient) throws ParsingException, IOException {
        Document doc = getDocument(patient);
        Element patientElement = findPatientElement(doc);
        Element genderElement = patientElement.getFirstChildElement("gender", xmlns);
        genderElement.removeAttribute(genderElement.getAttribute(0));
        genderElement.addAttribute(new Attribute("newAttribute", "somevalue"));
        return doc.toXML();
    }

    public String withInvalidGenderInXML(Patient patient) throws ParsingException, IOException {
        Document doc = getDocument(patient);
        findPatientElement(doc).getFirstChildElement("gender", xmlns)
                .getAttribute("value").setValue("random");
        return doc.toXML();
    }

    public String withMultipleGenderElementsInXML(Patient patient) throws ParsingException, IOException {
        Document doc = getDocument(patient);
        Element patientElement = findPatientElement(doc);
        patientElement.appendChild(patientElement.getFirstChildElement("gender", xmlns).copy());
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
        Element patientElement = findPatientElement(doc);
        patientElement.removeChildren();
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
        patientElement.appendChild(maritalStatus);

        return doc.toXML();

    }

    public String withDuplicateNameDataInXML(Patient patient) throws ParsingException, IOException {
        Document doc = getDocument(patient);
        Element patientElement = findPatientElement(doc);
        patientElement.appendChild(patientElement.getFirstChildElement("name", xmlns).copy());
        return doc.toXML();


    }

    private Document getDocument(Patient patient) throws ParsingException, IOException {
        this.composer.setPatient(patient);
        Builder parser = new Builder();
        return parser.build(this.composer.composePatient(), null);
    }

    private Element findPatientElement(Document document) {
        return document.getRootElement().getFirstChildElement("entry", xmlns)
                .getFirstChildElement("resource", xmlns).getFirstChildElement("Patient", xmlns);
    }

}
