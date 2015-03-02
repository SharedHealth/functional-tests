package data;

import domain.Patient;

public class PatientDataStore {


    protected AddressDataStore address = new AddressDataStore();

    private  String id1 = String.valueOf(System.currentTimeMillis()).substring(7);

    public Patient defaultPatient = new Patient()
            .withNid("9000000" + id1)
            .withBinBRN("90000001234" + id1)
            .withUID("90000" + id1)
            .withGiven_name("A" + id1)
            .withSur_name("ATEST")
            .withDateOfBirth("2000-03-01")
            .withGender("M")
            .address(address.defaultAddress)
            .withCatchment("Sylhet -Habiganj -Ajmiriganj")
            .withFieldNid("National Identity Card No(NID)")
            .withFieldBinBRN("Birth Registration No(BRN)")
            .withFieldGiven_name("Given Name")
            .withFieldSur_name("Sur Name")
            .withFieldDateOfBirth("Date of Birth")
            .withFieldGender("Gender")
            .build();

    public Patient barisalPatient = new Patient()
            .withNid("9000000" + id1)
            .withBinBRN("90000001234" + id1)
            .withGiven_name("A" + id1)
            .withSur_name("ATEST")
            .withDateOfBirth("2000-03-01")
            .withGender("M")
            .address(address.getAddressForBarisal)
            .build();

    public Patient dhakaPatient = new Patient()
            .withNid("9000000" + id1)
            .withBinBRN("90000001234" + id1)
            .withGiven_name("A" + id1)
            .withSur_name("ATEST")
            .withDateOfBirth("2000-03-01")
            .withGender("M")
            .address(address.getAddressForDhaka)
            .build();

}
