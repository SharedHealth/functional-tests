package tests.api;

import categories.ApiTest;
import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.basic;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.response.Response;
import data.EncounterBundleData;
import static data.EncounterBundleData.validEncounter;
import data.PatientFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;

import org.junit.*;
import org.junit.experimental.categories.Category;
import repo.PatientRepo;

import java.util.HashMap;
import java.util.Map;

@Ignore
public class SHRIntegrationTests {

    private PatientRepo patientRepo = new PatientRepo();
    private String hid;
    private final Integer ENCOUNTER_UUID_LENGTH = 36;

    @Before
    public void setupPatientInMCI() {
        RestAssured.baseURI = "http://172.18.46.54";
        RestAssured.port = 8080;
        RestAssured.authentication = basic("shr", "password");

        PatientFactory dataStore = new PatientFactory();
        hid = patientRepo.create(dataStore.defaultPatient);
        System.out.println("Patient with hid " + hid + " is created.");
    }

    @Category(ApiTest.class)
    @Test
    public void verifyGetEncounterBundle() {
        String json = replaceWithProperHid(validEncounter());

        given().contentType("application/json; charset=utf-8")
                .body(json)
                .when().post("/patients/" + hid + "/encounters")
                .then().assertThat()
                .statusCode(200)
                .contentType("application/json");

        Response response = given().when().get("/patients/" + hid + "/encounters");
        response.then().assertThat()
                .statusCode(200)
                .header("Content-Type", "application/json")
                .body("", Matchers.anything());
        String encounterId = response.jsonPath().getString("encounterId");
        Assert.assertNotNull(encounterId);
        System.out.println("Encounter Bundle created in shr with encounterId " + encounterId);
    }

    @Category(ApiTest.class)
    @Test
    public void shouldAcceptValidEncounter() throws Exception {
        String json = replaceWithProperHid(validEncounter());
        String response = given().contentType("application/json; charset=utf-8")
                .body(json)
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

    @Category(ApiTest.class)
    @Test
    public void shouldRejectEncounterWithoutSystemForDiagnosis() {
        String json = replaceWithProperHid(EncounterBundleData.withMissingSystemForDiagnosis());
        Map response = send(json);

        assertThat(response.get("code").toString(), is("514"));
    }

    @Category(ApiTest.class)
    @Test
    public void shouldRejectEncounterWithInvalidCodeForDiagnosis() throws Exception {
        String json = replaceWithProperHid(EncounterBundleData.withInvalidCodeForDiagnosis());
        Map response = send(json);

        assertThat(response.get("code").toString(), is("511"));
    }

    @Category(ApiTest.class)
    @Test
    public void shouldRejectEncounterWithOneInvalidCodeForDiagnosis() throws Exception {
        String json = replaceWithProperHid(EncounterBundleData.withInvalidCodeForDiagnosis());
        Map response = send(json);

        assertThat(response.get("code").toString(), is("511"));
    }

    public String replaceWithProperHid(String json) {
        Map valuesMap = new HashMap<>();
        valuesMap.put("healthIdPlaceholder", hid);
        StrSubstitutor substitutor = new StrSubstitutor(valuesMap);
        return substitutor.replace(json);
    }

    @After
    public void tearDown() {
        RestAssured.reset();
    }
}
