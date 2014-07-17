package data;

import domain.Address;
import domain.Patient;

public class PatientData {



    public static Address defaultAddress = new Address.AddressBuilder()
            .division("Khulna")
            .district("Chuadanga Zila")
            .upazilla("Chuadanga Sadar Upazila")
            .union("Ward No-01")
            .addressLine1("Test")
            .build();

    private static String id1 = String.valueOf(System.currentTimeMillis()).substring(7);


    public static Patient newPatient1 = new Patient.PatientBuilder().firstName("A"+id1)
            .lastName("ATEST").gender("Male").dateOfBirth("01-03-2000").address(defaultAddress)
            .nid("9000000"+id1).hid("9000000000000"+id1).education("5th Pass and Below").occupation("Agriculture").primaryContact("Primary One").build();

    private static String id2 = String.valueOf(System.currentTimeMillis()).substring(7);
    public static Patient newPatient2 = new Patient.PatientBuilder().firstName("A"+id2)
            .lastName("ATEST").gender("Male").dateOfBirth("01-03-2000").address(defaultAddress)
            .nid("9000000"+id2).hid("9000000000000"+id2).education("5th Pass and Below").occupation("Agriculture").primaryContact("Primary One").build();



    public static Patient defaultPatient = new Patient.PatientBuilder().firstName("PTHREE")
            .lastName("TEST").gender("Male").dateOfBirth("01-03-2000").address(defaultAddress)
            .nid("NID0033").hid("BH0032").education("5th Pass and Below").occupation("Agriculture").primaryContact("").build();


}
