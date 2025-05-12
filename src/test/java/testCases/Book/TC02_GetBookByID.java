package testCases.Book;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import static io.restassured.RestAssured.given;

public class TC02_GetBookByID extends TestBase {

    String getResponseBookID,getResponseTitle,getResponseAuthor,getResponseIsbn,getResponseReleaseDate , dateFormatRegex;

    @Test(priority = 1, description = "Get BooK details api by id")
    public void checkGetBookById_P() {
        Response response =
        given()
                .log().all().header("Content-Type","application/json")
                .header("g-token", "ROM831ESV")
        .when().get("/books/"+bookID)
        .then()
                .log().all()
                .assertThat().statusCode(200).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

        // extract getBookDetails api response to use it in assertion
        getResponseBookID = response.jsonPath().getString("id");
        getResponseTitle = response.jsonPath().getString("title");
        getResponseAuthor = response.jsonPath().getString("author");
        getResponseIsbn = response.jsonPath().getString("isbn");
        getResponseReleaseDate = response.jsonPath().getString("releaseDate");


        Assert.assertNotNull(response.jsonPath().getString("id"), "ID in response is null");
        Assert.assertNotNull(response.jsonPath().getString("title"), "Title in response is null");
        Assert.assertNotNull(response.jsonPath().getString("author"), "Author in response is null");
        Assert.assertNotNull(response.jsonPath().getString("isbn"), "ISBN in response is null");
        Assert.assertNotNull(response.jsonPath().getString("releaseDate"), "Release Date in response is null");

        dateFormatRegex = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$";
        Assert.assertTrue(getResponseReleaseDate.matches(dateFormatRegex), "releaseDate is not in valid format");


        // compare getBookingDetails api response WITH values extracted from CreateBookApi
        Assert.assertEquals(getResponseBookID, String.valueOf(bookID), "Book ID does not match");
        Assert.assertEquals(getResponseTitle, bookTitle, "Title does not match");
        Assert.assertEquals(getResponseAuthor, bookAuthor, "Author does not match");
        Assert.assertEquals(getResponseIsbn, bookIsbn, "ISBN does not match");
        Assert.assertEquals(getResponseReleaseDate, bookReleaseDate, "Release Date does not match");

    }
}
