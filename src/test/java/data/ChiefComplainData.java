package data;

import domain.ChiefComplain;

public class ChiefComplainData {


    public static ChiefComplain ChiefComplainWithReferenceTermForEncounterSync = new ChiefComplain.ChiefComplainBuilder()
            .chiefComplainName("Pain in throat and chest")
            .duration("4")
            .durationUnit("Days")
            .isCodedChiefComplain(true)
            .build();

    public static ChiefComplain ChiefComplainWithOutReferenceTermForEncounterSync = new ChiefComplain.ChiefComplainBuilder()
            .chiefComplainName("Other chest pain")
            .duration("11")
            .durationUnit("Hours")
            .isCodedChiefComplain(true)
            .build();

    public static ChiefComplain NonCodedChiefComplainForEncounterSync = new ChiefComplain.ChiefComplainBuilder()
            .chiefComplainName("Mild Fever")
            .duration("30")
            .durationUnit("Minutes")
            .isCodedChiefComplain(false)
            .build();

}
