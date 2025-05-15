package testCases.WishLists;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TC03_RetrieveAllWishlists extends TestBase {

    @Test(priority = 1, description = "Get wishlist details api by id")
    public void checkRetrieveAllBooks_P() {
        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .when().get("/wishlists")
                        .then()
                        .log().all()
                        .assertThat().statusCode(200).extract().response();

        Assert.assertTrue(response.getTime() < 5000);


        // to captures the json array of users
        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> wishListObject = jsonPath.getList("");

        // Assert response is a non-empty array
        Assert.assertNotNull(wishListObject, "Response is null");
        Assert.assertFalse(wishListObject.isEmpty(), "Books array is empty");

        Map<String, Object> firstWishList = wishListObject.get(0);

        Assert.assertTrue(firstWishList.containsKey("id"), "Missing 'id'");
        Assert.assertTrue(firstWishList.get("id") instanceof Integer, "'id' is not a number");

        Assert.assertTrue(firstWishList.containsKey("name"), "Missing 'name'");
        Assert.assertTrue(firstWishList.get("name") instanceof String, "'name' is not a string");

        Assert.assertTrue(firstWishList.containsKey("books"), "Missing 'books'");
        Assert.assertTrue(firstWishList.get("books") instanceof List<?>, "'books' is not a string");

    }
}
