package testCases.HouseHolds;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TC02_GetHouseHold extends TestBase {

    @Test(priority = 1, description = "Get household details after create", dependsOnMethods = {"checkCreateNewHouseHold_P"})
    public void checkGetHouseHoldById_P() {
        validateHouseHoldDetails(houseHoldName); // check after create
    }

    @Test(priority = 2, description = "Get household details after update " , dependsOnMethods = {"checkUpdateHouseHoldAdded_P"})
    public void checkGetHouseHoldAfterUpdate_P() {
        validateHouseHoldDetails(houseHoldName); // or updatedHouseHoldName if name was changed
    }

    @Test(priority = 3, description = "Get household details api by id", dependsOnMethods = {"checkPartialUpdate_P"})
    public void checkRetrieveAllHouseHold_P() {
        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .when().get("/households")
                        .then()
                        .log().all()
                        .assertThat().statusCode(200).extract().response();

        Assert.assertTrue(response.getTime() < 5000);


        // to captures the json array of books
        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> houseHold = jsonPath.getList("");

        // Assert response is a non-empty array
        Assert.assertNotNull(houseHold, "Response is null");
        Assert.assertFalse(houseHold.isEmpty(), "Books array is empty");

        Map<String, Object> firstBook = houseHold.get(0);

        Assert.assertTrue(firstBook.containsKey("id"), "Missing 'id'");
        Assert.assertTrue(firstBook.get("id") instanceof Integer, "'id' is not a number");

        Assert.assertTrue(firstBook.containsKey("name"), "Missing 'title'");
        Assert.assertTrue(firstBook.get("name") instanceof String, "'title' is not a string");

        Assert.assertTrue(firstBook.containsKey("createdAt"), "Missing 'createdAt'");
        Assert.assertTrue(firstBook.get("createdAt") instanceof String, "'createdAt' is not a string");

        Assert.assertTrue(firstBook.containsKey("updatedAt"), "Missing 'updatedAt'");
        Assert.assertTrue(firstBook.get("updatedAt") instanceof String, "'updatedAt' is not a string");
    }

    @Test(priority = 4, description = "check delete household by id " ,dependsOnMethods = {"checkDeleteHouseHoldById_P"})
    public void checkGetHouseHoldAfterDelete_P() {
        Response response =
                given()
                        .auth().basic("admin", "admin")
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                        .when().get("/households/" + householdID)
                        .then()
                        .log().all()
                        .assertThat().statusCode(404).extract().response();

        Assert.assertTrue(response.getTime() < 5000);
    }

    private void validateHouseHoldDetails(String expectedName) {
        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .when().get("/households/" + householdID)
                        .then()
                        .log().all()
                        .assertThat().statusCode(200).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

        String actualId = response.jsonPath().getString("id");
        String actualName = response.jsonPath().getString("name");

        Assert.assertNotNull(actualId, "ID in response is null");
        Assert.assertNotNull(actualName, "name in response is null");

        Assert.assertEquals(actualId, String.valueOf(householdID), "HouseHold ID does not match");
        Assert.assertEquals(actualName, expectedName, "HouseHold Name does not match");
    }

}
