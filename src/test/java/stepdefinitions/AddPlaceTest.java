package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utility;


import java.io.IOException;

public class AddPlaceTest extends Utility {

    // Response object to hold API response
    Response response;
    // Instance of TestDataBuild to create payload data
    TestDataBuild data = new TestDataBuild();
    // RequestSpecification object to set up request specifications
    RequestSpecification request;
    // Static variable to hold the place_id for further use
    static String place_id;


    // Step definition to prepare the payload for AddPlaceAPI
    @Given("the payload for AddPlaceAPI is prepared with {string} {string} and {string}")
    public void the_payload_for_add_place_api_is_prepared(String name, String language, String address) throws IOException {
        // Prepare request with the given name, language, and address
        request = Utility.RequestSetUp().body(data.addPlacePayload(name, language, address));
    }


    // Step definition for sending a request to the API
    @When("the user sends a request to {string} with {string} http method")
    public void the_user_sends_a_request_to_add_place_api(String resource, String httpMethod) {
        // Get API resource from enum based on the string input
        APIResources resourceAPI = APIResources.valueOf(resource);
        System.out.println(resourceAPI.getResource());
        // Check HTTP method and send the corresponding request
        if (httpMethod.equalsIgnoreCase("POST")) {
            response = request.when().post(resourceAPI.getResource()).then().extract().response();
        } else if (httpMethod.equalsIgnoreCase("GET")) {
            response = request.when().get(resourceAPI.getResource()).then().extract().response();
        }
    }

    // Step definition to check if the API call was successful based on status code
    @Then("the API call is successful with status code {int}")
    public void the_api_call_is_successful_with_status_code(Integer expectedCode) {
        // Assert the response status code matches the expected code
        Assert.assertEquals(response.statusCode(), expectedCode, "Expected code is not matching with Actual Code");
    }

    // Step definition to check a specific parameter in the response
    @And("the {string} in the response is {string}")
    public void the_status_in_the_response_is(String actualParameter, String expectedParameter) {
        // Assert that the actual parameter value matches the expected value
        Assert.assertEquals(getJsonParameter(response, actualParameter), expectedParameter, "The Status in response doesn't match the status expected");
    }

    // Step definition to verify that place_id maps to the expected name using a GET request
    @And("verify that the place_id maps to {string} using {string}")
    public void verify_that_the_place_id_maps_to_using(String expectedName, String resource) {
        // Extract place_id from the response
        place_id = getJsonParameter(response, "place_id");
        // Add place_id as a query parameter for the next request
        request = request.queryParam("place_id", place_id);
        // Send a GET request to verify the place_id
        the_user_sends_a_request_to_add_place_api(resource, "GET");
        // Assert that the actual name matches the expected name
        String actualName = getJsonParameter(response, "name");
        Assert.assertEquals(actualName, expectedName, "The actual name and expected name doesn't match");
    }

    @Given("building the delete place payload using place_id")
    public void building_the_delete_place_payload_using_place_id() throws IOException {
        request = Utility.RequestSetUp().body(data.deletePlacePayload(place_id));
    }

}