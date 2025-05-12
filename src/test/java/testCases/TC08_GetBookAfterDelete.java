package testCases;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TC08_GetBookAfterDelete extends TestBase{

    String responseBody;
    @Test(priority = 1, description = "check delete book by id ")
    public void checkGetBookAfterDelete_P() {
        Response response =
                given()
                        .auth().basic("admin", "admin")
                        .log().all().header("Content-Type", "application/json")
                        .header("g-token", "ROM831ESV")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                .when().get("/books/" + bookID)
                .then()
                        .log().all()
                        .assertThat().statusCode(404).extract().response();

        Assert.assertTrue(response.getTime() < 5000);

        responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Book not found"), "Expected message 'Book not found' but got: " + responseBody);

    }

}
