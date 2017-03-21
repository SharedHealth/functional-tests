package domain;

import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import nu.xom.Element;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static domain.PatientFHIRXMLComposer.xmlns;

public class Composition extends Resource {
        ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
        private final String baseUrl = config.property.get("mci_registry");
        private final String facilityId = config.property.get("facility_id");

        public String hid;
        public String encounterId;
        public String complaintId;
        public Date date;
        private String confidentiality;

        public void setConfidentiality(String confidentiality) {
            this.confidentiality = confidentiality;
        }

        public Composition() {
            super("Composition");
          this.confidentiality = "N";

        }

        public Element create() {
            Element superParent = super.create();
            Element parent = superParent.getFirstChildElement(this.name, xmlns);

            //  Might modify date later
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.'000+05:30'");
            String format = simpleDateFormat.format(this.date);

            //date
            Element eventDate = createSingleNode("date", format);
            parent.appendChild(eventDate);

            //type
            Element type = createType();
            parent.appendChild(type);

            //title
            Element title = createSingleNode("title", "Patient Clinical Encounter");
            parent.appendChild(title);

            //status
            Element status = createSingleNode("status", "final");
            parent.appendChild(status);

            //confidential
            Element confidentiality = createSingleNode("confidential", this.confidentiality);
            parent.appendChild(confidentiality);

            //subject
            Element subject = createSubject();
            parent.appendChild(subject);

            //author
            Element author = new Element("author", xmlns);
            Element authorReference = createSingleNode("reference", config.property.get("hrm_server")+"/facilities/" + facilityId + ".json");
            author.appendChild(authorReference);
            parent.appendChild(author);

            //encounter
            Element encounter = createEncounter(this.encounterId);
            parent.appendChild(encounter);

            //section
            CompositionSectionValues encounterSection = new CompositionSectionValues(this.encounterId, "Encounter");
            CompositionSectionValues complaintSection = new CompositionSectionValues(this.complaintId, "Complaint");
            ArrayList<CompositionSectionValues> compositionSectionValues = new ArrayList<>();
            compositionSectionValues.add(encounterSection);
            compositionSectionValues.add(complaintSection);
            createSections(compositionSectionValues, parent);
            return superParent;

        }

        public Element createType() {
            Element type = new Element("type", xmlns);
            Element coding = new Element("coding", xmlns);
            Element system = createSingleNode("system", xmlns + "/vs/doc-typecodes");
            Element code = createSingleNode("code", config.property.get("code_id"));
            Element display = createSingleNode("display", "Details Document");
            coding.appendChild(system);
            coding.appendChild(code);
            coding.appendChild(display);
            type.appendChild(coding);
            return type;
        }

        public Element createSubject() {
            Element subject = new Element("subject", xmlns);
            Element reference = createSingleNode("reference", baseUrl + "/api/default/patients/" + hid);
            Element referenceDisplay = createSingleNode("display", hid);
            subject.appendChild(reference);
            subject.appendChild(referenceDisplay);
            return subject;
        }

        private Element createSections(ArrayList<CompositionSectionValues> sections, Element parent) {
            for (CompositionSectionValues sectionElement : sections) {
                Element section = new Element("section", xmlns);
                Element entry = new Element("entry", xmlns);
                section.appendChild(entry);
                Element reference = createSingleNode("reference", "urn:uuid:" + sectionElement.referenceValue);
                Element display = createSingleNode("display", sectionElement.displayValue);
                entry.appendChild(reference);
                entry.appendChild(display);
                parent.appendChild(section);
            }

            return parent;
        }
    }
