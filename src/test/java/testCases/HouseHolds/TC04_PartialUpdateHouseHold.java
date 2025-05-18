package testCases.HouseHolds;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import static io.restassured.RestAssured.given;
import static util.Utility.*;

public class TC04_PartialUpdateHouseHold extends TestBase {

    String title = bookTitle;
    String authorName = bookAuthor;
    String partialUpdateIsbn = generateRandomISBN();
    String partialUpdateReleaseDate = generateRandomDate();
    String releaseDate, createdAt, updatedAt, dateFormatRegex;


    @Test(priority = 1, description = "check partial update api with valid data")
    public void checkPartialUpdate_P() {
        JSONObject partialUpdateHouseHoldBody = new JSONObject();

        partialUpdateHouseHoldBody.put("name" , generateRandomHouseholdName());

        Response response =
                given()
                        .auth().basic("admin", "admin")
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                        .body(partialUpdateHouseHoldBody.toJSONString())

                .when().put("/households/" + householdID)
                .then()
                        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/createHouseHoldSchema.json"))
                        .log().all().assertThat().statusCode(200).extract().response();


        Assert.assertTrue(response.getTime() < 5000);


    }


}
