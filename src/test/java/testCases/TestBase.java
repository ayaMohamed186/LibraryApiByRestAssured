package testCases;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;

public class TestBase {

    public static int bookID,householdID, userID ;
    public static String bookTitle,bookAuthor,bookIsbn,bookReleaseDate,houseHoldName,userFirstName,userLastName,userEmail;

    @BeforeTest
    public void setBaseURL(){
        RestAssured.baseURI = "http://localhost:3000";
    }


}
