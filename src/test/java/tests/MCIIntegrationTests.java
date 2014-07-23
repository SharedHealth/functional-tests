package tests;


import data.PatientData;
import domain.Patient;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.*;

public class MCIIntegrationTests {

    protected Patient primaryPatient;


    @Before
    public void setUp(){

        RestAssured.baseURI = "http://172.18.46.53";
        RestAssured.port = 8081;
        RestAssured.basePath = "/api/v1";
        RestAssured.authentication = basic("mci", "password");
        RestAssured.rootPath = "";
    }

    @Test
    public void verifyGetPatientByHID(){
        given().pathParam("hid", "9000000000000351311")
                .when().get("/patients/{hid}")
                .then().body("gender", Matchers.equalTo("1"))
                        .body("hid", Matchers.equalTo("9000000000000351311"))
                        .body("nid", Matchers.equalTo("9000000351311"))
                        .body("first_name", Matchers.equalTo("A351311"))
                        .body("last_name", Matchers.equalTo("ATEST"))
                        .body("date_of_birth", Matchers.equalTo("2000-03-01"))
                        .body("gender", Matchers.equalTo("1"))
                        .body("occupation", Matchers.equalTo("02"))
                        .body("edu_level", Matchers.equalTo("02"))
                        .body("fathers_first_name", Matchers.equalTo("Primary One"))
                        .body("present_address.address_line", Matchers.equalTo("Test"))
                        .body("present_address.division_id", Matchers.equalTo("40"))
                        .body("present_address.district_id", Matchers.equalTo("4018"))
                        .body("present_address.upazilla_id", Matchers.equalTo("401823"))
                        .body("present_address.union_id", Matchers.equalTo("40010801"));
    }

    @Test
    public void verifyCreatePatient(){


        primaryPatient = PatientData.newPatient1;

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


        System.out.println(person);

        given().contentType("application/json")
                .body(person.toString())
                .when().post("/patients")
                .then().assertThat().statusCode(201);

        given().pathParam("hid", primaryPatient.getHid())
                .when().get("/patients/{hid}")
                .then().body("gender", Matchers.equalTo("1"))
                .body("hid", Matchers.equalTo(primaryPatient.getHid()))
                .body("nid", Matchers.equalTo(primaryPatient.getNid()))
                .body("first_name", Matchers.equalTo(primaryPatient.getFirstName()))
                .body("last_name", Matchers.equalTo(primaryPatient.getLastName()))
                .body("date_of_birth", Matchers.equalTo("2000-03-01"))
                .body("gender", Matchers.equalTo("1"))
                .body("occupation", Matchers.equalTo("02"))
                .body("edu_level", Matchers.equalTo("02"))
                .body("fathers_first_name", Matchers.equalTo(primaryPatient.getPrimaryContact()))
                .body("present_address.address_line", Matchers.equalTo(primaryPatient.getAddress().getAddressLine1()))
                .body("present_address.division_id", Matchers.equalTo("40"))
                .body("present_address.district_id", Matchers.equalTo("4018"))
                .body("present_address.upazilla_id", Matchers.equalTo("401823"))
                .body("present_address.union_id", Matchers.equalTo("40010801"));
    }


    }
