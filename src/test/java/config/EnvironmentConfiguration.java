package config;

import java.util.HashMap;
import java.util.Map;


/*


 */
public class EnvironmentConfiguration {
    public static final String HRM_SERVER_BASE_URL_KEY = "hrm_server";
    public static final String FACILITY_ID_KEY = "facility_id";
    public static final String PROVIDER_ID_KEY = "provider_id";
    public static final String MCI_SERVER_BASE_URL_KEY = "mci_server_base_url";
    public static final String MCI_PATIENT_CONTEXT_PATH_KEY = "mci_registry_patient_context_path";
    public static final String TR_SERVER_BASE_URL_KEY = "tr_server";
    public static final String TR_FEVER_CONCEPT_UUID_KEY = "tr_fever_concept_uuid";
    public static final String IDP_SERVER_BASE_URL = "idp_server_base_url";
    public static final String SHR_SERVER_BASE_URL_KEY = "shr_registry";

    public static Map<String, ConfigurationProperty> environments = new HashMap<String, ConfigurationProperty>();


    public static ConfigurationProperty getEnvironmentProperties() {
        Map<String, String> env = System.getenv();

        ConfigurationProperty devEnvironment = new ConfigurationProperty();
        devEnvironment.property.put(MCI_SERVER_BASE_URL_KEY, "https://mci-dev.twhosted.com");
        devEnvironment.property.put("mci_registry_without_scheme", "mci-dev.twhosted.com");
        devEnvironment.property.put(MCI_PATIENT_CONTEXT_PATH_KEY, "/api/v1/patients");
        devEnvironment.property.put(IDP_SERVER_BASE_URL, "https://hie-idp-dev.twhosted.com");
        devEnvironment.property.put(SHR_SERVER_BASE_URL_KEY, "https://shr-dev.twhosted.com/v2");
        devEnvironment.property.put(HRM_SERVER_BASE_URL_KEY, "http://172.21.2.184:8084/api/1.0");
        devEnvironment.property.put(FACILITY_ID_KEY, "10019842");
        devEnvironment.property.put("code_id", "51899-3");
        devEnvironment.property.put(TR_SERVER_BASE_URL_KEY, "http://tr-dev.twhosted.com");
        devEnvironment.property.put(PROVIDER_ID_KEY, "24");
        devEnvironment.property.put(TR_FEVER_CONCEPT_UUID_KEY, "d22b304b-878d-11e5-95dd-005056b0145c");

        ConfigurationProperty localEnvironment = new ConfigurationProperty();
        localEnvironment.property.put(MCI_SERVER_BASE_URL_KEY, "http://192.168.33.20:8081");
        localEnvironment.property.put(MCI_PATIENT_CONTEXT_PATH_KEY, "/api/v1/patients");
        localEnvironment.property.put(IDP_SERVER_BASE_URL, "http://192.168.33.19:8084");
        localEnvironment.property.put(SHR_SERVER_BASE_URL_KEY, "http://192.168.33.20:8082/v2");
        localEnvironment.property.put(TR_SERVER_BASE_URL_KEY, "http://tr-showcase.twhosted.com");
        localEnvironment.property.put(HRM_SERVER_BASE_URL_KEY, "http://192.168.33.19:8084/api/1.0");
        localEnvironment.property.put(TR_FEVER_CONCEPT_UUID_KEY, "d22b304b-878d-11e5-95dd-005056b0145c");
        localEnvironment.property.put(FACILITY_ID_KEY, "10019842");
        localEnvironment.property.put(PROVIDER_ID_KEY, "24");

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


