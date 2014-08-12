package data;

import static utils.FileUtil.asString;

public class EncounterBundleData {

    public static String validEncounter(){
        return asString("encounters/diagnosis/valid.json");
    }

    public static String withMissingSystemForDiagnosis() {
        return asString("encounters/diagnosis/with_missing_system.json");
    }

    public static String withInvalidCodeForDiagnosis() {
        return asString("encounters/diagnosis/with_invalid_code.json");
    }

    public static String withOneInvalidCodeForDiagnosis() {
        return asString("encounters/diagnosis/with_multiple_ref_having_one_invalid_code.json");
    }
}
