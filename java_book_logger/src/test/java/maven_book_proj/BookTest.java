package maven_book_proj;

import maven_book_proj.objects.*;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    private Book book;

    @BeforeEach
    void setUp() {
        HashMap<String, String> bookData = new HashMap<>() {
            {
                put("book_id", "1");
                put("title", "test book");
                put("year", "2022");
                put("genre", "general_fiction");
                put("format", "hardcover");
                put("pages", "1");
                put("duration", "");
                put("author_id", "1");
                put("imprint_id", "1");
                put("translator_id", "1");
                put("narrator_id", "1");
                put("illustrator_id", "1");
            }
        };
        this.book = new Book(bookData);
    }

    @Test
    void testConstructor() {
        assertInstanceOf(Book.class, this.book);
    }

    @Test
    void testGetBookTitleReturnsString() {
        String expectedResult = "test book";
        String actualResult = this.book.getTitle();
        assertEquals(expectedResult, actualResult);
    }
}
