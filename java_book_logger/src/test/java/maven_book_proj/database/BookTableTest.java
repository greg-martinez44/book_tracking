package maven_book_proj.database;

import org.junit.jupiter.api.Test;

import maven_book_proj.objects.Book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class BookTableTest implements CommonTests {

    private BookTable bookTable;

    @BeforeEach
    void setUp() throws SQLException {
        this.bookTable = new BookTable("dev");
    }

    @AfterEach
    void tearDown() throws SQLException {
        this.bookTable.disconnect();
    }

    @Test
    public void testGetTableName() {
        String expectedResult = "library_dev.books";
        String actualResult = this.bookTable.getTableName();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetRecords() throws SQLException {
        List<Book> books = this.bookTable.getRecords();
        Integer numberOfBooksAtTimeOfTesting = 740;
        Integer numberOfRecordsReturned = books.size();
        assertTrue(numberOfRecordsReturned >= numberOfBooksAtTimeOfTesting);
    }

    @Test
    public void testGetRecord() throws SQLException {
        //
    }

    @Test
    void testFirstBookInGetBooksMatchesFirstBookInDatabase() throws SQLException {
        List<Book> books = this.bookTable.getRecords();
        String expectedFirstBookTitle = "Uzumaki";
        String actualFirstBookTitle = books.get(0).getTitle();
        assertEquals(expectedFirstBookTitle, actualFirstBookTitle);
    }

    @Test
    public void testGetTableIdFromTableIdentifier() throws SQLException {
        String bookTitleToSearch = "Slaughterhouse-Five";
        Integer expectedResult = 158;
        Integer actualResult = this.bookTable.getTableId(bookTitleToSearch);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetTableIdFromTableIdentifierWithMultipleEntries() throws SQLException {
        String bookTitleToSearch = "The Heroes";
        Integer expectedResult = 789;
        Integer actualResult = this.bookTable.getTableId(bookTitleToSearch);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testOnlyOneConnectionAllowed() throws SQLException {
        assertThrows(SQLException.class, () -> new BookTable("dev"));
    }

    @Test
    void testOnlyOneConnectionAllowedOfEitherSchema() throws SQLException {
        assertThrows(SQLException.class, () -> new BookTable("main"));
    }

    @Test
    void testDisconnectingOpensUpNewInstanceOfBookTable() throws SQLException {
        this.bookTable.disconnect();
        BookTable bookTable2 = new BookTable("dev");
        bookTable2.disconnect();
        assertTrue(true);
    }

    @Test
    void testBookTableCannotPerformQueriesWhenDisconnected() throws SQLException {
        this.bookTable.disconnect();
        assertThrows(SQLException.class, () -> this.bookTable.getRecords());
    }

    @Test
    void testGetTableIdReturnsZeroIfBookDoesNotExist() throws SQLException {
        String title = "some new book";
        Integer expectedResult = 0;
        Integer actualResult = this.bookTable.getTableId(title);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testInsertRecord() throws SQLException {
        HashMap<String, String> bookData = new HashMap<>() {
            {
                put("title", "test_title");
                put("year", "2022");
                put("genre", "fantasy");
                put("format", "paperback");
                put("pages", "123");
                put("duration", null);
                put("author_id", "999");
                put("imprint_id", "99999");
                put("translator_id", null);
                put("illustrator_id", null);
                put("narrator_id", null);
            }
        };
        Book book = new Book(bookData);
        this.bookTable.insertRecord(book);
        String bookTitle = bookData.get("title");
        Integer bookId = this.bookTable.getTableId(bookTitle);
        assertTrue(bookId > 0);
    }

    @Test
    public void testDeleteRecord() throws SQLException {
        String bookTitle = "test_title";
        Integer bookId = this.bookTable.getTableId(bookTitle);
        if (bookId == 0) {
            HashMap<String, String> bookData = new HashMap<>() {
                {
                    put("title", bookTitle);
                    put("year", "2022");
                    put("genre", "fantasy");
                    put("format", "paperback");
                    put("pages", "123");
                    put("duration", null);
                    put("author_id", "999");
                    put("imprint_id", "99999");
                    put("translator_id", null);
                    put("illustrator_id", null);
                    put("narrator_id", null);
                }
            };
            Book book = new Book(bookData);
            this.bookTable.insertRecord(book);
            bookId = this.bookTable.getTableId(bookTitle);
        }
        this.bookTable.deleteRecord(bookId);
        Integer expectedValue = 0;
        Integer actualValue = this.bookTable.getTableId(bookTitle);
        assertEquals(expectedValue, actualValue);
    }

}
