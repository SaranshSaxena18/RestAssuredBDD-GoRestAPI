package StepDefinition;

import org.testng.annotations.Test;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import utils.ExtentTestManager;
import utils.PropertyFileReader;
import utils.SpecificationFactory;

public class Hooks {
	private static final ThreadLocal<String> ScenarioName = new ThreadLocal<>();
	@Before
	public void setUp(Scenario scenario) {
		String scenarioName = scenario.getName();
		ExtentTestManager.createTest(scenarioName);
	}
	
	@After(order=1)
	public void cleanUp() {
        RequestSpecification reqSpec = SpecificationFactory.getRequestSpecification();
        if (reqSpec != null) {
        	if(StepDefinition.newUserId != 0)
        	{
        		// Delete the user created in the test
    			System.out.println("Cleaning up by deleting user with ID: " + StepDefinition.newUserId);
    			// Use the DELETE endpoint to remove the user
            	RestAssured.given().spec(reqSpec).when().delete(PropertyFileReader.getEndPoint("delete") + StepDefinition.newUserId)
                    .then().statusCode(204).log().all();
        	}
        } else {
            System.err.println("RequestSpecification is null in cleanUp.");
        }
        StepDefinition.newUserId = 0;
    }
	
	 @After(order=0)
		public void tearDown()
		{
	   		ExtentTestManager.flush();
		}
}