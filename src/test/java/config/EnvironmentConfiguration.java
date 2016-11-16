package config;

import java.util.HashMap;
import java.util.Map;


/*


 */
public class EnvironmentConfiguration {
    public static Map<String, CongifurationProperty> environments = new HashMap<String, CongifurationProperty>();


    public static CongifurationProperty getEnvironmentProperties() {
        Map<String, String> env = System.getenv();

        CongifurationProperty devEnvironment = new CongifurationProperty();
        devEnvironment.property.put("mci_registry", "http://172.18.46.108:8085");
        devEnvironment.property.put("mci_registry_patient_context_path", "/api/v2/patients");

        environments.put("dev", devEnvironment);

        return (env.get("environment") == null) ? getDefaultProperties() : getSpecificEnvironmentProperties();

    }

    private static CongifurationProperty getDefaultProperties() {


        return  environments.get("dev");

    }

    private static CongifurationProperty getSpecificEnvironmentProperties() {
        return null;
    }

}


