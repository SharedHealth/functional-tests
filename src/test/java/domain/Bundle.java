package domain;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static domain.PatientFHIRXMLComposer.xmlns;

public class Bundle {
    String id;
    MetaInfo metaInfo;
    String type;
    private ArrayList<Entry> entry;

    public Bundle() {
        this.id = UUID.randomUUID().toString();
        this.metaInfo = new MetaInfo();
        this.metaInfo.date = new Date();
        this.type = "collection";
        this.entry = new ArrayList<Entry>();

    }

    public String asXML()
    {
        Element element = this.create();
        Document document = new Document(element);
        return document.toXML();
    }

    public Element create(){

        Element bundle = new Element("Bundle", xmlns);

        Element idElement = new Element("id",xmlns);
        idElement.addAttribute(new Attribute("value",this.id));
        bundle.appendChild(idElement);

        Element metaInfo = this.metaInfo.createMetaInfo();
        bundle.appendChild(metaInfo);

        Element type = new Element("type",xmlns);
        type.addAttribute(new Attribute("value","collection"));
        bundle.appendChild(type);

        for (Entry singleEntry : this.entry) {
            bundle.appendChild(singleEntry.create());
        }

       return bundle;

    }

    public void addEntry(Entry entry){
        this.entry.add(entry);

    }

}
