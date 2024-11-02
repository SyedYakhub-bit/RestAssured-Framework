package Serialization;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;


import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SpecBuilder {
    public static void main(String[] args) {

        /*we create an object and using that and set method we set the values for the defined values like
        accuracy,location etc*/

        AddPlaceSerialization place = new AddPlaceSerialization();
        place.setAccuracy(50);
        place.setAddress("Near Noorani Masjid Upparahalli,Tumkur");
        place.setLanguage("Urdu");
        place.setName("Syed Yakhub");
        place.setPhone_number("6360980036");
        place.setWebsite("https://rahulshettyacademy.com");
        List<String> types = new ArrayList<>();
        types.add("shoe park");
        types.add("shop");
        place.setTypes(types);
        Location location = new Location();
        location.setLat(-38.383494);
        location.setLng(33.427362);
        place.setLocation(location);

        /*In Serialization, we give the values from response body by creating an object using
         POJO classes and try to generate the response body in a JSON format */


/* SpecBuilders are used to reduce the re-writing of common things like baseurl,queryparam,statuscode
assertions all these are wrapped in an object and that is used in the code which reduces the code length
and if we have 100 of api requests we don't need to give each time all the parameters we can use the defined
object for both request and response*/

        RequestSpecification requestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON).addQueryParam("key", "qaclick123").build();

        ResponseSpecification responseSpec = new ResponseSpecBuilder().expectContentType(ContentType.JSON)
                .expectStatusCode(200).build();

        AddPlaceSerialization addPlaceSerializationResponse = given().spec(requestSpec).log().all().body(place).when().post("/maps/api/place/add/json").then().log().all()
                .spec(responseSpec).extract().as(AddPlaceSerialization.class);

        System.out.println("This is the Place Id generated: "+ addPlaceSerializationResponse.getPlace_id()); //printing the address
        Assert.assertEquals("OK", addPlaceSerializationResponse.getStatus(),"The status doesn't match");

    }
}
