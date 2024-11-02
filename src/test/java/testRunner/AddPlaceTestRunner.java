package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(features = "src/test/java/features", glue = {"stepdefinitions"},plugin="json:target/jsonReports/cucumber-report.json",tags = "@add_place or @delete_place or @regression")

public class AddPlaceTestRunner extends AbstractTestNGCucumberTests {
}
