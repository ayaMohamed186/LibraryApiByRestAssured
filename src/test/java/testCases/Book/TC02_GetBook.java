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

    String responseBody;
    String id,title,author,isbn,releaseDate;

    @Test(priority = 1, description = "Get Book details API by ID", dependsOnMethods = {"checkCreateNewBook_P"})
    public void checkGetBookById_P() {
        validateBookDetails(bookTitle, bookAuthor, bookIsbn, bookReleaseDate);
    }

    @Test(priority = 2, description = "Get Book details API by ID", dependsOnMethods = {"checkUpdateBookAdded_P"})
    public void checkGetBookByIdAfterUpdate_P() {
        validateBookDetails(bookTitle, bookAuthor, bookIsbn, bookReleaseDate);
    }

    @Test(priority = 3 , description = "Get BooK details api by id" , dependsOnMethods = {"checkPartialUpdate_P"})
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

    @Test(priority = 4, description = "check delete book by id " , dependsOnMethods = "checkDeleteBookById_P")
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


    private void validateBookDetails(String expectedTitle, String expectedAuthor, String expectedIsbn, String expectedReleaseDate) {
        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .when().get("/books/" + bookID)
                        .then()
                        .log().all()
                        .assertThat().statusCode(200).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

         id = response.jsonPath().getString("id");
         title = response.jsonPath().getString("title");
         author = response.jsonPath().getString("author");
         isbn = response.jsonPath().getString("isbn");
         releaseDate = response.jsonPath().getString("releaseDate");

        Assert.assertNotNull(id, "ID in response is null");
        Assert.assertNotNull(title, "Title in response is null");
        Assert.assertNotNull(author, "Author in response is null");
        Assert.assertNotNull(isbn, "ISBN in response is null");
        Assert.assertNotNull(releaseDate, "Release Date in response is null");

        String dateFormatRegex = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$";
        Assert.assertTrue(releaseDate.matches(dateFormatRegex), "releaseDate is not in valid format");

        Assert.assertEquals(id, String.valueOf(bookID), "Book ID does not match");
        Assert.assertEquals(title, expectedTitle, "Title does not match");
        Assert.assertEquals(author, expectedAuthor, "Author does not match");
        Assert.assertEquals(isbn, expectedIsbn, "ISBN does not match");
        Assert.assertEquals(releaseDate, expectedReleaseDate, "Release Date does not match");
    }




}
