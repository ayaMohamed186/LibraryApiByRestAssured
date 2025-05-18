package testCases.WishLists;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.createUserBody;
import testCases.TestBase;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static util.Utility.*;

public class TC01_CreateWishList extends TestBase {
    String  createdAt ,updatedAt,dateFormatRegex;
    String wishListName = getExcelData(0,0,"Sheet1");
    String wishListBooks = generateRandomBookTitlesString(3);
    String requestBody;

    public TC01_CreateWishList() throws IOException, ParseException {
    }

    @Test(priority = 1, description = "Create wishlist api with valid data")
    public void checkCreateNewWishList_P() throws JsonProcessingException {
        requestBody = "{\n" +
                "  \"name\": \"" + wishListName + "\",\n" +
                "  \"books\": \"" + wishListBooks + "\"\n" +
                "}";

        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .body(requestBody)

                .when().post("/wishlists")
                .then()
                        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/createWishListSchema.json"))
                        .log().all().assertThat().statusCode(201).extract().response();


        Assert.assertTrue(response.getTime() < 5000);

        Assert.assertEquals(response.jsonPath().getString("name"), wishListName, "Returned wishlist name does not match the request title");
        Assert.assertEquals(response.jsonPath().getString("books"), wishListBooks , "Returned wishlist books does not match the request author");


        createdAt = response.jsonPath().getString("createdAt");
        updatedAt = response.jsonPath().getString("updatedAt");
        dateFormatRegex = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$";

        Assert.assertTrue(createdAt.matches(dateFormatRegex), "createdAt is not in valid format");
        Assert.assertTrue(updatedAt.matches(dateFormatRegex), "updatedAt is not in valid format");


        // save response values to use it in next request
        staticWishListID = response.jsonPath().getInt("id");
        staticWishlistName = response.jsonPath().getString("name");
        staticWishlistBooks = response.jsonPath().getString("books");

    }

}
