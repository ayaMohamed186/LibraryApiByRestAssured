package testCases.WishLists;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TC02_GetWishListByID extends TestBase {
    String responseBody, id,name,books;

    @Test(priority = 1, description = "Get wishlist details by ID" , dependsOnMethods = {"checkCreateNewWishList_P"})
    public void checkGetWishListById_P() {
        validateWishListDetails(staticWishlistName, staticWishlistBooks);
    }

    @Test(priority = 2, description = "Get wishlist details after update" , dependsOnMethods = {"checkUpdateWishListAdded_P"})
    public void checkGetWishListAfterUpdate_P() {
        validateWishListDetails(staticWishlistName, staticWishlistBooks);
    }

    @Test(priority = 3, description = "Get wishlist details api by id" , dependsOnMethods = {"checkPartialUpdate_P"})
    public void checkRetrieveAllWishList_P() {
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

    @Test(priority = 4, description = "check delete wishlist by id " , dependsOnMethods = {"checkDeleteWishListById_P"})
    public void checkGetWishListAfterDelete_P() {
        Response response =
                given()
                        .auth().basic("admin", "admin")
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                        .when().get("/wishlists/" + staticWishListID)
                        .then()
                        .log().all()
                        .assertThat().statusCode(404).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

        responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Wishlist not found"), "Expected message 'wishList not found' but got: " + responseBody);

    }


    private void validateWishListDetails(String expectedName, String expectedBooks) {
        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .when().get("/wishlists/" + staticWishListID)
                        .then()
                        .log().all()
                        .assertThat().statusCode(200).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

         id = response.jsonPath().getString("id");
         name = response.jsonPath().getString("name");
         books = response.jsonPath().getString("books");

        Assert.assertNotNull(id, "Wishlist ID is null");
        Assert.assertNotNull(name, "Wishlist name is null");
        Assert.assertNotNull(books, "Wishlist books is null");

        Assert.assertEquals(id, String.valueOf(staticWishListID), "Wishlist ID does not match");
        Assert.assertEquals(name, expectedName, "Wishlist name does not match");
        Assert.assertEquals(books, expectedBooks, "Wishlist books do not match");
    }


}
