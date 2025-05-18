package testCases.Users;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TC02_GetUser extends TestBase {
    String id,firstName,lastName,email ,responseBody;

    @Test(priority = 1, description = "Get user details API by ID", dependsOnMethods = {"checkCreateNewUser_P"})
    public void checkGetUserById_P() {
        validateUserDetails(userFirstName, userLastName, userEmail);
    }

    @Test(priority = 2, description = "Get user details API after update", dependsOnMethods = {"checkUpdateUserAdded_P"})
    public void checkGetUserAfterUpdate_P() {
        validateUserDetails(userFirstName, userLastName, userEmail);
    }

    @Test(priority = 3, description = "Get user details api by id" ,dependsOnMethods = {"checkPartialUpdate_P"})
    public void checkRetrieveAllUsers_P() {
        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .when().get("/users")
                        .then()
                        .log().all()
                        .assertThat().statusCode(200).extract().response();

        Assert.assertTrue(response.getTime() < 5000);


        // to captures the json array of users
        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> books = jsonPath.getList("");

        // Assert response is a non-empty array
        Assert.assertNotNull(books, "Response is null");
        Assert.assertFalse(books.isEmpty(), "Books array is empty");

        Map<String, Object> firstBook = books.get(0);

        Assert.assertTrue(firstBook.containsKey("id"), "Missing 'id'");
        Assert.assertTrue(firstBook.get("id") instanceof Integer, "'id' is not a number");

        Assert.assertTrue(firstBook.containsKey("firstName"), "Missing 'firstName'");
        Assert.assertTrue(firstBook.get("firstName") instanceof String, "'firstName' is not a string");

        Assert.assertTrue(firstBook.containsKey("lastName"), "Missing 'lastName'");
        Assert.assertTrue(firstBook.get("lastName") instanceof String, "'lastName' is not a string");

        Assert.assertTrue(firstBook.containsKey("email"), "Missing 'email'");
        Assert.assertTrue(firstBook.get("email") instanceof String, "'email' is not a string");

        Assert.assertTrue(firstBook.containsKey("createdAt"), "Missing 'createdAt'");
        Assert.assertTrue(firstBook.get("createdAt") instanceof String, "'createdAt' is not a string");

        Assert.assertTrue(firstBook.containsKey("updatedAt"), "Missing 'updatedAt'");
        Assert.assertTrue(firstBook.get("updatedAt") instanceof String, "'updatedAt' is not a string");
    }

    @Test(priority = 4, description = "check delete user by id " ,dependsOnMethods = {"checkDeleteUserById_P"})
    public void checkGetUserAfterDelete_P() {
        Response response =
                given()
                        .auth().basic("admin", "admin")
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                        .when().get("/users/" + userID)
                        .then()
                        .log().all()
                        .assertThat().statusCode(404).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

        responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("User not found"), "Expected message 'user not found' but got: " + responseBody);

    }



    private void validateUserDetails(String expectedFirstName, String expectedLastName, String expectedEmail) {
        Response response =
                given()
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .when().get("/users/" + userID)
                        .then()
                        .log().all()
                        .assertThat().statusCode(200).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

         id = response.jsonPath().getString("id");
         firstName = response.jsonPath().getString("firstName");
         lastName = response.jsonPath().getString("lastName");
         email = response.jsonPath().getString("email");

        Assert.assertNotNull(id, "ID in response is null");
        Assert.assertNotNull(firstName, "First name is null");
        Assert.assertNotNull(lastName, "Last name is null");
        Assert.assertNotNull(email, "Email is null");

        Assert.assertEquals(id, String.valueOf(userID), "User ID does not match");
        Assert.assertEquals(firstName, expectedFirstName, "First name does not match");
        Assert.assertEquals(lastName, expectedLastName, "Last name does not match");
        Assert.assertEquals(email, expectedEmail, "Email does not match");
    }


}
