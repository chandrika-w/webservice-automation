package com.ckw.web.auto.systest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class WebServiceTest extends WebServiceTestBase{

   // private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODgwNDQ2ODksInN1YiI6Ik1yaW5tb3lNYWp1bWRhciIsInVzZXJJZCI6ImRlbW8iLCJyb2xlIjoiVVNFUiJ9.mVjXei7K8TBaFbL_fq6jBQrp-9edF0i2Fer-Pexs6bg";

    @BeforeEach
    public void setUpEachTest(){
        token = generateToken("demo", "demo");
    }

    @Test
    //Gets the version of the rest api
    public void version(){
        Response response= given()
                .header("Authorization",token)
                .when()
                .log()
                .all()
                .get("/version");
        Assertions.assertEquals(200,response.getStatusCode());
        response.getBody().prettyPrint();
    }
}
