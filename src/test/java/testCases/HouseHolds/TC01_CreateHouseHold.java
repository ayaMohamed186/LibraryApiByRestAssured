package testCases.HouseHolds;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import static io.restassured.RestAssured.given;
import static util.Utility.*;

public class TC01_CreateHouseHold extends TestBase {

    String createdAt, updatedAt, dateFormatRegex;

    @Test(priority = 1, description = "Create new household with valid data")
    public void checkCreateNewHouseHold_P() {
        JSONObject createHouseHoldBody = new JSONObject();

        createHouseHoldBody.put("name", generateRandomHouseholdName());

        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .body(createHouseHoldBody.toJSONString())  //to json string?

                .when().post("/households")
                .then()
                        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/createHouseHoldSchema.json"))
                        .log().all().assertThat().statusCode(201).extract().response();

        Assert.assertTrue(response.getTime() < 5000);
        Assert.assertEquals(response.jsonPath().getString("name"), createHouseHoldBody.get("name"), "Returned name does not match the request name");


        createdAt = response.jsonPath().getString("createdAt");
        updatedAt = response.jsonPath().getString("updatedAt");
        dateFormatRegex = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$";

        Assert.assertTrue(createdAt.matches(dateFormatRegex), "createdAt is not in valid format");
        Assert.assertTrue(updatedAt.matches(dateFormatRegex), "updatedAt is not in valid format");


        //extract values in response
        householdID = response.jsonPath().getInt("id");
        houseHoldName = response.jsonPath().getString("name");


    }
}
