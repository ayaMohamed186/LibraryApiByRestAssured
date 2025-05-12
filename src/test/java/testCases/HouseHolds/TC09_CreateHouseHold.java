package testCases.HouseHolds;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import testCases.TestBase;
import static io.restassured.RestAssured.given;
import static util.Utility.*;

public class TC09_CreateHouseHold extends TestBase {


    @Test(priority = 1, description = "Create new household with valid data")
    public void checkCreateNewHouseHold_P() {
        JSONObject createHouseHoldBody = new JSONObject();

        createHouseHoldBody.put("name",generateRandomHouseholdName());

        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .body(createHouseHoldBody.toJSONString())  //to json string?

                .when().post("/households")
                .then()
                        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/createHouseHoldSchema.json"))
                        .log().all().assertThat().statusCode(201).extract().response();

        

    }
}
