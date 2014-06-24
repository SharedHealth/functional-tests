package domain;

import static domain.Address.AddressBuilder;

public class Test {

    public static void main(String[] args) {
        Address address = new AddressBuilder()
                .division("div")
                .district("distt")
                .upazilla("upazilla")
                .union("union")
                .addressLine1("lane1")
                .build();
        System.out.println(address);
    }
}
