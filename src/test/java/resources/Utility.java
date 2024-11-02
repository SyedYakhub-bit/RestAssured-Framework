package resources;


import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import supportfiles.PropertiesReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;


import static io.restassured.RestAssured.given;

public class Utility {
    // PrintStream object for logging requests and responses
    private static PrintStream log;

    // Method to set up request specifications
    public static RequestSpecification RequestSetUp() throws IOException {
        // Load properties from external file
        PropertiesReader propertiesReader = new PropertiesReader();
        propertiesReader.loadProperties();
        // Set base URI for REST Assured
        RestAssured.baseURI = PropertiesReader.baseURLRahulShetty;

        // Create a logging file if it doesn't exist
        if (log == null) {
            // 'false' here indicates that we do not want to append to the file. Instead, we want to overwrite it each time the test runs.
            log = new PrintStream(new FileOutputStream("logging.txt", false));
        }
        // Initialize and return a RequestSpecification object
        RequestSpecification request = given().queryParam("key", "qaclick123") // Add API key as a query parameter
                .filter(RequestLoggingFilter.logRequestTo(log)) // Log request details
                .filter(ResponseLoggingFilter.logResponseTo(log)) // Log response details
                .header("Content-Type", "application/json"); // Set content type to JSON
        return request;
    }

    // Method to extract a specific JSON parameter from the response
    public String getJsonParameter(Response response, String actualParameter) {
        // Parse the response to JsonPath for easy extraction of parameters
        JsonPath js = new JsonPath(response.asString());
        return js.get(actualParameter); // Return the value of the specified parameter
    }
}