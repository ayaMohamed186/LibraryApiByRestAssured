package testCases;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.path.json.JsonPath;


import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TC03_RetrieveAllBooks extends TestBase{

    @Test(priority = 1, description = "Get BooK details api by id")
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
}
