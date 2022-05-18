package maven_book_proj.database;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.file.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookDBTest {

    @Test
    void testBookConnIsNotEmpty() throws IOException, JSONException {
        String actualBookConnString = System.getenv("BOOKS_CONN");
        if (actualBookConnString == null) {
            Path filePath = Paths.get("externals/env.json");
            List<String> fromFile = Files.readAllLines(
                    filePath,
                    StandardCharsets.UTF_8);
            String joinedString = String.join("", fromFile);
            JSONObject systemEnv = new JSONObject(joinedString);
            actualBookConnString = systemEnv.getString("BOOKS_CONN");
        }
        assertTrue(actualBookConnString != null);
    }

}
