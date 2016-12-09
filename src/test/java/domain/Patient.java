package domain;

import ca.uhn.fhir.model.primitive.BooleanDt;

import java.util.Date;
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
    public String nid;
    public String hid;
    public String uid;
    public String binBRN;
    public String householdCode;

    public String given;
    public String family;
    public String gender;

    public String birthDate;
    public String dobType;
    public String birthTime;

    public Address address;
    public String addressLine;
    public String addressCode;
    public String countryCode;

    public Boolean isDead;
    public Date dateOfDeath;
    public Boolean confidentiality;
    public Boolean active;

    public String education;
    public String occupation;
    public String phoneNumber;

    public Patient() {
    }

    public Patient(PatientBuilder patientBuilder) {
        this.given = patientBuilder.firstName;
        this.family = patientBuilder.lastName;
        this.gender = patientBuilder.gender;
        this.birthDate = patientBuilder.dateOfBirth;
        this.address = patientBuilder.address;
        this.nid = patientBuilder.nid;
        this.hid = patientBuilder.hid;
        this.education = patientBuilder.education;
        this.occupation = patientBuilder.occupation;
        this.phoneNumber = patientBuilder.primaryContact;
        this.uid = patientBuilder.uid;
        this.binBRN = patientBuilder.binBRN;
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
            this.uid = uid;
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

