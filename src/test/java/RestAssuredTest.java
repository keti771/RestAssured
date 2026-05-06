import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.path.json.JsonPath;
import java.util.List;
import org.testng.Assert;
import java.util.List;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;

public class RestAssuredTest {

    // 1. ცვლადი კლასის დონეზე, რომ ყველგან გამოვიყენო
    private RequestSpecification requestSpec;

    @BeforeClass
    public void setup() {
        // 2. Request Specification-ის ინიციალიზაცია
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com")
                .setContentType(ContentType.JSON)
                .build();
    }

    // 3. მეთოდი, რომელიც ტოკენს აგენერირებს
    public String createToken() {
        String authBody = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        Response response = given()
                .spec(requestSpec)
                .body(authBody)
                .when()
                .post("/auth");

        return response.path("token");
    }

    @Test
    public void test1() {
        // 4. აქ ვიძახებ createToken()-ს იმავე კლასში
        String myToken = createToken();

        System.out.println("ჩემი ტოკენია: " + myToken);

        // მარტივი შემოწმება
        assert myToken != null;
       }
    @Test
    public void deleteBookingTest() {
        // 1. ვიღებ ტოკენს
        String token = createToken();

        // 2. ვშლი კონკრეტულ ჯავშანს (მაგალითად, ID: 1)
         given()
                .spec(requestSpec)
                .header("Cookie", "token=" + token)
                .when()
                .delete("/booking/1")
                .then()
                .log().all()
                .statusCode(201); // ამ კონკრეტულ API-ს წარმატებულ წაშლაზე აქვს კოდი 201 (Created)
    }
    @Test
    public void bookstoreJsonPathTest() {

        // 1. გამოძახება
        Response response = given()
                .baseUri("https://bookstore.toolsqa.com")
                .when()
                .get("/BookStore/v1/Books");

        JsonPath jsonPath = response.jsonPath();

        // 2. გვერდების რაოდენობის შემოწმება (ყველა < 1000)
        List<Integer> pages = jsonPath.getList("books.pages");
        for (Integer pageCount : pages) {
            Assert.assertTrue(pageCount < 1000, "გვერდების რაოდენობა აღემატება 1000-ს: " + pageCount);
        }

        // 3. ავტორების ვალიდაცია
        String firstAuthor = jsonPath.getString("books[0].author");
        String secondAuthor = jsonPath.getString("books[1].author");

        // გამოიყენე რეალური ავტორები, რომლებსაც API აბრუნებს (მაგალითად: Richard E. Silverman და Addy Osmani)
        Assert.assertEquals(firstAuthor, "Richard E. Silverman", "პირველი ავტორი არასწორია!");
        Assert.assertEquals(secondAuthor, "Addy Osmani", "მეორე ავტორი არასწორია!");

        // 4. სტატუს კოდის შემოწმება
        response.then().statusCode(200);

        System.out.println("ტესტი წარმატებით დასრულდა!");
    }
}
