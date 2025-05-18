package testCases.WishLists;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import static io.restassured.RestAssured.given;
import static util.Utility.generateRandomBookTitlesString;

public class TC04_PartialUpdateForWishList extends TestBase {

    String wishListBooks = generateRandomBookTitlesString(2);
    String requestBody;


    @Test(priority = 1, description = "check partial update for wishlist api with valid data")
    public void checkPartialUpdate_P() throws JsonProcessingException {
        requestBody = "{\n" +
                "  \"name\": \"" + staticWishlistName + "\",\n" +
                "  \"books\": \"" + wishListBooks + "\"\n" +
                "}";


        Response response =
                given()
                        .auth().basic("admin", "admin")
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                        .body(requestBody)

                        .when().put("/wishlists/" + staticWishListID)
                        .then()
                        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/createWishListSchema.json"))
                        .log().all().assertThat().statusCode(200).extract().response();


        Assert.assertTrue(response.getTime() < 5000);


    }

}
