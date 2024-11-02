import Files.payload;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Basics{
    public static void main(String[] args) {

        RestAssured.baseURI="https://rahulshettyacademy.com";

       Response response= given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                .body(payload.Addplace()).when().post("maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP")).extract().response();
        System.out.println("This is the response in String format  "+ response.asString());

        //JsonPath js=new JsonPath(response);
       String place_id=response.jsonPath().get("place_id");
        System.out.println("This is the place Id extracted from the response  " + place_id);


        // Here we are giving the above added place id to update with new address as defined below and Checking the update is successfull or not.
        String newaddress="Summer walk, India";
        given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
                .body("{\n" +
                        "\"place_id\":\""+place_id+"\",\n" +
                        "\"address\":\""+newaddress+"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}\n")
                .when().put("maps/api/place/update/json")
                .then().assertThat().log().all().statusCode(200).body("msg",equalTo("Address successfully updated"));


       //Here we are storing the response of GET request in a String
        Response getplaceresponse=given().log().all().queryParam("key","qaclick123")
                .queryParam("place_id",place_id).when().get("maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200).extract().response();
        System.out.println("This is the response in String format  "+ getplaceresponse.asString());

        //Here we are Extracting the Updated address from the response using address as key word.
        String actual_address=getplaceresponse.jsonPath().get("address");
        System.out.println("This is the Actual Address  "+actual_address);
        Assert.assertEquals(actual_address,newaddress);


    }
}