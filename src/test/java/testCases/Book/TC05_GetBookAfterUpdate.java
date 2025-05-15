package testCases.Book;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import static io.restassured.RestAssured.given;

public class TC05_GetBookAfterUpdate extends TestBase {

    String getResponseBookID, getResponseUpdatedTitle, getResponseUpdatedAuthor,
            getResponseUpdatedIsbn, getResponseUpdatedReleaseDate, dateFormatRegex;

    @Test(priority = 1, description = "Get BooK details api by id")
    public void checkGetBookByIdAfterUpdate_P() {
        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .when().get("/books/" + bookID)
                        .then()
                        .log().all()
                        .assertThat().statusCode(200).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

        // extract getBookDetailsAfterUpdate api response to use it in assertion
        getResponseBookID = response.jsonPath().getString("id");
        getResponseUpdatedTitle = response.jsonPath().getString("title");
        getResponseUpdatedAuthor = response.jsonPath().getString("author");
        getResponseUpdatedIsbn = response.jsonPath().getString("isbn");
        getResponseUpdatedReleaseDate = response.jsonPath().getString("releaseDate");


        Assert.assertNotNull(response.jsonPath().getString("id"), "ID in response is null");
        Assert.assertNotNull(response.jsonPath().getString("title"), "Title in response is null");
        Assert.assertNotNull(response.jsonPath().getString("author"), "Author in response is null");
        Assert.assertNotNull(response.jsonPath().getString("isbn"), "ISBN in response is null");
        Assert.assertNotNull(response.jsonPath().getString("releaseDate"), "Release Date in response is null");

        dateFormatRegex = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$";
        Assert.assertTrue(getResponseUpdatedReleaseDate.matches(dateFormatRegex), "releaseDate is not in valid format");


        // compare getBookingDetails api response WITH values extracted from updateBookApi
        Assert.assertEquals(getResponseBookID, String.valueOf(bookID), "Book ID does not match");
        Assert.assertEquals(getResponseUpdatedTitle, bookTitle, "Title does not match");
        Assert.assertEquals(getResponseUpdatedAuthor, bookAuthor, "Author does not match");
        Assert.assertEquals(getResponseUpdatedIsbn, bookIsbn, "ISBN does not match");
        Assert.assertEquals(getResponseUpdatedReleaseDate, bookReleaseDate, "Release Date does not match");

    }
}