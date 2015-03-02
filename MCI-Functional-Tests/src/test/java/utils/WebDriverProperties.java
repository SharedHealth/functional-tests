package utils;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Properties;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.EncoderConfig.encoderConfig;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


public class WebDriverProperties {

    public static String getProperty(String propertyName) {
        if (isNotBlank(System.getProperty(propertyName))) {
            return System.getProperty(propertyName);
        } else {
            Properties properties = loadProperties();
            return properties.getProperty(propertyName);
        }
    }

    private static Properties loadProperties()  {
        Properties properties = new Properties();
        try {
            properties.load(WebDriverProperties.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static String getToken() throws JSONException {

        RestAssured.baseURI = WebDriverProperties.getProperty("MCI_IDENTITY_SERVER_URI");
        RestAssured.basePath = "";
        RestAssured.port = Integer.parseInt(WebDriverProperties.getProperty("MCI_IDENTITY_PORT"));
        RestAssured.rootPath = "";
        RestAssured.config = new RestAssuredConfig().encoderConfig(encoderConfig().defaultContentCharset("UTF-8"));

        JSONObject credential = new JSONObject();
        credential.put("user", WebDriverProperties.getProperty("MCI_USER_NAME"));
        credential.put("password", WebDriverProperties.getProperty("MCI_USER_PASSWORD"));

        Response response = given().contentType("application/json")
                .body(credential.toString())
                .post("/login").andReturn();


        return response.getBody().asString().replace("\"", "");

    }
}
