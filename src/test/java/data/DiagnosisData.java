package data;

import domain.Diagnosis;

public class DiagnosisData {


    public static Diagnosis DiagnosisWithReferenceTermForEncounterSync = new Diagnosis.DiagnosisBuilder()
            .diagnosisName("Superficial injury of shoulder and upper arm")
            .order("PRIMARY")
            .certainty("PRESUMED")
            .status("TRUE")
            .build();

    public static Diagnosis DiagnosisWithOutReferenceTermForEncounterSync = new Diagnosis.DiagnosisBuilder()
            .diagnosisName("Fracture in upper arm")
            .order("SECONDARY")
            .certainty("PRESUMED")
            .status("TRUE")
            .build();

}
