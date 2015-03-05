package domain;

public class Address {

    private String division;
    private String district;
    private String upazilla;
    private String cityCorporation;
    private String union_or_urban_ward;
    private String addressLine1;

    private String divisionF;
    private String districtF;
    private String upazillaF;
    private String cityCorporationF;
    private String union_or_urban_wardF;
    private String addressLine1F;

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


    public String getDivisionF() {
        return divisionF;
    }

    public String getDistrictF() {
        return districtF;
    }

    public String getUpazillaF() {
        return upazillaF;
    }

    public String getUnion_or_urban_wardF() {
        return union_or_urban_wardF;
    }

    public String getCityCorporationF() {
        return cityCorporationF;
    }

    public String getAddressLine1F() { return addressLine1F; }


    private Address(AddressBuilder builder) {
        this.division = builder.division;
        this.district = builder.district;
        this.upazilla = builder.upazilla;
        this.cityCorporation = builder.cityCorporation;
        this.union_or_urban_ward = builder.union_or_urban_ward;
        this.addressLine1 = builder.addressLine1;

        this.divisionF = builder.divisionF;
        this.districtF = builder.districtF;
        this.upazillaF = builder.upazillaF;
        this.cityCorporationF = builder.cityCorporationF;
        this.union_or_urban_wardF = builder.union_or_urban_wardF;
        this.addressLine1F = builder.addressLine1F;

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

        private String divisionF;
        private String districtF;
        private String upazillaF;
        private String union_or_urban_wardF;
        private String addressLine1F;
        private String cityCorporationF;

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

        public AddressBuilder divisionF(String divisionF) {
            this.divisionF = divisionF;
            return this;
        }

        public AddressBuilder districtF(String districtF) {
            this.districtF = districtF;
            return this;
        }

        public AddressBuilder upazillaF(String upazillaF) {
            this.upazillaF = upazillaF;
            return this;
        }

        public AddressBuilder union_or_urban_wardF(String union_or_urban_wardF) {
            this.union_or_urban_wardF = union_or_urban_wardF;
            return this;
        }

        public AddressBuilder cityCorporationF(String cityCorporationF) {
            this.cityCorporationF = cityCorporationF;
            return this;
        }

        public AddressBuilder addressLine1F(String addressLine1F) {
            this.addressLine1F = addressLine1F;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }
}
