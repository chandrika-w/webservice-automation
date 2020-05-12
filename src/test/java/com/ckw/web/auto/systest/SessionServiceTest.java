package com.ckw.web.auto.systest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.given;

public class SessionServiceTest extends WebServiceTestBase{

    @Test
    public void verifySessionWithValidData(){
        JSONObject requestObject = new JSONObject();
        requestObject.put("username", "demo");
        requestObject.put("password", "demo");
        Response response = given()
                //.auth()
                //.basic("demo","demo")
                .body(requestObject.toString())
                .when()
                .log()
                .all()
                .post("/session");
        Assertions.assertEquals(200,response.getStatusCode());
        response.getBody().prettyPrint();
    }

    @ParameterizedTest
    @CsvSource({
            "demo, demo1",
            "demo1, demo",
            "demo1, demo1"
    })
    public void verifySessionWithInvalidData(String username, String password){
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
        Assertions.assertEquals(401,response.getStatusCode());
        response.getBody().prettyPrint();
    }
}
