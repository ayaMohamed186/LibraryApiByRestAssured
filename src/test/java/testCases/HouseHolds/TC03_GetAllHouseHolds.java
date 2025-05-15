package testCases.HouseHolds;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TC03_GetAllHouseHolds extends TestBase {

    @Test(priority = 1, description = "Get household details api by id")
    public void checkRetrieveAllBooks_P() {
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

}
