package domain;

public class Diagnosis {

    private String diagnosisName;
    private String order;
    private String certainty;
    private String status;

    public String getDiagnosisName() {
        return diagnosisName;
    }

    public String getOrder() {
        return order;
    }

    public String getCertainty() {
        return certainty;
    }

    public String getStatus() {
        return status;
    }


    private Diagnosis(DiagnosisBuilder builder) {
        this.diagnosisName = builder.diagnosisName;
        this.order = builder.order;
        this.certainty = builder.certainty;
        this.status = builder.status;
    }


    public static class DiagnosisBuilder {

        private String diagnosisName;
        private String order;
        private String certainty;
        private String status;

        public DiagnosisBuilder diagnosisName(String diagnosisName) {
            this.diagnosisName = diagnosisName;
            return this;
        }

        public DiagnosisBuilder order(String order) {
            this.order = order;
            return this;
        }

        public DiagnosisBuilder certainty(String certainty) {
            this.certainty = certainty;
            return this;
        }

        public DiagnosisBuilder status(String status) {
            this.status = status;
            return this;
        }

        public Diagnosis build() {
            return new Diagnosis(this);
        }
    }
}
