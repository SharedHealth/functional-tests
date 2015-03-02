package domain;

public class Address {

    private String division;
    private String district;
    private String upazilla;
    private String cityCorporation;
    private String union_or_urban_ward;
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

    public String getUnion_or_urban_ward() {
        return union_or_urban_ward;
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
        this.union_or_urban_ward = builder.union_or_urban_ward;
        this.addressLine1 = builder.addressLine1;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Address{");
        sb.append("division='").append(division).append('\'');
        sb.append(", district='").append(district).append('\'');
        sb.append(", upazilla='").append(upazilla).append('\'');
        sb.append(", union_or_urban_ward='").append(union_or_urban_ward).append('\'');
        sb.append(", addressLine1='").append(addressLine1).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static class AddressBuilder {

        private String division;
        private String district;
        private String upazilla;
        private String union_or_urban_ward;
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

        public AddressBuilder union_or_urban_ward(String union_or_urban_ward) {
            this.union_or_urban_ward = union_or_urban_ward;
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
