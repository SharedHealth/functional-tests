package data;

import domain.FamilyHistory;

public class FamilyHistoryData {


    public static FamilyHistory familyHistory = new FamilyHistory.FamilyHistoryBuilder()
            .relationShipName("FTH")
            .relationShipDisplayText("father")
            .bornOnDate("1955-09-09")
            .bornOnDateDisplayText("9-Sep-1955")
            .onsetAge("13")
            .relationshipNotes("Relationship Notes")
            .relationshipDiagnosis("Fracture in upper arm")
            .build();


}
