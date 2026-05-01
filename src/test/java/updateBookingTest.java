import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.json.JSONObject;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class updateBookingTest {

    @Test(description = "Update Booking and Check Response")
    public void updateBookingTest() {

        BookingDates dates = new BookingDates("2026-05-01", "2026-05-10");
        BookingRequest updateBody = new BookingRequest(
                "Vivien",
                "Westwood",
                150,
                true,
                dates,
                "Breakfast"
        );

        given()
                .filter(new AllureRestAssured()) // <--- Allure ფილტრი
                .baseUri("https://restful-booker.herokuapp.com")
                .contentType(ContentType.JSON)
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .body(updateBody)
                .when()
                .put("/booking/1")
                .then()
                .log().all()

                .statusCode(200)

                .body("firstname", equalTo("Vivien"))
                .body("lastname", equalTo("Westwood"))
                .body("bookingdates.checkin", equalTo("2026-05-01"))

                .body("totalprice", is(150));
    }
    @Test
    public void updateBookingWithJson() {
        // 1. ვქმნით JSON-ს (JSONObject-ის გამოყენებით)
        JSONObject mainBody = new JSONObject();
        mainBody.put("firstname", "Keti");
        mainBody.put("lastname", "SuperMom");
        mainBody.put("totalprice", 67);
        mainBody.put("depositpaid", true);

        JSONObject dates = new JSONObject();
        dates.put("checkin", "2026-05-01");
        dates.put("checkout", "2026-05-10");

        mainBody.put("bookingdates", dates);
        mainBody.put("additionalneeds", "Late checkout");

        // 2. მოთხოვნის გაგზავნა და სტატუსის ამოღება (extract)
        int status = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=") // admin:password123
                .body(mainBody.toString()) // JSONObject გადაგვყავს String-ში
                .when()
                .put("https://restfulbooker.herokuapp.com/booking/1")
                .then()
                .extract()
                .statusCode();

        // 3. დალოგვა მხოლოდ თუ სტატუსი არის 201
        if (status == 201) {
            System.out.println("Success! Status is 201. Full response below:");
            // ხელახლა ვიღებთ მონაცემებს დასალოგად ან ვიყენებთ Response ობიექტს
            given().when().get("https://restfulbooker.herokuapp.com/booking/1").then().log().all();
        } else {
            System.out.println("Status was: " + status + ". Criteria (201) not met.");
        }
    }
    @Test
    public void validateBookPagesTest() {
        given()
                .when()
                .get("https://bookstore.toolsqa.com/BookStore/v1/Books")
                .then()
                .assertThat()
                .body("books.pages", everyItem(lessThan(1000)));
    }

    @Test(description = "პირველი და მეორე წიგნის ავტორების შემოწმება")
    public void validateBookAuthorsTest() {
        given()
                .when()
                .get("https://bookstore.toolsqa.com/BookStore/v1/Books")
                .then()
                .assertThat()
                .body("books[0].author", equalTo("Richard E. Silverman"))
                .body("books[1].author", equalTo("Addy Osmani"));
    }
}

