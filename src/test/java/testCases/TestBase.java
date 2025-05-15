package testCases;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;

public class TestBase {

    public static int bookID,householdID, userID,staticWishListID;
    public static String bookTitle,bookAuthor,bookIsbn,bookReleaseDate,houseHoldName,userFirstName,userLastName,userEmail
            ,staticWishlistName,staticWishlistBooks;

    @BeforeTest
    public void setBaseURL(){
        RestAssured.baseURI = "http://localhost:3000";
    }


}
