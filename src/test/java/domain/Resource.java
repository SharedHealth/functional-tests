package domain;

import nu.xom.Attribute;
import nu.xom.Element;

import java.util.UUID;

import static domain.PatientFHIRXMLComposer.xmlns;

public class Resource {

        public String getIdentifier() {
            return identifier;
        }

        private String identifier;
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
            Element resource = new Element("resource",xmlns);
            Element component = new Element(this.name, xmlns);
            resource.appendChild(component);
            Element identifier = new Element("identifier", xmlns);
            Element identifierValue = createSingleNode("value", fullUrl());
            identifier.appendChild(identifierValue);
            component.appendChild(identifier);
            return resource;
        }
    }
