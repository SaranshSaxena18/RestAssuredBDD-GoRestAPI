package Runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
			features="src/main/java/Features", glue={"StepDefinition"}, tags="@RunThis"
		)
public class TestRunner extends AbstractTestNGCucumberTests {

}
