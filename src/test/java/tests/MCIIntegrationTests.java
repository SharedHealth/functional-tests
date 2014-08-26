package tests;


import categories.MciApiTest;
import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.basic;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.EncoderConfig.encoderConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import data.PatientData;
import domain.Patient;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebDriver;

public class MCIIntegrationTests {

    protected Patient primaryPatient;
    private static WebDriver driver;

    @Before
    public void setUp() {

        RestAssured.baseURI = "http://172.18.46.53";
        RestAssured.port = 8081;
        RestAssured.basePath = "/api/v1";
        RestAssured.authentication = basic("mci", "password");
        RestAssured.rootPath = "";
        RestAssured.config = new RestAssuredConfig().encoderConfig(encoderConfig().defaultContentCharset("UTF-8"));
    }

    @Test
    public void verifyGetPatientByNID() {
        given().pathParam("nid", "9000000794053")
                .when().get("/patients?nid={nid}")
                .then().body("gender", Matchers.equalTo("1"))
                .body("hid", Matchers.notNullValue())
                .body("nid", Matchers.equalTo("9000000794053"))
                .body("first_name", Matchers.equalTo("A794053"))
                .body("last_name", Matchers.equalTo("ATEST"))
                .body("date_of_birth", Matchers.equalTo("2000-03-01"))
                .body("gender", Matchers.equalTo("1"))
                .body("occupation", Matchers.equalTo("02"))
                .body("edu_level", Matchers.equalTo("02"))
                .body("fathers_first_name", Matchers.equalTo("Primary One"))
                .body("present_address.address_line", Matchers.equalTo("Test"))
                .body("present_address.division_id", Matchers.equalTo("10"))
                .body("present_address.district_id", Matchers.equalTo("09"))
                .body("present_address.upazilla_id", Matchers.equalTo("18"))
                .body("present_address.city_corporation", Matchers.equalTo("16"))
                .body("present_address.ward", Matchers.equalTo("01"));

        System.out.println("Patient with NID " + "9000000351311" + " verified in MCI");

    }


    @Test
    public void verifyGetPatientByHID() {
        given().pathParam("hid", "9000000000000351311")
                .when().get("/patients/{hid}")
                .then().body("hid", Matchers.equalTo("9000000000000351311"))
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

        System.out.println("Patient with HID " + "9000000000000351311" + " verified in MCI");
    }

    @Category(MciApiTest.class)
    @Test
    public void verifyCreatePatientWithAllData() {


        primaryPatient = new PatientData().newPatient3;

        JSONObject person = new JSONObject();
        JSONObject present_address = new JSONObject();
        JSONObject permanent_address = new JSONObject();
        try {
            person.put("nid", primaryPatient.getNid());
            person.put("uid", primaryPatient.getUid());
            person.put("first_name", primaryPatient.getFirstName());
            person.put("middle_name", "Ali");
            person.put("last_name", primaryPatient.getLastName());
            person.put("full_name_bangla", " হোসেন মন্ডল");
            person.put("fathers_name_bangla", "এ বি এম আখতার হোসেন মন্ডল");
            person.put("fathers_first_name", "Akhtar");
            person.put("fathers_middle_name", "Hossaine");
            person.put("fathers_last_name", "Mondal");
            person.put("fathers_uid", primaryPatient.getFatherUid());
            person.put("fathers_nid", primaryPatient.getFatherNid());
            person.put("fathers_brn", primaryPatient.getFatherBRN());
            person.put("mothers_name_bangla", "আনোয়ারা খাতুন");
            person.put("mothers_first_name", "Anowara");
            person.put("mothers_middle_name", "Khatun");
            person.put("mothers_last_name", "antora");
            person.put("mothers_uid", primaryPatient.getMotherUid());
            person.put("mothers_nid", primaryPatient.getMotherNid());
            person.put("mothers_brn", primaryPatient.getMotherBRN());
            person.put("place_of_birth", "Dhaka");
            person.put("marriage_id", "12345678");
            person.put("spouse_name_bangla", "আখতার");
            person.put("spouse_name", "Akhtar");
            person.put("spouse_uid_nid", "1234567890");
            person.put("date_of_birth", "1983-09-21");
            person.put("gender", "2");
            person.put("ethnicity", 11);
            person.put("marital_status", 5);
            person.put("religion", 1);
            person.put("blood_group", 1);
            person.put("bin_brn", primaryPatient.getBinBRN());
            person.put("occupation", 11);
            person.put("edu_level", "01");
            person.put("nationality", "bangladeshi");
            person.put("disability", 1);
            present_address.put("address_line", primaryPatient.getAddress().getAddressLine1());
            present_address.put("division_id", 40);
            present_address.put("district_id", 18);
            present_address.put("union_id", 01);
            present_address.put("upazilla_id", 23);
            present_address.put("holding_number", "444444444");
            present_address.put("street", "Dhaka");
            present_address.put("area_mouja", "Kallayanpur");
            present_address.put("village", 12);
            present_address.put("post_office", "Dhaka");
            present_address.put("post_code", "1207");
            present_address.put("ward", 13);
            present_address.put("thana", 45);
            present_address.put("city_corporation", 12);
            present_address.put("country", "050");
            person.put("present_address", present_address);
            permanent_address.put("address_line", "xyz");
            permanent_address.put("division_id", 10);
            permanent_address.put("district_id", 10);
            permanent_address.put("union_id", 10);
            permanent_address.put("upazilla_id", 10);
            permanent_address.put("holding_number", "444444444");
            permanent_address.put("street", "Dhaka");
            permanent_address.put("area_mouja", "Kallayanpur");
            permanent_address.put("village", 12);
            permanent_address.put("post_office", "Dhaka");
            permanent_address.put("post_code", "1207");
            permanent_address.put("ward", 13);
            permanent_address.put("thana", 45);
            permanent_address.put("city_corporation", 12);
            permanent_address.put("country", "050");
            person.put("permanent_address", present_address);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        given().contentType("application/json")
                .body(person.toString())
                .when().post("/patients")
                .then().assertThat().statusCode(201);

        System.out.println("Patient with NID " + primaryPatient.getNid() + " created");


        given().pathParam("nid", primaryPatient.getNid())
                .when().get("/patients?nid={nid}")
                .then()
                .body("nid", Matchers.equalTo(primaryPatient.getNid()))
                .body("first_name", Matchers.equalTo(primaryPatient.getFirstName()))
                .body("last_name", Matchers.equalTo(primaryPatient.getLastName()))
                .body("date_of_birth", Matchers.equalTo("1983-09-21"))
                .body("gender", Matchers.equalTo("2"))
                .body("occupation", Matchers.equalTo("11"))
                .body("edu_level", Matchers.equalTo("01"))
                .body("fathers_first_name", Matchers.equalTo("Akhtar"))
                .body("present_address.address_line", Matchers.equalTo(primaryPatient.getAddress().getAddressLine1()))
                .body("present_address.division_id", Matchers.equalTo("40"))
                .body("present_address.district_id", Matchers.equalTo("18"))
                .body("present_address.upazilla_id", Matchers.equalTo("23"))
                .body("present_address.city_corporation", Matchers.equalTo("12"))
                .body("present_address.ward", Matchers.equalTo("01"))
                .body("present_address.holding_number", Matchers.equalTo("444444444"))
                .body("present_address.street", Matchers.equalTo("Dhaka"))
                .body("present_address.area_mouja", Matchers.equalTo("Kallayanpur"))
                .body("present_address.village", Matchers.equalTo("12"))
                .body("present_address.post_office", Matchers.equalTo("Dhaka"))
                .body("present_address.post_code", Matchers.equalTo("1207"))
                .body("present_address.ward", Matchers.equalTo("13"))
                .body("present_address.thana", Matchers.equalTo("45"))
                .body("present_address.country", Matchers.equalTo("050"))

                .body("nid", Matchers.equalTo(primaryPatient.getNid()))
                .body("uid", Matchers.equalTo(primaryPatient.getUid()))
                .body("first_name", Matchers.equalTo(primaryPatient.getFirstName()))
                .body("middle_name", Matchers.equalTo("Ali"))
                .body("last_name", Matchers.equalTo(primaryPatient.getLastName()))
                .body("full_name_bangla", Matchers.equalTo(" হোসেন মন্ডল"))
                .body("fathers_name_bangla", Matchers.equalTo("এ বি এম আখতার হোসেন মন্ডল"))
                .body("fathers_first_name", Matchers.equalTo("Akhtar"))
                .body("fathers_middle_name", Matchers.equalTo("Hossaine"))
                .body("fathers_last_name", Matchers.equalTo("Mondal"))
                .body("fathers_uid", Matchers.equalTo(primaryPatient.getFatherUid()))
                .body("fathers_nid", Matchers.equalTo(primaryPatient.getFatherNid()))
                .body("fathers_brn", Matchers.equalTo(primaryPatient.getFatherBRN()))
                .body("mothers_name_bangla", Matchers.equalTo("আনোয়ারা খাতুন"))
                .body("mothers_first_name", Matchers.equalTo("Anowara"))
                .body("mothers_middle_name", Matchers.equalTo("Khatun"))
                .body("mothers_last_name", Matchers.equalTo("antora"))
                .body("mothers_uid", Matchers.equalTo(primaryPatient.getMotherUid()))
                .body("mothers_nid", Matchers.equalTo(primaryPatient.getMotherNid()))
                .body("mothers_brn", Matchers.equalTo(primaryPatient.getMotherBRN()))
                .body("place_of_birth", Matchers.equalTo("Dhaka"))
                .body("marriage_id", Matchers.equalTo("12345678"))
                .body("spouse_name_bangla", Matchers.equalTo("আখতার"))
                .body("spouse_name", Matchers.equalTo("Akhtar"))
                .body("spouse_uid_nid", Matchers.equalTo("1234567890"))
                .body("date_of_birth", Matchers.equalTo("1983-09-21"))
                .body("gender", Matchers.equalTo("2"))
                .body("ethnicity", Matchers.equalTo("11"))
                .body("marital_status", Matchers.equalTo("5"))
                .body("religion", Matchers.equalTo("1"))
                .body("blood_group", Matchers.equalTo("1"))
                .body("bin_brn", Matchers.equalTo(primaryPatient.getBinBRN()))
                .body("occupation", Matchers.equalTo("11"))
                .body("edu_level", Matchers.equalTo("01"))
                .body("nationality", Matchers.equalTo("bangladeshi"))
                .body("disability", Matchers.equalTo("1"));

        System.out.println("Patient with NID " + primaryPatient.getNid() + " verified in MCI");

//Commented out the permanent address verification as it MCI does not return permanent address for now
//                .body("permanent_address.address_line", Matchers.equalTo(primaryPatient.getAddress().getAddressLine1()))
//                .body("permanent_address.division_id", Matchers.equalTo("40"))
//                .body("permanent_address.district_id", Matchers.equalTo("4018"))
//                .body("permanent_address.union_id", Matchers.equalTo("40010801"))
//                .body("permanent_address.upazilla_id", Matchers.equalTo("401823"))
//                .body("permanent_address.holding_number", Matchers.equalTo("444444444"))
//                .body("permanent_address.street", Matchers.equalTo("Dhaka"))
//                .body("permanent_address.area_mouja",Matchers.equalTo( "Kallayanpur"))
//                .body("permanent_address.village", Matchers.equalTo("12"))
//                .body("permanent_address.post_office",Matchers.equalTo( "Dhaka"))
//                .body("permanent_address.post_code", Matchers.equalTo("1207"))
//                .body("permanent_address.ward", Matchers.equalTo("13"))
//                .body("permanent_address.thana", Matchers.equalTo("45"))
//                .body("permanent_address.city_corporation",Matchers.equalTo("12"))
//                .body("permanent_address.country",Matchers.equalTo( "050"))

    }

    @Category(MciApiTest.class)
    @Test
    public void verifyCreatePatient() {

        PatientData dataStore = new PatientData();
        primaryPatient = dataStore.newPatient1;

        JSONObject person = new JSONObject();
        JSONObject present_address = new JSONObject();
        try {
            person.put("nid", primaryPatient.getNid());
            person.put("first_name", primaryPatient.getFirstName());
            person.put("last_name", primaryPatient.getLastName());
            person.put("date_of_birth", "2000-03-01");
            person.put("gender", "1");
            person.put("occupation", "02");
            person.put("edu_level", "02");
            person.put("fathers_first_name", primaryPatient.getPrimaryContact());
            present_address.put("address_line", primaryPatient.getAddress().getAddressLine1());
            present_address.put("division_id", "10");
            present_address.put("district_id", "09");
            present_address.put("upazilla_id", "18");
            present_address.put("city_corporation", "16");
            present_address.put("ward", "01");
            person.put("present_address", present_address);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        given().contentType("application/json")
                .body(person.toString())
                .when().post("/patients")
                .then().assertThat().statusCode(201);
        System.out.println("Patient with NID " + primaryPatient.getNid() + " created in MCI ");

        given().pathParam("nid", primaryPatient.getNid())
                .when().get("/patients?nid={nid}")
                .then().body("gender", Matchers.equalTo("1"))
                .body("hid", Matchers.notNullValue())
                .body("nid", Matchers.equalTo(primaryPatient.getNid()))
                .body("first_name", Matchers.equalTo(primaryPatient.getFirstName()))
                .body("last_name", Matchers.equalTo(primaryPatient.getLastName()))
                .body("date_of_birth", Matchers.equalTo("2000-03-01"))
                .body("gender", Matchers.equalTo("1"))
                .body("occupation", Matchers.equalTo("02"))
                .body("edu_level", Matchers.equalTo("02"))
                .body("fathers_first_name", Matchers.equalTo(primaryPatient.getPrimaryContact()))
                .body("present_address.address_line", Matchers.equalTo(primaryPatient.getAddress().getAddressLine1()))
                .body("present_address.division_id", Matchers.equalTo("10"))
                .body("present_address.district_id", Matchers.equalTo("09"))
                .body("present_address.upazilla_id", Matchers.equalTo("18"))
                .body("present_address.city_corporation", Matchers.equalTo("16"))
                .body("present_address.ward", Matchers.equalTo("01"));

        System.out.println("Patient with NID " + primaryPatient.getNid() + " verified in MCI");
    }

    public void tearDown() {

    }

}
