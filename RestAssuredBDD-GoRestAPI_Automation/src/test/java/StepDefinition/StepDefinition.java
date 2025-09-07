package StepDefinition;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.CreateUserSuccessResponsePojo;
import pojo.EditUserResponsePojo;
import pojo.GetUserResponsePojo;
import utils.GetData;
import utils.PropertyFileReader;
import utils.SpecificationFactory;
import utils.UserDetailsGenerator;
import utils.testDataBuild;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

public class StepDefinition {
	RequestSpecification reqSpec;
	Response response;
	CreateUserSuccessResponsePojo createUserSuccessResponsePojo;
	String name, gender, email, status;
	Map<String, Object> existingUserDetails = new HashMap<>();
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
		StepDefinition.existingUserId = UserId;
		if(APIName.equalsIgnoreCase("Get User"))
		{
			reqSpec = given().spec(SpecificationFactory.getRequestSpecification());
		}
	}
	
	private List<Object[]> scenarioData;//to hold the scenario data fetched from excel
	@Given("{string} with a valid userId")
	public void user_with_a_valid_userId(String APIName) 
	{ 
		String scenarioName = Hooks.getScenarioName();
		scenarioData = GetData.getScenarioData(scenarioName);
		StepDefinition.existingUserId = scenarioData.get(0)[0] != null ? Integer.parseInt(scenarioData.get(0)[1].toString()) : 0;// Extracting userId from the scenario data
		//System.out.println("existingUserId - "+existingUserId);
		if(APIName.equalsIgnoreCase("Delete User"))
		{
			reqSpec = given().spec(SpecificationFactory.getRequestSpecification());
		}
		else if(APIName.equalsIgnoreCase("Edit User"))
		{
			System.out.println("Fetching user details for userId: " + existingUserId+" before PUT request");
		    response = given().spec(SpecificationFactory.getRequestSpecification()).when().get(PropertyFileReader.getEndPoint("get")+existingUserId).then().log().body()
		    		.spec(SpecificationFactory.getResponseSpecification("Get single user")).extract().response();//fetching user details before PUT request so that we can update the details.
		    GetUserResponsePojo getUserResponse = response.as(GetUserResponsePojo.class);
			existingUserDetails.put("idKey", getUserResponse.getId());//storing user details in map to compare it after PUT request
			existingUserDetails.put("genderKey", getUserResponse.getGender());
			existingUserDetails.put("statusKey", getUserResponse.getStatus());
			this.gender = ((String) existingUserDetails.get("genderKey")).equalsIgnoreCase("male") ? "female" : "male"; //changing value
			this.status = ((String) existingUserDetails.get("statusKey")).equalsIgnoreCase("active") ? "inactive" : "active"; //changing value
			this.name = UserDetailsGenerator.generateRandomName();//generating random name
			this.email = UserDetailsGenerator.generateRandomEmail();//generating random email
			System.out.println("Edit User / Put REQUEST body");
			reqSpec = given().log().body().spec(SpecificationFactory.getRequestSpecification()).body(testDataBuild.createEditUserPayload(existingUserId, name, gender, email, status));
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
		else if(APIMethod.equalsIgnoreCase("PUT"))
		{
			response = reqSpec.when().put(PropertyFileReader.getEndPoint(APIMethod) + existingUserId);
		}
		else if(APIMethod.equalsIgnoreCase("DELETE"))
		{
			response = reqSpec.when().delete(PropertyFileReader.getEndPoint(APIMethod) + existingUserId);
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
		else if(APIMethod.equalsIgnoreCase("PUT"))
		{
			System.out.println("Edit User / Put RESPONSE body");
			response = response.then().spec(SpecificationFactory.getResponseSpecification(APIMethod)).log().body().extract().response();
			EditUserResponsePojo getEditUserResponse = response.as(EditUserResponsePojo.class);
			assertEquals(existingUserId, getEditUserResponse.getId());
			assertEquals(name, getEditUserResponse.getName());
			assertEquals(gender, getEditUserResponse.getGender());
			assertEquals(email, getEditUserResponse.getEmail());
			assertEquals(status, getEditUserResponse.getStatus());
		}
		else if (APIMethod.equalsIgnoreCase("DELETE"))
		{
			response.then().statusCode(204);
			System.out.println("User with ID: " + existingUserId + " has been deleted successfully.");
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
	
	@Then("Verify user existence after {string} API request")
	public void verify_user_existence(String APIMethod) {
	    if(APIMethod.equalsIgnoreCase("POST"))
	    {
	    	response = given().spec(SpecificationFactory.getRequestSpecification()).when().get(PropertyFileReader.getEndPoint("get")+newUserId).then().spec(SpecificationFactory.getResponseSpecification("Get single user")).extract().response();
		    createUserSuccessResponsePojo = response.as(CreateUserSuccessResponsePojo.class);
		    assertEquals(newUserId, createUserSuccessResponsePojo.getId());
			assertEquals(name, createUserSuccessResponsePojo.getName());
			assertEquals(gender, createUserSuccessResponsePojo.getGender());
			assertEquals(email, createUserSuccessResponsePojo.getEmail());
			assertEquals(status, createUserSuccessResponsePojo.getStatus());
	    }
	    else if(APIMethod.equalsIgnoreCase("PUT")) 
	    {
	    	response = given().spec(SpecificationFactory.getRequestSpecification()).when().get(PropertyFileReader.getEndPoint("get")+existingUserId).then().spec(SpecificationFactory.getResponseSpecification("Get single user")).extract().response();
	    	GetUserResponsePojo getUserResponse = response.as(GetUserResponsePojo.class);
			assertEquals(existingUserId, getUserResponse.getId());
			assertEquals(name, getUserResponse.getName());
			assertEquals(gender, getUserResponse.getGender());
			assertEquals(email, getUserResponse.getEmail());
			assertEquals(status, getUserResponse.getStatus());
	    }
	    else if(APIMethod.equalsIgnoreCase("DELETE"))
	    {
	    	response = given().spec(SpecificationFactory.getRequestSpecification()).when().get(PropertyFileReader.getEndPoint("get")+existingUserId);
	    	if(response.getStatusCode() == 404) {
	    		System.out.println("User with ID: " + existingUserId + " has been successfully deleted.");
	    	} else {
	    		System.out.println("User with ID: " + existingUserId + " still exists.");
	    	}
	    }
	}
	
	public void getUserDetails(int existingUserId) {
		

	}
}