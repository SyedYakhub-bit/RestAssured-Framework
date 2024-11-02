import Files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

//To Add and Delete Multiple books in a single run using POST method and get the ID generated after adding it.
public class AddBooks {
    @Test(dataProvider = "Booksdata")
    public void AddBook(String isbn,String aisle){
        RestAssured.baseURI="http://216.10.245.166";
        String response=given().log().all().header("Content-Type","text/plain")
                .body(payload.AddBook(isbn,aisle))
                .when().post("Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js=new JsonPath(response);
        String id=js.getString("ID");
        System.out.println("This is the ID "+id);

        given().log().all().header("Content-Type","text/plain")
                .body(payload.DeleteBook(id))
                .when().post("Library/DeleteBook.php")
                .then().log().all().assertThat().statusCode(200);

    }

    //this will provide the data to the above code
    @DataProvider(name="Booksdata")
    public Object[][] getData(){
        return new Object[][] {{"abbas","123"},{"usman","456"},{"anna","2526"}};

    }

}
