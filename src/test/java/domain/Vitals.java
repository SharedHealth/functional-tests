package domain;

public class Vitals {

    private String systolicBloodPressure;
    private String diastolicBloodPressure;
    private String pulse;
    private String temperature;

    public String getSystolicBloodPressure() {
        return systolicBloodPressure;
    }

    public String getDiastolicBloodPressure() {
        return diastolicBloodPressure;
    }

    public String getPulse() {
        return pulse;
    }

    public String getTemperature() {
        return temperature;
    }

    private Vitals(VitalsBuilder builder) {
        this.systolicBloodPressure = builder.systolicBloodPressure;
        this.diastolicBloodPressure = builder.diastolicBloodPressure;
        this.pulse = builder.pulse;
        this.temperature = builder.temperature;
    }


    public static class VitalsBuilder {

        private String systolicBloodPressure;
        private String diastolicBloodPressure;
        private String pulse;
        private String temperature;

        public VitalsBuilder systolicBloodPressure(String systolicBloodPressure) {
            this.systolicBloodPressure = systolicBloodPressure;
            return this;
        }

        public VitalsBuilder diastolicBloodPressure(String diastolicBloodPressure) {
            this.diastolicBloodPressure = diastolicBloodPressure;
            return this;
        }

        public VitalsBuilder pulse(String pulse) {
            this.pulse = pulse;
            return this;
        }

        public Vitals build() {
            return new Vitals(this);
        }

        public VitalsBuilder temperature(String temperature) {
            this.temperature = temperature;
            return this;
        }
    }
}
