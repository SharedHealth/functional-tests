package data;

import domain.Patient;

/**
 * Created by ashutosh on 24/02/15.
 */
public class UpdatedPatientData {

    protected AddressDataStore address = new AddressDataStore();

    public Patient updatedPatientInfo = new Patient()
            .withGiven_name("UpdatedName")
            .withSur_name("UpdatedSurName")
            .withGender("Female")
            .address(address.updateAddressForAmtali)
            .build();
}
