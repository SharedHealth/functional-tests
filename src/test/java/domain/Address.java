package domain;

public class Address {

    private String division;
    private String district;
    private String upazilla;
    private String cityCorporation;
    private String union;
    private String addressLine1;

    public String getDivision() {
        return division;
    }

    public String getDistrict() {
        return district;
    }

    public String getUpazilla() {
        return upazilla;
    }

    public String getUnion() {
        return union;
    }

    public String getCityCorporation() {
        return cityCorporation;
    }

    public String getAddressLine1() { return addressLine1; }

    private Address(AddressBuilder builder) {
        this.division = builder.division;
        this.district = builder.district;
        this.upazilla = builder.upazilla;
        this.cityCorporation = builder.cityCorporation;
        this.union = builder.union;
        this.addressLine1 = builder.addressLine1;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Address{");
        sb.append("division='").append(division).append('\'');
        sb.append(", district='").append(district).append('\'');
        sb.append(", upazilla='").append(upazilla).append('\'');
        sb.append(", union='").append(union).append('\'');
        sb.append(", addressLine1='").append(addressLine1).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static class AddressBuilder {

        private String division;
        private String district;
        private String upazilla;
        private String union;
        private String addressLine1;
        private String cityCorporation;

        public AddressBuilder division(String division) {
            this.division = division;
            return this;
        }

        public AddressBuilder district(String district) {
            this.district = district;
            return this;
        }

        public AddressBuilder upazilla(String upazilla) {
            this.upazilla = upazilla;
            return this;
        }

        public AddressBuilder union(String union) {
            this.union = union;
            return this;
        }

        public AddressBuilder cityCorporation(String cityCorporation) {
            this.cityCorporation = cityCorporation;
            return this;
        }

        public AddressBuilder addressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }
}
