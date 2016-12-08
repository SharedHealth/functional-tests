package utils;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class IdentityLoginUtil {

    private static final String IDP_ACCESS_TOKEN_KEY = "access_token";

    public static String login(IdpUserEnum user, final String idpServerBaseUrl) {
        return loginFor(user, user, idpServerBaseUrl);
    }

    public static String loginFor(IdpUserEnum loginUser, IdpUserEnum logingSystemUser, final String idpServerBaseUrl) {
        return given().
                header("X-Auth-Token", logingSystemUser.getAuthToken()).header("client_id", logingSystemUser.getClientId()).
                formParam("email", loginUser.getEmail()).formParam("password", loginUser.getPassword())
                .post(idpServerBaseUrl + "/signin")
                .then().
                        statusCode(SC_OK)
                .extract().
                        response().jsonPath().getString(IDP_ACCESS_TOKEN_KEY);
    }
}
