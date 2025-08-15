package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
//ExtentTest is used to log test steps and results in the Extent Reports
//ExtentReports is used to generate the report itself
// ExtentSparkReporter is used to create a spark report format
public class ExtentTestManager {
	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();// ThreadLocal is used to ensure that each thread has its own instance of ExtentTest
	private static ExtentReports extent = getExtentReports();
	public synchronized static ExtentReports getExtentReports() {
		if (extent == null) {
			ExtentSparkReporter spark = new ExtentSparkReporter("target/extent-reports/extentReport.html");
			spark.config().setReportName("GoRest API Automation Report");
			spark.config().setDocumentTitle("GoRest API Automation");
			spark.config().setTheme(Theme.DARK);
			extent = new ExtentReports();//this step initializes the ExtentReports object
			extent.attachReporter(spark);//this step attaches the ExtentSparkReporter to the ExtentReports object
			
		}
		return extent;
	}
	
	public static synchronized ExtentTest createTest(ThreadLocal<String> scenarioName) {
		ExtentTest test = extent.createTest(scenarioName.get());//this step creates a new test in the report with the given test name
		extentTest.set(test);//this step sets the current thread's ExtentTest instance
		return test;
	}
	
	public static synchronized ExtentTest getExtentTest() {
		return extentTest.get();//this step retrieves the current thread's ExtentTest instance
	}
	
	public static synchronized void flush() {
		if (extent != null) {
			extent.flush();//this step flushes the ExtentReports object, writing all logs to the report file
		}
	}
}
