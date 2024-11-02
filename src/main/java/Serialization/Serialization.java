package Serialization;

import io.restassured.RestAssured;


import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Serialization {
    public static void main(String[] args) {
        RestAssured.baseURI="https://rahulshettyacademy.com";

        /* we create an object and using that and set method we set the values for the defined values like
        accuracy,location etc */

        AddPlaceSerialization place=new AddPlaceSerialization();
        place.setAccuracy(50);
        place.setAddress("Near Noorani Masjid Upparahalli,Tumkur");
        place.setLanguage("Urdu");
        place.setName("Syed Yakhub");
        place.setPhone_number("6360980036");
        place.setWebsite("https://rahulshettyacademy.com");
        List<String> types=new ArrayList<>();
        types.add("shoe park");
        types.add("shop");
        place.setTypes(types);
        Location location=new Location();
        location.setLat(-38.383494);
        location.setLng(33.427362);
        place.setLocation(location);


        /*In Serialization, we give the values from response body by creating an object using
         POJO classes and try to generate the response body in a JSON format */

        String response=given().queryParam("key","qaclick123").body(place)
                .when().post("/maps/api/place/add/json").then().log().all()
                .assertThat().statusCode(200).extract().response().asString();
        System.out.println(response);

    }
}
