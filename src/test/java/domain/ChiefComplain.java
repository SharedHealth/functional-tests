package domain;

public class ChiefComplain {

    private String chiefComplainName;
    private String duration;
    private String durationUnit;
    private boolean isCodedChiefComplain;

    public String getChiefComplainName() {
        return chiefComplainName;
    }

    public String getDuration() {
        return duration;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public boolean isCodedChiefComplain() {
        return isCodedChiefComplain;
    }

    private ChiefComplain(ChiefComplainBuilder builder) {
        this.chiefComplainName = builder.chiefComplainName;
        this.duration = builder.duration;
        this.durationUnit = builder.durationUnit;
        this.isCodedChiefComplain = builder.isCodedChiefComplain;
    }


    public static class ChiefComplainBuilder {

        private String chiefComplainName;
        private String duration;
        private String durationUnit;
        private boolean isCodedChiefComplain;


        public ChiefComplainBuilder chiefComplainName(String chiefComplainName) {
            this.chiefComplainName = chiefComplainName;
            return this;
        }

        public ChiefComplainBuilder duration(String duration) {
            this.duration = duration;
            return this;
        }

        public ChiefComplainBuilder durationUnit(String durationUnit) {
            this.durationUnit = durationUnit;
            return this;
        }

        public ChiefComplain build() {
            return new ChiefComplain(this);
        }

        public ChiefComplainBuilder isCodedChiefComplain(boolean isCodedChiefComplain) {
            this.isCodedChiefComplain = isCodedChiefComplain;
            return this;
        }
    }
}
