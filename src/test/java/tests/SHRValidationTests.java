package tests;

import com.jayway.restassured.RestAssured;
import data.EncounterBundleData;
import data.PatientData;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import repo.PatientRepo;

import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static data.EncounterBundleData.validEncounter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SHRValidationTests {

    private static final Integer ENCOUNTER_UUID_LENGTH = 36;
    private PatientRepo patientRepo = new PatientRepo();
    private String hid;

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "http://172.18.46.54";
        RestAssured.port = 8080;
        hid = patientRepo.create(PatientData.newPatient1);
    }

    @Test
    public void shouldAcceptValidEncounter() throws Exception {
        String response = given().contentType("application/json; charset=utf-8")
                .body(validEncounter())
                .when().post("/patients/" + hid + "/encounters")
                .then().extract().body().asString();

        assertThat(StringUtils.strip(response, "\"").length(), is(ENCOUNTER_UUID_LENGTH));
    }

    private Map send(String encounter) {
        return given().contentType("application/json; charset=utf-8")
                .body(encounter)
                .when().post("/patients/" + hid + "/encounters")
                .then().extract().body().as(Map.class);
    }

    @Test
    public void shouldRejectEncounterWithoutSystemForDiagnosis() {
        Map response = send(EncounterBundleData.withMissingSystemForDiagnosis());

        assertThat(response.get("code").toString(), is("514"));
    }

    @Test
    public void shouldRejectEncounterWithInvalidCodeForDiagnosis() throws Exception {
        Map response = send(EncounterBundleData.withInvalidCodeForDiagnosis());

        assertThat(response.get("code").toString(), is("511"));
    }

    @Test
    public void shouldRejectEncounterWithOneInvalidCodeForDiagnosis() throws Exception {
        Map response = send(EncounterBundleData.withOneInvalidCodeForDiagnosis());

        assertThat(response.get("code").toString(), is("511"));
    }
}
