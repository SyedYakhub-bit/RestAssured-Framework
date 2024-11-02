import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ExternalPayload{
    public static void main(String[] args) throws IOException {
        RestAssured.baseURI="https://rahulshettyacademy.com";
        String response= given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                .body(new String(Files.readAllBytes(Paths.get("C://Users//syed.yakhub//new5.json"))))
                .when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo("APP")).extract().response().asString();
        System.out.println("This is the response in String format  "+ response);

        JsonPath js=new JsonPath(response);
        String place_id= js.getString("place_id");
        System.out.println("This is the place Id extracted from the response  " + place_id);


        // Here we are giving the above added place id to update with new address as defined below and Checking the update is successfull or not.
        String newaddress="Summer walk, India";
        if (place_id != null) {
            given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
                    .body("{\n" +
                            "\"place_id\":\""+place_id+"\",\n" +
                            "\"address\":\""+newaddress+"\",\n" +
                            "\"key\":\"qaclick123\"\n" +
                            "}\n")
                    .when().put("maps/api/place/update/json")
                    .then().assertThat().log().all().statusCode(200).body("msg",equalTo("Address successfully updated"));
        } else {
            System.out.println("place_id is null. Cannot proceed with the update request.");
        }


        //Here we are storing the response of GET request in a String
        String getplaceresponse=given().log().all().queryParam("key","qaclick123")
                .queryParam("place_id",place_id).when().get("maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200).extract().response().asString();


        JsonPath js1=new JsonPath(getplaceresponse);

        //Here we are Extracting the Updated address from the response using address as key word.
        String actual_address=js1.get("address");
        System.out.println("This is the Actual Address  "+actual_address);
        Assert.assertEquals(actual_address,newaddress);


    }
}