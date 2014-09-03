package data;

import domain.FamilyHistory;

public class FamilyHistoryData {


    public static FamilyHistory familyHistory = new FamilyHistory.FamilyHistoryBuilder()
            .relationShip("FTH")
            .bornOnDate("1955-09-09")
            .onsetAge("13")
            .relationshipNotes("Relationship Notes")
            .relationshipDiagnosis("Fracture in upper arm")
            .build();


}
