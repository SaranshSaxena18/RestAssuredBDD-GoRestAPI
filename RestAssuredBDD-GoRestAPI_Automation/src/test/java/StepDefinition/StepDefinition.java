package StepDefinition;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import utils.PropertyFileReader;
import utils.SpecificationFactory;
import utils.testDataBuild;

import static io.restassured.RestAssured.*;
import org.testng.annotations.Test;

public class StepDefinition {
	RequestSpecification reqSpec;
	
	@Given("{string} API payload should be valid with {string}, {string}, {string}, {string}")
	public void api_payload_should_be_valid_with(String APIName, String name, String gender, String email, String status) 
	{
		if(APIName.equalsIgnoreCase("Add User"))
		{
			reqSpec = given().spec(SpecificationFactory.getRequestSpecification()).body(testDataBuild.createUserPayload(name, gender, email, status));
		}
	}

	@When("API is hit with {string} https request")
	public void api_is_hit_with_https_request(String APIMethod) {
		if(APIMethod.equalsIgnoreCase("POST"))
		{
			reqSpec.when().post(SpecificationFactory.getEndPoint("PostEndPoint"));
		}
	}

	@Then("{string} {string} response should be recieved")
	public void response_should_be_recieved(String string, String string2) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
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