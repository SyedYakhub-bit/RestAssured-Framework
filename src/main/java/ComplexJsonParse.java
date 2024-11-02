import Files.payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

//This Code is written by just using the Response that is useful when developers are still preparing the api

public class ComplexJsonParse {
    public static void main(String[] args) {
        JsonPath js = new JsonPath(payload.CoursePrice());

        //1. Print No of courses returned by API

        int courses = js.getInt("courses.size()");
        System.out.println("The total number of courses are " + courses);

        //2.Print Purchase Amount

        int amount = js.getInt("dashboard.purchaseAmount");
        System.out.println("The total purchase Amount is " + amount);

        // 3. Print Title of the first course
        String title = js.getString("courses[0].title");
        System.out.println("The title of the First Course is " + title);

        //4. Print All course titles and their respective Prices
        for (int i = 0; i < courses; i++) {
            String course_title = js.getString("courses[" + i + "].title");
            int course_price = js.getInt("courses[" + i + "].price");
            System.out.println("The course " + course_title + " costs " + course_price);
        }

        //5. Print no of copies sold by RPA Course
        for (int i = 0; i < courses; i++) {
            String course_title1 = js.getString("courses[" + i + "].title");
            if (course_title1.equalsIgnoreCase("RPA")) {
                System.out.println("The number of Copies by RPA is " + js.getString("courses[" + i + "].copies"));
                break;
            }
        }
        //6. Verify if Sum of all Course prices matches with Purchase Amount
        int sum = 0;
        for (int i = 0; i < courses; i++) {
            int course_price = js.getInt("courses[" + i + "].price");
            int course_copies = js.getInt("courses[" + i + "].copies");
            sum = sum + (course_price * course_copies);
        }
        System.out.println("The total sum of all the courses is " + sum);
        Assert.assertEquals(sum, amount,"The sum of all the courses is not equal to the purchase amount");
    }
}





