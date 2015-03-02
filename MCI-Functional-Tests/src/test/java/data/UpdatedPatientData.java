package data;

import domain.Patient;

/**
 * Created by ashutosh on 24/02/15.
 */
public class UpdatedPatientData {


    public Patient updatedPatientPerfonalInfo= new Patient()
            .withGiven_name("UpdatedName")
            .withSur_name("UpdatedSurName")
            .withGender("Female")
            .build();
}
