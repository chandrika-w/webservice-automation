package com.ckw.web.auto.systest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class UserServiceTest extends WebServiceTestBase {

    @BeforeEach
    public void setUpEachTest(){
        token = generateToken("demo", "demo");
    }

    @Test
    //Looking for a valid user
    public void getValidUserInfor(){
        addUser();
    }

    @Test
    public void getUserWithInvalidUsername(){
        String uniqueUserId=UUID.randomUUID().toString();
        Assertions.assertEquals("new",getUserId(uniqueUserId));
    }

    @Test
    public void getUserWithInvalidToken(){
        Response response = given()
                .header("authorization", "invalid"+token)
                .queryParam("name", "demo")
                .when()
                .log()
                .all()
                .get("/user");
        Assertions.assertEquals(401, response.getStatusCode());
    }

    @Test
    //Add a user
    public void addNewUser() {
        addUser();
        System.out.println("Successfully added the record  ");
    }

    @Test
    public void addUserWithInvalidToken(){
        String uniqueUserId = "USER_"+System.currentTimeMillis();
        JSONObject requestObject = new JSONObject();
        requestObject.put("active", true);
        requestObject.put("blocked", false);
        requestObject.put("company", "MyCompany2");
        requestObject.put("email", "abc@mycompany.kom");
        requestObject.put("firstName", "Jack2");
        requestObject.put("fullName", "Jack2 And Jill2");
        requestObject.put("lastName", "Jill2");
        requestObject.put("password", "abc123112");
        requestObject.put("role", "USER");
        requestObject.put("userId", uniqueUserId);
        Response response = given()
                //.auth()
                //.basic("demo","demo")
                .headers(Map.of("Authorization","invalid"+token, CONTENT_TYPE, "application/json"))
                .body(requestObject.toString())
                .when()
                .log()
                .all()
                .post("/user");
        Assertions.assertEquals(401, response.getStatusCode());
    }

    @Test
    public void addUserWithInvalidBody(){
        String uniqueUserId = "USER_"+System.currentTimeMillis();
        JSONObject requestObject = new JSONObject();
        requestObject.put("active", "bb");
        requestObject.put("blocked", false);
        requestObject.put("company", "MyCompany2");
        requestObject.put("email", "abc@mycompany.kom");
        requestObject.put("firstName", "Jack2");
        requestObject.put("fullName", "Jack2 And Jill2");
        requestObject.put("lastName", "Jill2");
        requestObject.put("password", "abc123112");
        requestObject.put("role", "USER");
        requestObject.put("userId", uniqueUserId);
        Response response = given()
                //.auth()
                //.basic("demo","demo")
                .headers(Map.of("Authorization",token, CONTENT_TYPE, "application/json"))
                .body(requestObject.toString())
                .when()
                .log()
                .all()
                .post("/user");
        Assertions.assertEquals(400, response.getStatusCode());
    }

    private void addUser(){
        String uniqueUserId = "USER_"+System.currentTimeMillis();
        JSONObject requestObject = new JSONObject();
        requestObject.put("active", true);
        requestObject.put("blocked", false);
        requestObject.put("company", "MyCompany2");
        requestObject.put("email", "abc@mycompany.kom");
        requestObject.put("firstName", "Jack2");
        requestObject.put("fullName", "Jack2 And Jill2");
        requestObject.put("lastName", "Jill2");
        requestObject.put("password", "abc123112");
        requestObject.put("role", "USER");
        requestObject.put("userId", uniqueUserId);
        Response response = given()
                //.auth()
                //.basic("demo","demo")
                .headers(Map.of("Authorization",token, CONTENT_TYPE, "application/json"))
                .body(requestObject.toString())
                .when()
                .log()
                .all()
                .post("/user");
        Assertions.assertEquals(200, response.getStatusCode());
        String userIdFrmDatabase=getUserId(uniqueUserId);
        Assertions.assertEquals(uniqueUserId, userIdFrmDatabase);
    }

    private String getUserId(String userId){
        Response response = given()
                .header("authorization", token)
                .queryParam("name", userId)
                .when()
                .log()
                .all()
                .get("/user");
        Assertions.assertEquals(200, response.getStatusCode());
        JSONObject jsonObjectSearch= new JSONObject(response.prettyPrint());
        return jsonObjectSearch.getJSONObject("data").getString("userId");

    }

}
