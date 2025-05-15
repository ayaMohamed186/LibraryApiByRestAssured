package testCases.Book;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import static io.restassured.RestAssured.given;
import static model.CreateBookBody.getCreateBookBody;
import static util.Utility.*;
import static util.Utility.generateRandomDate;

public class TC06_PartialUpdateForBook extends TestBase {

    String title = bookTitle;
    String authorName = bookAuthor;
    String partialUpdateIsbn = generateRandomISBN();
    String partialUpdateReleaseDate = generateRandomDate();

    @Test(priority = 1, description = "check partial update api with valid data")
    public void checkPartialUpdate_P() {
        Response response =
                given()
                        .auth().basic("admin", "admin")
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                        .body(getCreateBookBody(title, authorName, partialUpdateIsbn, partialUpdateReleaseDate))

                        .when().put("/books/" + bookID)
                        .then()
                        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/createBookSchema.json"))
                        .log().all().assertThat().statusCode(200).extract().response();


        Assert.assertTrue(response.getTime() < 5000);


    }

}
