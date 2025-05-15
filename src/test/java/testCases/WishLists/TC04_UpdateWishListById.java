package testCases.WishLists;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.createUserBody;
import testCases.TestBase;

import static io.restassured.RestAssured.given;
import static util.Utility.*;

public class TC04_UpdateWishListById extends TestBase {
    String createdAt ,updatedAt,dateFormatRegex;

    String wishListName = getExcelData(1,0,"Sheet1");
    String wishListBooks = generateRandomBookTitlesString(3);
    String requestBody;

    @Test(priority = 1, description = "Create wishlist api with valid data")
    public void checkUpdateBookAdded_P() throws JsonProcessingException {

        requestBody = "{\n" +
                "  \"name\": \"" + wishListName + "\",\n" +
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

        Assert.assertEquals(response.jsonPath().getString("name"), wishListName, "Returned name does not match the request name");
        Assert.assertEquals(response.jsonPath().getString("books"), wishListBooks, "Returned books does not match the request books");


        createdAt = response.jsonPath().getString("createdAt");
        updatedAt = response.jsonPath().getString("updatedAt");
        dateFormatRegex = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$";

        Assert.assertTrue(createdAt.matches(dateFormatRegex), "createdAt is not in valid format");
        Assert.assertTrue(updatedAt.matches(dateFormatRegex), "updatedAt is not in valid format");



        // save update response values to use it in next get request after update
        staticWishListID = response.jsonPath().getInt("id");
        staticWishlistName = response.jsonPath().getString("name");
        staticWishlistBooks = response.jsonPath().getString("books");
    }
}