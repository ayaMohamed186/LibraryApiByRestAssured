package testCases.Users;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import static io.restassured.RestAssured.given;

public class TC05_GetUserAfterUpdate extends TestBase {

    String getResponseUserID, getResponseFirstName, getResponseLastName, getResponseEmail;

    @Test(priority = 1, description = "Get user details api by id")
    public void checkGetBookByIdAfterUpdate_P() {

        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .when().get("/users/" + userID)
                        .then()
                        .log().all()
                        .assertThat().statusCode(200).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

        // extract getUserDetailsAfterUpdate api response to use it in assertion
        getResponseUserID = response.jsonPath().getString("id");
        getResponseFirstName = response.jsonPath().getString("firstName");
        getResponseLastName = response.jsonPath().getString("lastName");
        getResponseEmail = response.jsonPath().getString("email");


        Assert.assertNotNull(response.jsonPath().getString("id"), "ID in response is null");
        Assert.assertNotNull(response.jsonPath().getString("firstName"), "firstName in response is null");
        Assert.assertNotNull(response.jsonPath().getString("lastName"), "lastName in response is null");
        Assert.assertNotNull(response.jsonPath().getString("email"), "email in response is null");


        // compare getBookingDetails api response WITH values extracted from updateBookApi
        Assert.assertEquals(getResponseUserID, String.valueOf(userID), "user ID does not match");
        Assert.assertEquals(getResponseFirstName, userFirstName, "user firstname does not match");
        Assert.assertEquals(getResponseLastName, userLastName, "user lastname does not match");
        Assert.assertEquals(getResponseEmail, userEmail, "user email does not match");
    }
}