package builders;

public class AddressDetailsBuilder {

    private String addressLine1;
    private String union;
    private String upazilla;
    private String district;
    private String division;

    public String getAddressLine1() {
        return addressLine1;
    }

    public AddressDetailsBuilder withAddress(String address) {
        this.addressLine1 = address;
        return this;
    }

    public String getUnion() {
        return union;
    }

    public AddressDetailsBuilder withUnion(String union) {
        this.union = union;
        return this;
    }

    public String getUpazilla() {
        return upazilla;
    }

    public AddressDetailsBuilder withUpazilla(String upazilla) {
        this.upazilla = upazilla;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public AddressDetailsBuilder withDistrict(String district) {
        this.district = district;
        return this;
    }

    public String getDivision() {
        return division;
    }

    public AddressDetailsBuilder withDivision(String division) {
        this.division = division;
        return this;
    }
}
