package maven_book_proj.database;

import maven_book_proj.objects.CompletedRead;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

public class CompletedReadTableTest implements CommonTests {

    private CompletedReadTable completedReadTable;

    @BeforeEach
    void setUp() throws SQLException {
        this.completedReadTable = new CompletedReadTable("dev");
    }

    @AfterEach
    void tearDown() throws SQLException {
        this.completedReadTable.disconnect();
    }

    @Test
    public void testOnlyOneConnectionAllowed() {
        assertThrows(SQLException.class, () -> new CompletedReadTable("dev"));
    }

    @Test
    public void testGetTableName() {
        String expectedValue = "library_dev.completed_reads";
        String actualValue = this.completedReadTable.getTableName();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetRecords() throws SQLException {
        List<CompletedRead> completedReads = this.completedReadTable.getRecords();
        Integer numberOfRecordsAtTimeOfTest = 779;
        Integer numberOfRecordsReturned = completedReads.size();
        assertTrue(numberOfRecordsReturned >= numberOfRecordsAtTimeOfTest);
    }

    @Test
    public void testGetRecord() throws SQLException {
        //
    }

    @Test
    public void testGetTableIdFromTableIdentifier() throws SQLException {
        Integer bookId = 339;
        Integer expectedValue = 340;
        Integer actualValue = this.completedReadTable.getTableId(bookId);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetTableIdFromTableIdentifierGetsMostRecentReadId() throws SQLException {
        Integer bookId = 594;
        Integer expectedValue = 633;
        Integer actualValue = this.completedReadTable.getTableId(bookId);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testInsertRecord() throws SQLException {
        String bookId = "99999";
        String started = "2023-01-01";
        String finished = "2024-01-01";
        String rating = "5";
        HashMap<String, String> completedReadData = new HashMap<>() {
            {
                put("book_id", bookId);
                put("started", started);
                put("finished", finished);
                put("rating", rating);
            }
        };
        CompletedRead completedRead = new CompletedRead(completedReadData);
        this.completedReadTable.insertRecord(completedRead);
        Integer readId = this.completedReadTable.getTableId(bookId);
        assertTrue(readId > 0);
    }

    @Test
    public void testDeleteRecord() throws SQLException {
        String bookId = "99999";
        Integer readId = this.completedReadTable.getTableId(bookId);
        if (readId == 0) {
            String started = "2023-01-01";
            String finished = "2024-01-01";
            String rating = "5";
            HashMap<String, String> completedReadData = new HashMap<>() {
                {
                    put("book_id", bookId);
                    put("started", started);
                    put("finished", finished);
                    put("rating", rating);
                }
            };
            CompletedRead completedRead = new CompletedRead(completedReadData);
            this.completedReadTable.insertRecord(completedRead);
            readId = this.completedReadTable.getTableId(bookId);
        }
        this.completedReadTable.deleteRecord(readId);
        Integer expectedValue = 0;
        Integer actualValue = this.completedReadTable.getTableId(bookId);
        assertEquals(expectedValue, actualValue);
    }
}
