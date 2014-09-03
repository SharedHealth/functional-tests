package domain;

public class FamilyHistory {

    private String relationShip;
    private String bornOnDate;
    private String onsetAge;
    private String relationshipNotes;
    private String relationshipDiagnosis;

    public String getRelationShip() {
        return relationShip;
    }

    public String getBornOnDate() {
        return bornOnDate;
    }

    public String getOnsetAge() {
        return onsetAge;
    }

    public String getRelationshipNotes() {
        return relationshipNotes;
    }
    public String getRelationshipDiagnosis() {
        return relationshipDiagnosis;
    }

    private FamilyHistory(FamilyHistoryBuilder builder) {
        this.relationShip = builder.relationShip;
        this.bornOnDate = builder.bornOnDate;
        this.onsetAge = builder.onsetAge;
        this.relationshipNotes = builder.relationshipNotes;
        this.relationshipDiagnosis = builder.relationshipDiagnosis;
    }


    public static class FamilyHistoryBuilder {

        private String relationShip;
        private String bornOnDate;
        private String onsetAge;
        private String relationshipNotes;
        private String relationshipDiagnosis;

        public FamilyHistoryBuilder relationShip(String relationShip) {
            this.relationShip = relationShip;
            return this;
        }

        public FamilyHistoryBuilder bornOnDate(String bornOnDate) {
            this.bornOnDate = bornOnDate;
            return this;
        }

        public FamilyHistoryBuilder onsetAge(String onsetAge) {
            this.onsetAge = onsetAge;
            return this;
        }

        public FamilyHistory build() {
            return new FamilyHistory(this);
        }

        public FamilyHistoryBuilder relationshipNotes(String relationshipNotes) {
            this.relationshipNotes = relationshipNotes;
            return this;
        }

        public FamilyHistoryBuilder relationshipDiagnosis(String relationshipDiagnosis) {
            this.relationshipDiagnosis = relationshipDiagnosis;
            return this;
        }
    }
}
