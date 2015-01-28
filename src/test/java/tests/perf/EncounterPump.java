package tests.perf;

import categories.PerfTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import utils.FileUtil;

import java.util.HashMap;
import java.util.Map;

public class EncounterPump {
    public static final String HEALTH_ID = "HEALTHID";
    String baseUrl = "http://172.18.46.2:8081/patients/";
    Map<String, String> headers = new HashMap<>();

    @Before
    public void Setup() {
        headers.put("X-Auth-Token", "8dad0c07-caf8-48a9-ac2a-1815a9aa11a1");
    }

    @Category(PerfTest.class)
    @Test
    public void pump() throws Exception {
        String[] patients = FileUtil.asString("perf/patients.text").split("\n");
        WebClient webClient = new WebClient(baseUrl, headers);
        String registration = FileUtil.asString("perf/registration.xml");
        String bigEncounter = FileUtil.asString("perf/big-encounter.xml");
        String regularEncounter = FileUtil.asString("perf/regular-encounter.xml");

        for (String patient : patients) {
            long start = System.currentTimeMillis();
            webClient.post(patient + "/encounters", registration.replace(HEALTH_ID, patient), "application/xml;" +
                    "charset=UTF-8");
            webClient.post(patient + "/encounters", bigEncounter.replace(HEALTH_ID, patient), "application/xml;" +
                    "charset=UTF-8");
            webClient.post(patient + "/encounters", regularEncounter.replace(HEALTH_ID, patient), "application/xml;" +
                    "charset=UTF-8");
            System.out.println(String.format("Done doing patient : %s in time (milliseconds) %s", patient, System
                    .currentTimeMillis() - start));
        }
    }
}
