package StepDefinition;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import org.testng.annotations.Test;

public class StepDefinition {
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
	@Test
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