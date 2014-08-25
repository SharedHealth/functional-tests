package data;

import domain.Address;
import domain.Patient;

public class PatientData {


    public static Address defaultAddress = new Address.AddressBuilder()
            .division("BARISAL")
            .district("BHOLA")
            .upazilla("BHOLA SADAR")
            .cityCorporation("BHOLA PAURASAVA")
            .union("WARD NO-01")
            .addressLine1("Test")
            .build();

    private static String id1 = String.valueOf(System.currentTimeMillis()).substring(7);


    public static Patient newPatient1 = new Patient.PatientBuilder().firstName("A" + id1)
            .lastName("ATEST").gender("Male").dateOfBirth("01-03-2000").address(defaultAddress)
            .nid("9000000" + id1).education("5th Pass and Below").occupation("Agriculture").primaryContact("Primary One").build();

    private static String id2 = String.valueOf(System.currentTimeMillis()).substring(7);
    public static Patient newPatient2 = new Patient.PatientBuilder().firstName("A" + id2)
            .lastName("ATEST").gender("Male").dateOfBirth("01-03-2000").address(defaultAddress)
            .nid("9000000" + id2).education("5th Pass and Below").occupation("Agriculture").primaryContact("Primary One").build();

    private static String id3 = String.valueOf(System.currentTimeMillis() + 1).substring(7);

    public static Patient newPatient3 = new Patient.PatientBuilder().firstName("A" + id3)
            .lastName("ATEST").gender("Male").dateOfBirth("01-03-2000").address(defaultAddress)
            .education("5th Pass and Below").occupation("Agriculture").primaryContact("Primary One")
            .binBRN("90000000000" + id3).nid("9000000" + id3).uid("90000" + id3)
            .fatherBRN("91000000000" + id3).fatherNid("9100000" + id3).fatherUid("91000" + id3)
            .motherBRN("92000000000" + id3).motherNid("9200000" + id3).motherUid("92000" + id3)
            .build();


    public static Patient defaultPatient = new Patient.PatientBuilder().firstName("PTHREE")
            .lastName("TEST").gender("Male").dateOfBirth("01-03-2000").address(defaultAddress)
            .nid("NID0033").education("5th Pass and Below").occupation("Agriculture").primaryContact("").build();

    public static Patient patientForConsultation = new Patient.PatientBuilder().firstName("A902213")
            .lastName("ATEST").gender("Male").dateOfBirth("01-03-2000").address(defaultAddress)
            .nid("9000000902213").hid("9000000000000902213").education("5th Pass and Below").occupation("Agriculture").primaryContact("Primary One").build();


}
