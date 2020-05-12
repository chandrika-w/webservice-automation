package com.ckw.web.auto.systest;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static java.util.jar.Attributes.Name.CONTENT_TYPE;

public class EmployeeServiceTest extends WebServiceTestBase {
    @BeforeEach
    public void setUpEachTest(){
        token = generateToken("demo", "demo");
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
                .headers(Map.of("Authorization",token, HttpHeaders.CONTENT_TYPE, "application/json"))
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
