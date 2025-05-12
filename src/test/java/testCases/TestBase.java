package testCases;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;

public class TestBase {

    public static int bookID;
    public static String bookTitle,bookAuthor,bookIsbn,bookReleaseDate;

    @BeforeTest
    public void setBaseURL(){
        RestAssured.baseURI = "http://localhost:3000";
    }


}
