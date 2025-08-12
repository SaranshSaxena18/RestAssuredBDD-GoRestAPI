package StepDefinition;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.CreateUserSuccessResponsePojo;
import pojo.GetUserResponsePojo;
import utils.PropertyFileReader;
import utils.SpecificationFactory;
import utils.testDataBuild;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class StepDefinition {
	RequestSpecification reqSpec;
	Response response;
	CreateUserSuccessResponsePojo createUserSuccessResponsePojo;
	String name, gender, email, status;
	static int newUserId=0, existingUserId=0;
	@Given("{string} API payload should be valid with {string}, {string}, {string}, {string}")
	public void api_payload_should_be_valid_with(String APIName, String name, String gender, String email, String status) 
	{
		this.name = name;
		this.gender = gender;
		this.email = email;
		this.status = status;
		if(APIName.equalsIgnoreCase("Add User"))
		{
			reqSpec = given().spec(SpecificationFactory.getRequestSpecification()).body(testDataBuild.createUserPayload(name, gender, email, status));
		}
	}

	@Given("{string} with a valid userId {int}")
	public void with_a_valid_userId(String APIName, int UserId) 
	{ 
		this.existingUserId = UserId;
		if(APIName.equalsIgnoreCase("Get User"))
		{
			reqSpec = given().spec(SpecificationFactory.getRequestSpecification());
		}
	}
	
	@When("API is hit with {string} https request")
	public void api_is_hit_with_https_request(String APIMethod) {
		if(APIMethod.equalsIgnoreCase("POST"))
		{
			response = reqSpec.when().post(PropertyFileReader.getEndPoint(APIMethod));
		}
		else if(APIMethod.equalsIgnoreCase("GET"))
		{
			response = reqSpec.when().get(PropertyFileReader.getEndPoint(APIMethod) + existingUserId);
		}
	}

	@Then("{string} request response should be valid")
	public void response_should_be_valid(String APIMethod) {
		if(APIMethod.equalsIgnoreCase("POST"))
		{
			response = response.then().spec(SpecificationFactory.getResponseSpecification(APIMethod)).log().all().extract().response();
			createUserSuccessResponsePojo = response.as(CreateUserSuccessResponsePojo.class);
			newUserId = createUserSuccessResponsePojo.getId();
			assertEquals(name, createUserSuccessResponsePojo.getName());
			assertEquals(gender, createUserSuccessResponsePojo.getGender());
			assertEquals(email, createUserSuccessResponsePojo.getEmail());
			assertEquals(status, createUserSuccessResponsePojo.getStatus());
		}
	}
	
	@Then("{string} request response should be valid with following details {string}, {string}, {string}, {string}")
	public void request_response_should_be_valid_with_following_details(String APIMethod, String name, String gender, String email, String status) 
	{
		this.name = name;
		this.gender = gender;
		this.email = email;
		this.status = status;
		if(APIMethod.equalsIgnoreCase("GET"))
		{
			response = response.then().spec(SpecificationFactory.getResponseSpecification("GET single user")).log().all().extract().response();
			GetUserResponsePojo getUserResponse = response.as(GetUserResponsePojo.class);
			assertEquals(existingUserId, getUserResponse.getId());
			assertEquals(name, getUserResponse.getName());
			assertEquals(gender, getUserResponse.getGender());
			assertEquals(email, getUserResponse.getEmail());
			assertEquals(status, getUserResponse.getStatus());
		}
	}
	
	@Then("created user should exist")
	public void created_user_should_exist() {
	    response = given().spec(SpecificationFactory.getRequestSpecification()).when().get(PropertyFileReader.getEndPoint("get")+newUserId).then().spec(SpecificationFactory.getResponseSpecification("Get single user")).extract().response();
	    createUserSuccessResponsePojo = response.as(CreateUserSuccessResponsePojo.class);
	    assertEquals(newUserId, createUserSuccessResponsePojo.getId());
		assertEquals(name, createUserSuccessResponsePojo.getName());
		assertEquals(gender, createUserSuccessResponsePojo.getGender());
		assertEquals(email, createUserSuccessResponsePojo.getEmail());
		assertEquals(status, createUserSuccessResponsePojo.getStatus());
	}

	//@Test
	public void getFunction()
	{
		RestAssured.baseURI = "https://gorest.co.in";
		given().log().all().header("Authorization","Bearer dc231eda4693ea8fe44e764b2b79051d2486cd6e70b447256ca5ccc4cea8b5c1")
		.when().get("/public/v2/users")
		.then().log().all().statusCode(200);
	}
	//@Test
	public void postFunction()
	{
		RestAssured.baseURI = "https://gorest.co.in";
		given()
		  .log().all()
		  .header("Authorization","Bearer dc231eda4693ea8fe44e764b2b79051d2486cd6e70b447256ca5ccc4cea8b5c1")
		  .header("Content-Type","application/json")
		  .body("{\r\n"
		      + "    \"name\": \"Tenali Ramakrishna\",\r\n"
		      + "    \"gender\": \"male\",\r\n"
		      + "    \"email\": \"tenali.ramakrishna@15ce1.com\",\r\n"
		      + "    \"status\": \"active\"\r\n"
		      + "}")
		.when().post("/public/v2/users")
		.then().log().all().statusCode(201);

	}
	//@Test
	public void putfunction()
	{
		RestAssured.baseURI = "https://gorest.co.in";
		given().log().all().header("Authorization","Bearer dc231eda4693ea8fe44e764b2b79051d2486cd6e70b447256ca5ccc4cea8b5c1")
		.header("Content-Type","application/json")
		.body("{\r\n"
				+ "    \"id\":  \"8056720\",\r\n"
				+ "    \"name\": \"Tenali Ramakrishna\",\r\n"
				+ "    \"gender\": \"female\",\r\n"
				+ "    \"email\": \"tenali.ramakrishna@15ce1.com\",\r\n"
				+ "    \"status\": \"active\"\r\n"
				+ "}")
		.when().put("/public/v2/users/").then().log().all();
	}
	//@Test
	public void delfunction()
	{
		int id = 8056720;
		RestAssured.baseURI = "https://gorest.co.in";
		given().log().all().pathParam("id", id).header("Authorization","Bearer dc231eda4693ea8fe44e764b2b79051d2486cd6e70b447256ca5ccc4cea8b5c1")
		.when().delete("/public/v2/users/{id}").then().log().all().statusCode(204);
		
	}
}
//{
//    "id": 8056720,
//    "name": "Tenali Ramakrishna",
//    "email": "tenali.ramakrishna@15ce1.com",
//    "gender": "male",
//    "status": "active"
//}