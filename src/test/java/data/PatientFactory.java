package data;

import domain.Address;
import domain.Patient;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class PatientFactory {

    private static String getEncodedName(String randomCode) {
        StringBuilder codedString = new StringBuilder();
        for (int i = 0; i < randomCode.length(); i++) {
            codedString.append((char) (Integer.parseInt(randomCode.substring(i, i + 1)) + 65));

        }
        return codedString.toString();
    }

    public static Patient validPatientWithMandatoryInformation() {
        Patient patient = validPatientWithoutBirthTime();
        patient.birthTime = "16:50:00";
        return patient;
    }

    public static Patient validPatientWithoutBirthTime() {
        Patient patient = new Patient();
        patient.nid = String.valueOf(new Date().getTime());
        patient.given = "AHI" + PatientFactory.getEncodedName(patient.nid);
        patient.family = "REV" + PatientFactory.getEncodedName(StringUtils.reverse(patient.nid));
        patient.gender = "male";
        patient.birthDate = "1980-06-14";
        patient.addressCode = "201918991101";
        patient.addressLine = "6th " + PatientFactory.getEncodedName(patient.nid) + " lane";
        patient.countryCode = "50";
        return patient;
    }

    public static Patient validPatientWithAllInformation() {
        Patient patient = validPatientWithMandatoryInformation();
        return patient;
    }


    public static Address defaultAddress = new Address.AddressBuilder()
            .division("Barisal")
            .district("Barguna")
            .upazilla("Amtali")
            .cityCorporation("Unions Of Amtali Upazila")
            .union("Amtali")
            .addressLine1("Test")
            .build();

    public static Address addressForFacilityOne = new Address.AddressBuilder()
            .division("DHAKA")
            .district("DHAKA")
            .upazilla("DHAMRAI")
            .cityCorporation("AMTA")
            .union("SUTI PARA")
            .addressLine1("Test")
            .build();

    public static Address addressForFacilityTwo = new Address.AddressBuilder()
            .division("DHAKA")
            .district("Dhaka")
            .upazilla("Dohar")
            .cityCorporation("")
            .union("")
            .addressLine1("Test")
            .build();

    private String id1 = String.valueOf(System.currentTimeMillis()).substring(7);

    public Patient defaultPatient = new Patient.PatientBuilder()
            .firstName("A" + id1).lastName("ATEST")
            .gender("Male").dateOfBirth("01-03-2000")
            .address(defaultAddress)
            .nid("9000000" + id1)
            .education("5th Pass and Below")
            .occupation("Business")
            .primaryContact("Primary One").build();
    public Patient defaultPatientWithEditedName = new Patient.PatientBuilder()
            .firstName("A" + id1).lastName("ATESTEdit")
            .gender("Male").dateOfBirth("01-03-2000")
            .address(defaultAddress)
            .nid("9000000" + id1)
            .education("5th Pass and Below")
            .occupation("Business")
            .primaryContact("Primary One").build();

    public Patient patientForFacilityTwoCatchment = new Patient.PatientBuilder()
            .firstName("A" + id1).lastName("ATEST")
            .gender("Male").dateOfBirth("01-03-2000")
            .address(addressForFacilityTwo)
            .nid("9000000" + id1)
            .education("5th Pass and Below")
            .occupation("Business")
            .primaryContact("Primary One").build();

    private String id4 = String.valueOf(System.currentTimeMillis() + 1).substring(3);
    public Patient patientWithAllFieldDetails = new Patient.PatientBuilder()
            .firstName("A" + id4).lastName("ATEST")
            .gender("Male").dateOfBirth("01-03-2000")
            .address(defaultAddress)
            .education("5th Pass and Below")
            .occupation("Business")
            .primaryContact("Primary One")
            .binBRN("8000000" + id4).nid("800" + id4).uid("8" + id4)
            .fatherBRN("8100000" + id4).fatherNid("810" + id4).fatherUid("8" + id4)
            .motherBRN("8200000" + id4).motherNid("820" + id4).motherUid("8" + id4)
            .build();

    public JSONObject getAddressJsonForBarisal() {
        JSONObject present_address = new JSONObject();
        try {
            present_address.put("address_line", "Test");
            present_address.put("division_id", "10");
            present_address.put("district_id", "09");
            present_address.put("upazila_id", "18");
            present_address.put("city_corporation_id", "16");
            present_address.put("union_or_urban_ward_id", "01");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return present_address;
    }

    public JSONObject getAddressJsonForDhaka() {
        JSONObject present_address = new JSONObject();
        try {
            present_address.put("address_line", "Test");
            present_address.put("division_id", "30");
            present_address.put("district_id", "26");
            present_address.put("upazila_id", "18");
            present_address.put("city_corporation_id", "99");
            present_address.put("union_or_urban_ward_id", "42");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return present_address;
    }

    public JSONObject getAddressJsonForKhulna() {
        JSONObject present_address = new JSONObject();
        try {
            present_address.put("address_line", "Test");
            present_address.put("division_id", "40");
            present_address.put("district_id", "47");
            present_address.put("upazila_id", "53");
            present_address.put("city_corporation_id", "99");
            present_address.put("union_or_urban_ward_id", "10");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return present_address;
    }

    public JSONObject getAddressJsonForChittagong() {
        JSONObject present_address = new JSONObject();
        try {
            present_address.put("address_line", "Test");
            present_address.put("division_id", "20");
            present_address.put("district_id", "19");
            present_address.put("upazila_id", "81");
            present_address.put("city_corporation_id", "99");
            present_address.put("union_or_urban_ward_id", "72");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return present_address;
    }

    public JSONObject getAddressJsonForRajshahi() {
        JSONObject present_address = new JSONObject();
        try {
            present_address.put("address_line", "Test");
            present_address.put("division_id", "50");
            present_address.put("district_id", "70");
            present_address.put("upazila_id", "66");
            present_address.put("city_corporation_id", "99");
            present_address.put("union_or_urban_ward_id", "89");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return present_address;
    }

    public JSONObject getAddressJsonForRangpur() {
        JSONObject present_address = new JSONObject();
        try {
            present_address.put("address_line", "Test");
            present_address.put("division_id", "55");
            present_address.put("district_id", "32");
            present_address.put("upazila_id", "30");
            present_address.put("city_corporation_id", "33");
            present_address.put("union_or_urban_ward_id", "01");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return present_address;
    }

    public JSONObject getAddressJsonForSylhet() {
        JSONObject present_address = new JSONObject();
        try {
            present_address.put("address_line", "Test");
            present_address.put("division_id", "55");
            present_address.put("district_id", "32");
            present_address.put("upazila_id", "30");
            present_address.put("city_corporation_id", "33");
            present_address.put("union_or_urban_ward_id", "01");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return present_address;
    }

}
