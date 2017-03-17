package domain;

import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static domain.PatientFHIRXMLComposer.xmlns;

public class Entry {

    public String fullUrl;
    public Resource resource;


    public Entry(String fullUrl, Resource resource) {
        this.fullUrl = fullUrl;
        this.resource = resource;
    }

    public Element create() {
        Element entry = new Element("entry");

        Element fullUrl = new Element("fullUrl");
        fullUrl.addAttribute(new Attribute("value", resource.fullUrl()));
        Element res = resource.create();
        entry.appendChild(fullUrl);
        entry.appendChild(res);
        return entry;
    }
}


    class SectionValues {
        String displayValue;
        String referenceValue;

        public SectionValues(String referenceValue, String displayValue) {
            this.displayValue = displayValue;
            this.referenceValue = referenceValue;
        }
    }

    class Resource {

        public String identifier;
        public String name;

        public Resource(String name) {
            this.name = name;
            this.identifier = UUID.randomUUID().toString();
        }


        public Element createEncounter(String uuid) {
            Element encounter = new Element("encounter", xmlns);
            Element reference = createSingleNode("reference", "urn:uuid:" + uuid);
            encounter.appendChild(reference);
            return encounter;
        }

        public Element createSingleNode(String name, String value) {
            Element element = new Element(name, xmlns);
            element.addAttribute(new Attribute("value", value));
            return element;
        }

        public String fullUrl() {
            return "urn:uuid:" + this.identifier;
        }

        public Element create() {
            Element resource = new Element("resource");
            Element component = new Element(this.name, xmlns);
            resource.appendChild(component);

            //identifier
            Element identifier = new Element("identifier", xmlns);
            Element identifierValue = createSingleNode("value", fullUrl());
            identifier.appendChild(identifierValue);
            component.appendChild(identifier);
            return resource;
        }
    }

    class Composition extends Resource {
        private String validHid;
        private final String facilityId;

        ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
        private final String baseUrl = config.property.get("mci_registry");
        private String encounterId;
        private String complaintId;

        public Composition(String name, String validHid, String facilityId, String encounterId, String complaintId) {
            super(name);
            this.encounterId = encounterId;
            this.complaintId = complaintId;
            this.name = name;
            this.validHid = validHid;
            this.facilityId = facilityId;
        }

        public Element create() {
            Element superParent = super.create();
            Element parent = superParent.getFirstChildElement(this.name, xmlns);

            //  Might modify date later
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.'000+05:30'");
            Date today = new Date();
            String format = simpleDateFormat.format(today);

            //date
            Element date = createSingleNode("date", format);
            parent.appendChild(date);

            //type
            Element type = createType();
            parent.appendChild(type);

            //title
            Element title = createSingleNode("title", "Patient Clinical Encounter");
            parent.appendChild(title);

            //status
            Element status = createSingleNode("status", "final");
            parent.appendChild(status);

            //confidentiality
            Element confidentiality = createSingleNode("confidentiality", "N");
            parent.appendChild(confidentiality);

            //subject
            Element subject = createSubject();
            parent.appendChild(subject);

            //author
            Element author = new Element("author", xmlns);
            Element authorReference = createSingleNode("reference", "http://172.21.2.184:8084/api/1.0/facilities/" + facilityId + ".json");
            author.appendChild(authorReference);
            parent.appendChild(author);

            //encounter
            Element encounter = createEncounter(encounterId);
            parent.appendChild(encounter);

            //section
            SectionValues encounterSection = new SectionValues(encounterId, "Encounter");
            SectionValues complaintSection = new SectionValues(complaintId, "Complaint");
            ArrayList<SectionValues> sectionValues = new ArrayList<>();
            sectionValues.add(encounterSection);
            sectionValues.add(complaintSection);
            createSections(sectionValues, parent);
            return superParent;

        }

        public Element createType() {
            Element type = new Element("type", xmlns);
            Element coding = new Element("coding", xmlns);
            Element system = createSingleNode("system", xmlns + "/vs/doc-typecodes");
            Element code = createSingleNode("code", "51899-3");
            Element display = createSingleNode("display", "Details Document");
            coding.appendChild(system);
            coding.appendChild(code);
            coding.appendChild(display);
            type.appendChild(coding);
            return type;
        }

        public Element createSubject() {
            Element subject = new Element("subject", xmlns);
            Element reference = createSingleNode("reference", baseUrl + "/api/default/patients/" + validHid);
            Element referenceDisplay = createSingleNode("display", validHid);
            subject.appendChild(reference);
            subject.appendChild(referenceDisplay);
            return subject;
        }

        private Element createSections(ArrayList<SectionValues> sections, Element parent) {
            for (SectionValues sectionElement : sections) {
                Element section = new Element("section", xmlns);
                Element entry = new Element("entry", xmlns);
                section.appendChild(entry);
                Element reference = createSingleNode("reference", sectionElement.referenceValue);
                Element display = createSingleNode("display", sectionElement.displayValue);
                entry.appendChild(reference);
                entry.appendChild(display);
                parent.appendChild(section);
            }

            return parent;
        }
    }

    class Encounter extends Resource {
        private String validHid;
        private final String facilityId;

        ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
        private final String baseUrl = config.property.get("mci_registry");

        public Encounter(String name, String validHid, String facilityId) {
            super(name);
            this.name = name;
            this.validHid = validHid;
            this.facilityId = facilityId;
        }

        public Element createPatient() {
            Element patient = new Element("patient", xmlns);
            Element reference = createSingleNode("reference", baseUrl+"/api/default/patients/" + validHid);
            Element display = createSingleNode("display", validHid);
            patient.appendChild(reference);
            patient.appendChild(display);
            return patient;
        }

        public Element create() {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.'000+05:30'");
            Date today = new Date();
            String format = simpleDateFormat.format(today);

            Element superParent = super.create();
            Element parent = superParent.getFirstChildElement("Encounter", xmlns);

            //status
            Element status = createSingleNode("status", "finished");
            parent.appendChild(status);

            //class
            Element classField = createSingleNode("class", "field");
            parent.appendChild(classField);

            //type
            Element type = new Element("type", xmlns);
            Element text = createSingleNode("text", "Consultation");
            type.appendChild(text);
            parent.appendChild(type);

            //patient
            Element patient = createPatient();
            parent.appendChild(patient);

            //participant
            Element participant = new Element("participant", xmlns);
            Element individual = new Element("individual", xmlns);
            Element individualReference = createSingleNode("reference", "http://172.21.2.184:8084/api/1.0/providers/27.json");
            individual.appendChild(individualReference);
            participant.appendChild(individual);
            parent.appendChild(participant);

            //period
            Element period = new Element("period", xmlns);
            Element start = createSingleNode("start", format);
            period.appendChild(start);
            parent.appendChild(period);

            //serviceProvider
            Element serviceProvider = new Element("serviceProvider", xmlns);
            Element serviceProviderRef = createSingleNode("reference", "http://172.21.2.184:8084/api/1.0/facilities/" + facilityId + ".json");
            serviceProvider.appendChild(serviceProviderRef);
            parent.appendChild(serviceProvider);

            return superParent;
        }

    }

    class Condition extends Resource {

        private String validHid;
        ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
        private final String baseUrl = config.property.get("mci_registry");
        private String codeId;
        private String encounterId;


        public Condition(String name, String validHid, String codeId, String encounterId) {
            super(name);
            this.codeId = codeId;
            this.encounterId = encounterId;
            this.name = name;
            this.validHid = validHid;
        }


        public Element createPatient() {
            Element patient = new Element("patient", xmlns);
            Element reference = createSingleNode("reference", baseUrl+"/api/default/patients/"+validHid);
            Element display = createSingleNode("display", validHid);
            patient.appendChild(reference);
            patient.appendChild(display);
            return patient;
        }

        public Element create() {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.'000+05:30'");
            Date today = new Date();
            String formatedDate = simpleDateFormat.format(today);

            Element superParent = super.create();
            Element parent = superParent.getFirstChildElement(this.name, xmlns);

            //patient
            Element patient = createPatient();
            parent.appendChild(patient);

            //encounter
            Element encounter = createEncounter(encounterId);
            parent.appendChild(encounter);

            //asserter
            Element asserter = new Element("asserter", xmlns);
            Element asserterReference = createSingleNode("reference", "http://172.21.2.184:8084/api/1.0/providers/27.json");
            asserter.appendChild(asserterReference);
            parent.appendChild(asserter);

            //code
            Element code = createCode();
            parent.appendChild(code);

            //category
            Element category = createCategory();
            parent.appendChild(category);

            //clinicalStatus
            Element clinicalStatus = createSingleNode("clinicalStatus", "active");
            parent.appendChild(clinicalStatus);

            //verificationStatus
            Element verificationStatus = createSingleNode("verificationStatus", "provisional");
            parent.appendChild(verificationStatus);

            //onsetPeriod
            Element onsetPeriod = new Element("onsetPeriod", xmlns);
            Element start = createSingleNode("start", formatedDate);
            Element end = createSingleNode("end", formatedDate);
            onsetPeriod.appendChild(start);
            onsetPeriod.appendChild(end);
            parent.appendChild(onsetPeriod);

            return superParent;
        }

        private Element createCategory() {
            Element category = new Element("category", xmlns);
            Element coding = new Element("coding", xmlns);

            Element system = createSingleNode("system", xmlns+"/condition-category");
            Element codeElement = createSingleNode("code", "complaint");
            coding.appendChild(system);
            coding.appendChild(codeElement);
            category.appendChild(coding);
            return category;
        }

        private Element createCode() {
            Element code = new Element("code", xmlns);
            Element coding = new Element("coding", xmlns);

            Element system = createSingleNode("system", "http://tr-dev.twhosted.com/openmrs/ws/rest/v1/tr/concepts/"+codeId);
            Element codeElement = createSingleNode("code", codeId);
            Element displayElement = createSingleNode("display", "Fever");
            coding.appendChild(system);
            coding.appendChild(codeElement);
            coding.appendChild(displayElement);
            code.appendChild(coding);
            return code;
        }
    }
