package config;

import java.util.HashMap;
import java.util.Map;


/*


 */
public class EnvironmentConfiguration {
    public static Map<String, ConfigurationProperty> environments = new HashMap<String, ConfigurationProperty>();


    public static ConfigurationProperty getEnvironmentProperties() {
        Map<String, String> env = System.getenv();

        ConfigurationProperty devEnvironment = new ConfigurationProperty();
        devEnvironment.property.put("mci_registry", "https://mci-dev.twhosted.com");
        devEnvironment.property.put("mci_registry_patient_context_path", "/api/v2/patients");
        devEnvironment.property.put("idp_server_base_url", "https://hie-idp-dev.twhosted.com");
        devEnvironment.property.put("shr_registry","https://shr-dev.twhosted.com");
        devEnvironment.property.put("hrm_server", "http://172.21.2.184:8084/api/1.0");
        devEnvironment.property.put("facility_id","10019842");
        devEnvironment.property.put("code_id", "51899-3");
        devEnvironment.property.put("tr_server","http://tr-dev.twhosted.com");
        devEnvironment.property.put("provider_id", "27");

        ConfigurationProperty localEnvironment = new ConfigurationProperty();
        localEnvironment.property.put("mci_registry", "http://127.0.0.1:8085");
        localEnvironment.property.put("mci_registry_patient_context_path", "/api/v2/patients");
        localEnvironment.property.put("idp_server_base_url", "http://172.18.46.55:8084");
        localEnvironment.property.put("shr_registry","https://shr-dev.twhosted.com");

        environments.put("dev", devEnvironment);
        environments.put("local", localEnvironment);

        String environment = env.get("FT_ENVIRONMENT_NAME");
        return (environment == null) ? getDefaultProperties() : environments.get(environment);

    }

    private static ConfigurationProperty getDefaultProperties() {
        return environments.get("dev");
    }

    private static ConfigurationProperty getSpecificEnvironmentProperties() {
        return null;
    }

}


