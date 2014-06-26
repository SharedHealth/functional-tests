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

    private static String id = String.valueOf(System.currentTimeMillis()).substring(7);


    public static Patient newPatient = new Patient.PatientBuilder().firstName("A"+id)
            .lastName("ATEST").gender("Male").dateOfBirth("01-03-2000").address(defaultAddress)
            .nid("NIDA"+id).hid("HIDA"+id).education("5th Pass and Below").occupation("Agriculture").primaryContact("Primary One").build();



    public static Patient defaultPatient = new Patient.PatientBuilder().firstName("PTHREE")
            .lastName("TEST").gender("Male").dateOfBirth("01-03-2000").address(defaultAddress)
            .nid("NID0033").hid("BH0032").education("5th Pass and Below").occupation("Agriculture").primaryContact("").build();


}
