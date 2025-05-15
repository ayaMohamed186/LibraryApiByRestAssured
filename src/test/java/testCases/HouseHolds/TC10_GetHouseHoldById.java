package testCases.HouseHolds;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import static io.restassured.RestAssured.given;

public class TC10_GetHouseHoldById extends TestBase {

    String getResponseHouseHoldID,getResponseHouseHoldName ;

    @Test(priority = 1, description = "Get household details api by id")
    public void checkGetBookById_P() {
        Response response =
                given()
                        .log().all().header("Content-Type","application/json")
                        .header("g-token", "ROM831ESV")
                        .when().get("/households/"+householdID)
                        .then()
                        .log().all()
                        .assertThat().statusCode(200).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

        // extract getHouseHoldDetails api response to use it in assertion
        getResponseHouseHoldID = response.jsonPath().getString("id");
        getResponseHouseHoldName = response.jsonPath().getString("name");


        Assert.assertNotNull(response.jsonPath().getString("id"), "ID in response is null");
        Assert.assertNotNull(response.jsonPath().getString("name"), "name in response is null");


        // compare getHouseHoldDetails api response WITH values extracted from CreateHouseHoldApi
        Assert.assertEquals(getResponseHouseHoldID, String.valueOf(householdID), "HouseHold ID does not match");
        Assert.assertEquals(getResponseHouseHoldName, houseHoldName, "HouseHold Name does not match");


    }

}
