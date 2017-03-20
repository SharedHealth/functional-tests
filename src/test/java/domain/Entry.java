package domain;

import nu.xom.Attribute;
import nu.xom.Element;

import static domain.PatientFHIRXMLComposer.xmlns;

public class Entry {

    public Resource resource;

    public Entry(Resource resource) {

        this.resource = resource;
    }

    public Element create() {
        Element entry = new Element("entry",xmlns);

        Element fullUrl = new Element("fullUrl",xmlns);
        fullUrl.addAttribute(new Attribute("value", "urn:uuid:" + resource.getIdentifier()));
        Element res = resource.create();
        entry.appendChild(fullUrl);
        entry.appendChild(res);
        return entry;
    }
}