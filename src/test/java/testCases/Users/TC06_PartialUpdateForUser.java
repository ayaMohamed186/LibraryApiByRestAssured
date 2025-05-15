package testCases.Users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.createUserBody;
import testCases.TestBase;

import static io.restassured.RestAssured.given;
import static util.Utility.*;

public class TC06_PartialUpdateForUser extends TestBase {



    @Test(priority = 1, description = "check partial update for user api with valid data")
    public void checkPartialUpdate_P() throws JsonProcessingException {

        createUserBody createBody = new createUserBody();
        createBody.setFirstName(generateRandomFirstName()).setLastName(generateRandomLastName()).setEmail(userEmail);
        ObjectMapper mapper = new ObjectMapper();


        Response response =
                given()
                        .auth().basic("admin", "admin")
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                        .body(mapper.writeValueAsString(createBody))

                        .when().put("/users/" + userID)
                        .then()
                        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/createUserSchema.json"))
                        .log().all().assertThat().statusCode(200).extract().response();


        Assert.assertTrue(response.getTime() < 5000);


    }

}
