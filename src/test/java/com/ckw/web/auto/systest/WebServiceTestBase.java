package com.ckw.web.auto.systest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class WebServiceTestBase {

    protected String token;
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 9119;
    }
    protected String generateToken(String username, String password) {
        JSONObject requestObject = new JSONObject();
        requestObject.put("username", username);
        requestObject.put("password", password);
        Response response = given()
                //.auth()
                //.basic("demo","demo")
                .body(requestObject.toString())
                .when()
                .log()
                .all()
                .post("/session");
        JSONObject jsonResponse = new JSONObject(response.getBody().prettyPrint());
        return jsonResponse.getJSONObject("item").getString("token");
    }
}
