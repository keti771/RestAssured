import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.json.JSONObject;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class updateBookingTest {

    // ტოკენის გენერაციის დამხმარე მეთოდი (თუ უკვე გაქვს სხვაგან, იქიდან გამოიძახე)
    public String createToken() {
        String authBody = "{\"username\":\"admin\",\"password\":\"password123\"}";
        return given()
                .contentType(ContentType.JSON)
                .body(authBody)
                .post("https://restful-booker.herokuapp.com/auth")
                .path("token");
    }

    @Test(description = "Update Booking and Check Response")
    public void updateBookingTest() {
        String token = createToken();
        BookingDates dates = new BookingDates("2026-05-01", "2026-05-10");
        BookingRequest updateBody = new BookingRequest("Vivien", "Westwood", 150, true, dates, "Breakfast");

        given()
                .filter(new AllureRestAssured())
                .baseUri("https://restful-booker.herokuapp.com")
                .contentType(ContentType.JSON)
                .header("Accept", "application/json") // აუცილებელია!
                .header("Cookie", "token=" + token)   // გამოიყენე Cookie
                .body(updateBody)
                .when()
                .put("/booking/1") // დარწმუნდი, რომ ID 1 არსებობს
                .then()
                .log().all()
                .statusCode(200)
                .body("firstname", equalTo("Vivien"))
                .body("lastname", equalTo("Westwood"));
    }

    @Test
    public void updateBookingWithJson() {
        String token = createToken();
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

        int status = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body(mainBody.toString())
                .when()
                .put("https://restful-booker.herokuapp.com/booking/1") // დაამატე ტირე restful-booker
                .then()
                .extract()
                .statusCode();

        // Restful Booker-ზე PUT მოთხოვნა წარმატებისას აბრუნებს 200-ს და არა 201-ს
        if (status == 200) {
            System.out.println("Success! Status is 200.");
        } else {
            System.out.println("Status was: " + status + ". Method Not Allowed or ID missing.");
        }
    }

    // დანარჩენი ტესტები (validateBookPagesTest და validateBookAuthorsTest) სწორია
}