package maven_book_proj.database;

import maven_book_proj.objects.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.HashMap;

import java.sql.SQLException;

public class AuthorTableTest implements CommonTests {

    private AuthorTable authorTable;

    @BeforeEach
    void setUp() throws SQLException {
        this.authorTable = new AuthorTable("dev");
    }

    @AfterEach
    void tearDown() throws SQLException {
        this.authorTable.disconnect();
    }

    private void deleteTestEntry(Integer id) throws SQLException {
        if (id > 0) {
            this.authorTable.deleteRecord(id);
        }
    }

    @Test
    void testGetTableNameMain() throws SQLException {
        this.authorTable.disconnect();
        AuthorTable mainAuthorTable = new AuthorTable("main");
        String expectedResult = "library.authors";
        String actualResult = mainAuthorTable.getTableName();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetTableName() {
        String expectedResult = "library_dev.authors";
        String actualResult = this.authorTable.getTableName();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testAuthorTableCanExistAtSameTimeAsBookTable() throws SQLException {
        BookTable bookTable = new BookTable("dev");
        try {
            assertTrue(true);
        } finally {
            bookTable.disconnect();
        }
    }

    @Test
    public void testOnlyOneConnectionAllowed() {
        assertThrows(SQLException.class, () -> new AuthorTable("dev"));
    }

    @Test
    void testNoTwoAuthorTablesCanExistOfEitherSchema() throws SQLException {
        assertThrows(SQLException.class, () -> new AuthorTable("main"));
    }

    @Test
    public void testGetRecords() throws SQLException {
        List<Author> authors = this.authorTable.getRecords();
        Integer numberOfAuthorsAtTimeOfTest = 463;
        Integer numberOfRecordsReturned = authors.size();
        assertTrue(numberOfRecordsReturned >= numberOfAuthorsAtTimeOfTest);
    }

    @Test
    public void testGetRecord() throws SQLException {
        //
    }

    @Test
    public void testGetTableIdFromTableIdentifier() throws SQLException {
        String author = "Aaron Rosenberg";
        Integer expectedResult = 18;
        Integer actualResult = this.authorTable.getTableId(author);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetTableIdFRomTableIdentifierWithOtherAuthor() throws SQLException {
        String author = "Aaron Rosenberg";
        String otherAuthor = "Christie Golden";
        Integer expectedResult = 19;
        Integer actualResult = this.authorTable.getTableId(author, otherAuthor);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetTableIdReturnsZeroIfAuthorDoesNotExist() throws SQLException {
        String author = "some new author";
        Integer expectedResult = 0;
        Integer actualResult = this.authorTable.getTableId(author);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testInsertRecord() throws SQLException {
        HashMap<String, String> authorData = new HashMap<>() {
            {
                put("author", "test author");
            }
        };
        Author author = new Author(authorData);
        this.authorTable.insertRecord(author);
        String authorName = authorData.get("author");
        Integer newAuthorId = this.authorTable.getTableId(authorName);
        assertTrue(newAuthorId > 0);
    }

    @Test
    void testInsertRecordWithOtherAuthor() throws SQLException {
        HashMap<String, String> authorData = new HashMap<>() {
            {
                put("author", "test author 2");
                put("other_authors", "test other author");
            }
        };
        Author author = new Author(authorData);
        this.authorTable.insertRecord(author);
        String authorName = authorData.get("author");
        String otherAuthorName = authorData.get("other_authors");
        Integer newAuthorId = this.authorTable.getTableId(authorName, otherAuthorName);
        assertTrue(newAuthorId > 0);
        deleteTestEntry(newAuthorId);

    }

    @Test
    public void testDeleteRecord() throws SQLException {
        String authorToDelete = "test author";
        Integer authorIdToDelete = this.authorTable.getTableId(authorToDelete);
        deleteTestEntry(authorIdToDelete);
        Integer expectedResult = 0;
        Integer actualResult = this.authorTable.getTableId(authorToDelete);
        assertEquals(expectedResult, actualResult);
    }
}
