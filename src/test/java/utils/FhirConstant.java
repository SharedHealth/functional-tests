package utils;

public class FhirConstant {
    private static final String MCI_VALUESET_URI_BASE_PATH = "/api/v2/vs/";
    private static final String FHIR_EXTENSION_URL = "https://sharedhealth.atlassian.net/wiki/display/docs/fhir-extensions";

    public static final String MCI_PATIENT_IDENTIFIERS_VALUESET = "patient-identifiers";
    public static final String MCI_PATIENT_EDUCATION_DETAILS_VALUESET = "patient-education-details";
    public static final String MCI_PATIENT_OCCUPATION_VALUESET = "patient-occupation";
    public static final String MCI_PATIENT_DOB_TYPE_VALUESET = "patient-dob-type";

    public static final String MCI_IDENTIFIER_HID_CODE = "Health Id";
    public static final String MCI_IDENTIFIER_NID_CODE = "National Id";
    public static final String MCI_IDENTIFIER_BRN_CODE = "Birth Registration Number";
    public static final String MCI_IDENTIFIER_UID_CODE = "Unique Id";
    public static final String MCI_IDENTIFIER_HOUSE_HOLD_NUMBER_CODE = "House Hold Number";


    public static final String EDUCATION_DETAILS_EXTENSION_NAME = "EducationDetails";
    public static final String OCCUPATION_EXTENSION_NAME = "Occupation";
    public static final String CONFIDENTIALITY_EXTENSION_NAME = "Confidentiality";
    public static final String DOB_TYPE_EXTENSION_NAME = "DOBType";
    public static final String RELATION_ID_EXTENSION_NAME = "RelationId";
    public static final String HOUSE_HOLD_CODE_EXTENSION_NAME = "HouseHoldCode";

    public static String getFhirExtensionUrl(String extensionName) {
        return FHIR_EXTENSION_URL + "#" + extensionName;
    }

    public static String getMCIValuesetURI(String mciBaseUrl, String valuesetName) {
        return String.format("%s%s%s", mciBaseUrl, MCI_VALUESET_URI_BASE_PATH, valuesetName);
    }
}
