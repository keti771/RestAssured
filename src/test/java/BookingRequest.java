import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "additionalinfo",
        "firstname",
        "lastname",
        "totalprice",
        "depositpaid",
        "bookingdates"
})
public class BookingRequest {

    @JsonProperty("firstname")
    private String myFirstname;

    @JsonProperty("lastname")
    private String myLastname;

    private int totalprice;
    private boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalinfo;

    public BookingRequest() {}

    public BookingRequest(String firstname, String lastname, int totalprice, boolean depositpaid, BookingDates bookingdates, String additionalneeds) {
        this.myFirstname = firstname;
        this.myLastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.bookingdates = bookingdates;
        this.additionalinfo = additionalinfo;
    }

    public String getFirstname() { return myFirstname; }
    public void setFirstname(String firstname) { this.myFirstname = firstname; }
    public String getLastname() { return myLastname; }
    public void setLastname(String lastname) { this.myLastname = lastname; }
    public int getTotalprice() { return totalprice; }
    public void setTotalprice(int totalprice) { this.totalprice = totalprice; }
    public boolean isDepositpaid() { return depositpaid; }
    public void setDepositpaid(boolean depositpaid) { this.depositpaid = depositpaid; }
    public BookingDates getBookingdates() { return bookingdates; }
    public void setBookingdates(BookingDates bookingdates) { this.bookingdates = bookingdates; }
    public String getAdditionalneeds() { return additionalinfo; }
    public void setAdditionalinfo(String additionalinfo) { this.additionalinfo = additionalinfo; }
}