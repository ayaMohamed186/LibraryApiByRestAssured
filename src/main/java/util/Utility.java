package util;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utility {
    public static String generateRandomBookName() {
        String[] adjectives = {"Mystic", "Silent", "Hidden", "Fateful", "Endless", "Eternal", "Lonely", "Wandering", "Forgotten"};
        String[] nouns = {"Journey", "Dream", "World", "Legacy", "Whisper", "Revenge", "Courage", "Heart", "Kingdom"};

        Random random = new Random();
        String randomAdjective = adjectives[random.nextInt(adjectives.length)];
        String randomNoun = nouns[random.nextInt(nouns.length)];

        return randomAdjective + " " + randomNoun;
    }

    public static String generateRandomAuthorName() {
        String[] firstNames = {"James", "Sarah", "Michael", "Emily", "David", "Laura", "John", "Rebecca", "William", "Olivia"};
        String[] lastNames = {"Smith", "Johnson", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas"};

        Random random = new Random();
        String randomFirstName = firstNames[random.nextInt(firstNames.length)];
        String randomLastName = lastNames[random.nextInt(lastNames.length)];

        return randomFirstName + " " + randomLastName;
    }

    public static String generateRandomISBN() {
        StringBuilder isbn = new StringBuilder("978"); // ISBN-13 prefix
        Random random = new Random();

        // Generate the next 9 digits
        for (int i = 0; i < 9; i++) {
            isbn.append(random.nextInt(10)); // Random digit between 0 and 9
        }

        // Calculate the check digit
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        isbn.append(checkDigit);

        return isbn.toString();
    }

    public static String generateRandomDate() {
        // Define start and end instants
        Instant start = Instant.parse("2000-01-01T00:00:00.000Z");
        Instant end = Instant.parse("2024-01-01T00:00:00.000Z");

        // Generate random timestamp in milliseconds
        long randomEpochMilli = ThreadLocalRandom.current().nextLong(start.toEpochMilli(), end.toEpochMilli());

        // Convert to LocalDateTime and format to ISO 8601
        LocalDateTime randomDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(randomEpochMilli), ZoneOffset.UTC);
        return randomDateTime.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }

    public static String generateRandomHouseholdName() {
        String[] adjectives = {"Happy", "Sunny", "Cozy", "Quiet", "Lovely"};
        String[] nouns = {"Nest", "Home", "Household", "Place", "Residence"};

        Random random = new Random();

        String randomAdjective = adjectives[random.nextInt(adjectives.length)];
        String randomNoun = nouns[random.nextInt(nouns.length)];
        int randomNumber = 1000 + random.nextInt(9000); // Generates a number from 1000 to 9999

        return randomAdjective + randomNoun + randomNumber;
    }



    public static String generateRandomFirstName() {
        String[] namePrefixes = {"El", "Jo", "Ma", "Ka", "Lu"};
        String[] nameRoots = {"li", "hn", "ry", "na", "ra", "ia"};

        Random random = new Random();

        String prefix = namePrefixes[random.nextInt(namePrefixes.length)];
        String root = nameRoots[random.nextInt(nameRoots.length)];
        int number = 1 + random.nextInt(99); // Random number from 1 to 99

        return prefix + root + number;
    }


    public static String generateRandomLastName() {
        String[] namePrefixes = {"Mc", "Van", "De", "O'", "Fitz"};
        String[] nameRoots = {"Donald", "Smith", "Johnson", "Brown", "Taylor", "Miller", "Clark"};

        Random random = new Random();

        String prefix = namePrefixes[random.nextInt(namePrefixes.length)];
        String root = nameRoots[random.nextInt(nameRoots.length)];
        int number = 100 + random.nextInt(900); // Random 3-digit number

        return prefix + root + number;
    }

    public static String generateRandomEmail() {
        String[] userPrefixes = {"user", "mail", "contact", "info", "hello"};
        String[] domains = {"example.com", "mail.com", "test.org", "demo.net", "inbox.io"};

        Random random = new Random();

        String prefix = userPrefixes[random.nextInt(userPrefixes.length)];
        int number = 1000 + random.nextInt(9000); // Random number from 1000 to 9999
        String domain = domains[random.nextInt(domains.length)];

        return prefix + number + "@" + domain;
    }




    public static String getSingleJsonData(String jsonFilePath, String key) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader(jsonFilePath);
        Object obj = jsonParser.parse(fileReader);

        JSONObject jsonObject = (JSONObject) obj;
        return jsonObject.get(key).toString();
    }

    public static String getExcelData(int RowNum, int ColNum, String SheetName) {
        XSSFWorkbook workBook;
        XSSFSheet sheet;
        String projectPath = System.getProperty("user.dir");
        String cellData = null;
        try {
            workBook = new XSSFWorkbook(projectPath + "/src/test/resources/data/data.xlsx");
            sheet = workBook.getSheet(SheetName);
            cellData = sheet.getRow(RowNum).getCell(ColNum).getStringCellValue();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
        return cellData;
    }
}
