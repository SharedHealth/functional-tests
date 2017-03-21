package domain;

import config.ConfigurationProperty;
import config.EnvironmentConfiguration;

import java.util.Date;

public class BundleFactory {

    private static Condition condition;
    private static Composition composition;
    private static Encounter encounter;
    private static Date today;
    private static Bundle bundle;


    private static final ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();

    public static String BundleWithConfidentialEncounter(String hid) {
        condition = new Condition();
        composition = new Composition();
        encounter = new Encounter();
        today = new Date();
        bundle = new Bundle();
        composition.setConfidentiality("Y");
        return createBundle(hid);
    }


    public static String BundleWithConditionEncounterForFever(String hid) {
        condition = new Condition();
        composition = new Composition();
        encounter = new Encounter();
        today = new Date();
        return createBundle(hid);


    }

    private static String createBundle(String hid) {
        String code_id = "d587c115-82f3-11e5-b875-0050568225ca";
        String code_name = "Fever";

        encounter.hid = hid;
        encounter.date = today;
        encounter.provider_id = config.property.get("provider_id");


        condition.hid = hid;
        condition.codeId = code_id;
        condition.encounterId = encounter.getIdentifier();
        condition.date = today;
        condition.provider_id = config.property.get("provider_id");
        condition.code_name = code_name;

        composition.hid = hid;
        composition.complaintId = condition.getIdentifier();
        composition.encounterId = encounter.getIdentifier();
        composition.date = today;
        Bundle bundle = new Bundle();

        Entry compositionEntry = new Entry(composition);
        bundle.addEntry(compositionEntry);


        Entry encounterEntry = new Entry(encounter);
        bundle.addEntry(encounterEntry);


        Entry conditionEntry = new Entry(condition);
        bundle.addEntry(conditionEntry);
        return bundle.asXML();
    }
}
