package domain;

import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import nu.xom.Element;

import java.text.SimpleDateFormat;
import java.util.Date;

import static domain.PatientFHIRXMLComposer.xmlns;

public class Condition extends Resource {

        private final ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
        private final String baseUrl = config.property.get("mci_registry");
        public String codeId;
        public String encounterId;
        public String hid;
        public Date date;
        public String provider_id;
        public String code_name;


        public Condition() {
            super("Condition");
        }


        public Element createPatient() {
            Element patient = new Element("patient", xmlns);
            Element reference = createSingleNode("reference", baseUrl+"/api/default/patients/"+this.hid);
            Element display = createSingleNode("display", this.hid);
            patient.appendChild(reference);
            patient.appendChild(display);
            return patient;
        }

        public Element create() {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.'000+05:30'");
            String formatedDate = simpleDateFormat.format(date);

            Element superParent = super.create();
            Element parent = superParent.getFirstChildElement(this.name, xmlns);

            //patient
            Element patient = createPatient();
            parent.appendChild(patient);

            //encounter
            Element encounter = createEncounter(this.encounterId);
            parent.appendChild(encounter);

            //asserter
            Element asserter = new Element("asserter", xmlns);
            Element asserterReference = createSingleNode("reference", config.property.get("hrm_server")+"/providers/"+this.provider_id+".json");
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

            Element system = createSingleNode("system", config.property.get("tr_server")+"/openmrs/ws/rest/v1/tr/concepts/"+this.codeId);
            Element codeElement = createSingleNode("code", this.codeId);
            Element displayElement = createSingleNode("display", this.code_name);
            coding.appendChild(system);
            coding.appendChild(codeElement);
            coding.appendChild(displayElement);
            code.appendChild(coding);
            return code;
        }
    }
