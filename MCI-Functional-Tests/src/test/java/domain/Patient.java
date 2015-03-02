package domain;


public class Patient {
    private String given_name;
    private String sur_name;
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
    private String catchment;
    private String nidF;
    private String binBRNF;
    private String given_nameF;
    private String sur_nameF;
    private String dateOfBirthF;
    private String genderF;


    public Patient withGiven_name(String given_name) {
        this.given_name = given_name;
        return this;
    }

    public Patient withSur_name(String sur_name) {
        this.sur_name = sur_name;
        return this;
    }

    public Patient withDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public Patient withNid(String nid) {
        this.nid = nid;
        return this;
    }

    public Patient withBinBRN(String binBRN) {
        this.binBRN = binBRN;
        return this;
    }

    public Patient withUID(String uid) {
        this.uid = uid;
        return this;
    }

    public Patient withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public Patient address(Address address) {
        this.address = address;
        return this;
    }

    public Patient withCatchment(String catchment){

        this.catchment=catchment;
        return this;
    }
    public Patient withFieldNid(String nidF) {
        this.nidF = nidF;
        return this;
    }
    public Patient withFieldBinBRN(String binBRNF) {
        this.binBRNF = binBRNF;
        return this;
    }
    public Patient withFieldGiven_name(String given_nameF) {
        this.given_nameF = given_nameF;
        return this;
    }
    public Patient withFieldSur_name(String sur_nameF) {
        this.sur_nameF = sur_nameF;
        return this;
    }
    public Patient withFieldDateOfBirth(String dateOfBirthF) {
        this.dateOfBirthF = dateOfBirthF;
        return this;
    }
    public Patient withFieldGender(String genderF) {
        this.genderF = genderF;
        return this;
    }



    public String getGiven_name() {
        return given_name;
    }
    public String getSur_name() {
        return sur_name;
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
    public String getCatchment() { return catchment ;}

    public String getFieldGiven_name() {
        return given_nameF;
    }
    public String getFieldSur_name() {
        return sur_nameF;
    }
    public String getFieldGender() {
        return genderF;
    }
    public String getFieldDateOfBirth() {
        return dateOfBirthF;
    }
    public String getFieldNid() {
        return nidF;
    }
    public String getFieldBinBRN() { return binBRNF;  }

    public Patient build() {
            return  this;
        }
    }

