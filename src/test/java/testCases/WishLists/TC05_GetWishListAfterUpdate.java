package testCases.WishLists;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import static io.restassured.RestAssured.given;

public class TC05_GetWishListAfterUpdate extends TestBase {

    String getResponseWishListID, getResponseWishListName, getResponseWishListBooks;

    @Test(priority = 1, description = "Get wishlist details api by id")
    public void checkGetBookByIdAfterUpdate_P() {

        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .when().get("/wishlists/" + staticWishListID)
                        .then()
                        .log().all()
                        .assertThat().statusCode(200).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

        // extract getUserDetailsAfterUpdate api response to use it in assertion
        getResponseWishListID = response.jsonPath().getString("id");
        getResponseWishListName = response.jsonPath().getString("name");
        getResponseWishListBooks = response.jsonPath().getString("books");


        Assert.assertNotNull(response.jsonPath().getString("id"), "ID in response is null");
        Assert.assertNotNull(response.jsonPath().getString("name"), "wishlist name in response is null");
        Assert.assertNotNull(response.jsonPath().getString("books"), "wishlist books in response is null");


        // compare getBookingDetails api response WITH values extracted from updateBookApi
        Assert.assertEquals(getResponseWishListID, String.valueOf(staticWishListID), "wishlist ID does not match");
        Assert.assertEquals(getResponseWishListName, staticWishlistName, "wishlist name does not match");
        Assert.assertEquals(getResponseWishListBooks, staticWishlistBooks, "wishlist books does not match");
    }
}