package domain;

import nu.xom.Attribute;
import nu.xom.Element;

import java.text.SimpleDateFormat;
import java.util.Date;

import static domain.PatientFHIRXMLComposer.xmlns;

public class MetaInfo {
    public Date date;

    public Element createMetaInfo(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.'000+05:30'");
        String formatedDate = simpleDateFormat.format(this.date);

        Element meta = new Element("meta",xmlns);
        Element lastUpdated = new Element("lastUpdated", xmlns);
        lastUpdated.addAttribute(new Attribute("value",formatedDate));
        meta.appendChild(lastUpdated);
        return meta;
    }
}
