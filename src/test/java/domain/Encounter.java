package domain;

import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import nu.xom.Element;

import java.text.SimpleDateFormat;
import java.util.Date;

import static domain.PatientFHIRXMLComposer.xmlns;

public class Encounter extends Resource {
        ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
        private final String baseUrl = config.property.get("mci_registry");

        public String hid;
        public final String facilityId = config.property.get("facility_id");
        public Date date;
        public String provider_id;

        public Encounter() {
            super("Encounter");
        }

        public Element createPatient() {
            Element patient = new Element("patient", xmlns);
            Element reference = createSingleNode("reference", baseUrl+"/api/default/patients/" + this.hid);
            Element display = createSingleNode("display", this.hid);
            patient.appendChild(reference);
            patient.appendChild(display);
            return patient;
        }

        public Element create() {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.'000+05:30'");
            String format = simpleDateFormat.format(this.date);

            Element superParent = super.create();
            Element parent = superParent.getFirstChildElement(this.name, xmlns);

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
            Element individualReference = createSingleNode("reference", config.property.get("hrm_server") + "/providers/" + this.provider_id + ".json");
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
            Element serviceProviderRef = createSingleNode("reference",  config.property.get("hrm_server") + "/facilities/" + this.facilityId + ".json");
            serviceProvider.appendChild(serviceProviderRef);
            parent.appendChild(serviceProvider);

            return superParent;
        }

    }
