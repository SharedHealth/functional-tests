package domain;

public class Patient {
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private Address address;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
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

    private String nid;
    private String hid;
    private String education;
    private String occupation;
    private String primaryContact;

    public Patient(PatientBuilder patientBuilder) {
        this.firstName = patientBuilder.firstName;
        this.lastName = patientBuilder.lastName;
        this.gender = patientBuilder.gender;
        this.dateOfBirth = patientBuilder.dateOfBirth;
        this. address = patientBuilder.address;
        this.nid = patientBuilder.nid;
        this.hid = patientBuilder.hid;
        this.education = patientBuilder.education;
        this.occupation = patientBuilder.occupation;
        this.primaryContact = patientBuilder.primaryContact;

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


        public Patient build() {
            return new Patient(this);
        }
    }
}
