package utils;

public class FhirConstant {
    private static final String FHIR_EXTENSION_URL = "https://sharedhealth.atlassian.net/wiki/display/docs/fhir-extensions";

    public static final String EDUCATION_DETAILS_EXTENSION_NAME = "EducationDetails";
    public static final String OCCUPATION_EXTENSION_NAME = "Occupation";
    public static final String CONFIDENTIALITY_EXTENSION_NAME = "Confidentiality";
    public static final String DOB_TYPE_EXTENSION_NAME = "DOBType";
    public static final String RELATION_ID_EXTENSION_NAME = "RelationId";
    public static final String HOUSE_HOLD_CODE_EXTENSION_NAME = "HouseHoldCode";

    public static String getFhirExtensionUrl(String extensionName) {
        return FHIR_EXTENSION_URL + "#" + extensionName;
    }
}
