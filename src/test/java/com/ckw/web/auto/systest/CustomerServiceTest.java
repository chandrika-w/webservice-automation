package com.ckw.web.auto.systest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class CustomerServiceTest extends WebServiceTestBase{

    @BeforeEach
    public void setUpEachTest(){
        token = generateToken("demo", "demo");
    }

    @Test
    public void getCustomerList(){
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", 2);
        queryParams.put("size", 20);
        Response response=given()
                .header("Authorization",token)
                .queryParams(queryParams)
                .when()
                .log()
                .all()
                .get("/api/customers");
        Assertions.assertEquals(200,response.getStatusCode());
        response.getBody().prettyPrint();
    }

    @Test
    public void DeleteCustomer(){
        //Add a customer
        int uniqueCustomerId=getRandomInt();
        addCustomer(uniqueCustomerId);

        //Delete the customer
        Response response2 = given()
                .header("authorization", token)
                .when()
                .log()
                .all()
                .delete("/api/customers/"+uniqueCustomerId);
        Assertions.assertEquals(200, response2.getStatusCode());

        //Search the deleted customer
        JSONObject jsonObjectSearch = getCustomer(uniqueCustomerId);
        Assertions.assertEquals(0, jsonObjectSearch.getInt("totalItems"));
    }

    private int getRandomInt(){
        Random random = new Random();
        final int range = 100000 - 1000 + 1;
        return random.nextInt(range) + 1000;
    }

    private int getCustomerId(int customerId){
        JSONObject jsonObjectSearch = getCustomer(customerId);
        return jsonObjectSearch.getJSONArray("items").getJSONObject(0).getInt("id");
    }

    private JSONObject getCustomer(int customerId){
        Response response = given()
                .header("authorization", token)
                .queryParam("customerid", customerId)
                .when()
                .log()
                .all()
                .get("/api/customers");
        Assertions.assertEquals(200, response.getStatusCode());
        return new JSONObject(response.prettyPrint());
    }

    private void addCustomer(int customerId){

        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("address1","Add1");
        jsonObject1.put("address2","Add2");
        jsonObject1.put("city","City1");
        jsonObject1.put("company","Com1");
        jsonObject1.put("country","Australia");
        jsonObject1.put("email","abc11@aaa.com");
        jsonObject1.put("firstname","AAAA");
        jsonObject1.put("id",customerId);
        jsonObject1.put("lastname","BBBB");
        jsonObject1.put("phone","533234434");
        jsonObject1.put("postalcode","5409");
        jsonObject1.put("state","Tas");

        Response response=given()
                .headers(Map.of("Authorization",token, CONTENT_TYPE, "application/json"))
                .body(jsonObject1.toString())
                .when()
                .log()
                .all()
                .post("/api/customers");
        Assertions.assertEquals(200, response.getStatusCode());
        //Search weather customer added
        int userIdFrmDatabase= getCustomerId(customerId);
        Assertions.assertEquals(customerId, userIdFrmDatabase);
    }

}


