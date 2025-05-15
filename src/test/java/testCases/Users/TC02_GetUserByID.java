package testCases.Users;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import static io.restassured.RestAssured.given;

public class TC02_GetUserByID extends TestBase {

    String getResponseFirstName,getResponseLastName,getResponseEmail,getResponseUserID, dateFormatRegex;

    @Test(priority = 1, description = "Get user details api by id")
    public void checkGetBookById_P() {
        Response response =
        given()
                .log().all().header("Content-Type","application/json")
                .header("g-token", "ROM831ESV")
        .when().get("/users/"+userID)
        .then()
                .log().all()
                .assertThat().statusCode(200).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

        // extract getUserDetails api response to use it in assertion
        getResponseUserID = response.jsonPath().getString("id");
        getResponseFirstName = response.jsonPath().getString("firstName");
        getResponseLastName = response.jsonPath().getString("lastName");
        getResponseEmail = response.jsonPath().getString("email");


        Assert.assertNotNull(response.jsonPath().getString("id"), "ID in response is null");
        Assert.assertNotNull(response.jsonPath().getString("firstName"), "firstName in response is null");
        Assert.assertNotNull(response.jsonPath().getString("lastName"), "lastName in response is null");
        Assert.assertNotNull(response.jsonPath().getString("email"), "usrEmail in response is null");


        // compare getBookingDetails api response WITH values extracted from CreateBookApi
        Assert.assertEquals(getResponseUserID, String.valueOf(userID), "Book ID does not match");
        Assert.assertEquals(getResponseFirstName, userFirstName, "firstName does not match");
        Assert.assertEquals(getResponseLastName, userLastName, "lastName does not match");
        Assert.assertEquals(getResponseEmail, userEmail, "usrEmail does not match");

    }
}
