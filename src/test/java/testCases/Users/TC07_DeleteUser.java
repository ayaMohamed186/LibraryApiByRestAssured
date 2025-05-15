package testCases.Users;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testCases.TestBase;

import static io.restassured.RestAssured.given;

public class TC07_DeleteUser extends TestBase {

    @Test(priority = 1, description = "check delete user by id ")
    public void checkDeleteBookById_P() {
        Response response =
                given()
                        .auth().basic("admin", "admin")
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                .when().delete("/users/" + userID)
                .then()
                        .log().all()
                        .assertThat().statusCode(204).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

    }
}
