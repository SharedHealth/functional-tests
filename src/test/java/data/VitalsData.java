package data;

import domain.Vitals;

public class VitalsData {


    public static Vitals patientVitals = new Vitals.VitalsBuilder()
            .systolicBloodPressure("120.5")
            .diastolicBloodPressure("80.3")
            .pulse("79")
            .temperature("98.3")
            .build();


}
