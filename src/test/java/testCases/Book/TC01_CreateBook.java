package testCases.Book;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import static io.restassured.RestAssured.given;
import static model.CreateBookBody.getCreateBookBody;
import static util.Utility.*;

public class TC01_CreateBook extends TestBase {

    String validTitle = generateRandomBookName();
    String validAuthor = generateRandomAuthorName();
    String validIsbn = generateRandomISBN();
    String validReleaseDate = generateRandomDate();
    String releaseDate , createdAt ,updatedAt,dateFormatRegex;

    @Test(priority = 1, description = "Create BooK api with valid data")
    public void checkCreateNewBook_P() {
        Response response =
                given()
                        // .auth().basic("admin", "admin")
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .body(getCreateBookBody(validTitle, validAuthor, validIsbn, validReleaseDate))

                .when().post("/books")
                .then()
                        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/createBookSchema.json"))
                        .log().all().assertThat().statusCode(201).extract().response();


        Assert.assertTrue(response.getTime() < 5000);

        Assert.assertEquals(response.jsonPath().getString("title"), validTitle, "Returned title does not match the request title");
        Assert.assertEquals(response.jsonPath().getString("author"), validAuthor, "Returned author does not match the request author");
        Assert.assertEquals(response.jsonPath().getString("isbn"), validIsbn, "Returned ISBN does not match the request ISBN");
        Assert.assertEquals(response.jsonPath().getString("releaseDate"), validReleaseDate, "Returned releaseDate does not match the request releaseDate");

        Assert.assertNotNull(response.jsonPath().getString("id"), "ID in response is null");
        Assert.assertNotNull(response.jsonPath().getString("title"), "Title in response is null");
        Assert.assertNotNull(response.jsonPath().getString("author"), "Author in response is null");
        Assert.assertNotNull(response.jsonPath().getString("isbn"), "ISBN in response is null");
        Assert.assertNotNull(response.jsonPath().getString("releaseDate"), "Release Date in response is null");

        releaseDate = response.jsonPath().getString("releaseDate");
        createdAt = response.jsonPath().getString("createdAt");
        updatedAt = response.jsonPath().getString("updatedAt");
        dateFormatRegex = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$";

        Assert.assertTrue(releaseDate.matches(dateFormatRegex), "releaseDate is not in valid format");
        Assert.assertTrue(createdAt.matches(dateFormatRegex), "createdAt is not in valid format");
        Assert.assertTrue(updatedAt.matches(dateFormatRegex), "updatedAt is not in valid format");


        // save response values to use it in next request
        bookID = response.jsonPath().getInt("id");
        bookTitle = response.jsonPath().getString("title");
        bookAuthor = response.jsonPath().getString("author");
        bookIsbn = response.jsonPath().getString("isbn");
        bookReleaseDate = response.jsonPath().getString("releaseDate");

    }

}
