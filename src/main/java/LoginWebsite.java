import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class LoginWebsite {
    public static void main(String[] args) {
        RestAssured.baseURI="https://rahulshettyacademy.com/";
        Response response= given().log().all().when().get("locatorspractice/").then().log()
                .all().assertThat().statusCode(200).extract().response();
        System.out.println(response);
        System.out.println("To Check branching");

    }
}
