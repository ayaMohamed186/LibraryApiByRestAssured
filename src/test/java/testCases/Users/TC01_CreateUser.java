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

public class TC01_CreateUser extends TestBase {
    String  createdAt ,updatedAt,dateFormatRegex;

    @Test(priority = 1, description = "Create user api with valid data")
    public void checkCreateNewBook_P() throws JsonProcessingException {
        createUserBody createBody = new createUserBody();
        createBody.setFirstName(generateRandomFirstName()).setLastName(generateRandomLastName()).setEmail(generateRandomEmail());

        ObjectMapper mapper = new ObjectMapper();
        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .body(mapper.writeValueAsString(createBody))

                .when().post("/users")
                .then()
                        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/createUserSchema.json"))
                        .log().all().assertThat().statusCode(201).extract().response();


        Assert.assertTrue(response.getTime() < 5000);

        Assert.assertEquals(response.jsonPath().getString("firstName"), createBody.getFirstName(), "Returned title does not match the request title");
        Assert.assertEquals(response.jsonPath().getString("lastName"), createBody.getLastName(), "Returned author does not match the request author");
        Assert.assertEquals(response.jsonPath().getString("email"), createBody.getEmail() , "Returned ISBN does not match the request ISBN");


        createdAt = response.jsonPath().getString("createdAt");
        updatedAt = response.jsonPath().getString("updatedAt");
        dateFormatRegex = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$";

        Assert.assertTrue(createdAt.matches(dateFormatRegex), "createdAt is not in valid format");
        Assert.assertTrue(updatedAt.matches(dateFormatRegex), "updatedAt is not in valid format");

//
        // save response values to use it in next request
        userID = response.jsonPath().getInt("id");
        userFirstName = response.jsonPath().getString("firstName");
        userLastName = response.jsonPath().getString("lastName");
        userEmail = response.jsonPath().getString("email");

    }

}
