package domain;
import com.sun.tools.doclint.HtmlTag;
import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference;
import nu.xom.*;

import java.io.IOException;


public class Patient {

    public String given;
    public String family;
    public String gender;
    public String birthDate;
    private Address address;
    public String addressLine;
    public String addressCode;
    public String nid;
    private String hid;
    private String education;
    private String occupation;
    private String primaryContact;
    private String uid;
    private String fatherUid;
    private String fatherNid;
    private String fatherBRN;
    private String motherUid;
    private String motherNid;
    private String motherBRN;
    private String binBRN;
    public String countryCode;
    public String birthTime;

    public Patient()
    {

    }
    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }



    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }
    public String getGiven() {
        return given;
    }
    public String getFamily() {
        return family;
    }
    public String getGender() {
        return gender;
    }
    public String getBirthDate() {
        return birthDate;
    }
    public Address getAddress() {
        return address;
    }
    public String getNid() {
        return nid;
    }
    public String getHid() {
        return hid;
    }
    public String getEducation() {
        return education;
    }
    public String getOccupation() {
        return occupation;
    }
    public String getPrimaryContact() {
        return primaryContact;
    }
    public String getUid() {
        return uid;
    }
    public String getFatherUid() {
        return fatherUid;
    }
    public String getFatherNid() {
        return fatherNid;
    }
    public String getFatherBRN() {
        return fatherBRN;
    }
    public String getMotherUid() {
        return motherUid;
         }
    public String getMotherNid() {
        return motherNid;
    }
    public String getMotherBRN() {
        return motherBRN;
    }
    public String getBinBRN() {
        return binBRN;
    }


    public Patient(PatientBuilder patientBuilder) {
        this.given = patientBuilder.firstName;
        this.family = patientBuilder.lastName;
        this.gender = patientBuilder.gender;
        this.birthDate = patientBuilder.dateOfBirth;
        this. address = patientBuilder.address;
        this.nid = patientBuilder.nid;
        this.hid = patientBuilder.hid;
        this.education = patientBuilder.education;
        this.occupation = patientBuilder.occupation;
        this.primaryContact = patientBuilder.primaryContact;
        this.uid = patientBuilder.uid;
        this.fatherUid = patientBuilder.fatherUid;
        this.fatherNid = patientBuilder.fatherNid;
        this.fatherBRN = patientBuilder.fatherBRN;
        this.motherUid = patientBuilder.motherUid;
        this.motherNid = patientBuilder.motherNid;
        this.motherBRN = patientBuilder.motherBRN;
        this.binBRN = patientBuilder.binBRN;


    }

    public String withUnknowAttributeForGenderInXML() throws ParsingException, IOException {
        String xmlns = "http://hl7.org/fhir";
        Builder parser = new Builder();
        Document doc = parser.build(this.asXML(), null);
        doc.getRootElement().getFirstChildElement("gender", xmlns).addAttribute(new Attribute("newAttribute", "somevalue"));
        return doc.toXML();


    }

    public String withInvalidGenderInXML() throws ParsingException, IOException {
        String xmlns = "http://hl7.org/fhir";
        Builder parser = new Builder();
        Document doc = parser.build(this.asXML(), null);
        doc.getRootElement().getFirstChildElement("gender", xmlns).getAttribute("value").setValue("random");
        return  doc.toXML();





    }

    public String withMultipleGenderElementsInXML() throws ParsingException, IOException {
        String xmlns = "http://hl7.org/fhir";
        Builder parser = new Builder();
        Document doc = parser.build(this.asXML(), null);
        doc.getRootElement().appendChild(doc.getRootElement().getFirstChildElement("gender", xmlns).copy());
        return doc.toXML();
    }

    public String withUnknownElementInXML() throws ParsingException, IOException {
        Builder parser = new Builder();
        Document doc = parser.build(this.asXML(), null);
        Element root = doc.getRootElement();
        Element someElement = new Element("someElement");
        Attribute someElementValue = new Attribute("value", "somevalue");
        someElement.addAttribute((someElementValue));
        root.appendChild(someElement);
        return doc.toXML();

    }

    public String withMissingRequiredDataInXML() throws ParsingException, IOException {
        String xmlns = "http://hl7.org/fhir";
        Builder parser = new Builder();
        Document doc = parser.build(this.asXML(), null);
        Element root = doc.getRootElement();
        root.removeChildren();

        /*

          <maritalStatus>
        <coding>
            <system value="http://hl7.org/fhir/v3/MaritalStatus"/>
            <code value="M"/>
            <display value="Married"/>
        </coding>
        <text value="Getrouwd"/>
    </maritalStatus>
         */

        Element maritalStatus = new Element("maritalStatus", xmlns);
        Element coding = new Element("coding", xmlns);
        Element system = new Element("system", xmlns);
        system.addAttribute(new Attribute("value", "http://hl7.org/fhir/v3/MaritalStatus"));
        Element code = new Element("code", xmlns);
        code.addAttribute(new Attribute("value", "M"));
        Element display = new Element("display");
        display.addAttribute(new Attribute("value", "Married"));
        Element text = new Element("text");
        text.addAttribute(new Attribute("value", "Getrouwd"));
        coding.appendChild(system);
        coding.appendChild(code);
        coding.appendChild(display);
        maritalStatus.appendChild(coding);
        maritalStatus.appendChild(text);
        root.appendChild(maritalStatus);

        return doc.toXML();

    }

    public String withDuplicateNameDataInXML() throws ParsingException, IOException {
        String xmlns = "http://hl7.org/fhir";
        Builder parser = new Builder();
        Document doc = parser.build(this.asXML(), null);
        doc.getRootElement().appendChild(doc.getRootElement().getFirstChildElement("name", xmlns).copy());
        return doc.toXML();


    }
    public String asXML() throws ParsingException, IOException {

        /*

            <Patient xmlns="http://hl7.org/fhir">
         */
        Element root;
        String xmlns = "http://hl7.org/fhir";
        root = new Element("Patient", xmlns);
        


        /*
        <name>
        <family value="Raichand"/>
        <given value="Yashvardhan"/>
        </name>
        */

        Element name = new Element("name", xmlns);
        Element family = new Element("family", xmlns);
        family.addAttribute(new Attribute("value", this.family));
        Element given = new Element("given", xmlns);
        given.addAttribute(new Attribute("value", this.given));
        name.appendChild(family);
        name.appendChild(given);
        root.appendChild(name);


/*         <gender value="male"/> */

        Element gender = new Element("gender", xmlns);
        gender.addAttribute(new Attribute("value", this.gender));
        root.appendChild(gender);


/*
        <birthDate value="1976-01-12">
            <extension url="http://hl7.org/fhir/StructureDefinition/patient-birthTime">
                <valueDateTime value="1976-01-12T16:50:00+05:30"/>
            </extension>
        </birthDate>
 */


/*        <birthDate value="1976-01-12">

        </birthDate>*/

        Element birthDate = new Element("birthDate", xmlns);
        birthDate.addAttribute(new Attribute("value", this.birthDate));

        if(this.hasBirthTime())
        {
            Element birthDateExtension = new Element("extension", xmlns);
            birthDateExtension.addAttribute(new Attribute("url", "http://hl7.org/fhir/StructureDefinition/patient-birthTime"));
            Element birthDateExtensionValue = new Element("valueDateTime", xmlns);
            birthDateExtensionValue.addAttribute(new Attribute("value", this.birthDate + "T" + this.birthTime  + "+05:30"));

            birthDateExtension.appendChild(birthDateExtensionValue);
            birthDate.appendChild(birthDateExtension);

        }

        root.appendChild(birthDate);

        /*

            <address>
                <extension url="https://sharedhealth.atlassian.net/wiki/display/docs/fhir-extensions#AddressCode">
                    <valueString value="201918991101"/>
                </extension>
                <line value="3rd lane"/>
                <country value="050"/>
            </address>
            </Patient>
         */

        Element address = new Element("address", xmlns);
        Element addressExtension = new Element("extension", xmlns);
        addressExtension.addAttribute(new Attribute("url", "https://sharedhealth.atlassian.net/wiki/display/docs/fhir-extensions#AddressCode"));
        Element addressExtensionValueString = new Element("valueString", xmlns);
        addressExtensionValueString.addAttribute(new Attribute("value", this.addressCode));
        Element addressLine = new Element("line", xmlns);
        addressLine.addAttribute(new Attribute("value", this.addressLine));
        Element country = new Element("country", xmlns);
        country.addAttribute(new Attribute("value", this.countryCode));
        addressExtension.appendChild(addressExtensionValueString);
        address.appendChild(addressExtension);
        address.appendChild(addressLine);
        address.appendChild(country);
        root.appendChild(address);


        Document patientDetails = new Document(root);
        Builder parser = new Builder();
        parser.build(patientDetails.toXML(), null);
        return patientDetails.toXML();

    }

    boolean hasBirthTime() {

        return this.birthTime != null;
    }


    public static class PatientBuilder {

        private String firstName;
        private String lastName;
        private String gender;
        private String dateOfBirth;
        private Address address;
        private String nid;
        private String hid;
        private String education;
        private String occupation;
        private String primaryContact;
        private String uid;
        private String fatherUid;
        private String fatherNid;
        private String fatherBRN;
        private String motherUid;
        private String motherNid;
        private String motherBRN;
        private String binBRN;


        public PatientBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public PatientBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public PatientBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }
        public PatientBuilder dateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }
        public PatientBuilder address(Address address) {
            this.address = address;
            return this;
        }
        public PatientBuilder nid(String nid) {
            this.nid = nid;
            return this;
        }
        public PatientBuilder hid(String hid) {
            this.hid = hid;
            return this;
        }
        public PatientBuilder education(String education) {
            this.education = education;
            return this;
        }
        public PatientBuilder occupation(String occupation) {
            this.occupation = occupation;
            return this;
        }
        public PatientBuilder primaryContact(String primaryContact) {
            this.primaryContact = primaryContact;
            return this;
        }
        public PatientBuilder uid(String uid) {
            this.uid= uid;
            return this;
        }
        public PatientBuilder fatherUid(String fatherUid) {
            this.fatherUid = fatherUid;
            return this;
        }
        public PatientBuilder fatherNid(String fatherNid) {
            this.fatherNid = fatherNid;
            return this;
        }
        public PatientBuilder fatherBRN(String fatherBRN) {
            this.fatherBRN = fatherBRN;
            return this;
        }
        public PatientBuilder motherUid(String motherUid) {
            this.motherUid = motherUid;
            return this;
        }
        public PatientBuilder motherNid(String motherNid) {
            this.motherNid = motherNid;
            return this;
        }
        public PatientBuilder motherBRN(String motherBRN) {
            this.motherBRN = motherBRN;
            return this;
        }
        public PatientBuilder binBRN(String binBRN) {
            this.binBRN = binBRN;
            return this;
        }


        public Patient build() {
            return new Patient(this);
        }
    }
}

class PatientXMLBuilder {
    Patient patient;
    String xmlns;
    Element root;

    public PatientXMLBuilder(Patient patient) {
        this.patient = patient;
        this.xmlns = "http://hl7.org/fhir";

    }

    private void createRootElement()
    {
         root = new Element("Patient", xmlns);

    }

    public String composeAsXML() {
        this.compose();
        Document document = new Document(root);
        return document.toXML();

    }

    private void compose() {
        this.createRootElement();


    }

    public String asXML() throws ParsingException, IOException {

        /*

            <Patient xmlns="http://hl7.org/fhir">
         */

        String xmlns = "http://hl7.org/fhir";



        /*
        <name>
        <family value="Raichand"/>
        <given value="Yashvardhan"/>
        </name>
        */

        Element name = new Element("name", xmlns);
        Element family = new Element("family", xmlns);
        family.addAttribute(new Attribute("value", this.patient.family));
        Element given = new Element("given", xmlns);
        given.addAttribute(new Attribute("value", this.patient.given));
        name.appendChild(family);
        name.appendChild(given);
        root.appendChild(name);


/*         <gender value="male"/> */

        Element gender = new Element("gender", xmlns);
        gender.addAttribute(new Attribute("value", this.patient.gender));
        root.appendChild(gender);


/*
        <birthDate value="1976-01-12">
            <extension url="http://hl7.org/fhir/StructureDefinition/patient-birthTime">
                <valueDateTime value="1976-01-12T16:50:00+05:30"/>
            </extension>
        </birthDate>
 */


/*        <birthDate value="1976-01-12">

        </birthDate>*/

        Element birthDate = new Element("birthDate", xmlns);
        birthDate.addAttribute(new Attribute("value", this.patient.birthDate));

        if(this.patient.hasBirthTime())
        {
            Element birthDateExtension = new Element("extension", xmlns);
            birthDateExtension.addAttribute(new Attribute("url", "http://hl7.org/fhir/StructureDefinition/patient-birthTime"));
            Element birthDateExtensionValue = new Element("valueDateTime", xmlns);
            birthDateExtensionValue.addAttribute(new Attribute("value", this.patient.birthDate + "T" + this.patient.birthTime  + "+05:30"));

            birthDateExtension.appendChild(birthDateExtensionValue);
            birthDate.appendChild(birthDateExtension);

        }

        root.appendChild(birthDate);

        /*

            <address>
                <extension url="https://sharedhealth.atlassian.net/wiki/display/docs/fhir-extensions#AddressCode">
                    <valueString value="201918991101"/>
                </extension>
                <line value="3rd lane"/>
                <country value="050"/>
            </address>
            </Patient>
         */

        Element address = new Element("address", xmlns);
        Element addressExtension = new Element("extension", xmlns);
        addressExtension.addAttribute(new Attribute("url", "https://sharedhealth.atlassian.net/wiki/display/docs/fhir-extensions#AddressCode"));
        Element addressExtensionValueString = new Element("valueString", xmlns);
        addressExtensionValueString.addAttribute(new Attribute("value", this.patient.addressCode));
        Element addressLine = new Element("line", xmlns);
        addressLine.addAttribute(new Attribute("value", this.patient.addressLine));
        Element country = new Element("country", xmlns);
        country.addAttribute(new Attribute("value", this.patient.countryCode));
        addressExtension.appendChild(addressExtensionValueString);
        address.appendChild(addressExtension);
        address.appendChild(addressLine);
        address.appendChild(country);
        root.appendChild(address);


        Document patientDetails = new Document(root);
        Builder parser = new Builder();
        parser.build(patientDetails.toXML(), null);
        return patientDetails.toXML();

    }



}