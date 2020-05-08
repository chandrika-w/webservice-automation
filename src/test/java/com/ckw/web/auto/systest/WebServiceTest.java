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
import static javax.swing.UIManager.get;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class WebServiceTest {

   // private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODgwNDQ2ODksInN1YiI6Ik1yaW5tb3lNYWp1bWRhciIsInVzZXJJZCI6ImRlbW8iLCJyb2xlIjoiVVNFUiJ9.mVjXei7K8TBaFbL_fq6jBQrp-9edF0i2Fer-Pexs6bg";
    private String token;
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 9119;
    }

    @BeforeEach
    public void setUpEachTest(){
        token = generateToken("demo", "demo");
    }

    @Test
    //Looking for a user
    public void getUserInfor(){
        addUser();
    }

    @Test
    //Add a user
    public void buildJsonObject() {
        addUser();
        System.out.println("Successfully added the record  ");
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

    @Test
    //Get list of customers
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
    public void addNewCustomer(){
        //Add a customer
        int uniqueCustomerId=getRandomInt();
        addCustomer(uniqueCustomerId);
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

    @Test
    public void deleteEmployee(){
        //add an employee
        int uniqueEmployeeId=getRandomInt();
        addEmployee(uniqueEmployeeId);
        //Delete an employee
        Response response = given()
                .header("authorization", token)
                .when()
                .log()
                .all()
                .delete("/api/Employees/"+uniqueEmployeeId);
        Assertions.assertEquals(200, response.getStatusCode());

        //Search the deleted employee
        JSONObject jsonObjectSearch = getEmployee(uniqueEmployeeId);
        Assertions.assertEquals(0, jsonObjectSearch.getInt("totalItems"));
    }

    @Test
    public void addNewEmployee(){
        //Add an employee
        int uniqueEmployeeId=getRandomInt();
        addEmployee(uniqueEmployeeId);
    }

    @Test
    public void listOfEmployees(){
        Response response=given()
                .header("Authorization",token)
                .queryParams("size",20)
                .when()
                .log()
                .all()
                .get("/api/employees");
        Assertions.assertEquals(200,response.getStatusCode());
        response.getBody().prettyPrint();
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

        private String generateToken(String username, String password) {
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

    private void addEmployee(int uniqueEmployeeId){
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("address1","Add1");
        jsonObject1.put("address2","Add2");
        jsonObject1.put("avatar","xyz");
        jsonObject1.put("city","City1");
        jsonObject1.put("country","Australia");
        jsonObject1.put("department","accounts");
        jsonObject1.put("email","abc11@aaa.com");
        jsonObject1.put("firstname","AAAA");
        jsonObject1.put("id",uniqueEmployeeId);
        jsonObject1.put("jobTitle","Accountant");
        jsonObject1.put("lastname","BBBB");
        jsonObject1.put("managerId",126666);
        jsonObject1.put("phone","533234434");
        jsonObject1.put("postalcode","5409");
        jsonObject1.put("state","Tas");

        Response response=given()
                .headers(Map.of("Authorization",token, CONTENT_TYPE, "application/json"))
                .body(jsonObject1.toString())
                .when()
                .log()
                .all()
                .post("/api/employees");
        Assertions.assertEquals(200, response.getStatusCode());
        //Search weather employee added
        int employeeIdFrmDatabase= getEmployeeId(uniqueEmployeeId);
        Assertions.assertEquals(uniqueEmployeeId, employeeIdFrmDatabase);
    }

    private int getEmployeeId(int employeeId){
        JSONObject jsonObjectSearch = getEmployee(employeeId);
        return jsonObjectSearch.getJSONArray("items").getJSONObject(0).getInt("id");
    }

    private JSONObject getEmployee(int employeeId){
        Response response = given()
                .header("authorization", token)
                .queryParam("employeeid", employeeId)
                .when()
                .log()
                .all()
                .get("/api/employees");
        Assertions.assertEquals(200, response.getStatusCode());
        return new JSONObject(response.prettyPrint());
    }



}
