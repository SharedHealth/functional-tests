package builders;

public class PatientData {


    public static AddressDetailsBuilder defaultAddress = new AddressDetailsBuilder().withAddress("Test")
            .withUnion("Ward No-01").withUpazilla("Chuadanga Sadar Upazila")
            .withDistrict("Chuadanga Zila").withDivision("Khulna");

    private static String id = String.valueOf(System.currentTimeMillis()).substring(7);

    public static PatientDataBuilder newPatient = new PatientDataBuilder().withFirstName("A"+id)
            .withLastName("ATEST").withGender("Male").withDateOfBirth("01-03-2000").withAddress(defaultAddress)
            .withNid("NIDA"+id).withHid("HIDA"+id).withEducation("5th Pass and Below").withOccupation("Agriculture").withPrimaryContact("Primary One");




    public static PatientDataBuilder defaultPatient = new PatientDataBuilder().withFirstName("PTHREE")
            .withLastName("TEST").withGender("Male").withDateOfBirth("01-03-2000").withAddress(defaultAddress)
            .withNid("NID0033").withHid("BH0032").withEducation("5th Pass and Below").withOccupation("Agriculture").withPrimaryContact("");
}
