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

public class TC04_UpdateUserById extends TestBase {
    String createdAt ,updatedAt,dateFormatRegex;


    @Test(priority = 1, description = "Create user api with valid data")
    public void checkUpdateBookAdded_P() throws JsonProcessingException {
        createUserBody createBody = new createUserBody();
        createBody.setFirstName(generateRandomFirstName()).setLastName(generateRandomLastName()).setEmail(generateRandomEmail());

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

        Assert.assertEquals(response.jsonPath().getString("firstName"), createBody.getFirstName(), "Returned firstname does not match the request firstname");
        Assert.assertEquals(response.jsonPath().getString("lastName"), createBody.getLastName(), "Returned lastname does not match the request lastname");
        Assert.assertEquals(response.jsonPath().getString("email"), createBody.getEmail(), "Returned email does not match the request email");


        createdAt = response.jsonPath().getString("createdAt");
        updatedAt = response.jsonPath().getString("updatedAt");
        dateFormatRegex = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$";

        Assert.assertTrue(createdAt.matches(dateFormatRegex), "createdAt is not in valid format");
        Assert.assertTrue(updatedAt.matches(dateFormatRegex), "updatedAt is not in valid format");



        // save update response values to use it in next get request after update
        userID = response.jsonPath().getInt("id");
        userFirstName = response.jsonPath().getString("firstName");
        userLastName = response.jsonPath().getString("lastName");
        userEmail = response.jsonPath().getString("email");
    }
}