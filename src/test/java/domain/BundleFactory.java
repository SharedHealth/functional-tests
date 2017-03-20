package domain;

import config.ConfigurationProperty;
import config.EnvironmentConfiguration;

import java.util.Date;

public class BundleFactory {
   private static final ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();

    public static String BundleWithConditionEncounterForFever(String hid)
   {
       String code_id = "d587c115-82f3-11e5-b875-0050568225ca";
       String code_name = "Fever";

       Date today = new Date();
       Condition condition = new Condition();
       Composition composition = new Composition();

       Encounter encounter = new Encounter();
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
