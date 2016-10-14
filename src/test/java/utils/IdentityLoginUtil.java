package utils;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class IdentityLoginUtil {

    private static final String IDP_ACCESS_TOKEN_KEY = "access_token";

    public static String login(IdpUserEnum user) {
        return given().
                header("X-Auth-Token", user.getAuthToken()).header("client_id", user.getClientId()).
                formParam("email", user.getEmail()).formParam("password", user.getPassword())
                .post("http://172.18.46.60:8084/signin")
                .then().
                        statusCode(SC_OK)
                .extract().
                        response().jsonPath().getString(IDP_ACCESS_TOKEN_KEY);
    }

    public static String loginFor(IdpUserEnum loginUser, IdpUserEnum logingSystemUser) {
        return given().
                header("X-Auth-Token", logingSystemUser.getAuthToken()).header("client_id", logingSystemUser.getClientId()).
                formParam("email", loginUser.getEmail()).formParam("password", loginUser.getPassword())
                .post("http://172.18.46.60:8084/signin")
                .then().
                        statusCode(SC_OK)
                .extract().
                        response().jsonPath().getString(IDP_ACCESS_TOKEN_KEY);
    }
}
