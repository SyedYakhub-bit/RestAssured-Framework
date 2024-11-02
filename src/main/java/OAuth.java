import Pojo.Api;
import Pojo.GetCourses;
import Pojo.WebAutomation;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import supportfiles.PropertiesReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OAuth {
    public static void main(String[] args) {
        // Load properties
        PropertiesReader propertiesReader = new PropertiesReader();
        propertiesReader.loadProperties();
        // Step 1: Request an OAuth 2.0 access token from the authorization server
        String response = given().formParams("client_id", PropertiesReader.client_id)
                .formParams("client_secret", PropertiesReader.client_secret)
                .formParams("grant_type", PropertiesReader.grant_type)
                .formParams("scope", PropertiesReader.scope).when().log().all()
                .post(PropertiesReader.baseURLForAccessToken)
                .then().log().all().extract().response().asString();

        System.out.println(response);
        JsonPath js = new JsonPath(response);
        String accessToken = js.getString("access_token");
        System.out.println("This is the access token: "+accessToken);

        /* Here we are extracting the response as object of the class (GetCourses) that is the main class of our
        pojo and object gc is used to get the instructor and LinkedIn link */
        GetCourses gc = given().queryParams("access_token", accessToken).when().log().all()
                .get("https://rahulshettyacademy.com/oauthapi/getCourseDetails")
                .as(GetCourses.class);
        System.out.println("This is the Instructor name " + gc.getInstructor());
        System.out.println("This is the LinkedIn link: " + gc.getLinkedIn());

        //Here we are getting the Course title from the Api you can see how we navigate
        System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());

        //to get the price of particular course

        List<Api> ApiCourses = gc.getCourses().getApi();

        //here I have used enhanced for loop
        for (Api apiCourse : ApiCourses) {
            if (apiCourse.getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
                System.out.println(apiCourse.getPrice());
            }
        }

        List<WebAutomation> WebAutomationtitles = gc.getCourses().getWebAutomation();
        //Below line is used to define the arraylist to store the actual array list
        ArrayList<String> actualArrayList = new ArrayList<>();

        for (WebAutomation webAutomationtitle : WebAutomationtitles) {
            System.out.println(webAutomationtitle.getCourseTitle());
            actualArrayList.add(String.valueOf(webAutomationtitle.getCourseTitle()));
        }

        String soapUIWebServicesTestingPrice = gc.getCourses().getApi().get(1).getPrice();
        System.out.println("price of soap UI webservice testing is " + soapUIWebServicesTestingPrice);

        //Compare the contents of WebAutomation Course titles
        /* The purpose of using ArrayList is it provide as dynamic size and if we use
        the Array we need to give the size of the array which is fixed */
        ArrayList<String> expectedarrayList = new ArrayList<>(Arrays.asList("Selenium Webdriver Java", "Cypress", "Protractor"));
        Assert.assertEquals(expectedarrayList, actualArrayList);

    }
}
