package model;

public class CreateBookBody {

    public static String getCreateBookBody(String title , String author , String isbn , String releaseDate){
        String requestBody =
                "{\n" +
                "    \"title\": \"" + title + "\",\n" +
                "    \"author\": \"" + author + "\",\n" +
                "    \"isbn\": \"" + isbn + "\",\n" +
                "    \"releaseDate\": \"" + releaseDate + "\"\n" +
                "}";
        return requestBody;
    }




}
