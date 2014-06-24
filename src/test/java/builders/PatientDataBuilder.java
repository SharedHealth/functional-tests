package builders;

public class PatientDataBuilder {
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private AddressDetailsBuilder address;
    private String nid;
    private String hid;
    private String education;
    private String occupation;
    private String primaryContact;




    public String getFirstName() {
        return firstName;
    }

    public PatientDataBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;

    }

    public PatientDataBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public PatientDataBuilder withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public PatientDataBuilder withDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public AddressDetailsBuilder getAddress() {
        return address;
    }

    public PatientDataBuilder withAddress(AddressDetailsBuilder address) {
        this.address = address;
        return this;
    }

    public String getNid() {
        return nid;
    }

    public PatientDataBuilder withNid(String nid) {
        this.nid = nid;
        return this;
    }

    public String getHid() {
        return hid;
    }

    public PatientDataBuilder withHid(String hid) {
        this.hid = hid;
        return this;
    }

    public String getEducation() {
        return education;
    }

    public PatientDataBuilder withEducation(String education) {
        this.education = education;
        return this;
    }

    public String getOccupation() {
        return occupation;
    }

    public PatientDataBuilder withOccupation(String occupation) {
        this.occupation = occupation;
        return this;
    }

    public String getPrimaryContact() {
        return primaryContact;
    }

    public PatientDataBuilder withPrimaryContact(String primaryContact) {
        this.primaryContact = primaryContact;
        return this;
    }
}
