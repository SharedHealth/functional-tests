package data;

import domain.Address;
import domain.Patient;

public class PatientData {


    public static Address defaultAddress = new Address.AddressBuilder()
            .division("Barisal")
            .district("Barguna")
            .upazilla("AMTALI")
            .cityCorporation("Unions of AMTALI Upazila")
            .union("AMTALI")
            .addressLine1("Test")
            .build();

    public static Address addressForFacilityOne = new Address.AddressBuilder()
            .division("DHAKA")
            .district("DHAKA")
            .upazilla("DHAMRAI")
            .cityCorporation("AMTA")
            .union("SUTI PARA")
            .addressLine1("Test")
            .build();

    public static Address addressForFacilityTwo = new Address.AddressBuilder()
            .division("DHAKA")
            .district("DHAKA")
            .upazilla("DOHAR")
            .cityCorporation("BILASPUR")
            .union("MUKSUDPUR")
            .addressLine1("Test")
            .build();

    private  String id1 = String.valueOf(System.currentTimeMillis()).substring(7);
//    private  String id1 = "435316";
    private  String id3 = String.valueOf(System.currentTimeMillis() + 1).substring(7);


    public  Patient defaultPatient = new Patient.PatientBuilder()
            .firstName("A" + id1).lastName("ATEST")
            .gender("Male").dateOfBirth("01-03-2000")
            .address(defaultAddress)
            .nid("9000000" + id1)
            .education("5th Pass and Below")
            .occupation("Business")
            .primaryContact("Primary One").build();
    public  Patient defaultPatientWithEditedName = new Patient.PatientBuilder()
            .firstName("A" + id1+"Edit").lastName("ATESTEdit")
            .gender("Male").dateOfBirth("01-03-2000")
            .address(addressForFacilityOne)
            .nid("9000000" + id1)
            .education("5th Pass and Below")
            .occupation("Business")
            .primaryContact("Primary One").build();

    public  Patient patientForFacilityTwoCatchment = new Patient.PatientBuilder()
            .firstName("A" + id1).lastName("ATEST")
            .gender("Male").dateOfBirth("01-03-2000")
            .address(addressForFacilityTwo)
            .nid("9000000" + id1)
            .education("5th Pass and Below")
            .occupation("Business")
            .primaryContact("Primary One").build();

    public  Patient patientWithAllFieldDetails = new Patient.PatientBuilder()
            .firstName("A" + id3).lastName("ATEST")
            .gender("Male").dateOfBirth("01-03-2000")
            .address(defaultAddress)
            .education("5th Pass and Below")
            .occupation("Business")
            .primaryContact("Primary One")
            .binBRN("90000000000" + id3).nid("9000000" + id3).uid("90000" + id3)
            .fatherBRN("91000000000" + id3).fatherNid("9100000" + id3).fatherUid("91000" + id3)
            .motherBRN("92000000000" + id3).motherNid("9200000" + id3).motherUid("92000" + id3)
            .build();
}
