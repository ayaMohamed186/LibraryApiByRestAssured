package testCases.HouseHolds;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import static io.restassured.RestAssured.given;
import static util.Utility.*;

public class TC03_UpdateHouseHold extends TestBase {

    String createdAt, updatedAt, dateFormatRegex;

    @Test(priority = 1, description = "Update household api with valid data")
    public void checkUpdateHouseHoldAdded_P() {
        JSONObject updateHouseHoldBody = new JSONObject();

        updateHouseHoldBody.put("name", generateRandomHouseholdName());

        Response response =
                given()
                        .auth().basic("admin", "admin")
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                        .body(updateHouseHoldBody.toJSONString())

                .when().put("/households/" + householdID)
                .then()
                        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/createHouseHoldSchema.json"))
                        .log().all().assertThat().statusCode(200).extract().response();


        Assert.assertTrue(response.getTime() < 5000);

        Assert.assertEquals(response.jsonPath().getString("name"), updateHouseHoldBody.get("name"), "Returned name does not match the request name");

        createdAt = response.jsonPath().getString("createdAt");
        updatedAt = response.jsonPath().getString("updatedAt");
        dateFormatRegex = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$";

        Assert.assertTrue(createdAt.matches(dateFormatRegex), "createdAt is not in valid format");
        Assert.assertTrue(updatedAt.matches(dateFormatRegex), "updatedAt is not in valid format");


        // save update response values to use it in next get request after update
        householdID = response.jsonPath().getInt("id");
        houseHoldName = response.jsonPath().getString("name");
    }


}
