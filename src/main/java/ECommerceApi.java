import Files.payload;
import Pojo.LoginRequest;
import Pojo.LoginResponse;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;


import java.io.File;

import static io.restassured.RestAssured.given;

public class ECommerceApi {
    public static void main(String[] args) {

        RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();

        LoginRequest loginrequest = new LoginRequest();

        loginrequest.setUserEmail("syedyakhub11102000@gmail.com");
        loginrequest.setUserPassword("Yakhub@2001");

        LoginResponse loginResponse = given().spec(req).body(loginrequest).when().post("/api/ecom/auth/login").then().log().all()
                .extract().as(LoginResponse.class);

        String Token = loginResponse.getToken();
        String userId = loginResponse.getUserId();
        System.out.println("This is the token: " + Token);
        System.out.println("This is the userId: " + userId);

        RequestSpecification req1 = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", Token).build();

        //Add the product
        String response1 = given().spec(req1).param("productName", "Laptop")
                .param("productAddedBy", userId).param("productCategory", "Fashion")
                .param("productSubCategory", "shirts").param("productPrice", "1500")
                .param("productDescription", "Addias Originals")
                .param("productFor", "women")
                .multiPart("productImage", new File("C:\\Users\\syed.yakhub\\Pictures\\Camera Roll\\Laptops.jpg"))
                .when().post("/api/ecom/product/add-product").then().log().all().extract().response().asString();

        JsonPath js1 = new JsonPath(response1);
        String productId = js1.getString("productId");
        System.out.println(response1);
        System.out.println(productId);

        //Create Order
        RequestSpecification req2 = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", Token).setContentType(ContentType.JSON).build();

        String response2 = given().spec(req2).body(payload.CreateOrder(productId))
                .when().post("/api/ecom/order/create-order").then().log().all()
                .extract().response().asString();
        System.out.println(response2);
        JsonPath js2 = new JsonPath(response2);
        System.out.println(js2.getString("message"));
        String orders = js2.getString("orders[0]");
        System.out.println(orders);

        //Delete Order
        String response3 = given().log().all().spec(req2).pathParam("productId", productId).when()
                .delete("/api/ecom/product/delete-product/{productId}").then().log().all()
                .extract().response().asString();
        JsonPath js3 = new JsonPath(response3);
        String message = js3.getString("message");
        System.out.println("This is the Message " + message);
        Assert.assertEquals(message, "Product Deleted Successfully", "The message doesn't match the expected message");

        //Password Reset through Forgot password
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        String passwordMessageResponse = given().log().all().header("Content-Type","application/json")
                .body(payload.ForgotPassword()).when().post("/api/ecom/auth/new-password").then().log().all()
                .assertThat().statusCode(200).extract().response().asString();
        JsonPath js = new JsonPath(passwordMessageResponse);
        String resetPasswordMessage = js.get("message");
        System.out.println("After Resetting the password got the message: " + resetPasswordMessage);


    }
}
