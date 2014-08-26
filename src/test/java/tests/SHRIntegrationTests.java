package tests;

import categories.ShrTest;
import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.basic;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.EncoderConfig.encoderConfig;
import static com.jayway.restassured.config.RestAssuredConfig.newConfig;
import data.PatientData;
import domain.Patient;
import static org.apache.commons.io.FileUtils.readFileToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Category(ShrTest.class)
public class SHRIntegrationTests {

    protected static Patient primaryPatient;

    @BeforeClass
    public static void setupPatientInMCI() {
        RestAssured.baseURI = "http://172.18.46.53";
        RestAssured.port = 8081;
        RestAssured.basePath = "/api/v1";
        RestAssured.authentication = basic("mci", "password");
        RestAssured.rootPath = "";

        PatientData dataStore = new PatientData();
        primaryPatient = dataStore.newPatient1;

        JSONObject person = new JSONObject();
        JSONObject present_address = new JSONObject();
        try {
            person.put("hid", primaryPatient.getHid());
            person.put("nid", primaryPatient.getNid());
            person.put("first_name", primaryPatient.getFirstName());
            person.put("last_name", primaryPatient.getLastName());
            person.put("date_of_birth", "2000-03-01");
            person.put("gender", "1");
            person.put("occupation", "02");
            person.put("edu_level", "02");
            person.put("fathers_first_name", primaryPatient.getPrimaryContact());
            present_address.put("address_line", primaryPatient.getAddress().getAddressLine1());
            present_address.put("division_id", "40");
            present_address.put("district_id", "4018");
            present_address.put("upazilla_id", "401823");
            present_address.put("union_id", "40010801");
            person.put("present_address", present_address);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        given().contentType("application/json")
                .body(person.toString())
                .when().post("/patients")
                .then().assertThat().statusCode(201);

        given().pathParam("hid", primaryPatient.getHid())
                .when().get("/patients/{hid}")
                .then().statusCode(200);
        System.out.println("Patient with NID " + primaryPatient.getNid() + " created in MCI");
    }

    @Test
    public void verifyGetEncounterBundle(){
        final String json = createJsonObject();
        RestAssured.reset();
        RestAssured.baseURI = "http://172.18.46.54";
        RestAssured.port = 8080;
        RestAssured.config = newConfig().encoderConfig(encoderConfig().defaultContentCharset("UTF-8"));
        RestAssured.authentication = basic("shr", "password");

        given().header("Content-Type", "application/json")
                .body(json)
                .when().post("/patients/" + primaryPatient.getHid() + "/encounters")
                .then().assertThat()
                .statusCode(200)
                .contentType("application/json");

        given().when().get("/patients/" + primaryPatient.getHid() + "/encounters")
                .then().assertThat()
                .statusCode(200)
                .header("Content-Type", "application/json")
                .body("", Matchers.anything());
    }

    public String createJsonObject() {
        final URL resource = SHRIntegrationTests.class.getClassLoader().getResource("testFHIREncounter.json");
        if(resource == null) {
            return null;
        }
        String json = null;
        try {
            File file = new File(resource.toURI());
            json = StringUtils.deleteWhitespace(readFileToString(file).replaceAll("\\n", ""));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map valuesMap = new HashMap<>();
        valuesMap.put("healthIdPlaceholder", primaryPatient.getHid());
        StrSubstitutor substitutor = new StrSubstitutor(valuesMap);
        return substitutor.replace(json);
    }
}
