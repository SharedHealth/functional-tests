package domain;

public class FamilyHistory {

    private String relationShipName;
    private String relationShipDisplayText;
    private String bornOnDate;
    private String bornOnDateDisplayText;
    private String onsetAge;
    private String relationshipNotes;
    private String relationshipDiagnosis;
    private String relationshipDiagnosisDisplayText;

    public String getRelationShipName() {
        return relationShipName;
    }

    public String getRelationShipDisplayText() {
        return relationShipDisplayText;
    }

    public String getBornOnDate() {
        return bornOnDate;
    }

    public String getBornOnDateDisplayText() {
        return bornOnDateDisplayText;
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
    public String getRelationshipDiagnosisDisplayText() {
        return relationshipDiagnosisDisplayText;
    }

    private FamilyHistory(FamilyHistoryBuilder builder) {
        this.relationShipName = builder.relationShipName;
        this.relationShipDisplayText = builder.relationShipDisplayText;
        this.bornOnDate = builder.bornOnDate;
        this.bornOnDateDisplayText = builder.bornOnDateDisplayText;
        this.onsetAge = builder.onsetAge;
        this.relationshipNotes = builder.relationshipNotes;
        this.relationshipDiagnosis = builder.relationshipDiagnosis;
        this.relationshipDiagnosisDisplayText = builder.relationshipDiagnosisDisplayText;
    }


    public static class FamilyHistoryBuilder {

        private String relationShipName;
        private String relationShipDisplayText;
        private String bornOnDate;
        private String bornOnDateDisplayText;
        private String onsetAge;
        private String relationshipNotes;
        private String relationshipDiagnosis;
        public String relationshipDiagnosisDisplayText;

        public FamilyHistoryBuilder relationShipName(String relationShipName) {
            this.relationShipName = relationShipName;
            return this;
        }

        public FamilyHistoryBuilder relationShipDisplayText(String relationShipDisplayText) {
            this.relationShipDisplayText = relationShipDisplayText;
            return this;
        }

        public FamilyHistoryBuilder bornOnDate(String bornOnDate) {
            this.bornOnDate = bornOnDate;
            return this;
        }

        public FamilyHistoryBuilder bornOnDateDisplayText(String bornOnDateDisplayText) {
            this.bornOnDateDisplayText = bornOnDateDisplayText;
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

        public FamilyHistoryBuilder relationshipDiagnosisDisplayText(String relationshipDiagnosisDisplayText) {
            this.relationshipDiagnosisDisplayText = relationshipDiagnosisDisplayText;
            return this;
        }
    }
}
