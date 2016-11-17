package domain;
import nu.xom.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class MaritalStatus {
    public static Map<String, String> code = new HashMap<String, String>();

    public static String getMaritalStatus(String statusCode) {
        code.put("M", "Married");
        return code.get(statusCode);
    }
}

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
    public String maritalStatusCode;
    private PatientFHIRXMLData fhirxmlData ;
    public Patient()
    {
        fhirxmlData = new PatientFHIRXMLData(this);
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

        return fhirxmlData.withUnknownAttributeForGenderInXML();
    }

    public String withInvalidGenderInXML() throws ParsingException, IOException {
        return fhirxmlData.withInvalidGenderInXML();

    }

    public String withMultipleGenderElementsInXML() throws ParsingException, IOException {
        return fhirxmlData.withMultipleGenderElementsInXML();
    }

    public String withUnknownElementInXML() throws ParsingException, IOException {
        return fhirxmlData.withUnknownElementInXML();
    }

    public String withMissingRequiredDataInXML() throws ParsingException, IOException {

        return fhirxmlData.withMissingRequiredDataInXML();

    }

    public String withDuplicateNameDataInXML() throws ParsingException, IOException {
        return fhirxmlData.withDuplicateNameDataInXML();
    }

    public String asXML() throws ParsingException, IOException {

        PatientFHIRXMLComposer composer = new PatientFHIRXMLComposer(this);
        return composer.compose();

    }

    boolean hasBirthTime() {

        return this.birthTime != null;
    }

    public boolean hasNameDetails() {
        return this.family != null;
    }

    public boolean hasGenderDetails() {
        return this.gender != null;
    }

    public boolean hasBirthDetails() {
        return this.birthDate != null;
    }

    public boolean hasAddressDetails() {
        return this.addressCode != null;
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

