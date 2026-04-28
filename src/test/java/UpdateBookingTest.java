import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UpdateBookingTest {

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
}