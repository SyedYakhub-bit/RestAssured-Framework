import Files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.Test;


import static io.restassured.RestAssured.given;

//To Add a Book in to the Library using POST method and get the ID generated after adding it.
public class libraryapi {
    //here we are using TestNG to write the test cases.
    @Test
    public void AddBook(){
        RestAssured.baseURI="http://216.10.245.166";
        String response=given().log().all().header("Content-Type","text/plain")
                .body(payload.AddBook("syed","786"))
                .when().post("Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js=new JsonPath(response);
        String id=js.getString("ID");
        System.out.println("This is the ID "+id);


    }
}
