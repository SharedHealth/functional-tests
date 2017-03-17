package domain;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;

import static domain.PatientFHIRXMLComposer.xmlns;

public class Entry {
    
}

class Resource {

    public Element createEntry(String url,String componentType){
        Element entry = new Element("entry");

        Element fullUrl = new Element("fullUrl");
        fullUrl.addAttribute(new Attribute("value",url));
        Element resource = new Element("resource");
        Element component = new Element(componentType, xmlns);

        //identifier
        Element identifier = new Element("identifier", xmlns);
        Element identifierValue = createSingleNode("value", url);
        identifier.appendChild(identifierValue);

        component.appendChild(identifier);
        resource.appendChild(component);
        entry.appendChild(fullUrl);
        entry.appendChild(resource);
        return entry;
    }

    public Element createPatient(){
        Element patient = new Element("patient", xmlns);
        Element reference = createSingleNode("reference", "http://mci-dev.twhosted.com/api/default/patients/98000106461");
        Element display = createSingleNode("display", "98000106461");
        patient.appendChild(reference);
        patient.appendChild(display);
        return patient;
    }

    public Element createEncounter(){
        Element encounter = new Element("encounter",xmlns);
        Element reference = createSingleNode("reference", "urn:uuid:f5f1e736-a8ac-41d3-b839-644f575002ed");
        encounter.appendChild(reference);
        return encounter;
    }

    public Element createSingleNode(String name, String value){
        Element element = new Element(name,xmlns);
        element.addAttribute(new Attribute("value",value));
        return element;
    }
}

class Composition extends Resource {
    public String compose(){
        //date
        Element date = createSingleNode("date", "2017-03-14T11:35:42.000+05:30");

        //type
        Element type = new Element("type",xmlns);
        Element coding = new Element("coding",xmlns);
        Element system = createSingleNode("system", xmlns + "/vs/doc-typecodes");
        Element code = createSingleNode("code", "51899-3");
        Element display = createSingleNode("display", "Details Document");
        coding.appendChild(system);
        coding.appendChild(code);
        coding.appendChild(display);
        type.appendChild(coding);

        //title
        Element title = createSingleNode("title", "Patient Clinical Encounter");

        //status
        Element status = createSingleNode("status", "final");

        //confidentiality
        Element confidentiality = createSingleNode("confidentiality", "N");

        //subject
        Element subject = new Element("subject",xmlns);
        Element reference = createSingleNode("reference", "http://mci-dev.twhosted.com/api/default/patients/98000106461");
        Element referenceDisplay = createSingleNode("display", "98000106461");
        subject.appendChild(reference);
        subject.appendChild(referenceDisplay);

        //author
        Element author = new Element("author",xmlns);
        Element authorReference = createSingleNode("reference", "http://172.21.2.184:8084/api/1.0/facilities/10019842.json");
        author.appendChild(authorReference);

        //encounter
        Element encounter = createEncounter();

        //section
        String encounterEntry = "urn:uuid:f5f1e736-a8ac-41d3-b839-644f575002ed";
        String encounterDisplay = "Encounter";
        Element encounterSection = createSection(encounterEntry, encounterDisplay);

        String complaintEntry = "urn:uuid:fcf4d046-b709-433c-b381-44136c45ab7b";
        String complaintDisplay = "Complaint";
        Element complaintSection = createSection(complaintEntry, complaintDisplay);

        String fullUrl = "urn:uuid:49b38010-e850-4ac7-9c39-488f1a0a11a9";
        Element entry = createEntry(fullUrl,"Composition");

        Document document = new Document(entry);
        Element composition = document.getRootElement().getFirstChildElement("resource").getFirstChildElement("Composition",xmlns);
        composition.appendChild(date);
        composition.appendChild(type);
        composition.appendChild(title);
        composition.appendChild(status);
        composition.appendChild(confidentiality);
        composition.appendChild(subject);
        composition.appendChild(author);
        composition.appendChild(encounter);
        composition.appendChild(encounterSection);
        composition.appendChild(complaintSection);

        return document.toXML();
    }

    private Element createSection(String entryValue, String displayValue) {
        Element section = new Element("section",xmlns);
        Element entry = new Element("entry",xmlns);
        Element reference = createSingleNode("reference", entryValue);
        Element display = createSingleNode("display", displayValue);
        entry.appendChild(reference);
        entry.appendChild(display);
        section.appendChild(entry);
        return section;
    }
}

class Encounter extends Resource{
    public String compose()
    {
        //status
        Element status = createSingleNode("status", "finished");

        //class
        Element classField = createSingleNode("class", "field");

        //type
        Element type = new Element("type", xmlns);
        Element text = createSingleNode("text", "Consultation");
        type.appendChild(text);

        //patient
        Element patient = createPatient();

        //participant
        Element participant = new Element("participant", xmlns);
        Element individual = new Element("individual", xmlns);
        Element individualReference = createSingleNode("reference", "http://172.21.2.184:8084/api/1.0/providers/27.json");
        individual.appendChild(individualReference);
        participant.appendChild(individual);

        //period
        Element period = new Element("period", xmlns);
        Element start = createSingleNode("start", "2017-03-14T11:31:57.000+05:30");
        period.appendChild(start);

        //serviceProvider
        Element serviceProvider = new Element("serviceProvider", xmlns);
        Element serviceProviderRef = createSingleNode("reference", "http://172.21.2.184:8084/api/1.0/facilities/10019842.json");
        serviceProvider.appendChild(serviceProviderRef);

        Element entry = createEntry("urn:uuid:f5f1e736-a8ac-41d3-b839-644f575002ed","Encounter");
        Document document = new Document(entry);
        Element encounter = document.getRootElement().getFirstChildElement("resource").getFirstChildElement("Encounter",xmlns);

        encounter.appendChild(status);
        encounter.appendChild(classField);
        encounter.appendChild(type);
        encounter.appendChild(patient);
        encounter.appendChild(participant);
        encounter.appendChild(period);
        encounter.appendChild(serviceProvider);

        return document.toXML();
    }

}

class Condition extends Resource{
    public String compose(){

        //patient
        Element patient = createPatient();

        //encounter
        Element encounter = createEncounter();

        //asserter
        Element asserter = new Element("asserter",xmlns);
        Element asserterReference = createSingleNode("reference", "http://172.21.2.184:8084/api/1.0/providers/27.json");
        asserter.appendChild(asserterReference);

        //code
        Element code = createCode();

        //category
        Element category = createCategory();

        //clinicalStatus
        Element clinicalStatus = createSingleNode("clinicalStatus", "active");

        //verificationStatus
        Element verificationStatus = createSingleNode("verificationStatus","provisional");

        //onsetPeriod
        Element onsetPeriod = new Element("onsetPeriod", xmlns);
        Element start = createSingleNode("start", "2017-03-11T11:35:42.000+05:30");
        Element end = createSingleNode("end", "2017-03-11T11:35:42.000+05:30");
        onsetPeriod.appendChild(start);
        onsetPeriod.appendChild(end);

        Element entry = createEntry("urn:uuid:fcf4d046-b709-433c-b381-44136c45ab7b","Condition");
        Document document = new Document(entry);
        Element condition = document.getRootElement().getFirstChildElement("resource").getFirstChildElement("Condition",xmlns);

        condition.appendChild(patient);
        condition.appendChild(encounter);
        condition.appendChild(asserter);
        condition.appendChild(code);
        condition.appendChild(category);
        condition.appendChild(clinicalStatus);
        condition.appendChild(verificationStatus);
        condition.appendChild(onsetPeriod);

        return document.toXML();
    }

    private Element createCategory() {
        Element category = new Element("category",xmlns);
        Element coding = new Element("coding",xmlns);

        Element system = createSingleNode("system","http://hl7.org/fhir/condition-category");
        Element codeElement = createSingleNode("code","complaint");
        coding.appendChild(system);
        coding.appendChild(codeElement);
        category.appendChild(coding);
        return category;
    }

    private Element createCode() {
        Element code = new Element("code",xmlns);
        Element coding = new Element("coding",xmlns);

        Element system = createSingleNode("system","http://tr-dev.twhosted.com/openmrs/ws/rest/v1/tr/concepts/d587c115-82f3-11e5-b875-0050568225ca");
        Element codeElement = createSingleNode("code","d587c115-82f3-11e5-b875-0050568225ca");
        Element displayElement = createSingleNode("display","Fever");
        coding.appendChild(system);
        coding.appendChild(codeElement);
        coding.appendChild(displayElement);
        code.appendChild(coding);
        return code;
    }
}