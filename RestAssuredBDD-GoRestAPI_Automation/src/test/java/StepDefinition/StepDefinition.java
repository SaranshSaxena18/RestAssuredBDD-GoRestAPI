package StepDefinition;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

public class StepDefinition {
	public static void main(String [] args)
	{
		RestAssured.baseURI = "https://gorest.co.in";
		given().log().all().header("Authorization","12a32a59630789e7f8f00652542b3b01e0943543c1306b66c56db8542f9d96d2")
		.when().get("/public/v2/users")
		.then().log().all().statusCode(200);
	}
}
