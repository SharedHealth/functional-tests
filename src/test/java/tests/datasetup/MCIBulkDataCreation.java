package tests.datasetup;


import categories.ApiTest;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.RestAssuredConfig;
import data.PatientFactory;
import domain.Patient;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static com.jayway.restassured.RestAssured.basic;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.EncoderConfig.encoderConfig;


public class MCIBulkDataCreation {

    protected Patient primaryPatient;

    @Before
    public void setUp() {

        RestAssured.baseURI = "http://172.18.46.2";
//        RestAssured.baseURI = WebDriverProperties.getProperty("mciURL");
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/v1";
        RestAssured.authentication = basic("mci", "password");
        RestAssured.rootPath = "";
        RestAssured.config = new RestAssuredConfig().encoderConfig(encoderConfig().defaultContentCharset("UTF-8"));
    }


    @Category(ApiTest.class)
    @Test
    public void createMultiplePatient() {

        long startTime = System.currentTimeMillis();
//        for (int i = 1; i <= 1; i++) {
        for (int i = 1; i <= 1000000; i++) {
            long loopStartTime = System.currentTimeMillis();
            System.out.println(i + " ");
            verifyCreatePatientForBarisalDivision();
            verifyCreatePatientForDhakaDivision();
            verifyCreatePatientForKhulnaDivision();
            verifyCreatePatientForChittagongDivision();
            verifyCreatePatientForRajshahiDivision();
            verifyCreatePatientForRangpurDivision();
            verifyCreatePatientForSylhetDivision();
            long loopEndTime = System.currentTimeMillis();
            System.out.println("Time Taken: " + (loopEndTime - loopStartTime)  + " MilliSeconds");
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time Taken: " + (endTime - startTime) / 1000 + " Seconds");
    }


    public void verifyCreatePatientForBarisalDivision() {

        PatientFactory dataStore = new PatientFactory();
        primaryPatient = dataStore.patientWithAllFieldDetails;

        JSONObject person = createPatientDataToPost(dataStore);
        try {
            person.put("present_address", dataStore.getAddressJsonForBarisal());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        createPatient(person);
    }

    public void verifyCreatePatientForDhakaDivision() {

        PatientFactory dataStore = new PatientFactory();
        primaryPatient = dataStore.patientWithAllFieldDetails;

        JSONObject person = createPatientDataToPost(dataStore);
        try {
            person.put("present_address", dataStore.getAddressJsonForDhaka());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        createPatient(person);
    }

    public void verifyCreatePatientForKhulnaDivision() {

        PatientFactory dataStore = new PatientFactory();
        primaryPatient = dataStore.patientWithAllFieldDetails;

        JSONObject person = createPatientDataToPost(dataStore);
        try {
            person.put("present_address", dataStore.getAddressJsonForKhulna());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        createPatient(person);
    }

    public void verifyCreatePatientForChittagongDivision() {

        PatientFactory dataStore = new PatientFactory();
        primaryPatient = dataStore.patientWithAllFieldDetails;

        JSONObject person = createPatientDataToPost(dataStore);
        try {
            person.put("present_address", dataStore.getAddressJsonForChittagong());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        createPatient(person);
    }

    public void verifyCreatePatientForRangpurDivision() {

        PatientFactory dataStore = new PatientFactory();
        primaryPatient = dataStore.patientWithAllFieldDetails;

        JSONObject person = createPatientDataToPost(dataStore);
        try {
            person.put("present_address", dataStore.getAddressJsonForRangpur());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        createPatient(person);
    }

    public void verifyCreatePatientForRajshahiDivision() {

        PatientFactory dataStore = new PatientFactory();
        primaryPatient = dataStore.patientWithAllFieldDetails;

        JSONObject person = createPatientDataToPost(dataStore);
        try {
            person.put("present_address", dataStore.getAddressJsonForRajshahi());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        createPatient(person);
    }

    public void verifyCreatePatientForSylhetDivision() {

        PatientFactory dataStore = new PatientFactory();
        primaryPatient = dataStore.patientWithAllFieldDetails;

        JSONObject person = createPatientDataToPost(dataStore);
        try {
            person.put("present_address", dataStore.getAddressJsonForSylhet());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        createPatient(person);
    }

    private void createPatient(JSONObject person) {
//                System.out.println(person.toString());
        given().contentType("application/json")
                .body(person.toString())
                .when().post("/patients")
               .then().log().status();
//                .assertThat().statusCode(201);

//        given().pathParam("nid", primaryPatient.getNid())
//                .when().get("/patients?nid={nid}")
//                .then()
//                .body("results.hid[0]", Matchers.notNullValue());
        System.out.print("Patient with NID " + primaryPatient.getNid() + " Created in MCI ");
    }

    private JSONObject createPatientDataToPost(PatientFactory dataStore) {
        JSONObject person = new JSONObject();

        try {

            JSONObject phone_number = new JSONObject();
            person.put("nid", primaryPatient.getNid());
            person.put("bin_brn", primaryPatient.getBinBRN());
            person.put("uid", primaryPatient.getUid());
            person.put("given_name", primaryPatient.getGiven());
            person.put("sur_name", primaryPatient.getFamily());
            person.put("date_of_birth", "2000-03-01");
            person.put("gender", "M");
            person.put("occupation", "02");
            person.put("edu_level", "02");

            phone_number.put("country_code", "88");
            phone_number.put("area_code", "");
            phone_number.put("number", primaryPatient.getNid().substring(2));
            phone_number.put("extension", "");
            person.put("phone_number", phone_number);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return person;
    }

    @After
    public void tearDown() {
        RestAssured.reset();
    }

}
