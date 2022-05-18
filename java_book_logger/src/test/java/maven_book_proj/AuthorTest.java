package maven_book_proj;

import maven_book_proj.objects.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;

public class AuthorTest {

    private HashMap<String, String> testAuthorData;
    private Author author;

    @BeforeEach
    void setUp() {
        this.testAuthorData = new HashMap<>();
        this.testAuthorData.put("author_id", "1");
        this.testAuthorData.put("author", "test author");
        this.testAuthorData.put("other_authors", "test other author");
        this.author = new Author(this.testAuthorData);
    }

    @Test
    void testAuthorConstructor() {
        assertInstanceOf(Author.class, this.author);
    }

    @Test
    void testGetAuthorIdReturnsInteger() {
        Integer expectedResult = 1;
        Integer actualResult = this.author.getAuthorId();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetAuthorNameReturnsString() {
        String expectedResult = "test author";
        String actualResult = this.author.getAuthorName();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetOtherAuthorNameReturnsString() {
        String expectedResult = "test other author";
        String actualResult = this.author.getOtherAuthorName();
        assertEquals(expectedResult, actualResult);
    }
}
