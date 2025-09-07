package StepDefinition;

import java.util.Map;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import utils.ExtentTestManager;
import utils.GetData;
import utils.PropertyFileReader;
import utils.SpecificationFactory;

public class Hooks {
	private static final ThreadLocal<String> ScenarioName = new ThreadLocal<>();
	//private String scenarioName;
	@Before(order=1)
	public void setUp(Scenario scenario) {
		//scenarioName = scenario.getName();
		ExtentTestManager.createTest(ScenarioName);// Create a new test in ExtentReports for each scenario
	}
	
	public static String getScenarioName() {
		return ScenarioName.get();
	}

	@Before(order=0)
	public void before(Scenario scenario) {
		ScenarioName.set(scenario.getName());
		System.out.println("Starting scenario: " + ScenarioName.get());
	}
	
	@BeforeAll// This will run once before all scenarios
    public static void beforeAll() {
        GetData.initialize();// Initialize the data reader once before all scenarios
    }

	
	@After(order=1)// This will run after each scenario
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
	
	@After// This will run after each scenario
    public void afterScenario(Scenario scenario) {
        ExtentTest test = ExtentTestManager.getExtentTest();

        if (scenario.isFailed()) {
            test.fail("Scenario failed: " + scenario.getName());
        } else {
            test.pass("Scenario passed: " + scenario.getName());
        }
	}
	
	 @AfterAll// This will run once after all scenarios
		public static void tearDown() {
			ExtentTestManager.flush();
		}
}