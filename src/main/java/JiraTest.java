import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import static io.restassured.RestAssured.given;

public class JiraTest {
    public static void main(String[] args) {
        //This Code Adds a Comment automatically in jira.
        RestAssured.baseURI="http://localhost:8080";
        String expected_message="This is the actual comment";

        //This Session filter is used instead of JSONPath or you can use jsonpath to parse
        SessionFilter session=new SessionFilter();

        //here the given below relaxed http is used to validate the https website in real projects it is an optional thing.
        given().relaxedHTTPSValidation().header("Content-Type","application/json").body("{\"username\":\"syedyakhub\",\"password\":\"Afreen@1998\"}")
                        .log().all().filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();


       //Here we are giving the key that is id in path param you can observe and that is given in brackets in post as {Key}
        //important: Here we are using the query param to limit the output to give only comments field.
        String comment_response=given().pathParam("Key","10003").queryParam("fields","comments").header("Content-Type","application/json")
                .log().all().body("{\n" +
                        "    \"body\": \""+expected_message+"\",\n" +
                        "    \"visibility\": {\n" +
                        "        \"type\": \"role\",\n" +
                        "        \"value\": \"Administrators\"\n" +
                        "    }\n" +
                        "}").when().filter(session).post("/rest/api/2/issue/{Key}/comment").then().log().all().assertThat()
                .statusCode(201).extract().response().asString();

        JsonPath js=new JsonPath(comment_response);
        String comment_id=js.getString("id");

       //This below code gets the details of created issue
        String issuedetails=given().filter(session).pathParam("Key","10003").log().all().when().get("/rest/api/2/issue/{Key}")
                .then().log().all().extract().response().asString();
        System.out.println(issuedetails);

        JsonPath js1=new JsonPath(issuedetails);
        int comments_count=js1.getInt("fields.comment.comments.size()");
        for(int i=0;i<comments_count;i++){
            String comments_id=js1.get("fields.comment.comments["+i+"].id").toString();
            if(comments_id.equalsIgnoreCase(comment_id)){
                String message=js1.get("fields.comment.comments["+i+"].body");
                System.out.println(message);
                Assert.assertEquals(message,expected_message);
            }
        }

    }
}
