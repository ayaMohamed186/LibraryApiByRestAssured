package testCases.Book;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TC02_GetBook extends TestBase {

    String getResponseBookID,getResponseTitle,getResponseAuthor,getResponseIsbn,getResponseReleaseDate , dateFormatRegex;
    String responseBody;

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

    @Test(priority = 2 , description = "Get BooK details api by id")
    public void checkRetrieveAllBooks_P() {
        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .when().get("/books")
                        .then()
                        .log().all()
                        .assertThat().statusCode(200).extract().response();

        Assert.assertTrue(response.getTime() < 5000);


        // to captures the json array of books
        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> books = jsonPath.getList("");

        // Assert response is a non-empty array
        Assert.assertNotNull(books, "Response is null");
        Assert.assertFalse(books.isEmpty(), "Books array is empty");

        Map<String, Object> firstBook = books.get(0);

        Assert.assertTrue(firstBook.containsKey("id"), "Missing 'id'");
        Assert.assertTrue(firstBook.get("id") instanceof Integer, "'id' is not a number");

        Assert.assertTrue(firstBook.containsKey("title"), "Missing 'title'");
        Assert.assertTrue(firstBook.get("title") instanceof String, "'title' is not a string");

        Assert.assertTrue(firstBook.containsKey("author"), "Missing 'author'");
        Assert.assertTrue(firstBook.get("author") instanceof String, "'author' is not a string");

        Assert.assertTrue(firstBook.containsKey("publicationDate"), "Missing 'publicationDate'");
        Assert.assertTrue(firstBook.get("publicationDate") instanceof String, "'publicationDate' is not a string");

        Assert.assertTrue(firstBook.containsKey("isbn"), "Missing 'isbn'");
        Assert.assertTrue(firstBook.get("isbn") instanceof String, "'isbn' is not a string");

        Assert.assertTrue(firstBook.containsKey("createdAt"), "Missing 'createdAt'");
        Assert.assertTrue(firstBook.get("createdAt") instanceof String, "'createdAt' is not a string");

        Assert.assertTrue(firstBook.containsKey("updatedAt"), "Missing 'updatedAt'");
        Assert.assertTrue(firstBook.get("updatedAt") instanceof String, "'updatedAt' is not a string");
    }

    @Test(priority = 2, description = "check delete book by id ")
    public void checkGetBookAfterDelete_P() {
        Response response =
                given()
                        .auth().basic("admin", "admin")
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                        .when().get("/books/" + bookID)
                        .then()
                        .log().all()
                        .assertThat().statusCode(404).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

        responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Book not found"), "Expected message 'Book not found' but got: " + responseBody);

    }


}
