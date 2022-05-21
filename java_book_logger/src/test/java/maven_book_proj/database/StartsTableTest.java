package maven_book_proj.database;

import maven_book_proj.objects.StartedRead;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

public class StartsTableTest implements CommonTests {

    private StartsTable startsTable;

    @BeforeEach
    void setUp() throws SQLException {
        this.startsTable = new StartsTable("dev");
    }

    @AfterEach
    void tearDown() throws SQLException {
        this.startsTable.disconnect();
    }

    @Test
    public void testGetTableName() {
        String expectedResult = "library_dev.started_reads";
        String actualResult = this.startsTable.getTableName();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testOnlyOneConnectionAllowed() {
        assertThrows(SQLException.class, () -> new StartsTable("dev"));
    }

    @Test
    public void testGetRecords() throws SQLException {
        List<StartedRead> startedReads = this.startsTable.getRecords();
        Integer numberOfRecordsAtTimeOfTesting = 386;
        Integer numberOfRecordsReturned = startedReads.size();
        assertTrue(numberOfRecordsReturned >= numberOfRecordsAtTimeOfTesting);
    }

    @Test
    public void testGetRecord() throws SQLException {
        Integer startId = 13;
        String[] expectedValues = new String[] {
                "13",
                "44",
                "2021-09-25",
                "0"
        };
        StartedRead actualValues = this.startsTable.getRecord(startId);
        assertEquals(expectedValues[0], actualValues.getStartId().toString());
        assertEquals(expectedValues[1], actualValues.getBookId().toString());
        assertEquals(expectedValues[2], actualValues.getStarted());
        assertEquals(expectedValues[3], actualValues.getAddedToReads().toString());
    }

    @Test
    void testGetStartedReadFromBookIdWithSingleResult() throws SQLException {
        Integer bookId = 14;
        List<StartedRead> startedReads = this.startsTable.getRecords(bookId);
        StartedRead startedRead = startedReads.get(0);
        String expectedStarted = "2021-10-02";
        String actualStarted = startedRead.getStarted();
        Integer expectedAddedToRead = 1;
        Integer actualAddedToRead = startedRead.getAddedToReads();
        assertEquals(expectedStarted, actualStarted);
        assertEquals(expectedAddedToRead, actualAddedToRead);
    }

    @Test
    void testGetStartedReadsFromBookIdWithMultipleResult() throws SQLException {
        Integer bookId = 620;
        List<StartedRead> startedReads = this.startsTable.getRecords(bookId);
        StartedRead startedRead1 = startedReads.get(0);
        StartedRead startedRead2 = startedReads.get(1);
        String expectedStarted1 = "2021-05-29";
        String actualStarted1 = startedRead1.getStarted();
        String expectedStarted2 = "2021-12-29";
        String actualStarted2 = startedRead2.getStarted();
        assertEquals(expectedStarted1, actualStarted1);
        assertEquals(expectedStarted2, actualStarted2);
    }

    @Test
    public void testGetTableIdFromTableIdentifier() throws SQLException {
        Integer bookId = 620;
        String started = "2021-05-29";
        Integer expectedResult = 294;
        Integer actualResult = this.startsTable.getTableId(bookId, started);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testInsertRecord() throws SQLException {
        String bookId = "99999";
        String started = "2022-05-06";
        HashMap<String, String> startedReadParams = new HashMap<>();
        startedReadParams.put("book_id", bookId);
        startedReadParams.put("started", started);
        StartedRead startedRead = new StartedRead(startedReadParams);
        this.startsTable.insertRecord(startedRead);
        List<StartedRead> startedReads = this.startsTable.getRecords(startedRead.getBookId());
        assertTrue(startedReads.size() > 0);
    }

    @Test
    void testInsertedStartedReadHasAddedToReadEqualsZero() throws SQLException {
        Integer bookId = 99999;
        Integer startId = this.startsTable.getTableId(bookId);
        if (startId == 0 || startId == null) {
            String started = "2022-05-06";
            HashMap<String, String> startedReadParams = new HashMap<>();
            startedReadParams.put("book_id", bookId.toString());
            startedReadParams.put("started", started);
            StartedRead startedRead = new StartedRead(startedReadParams);
            this.startsTable.insertRecord(startedRead);
        }
        StartedRead startedRead = this.startsTable.getRecord(startId);
        Integer expectedResult = 0;
        Integer actualResult = startedRead.getAddedToReads();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetStartIdFromBookId() throws SQLException {
        Integer bookId = 99999;
        String started = "2022-05-06";
        Integer expectedResult = this.startsTable.getTableId(bookId, started);
        Integer actualResult = this.startsTable.getTableId(bookId);
        assertEquals(expectedResult, actualResult);
        assertTrue(actualResult > 0);
    }

    @Test
    void testGetStartIdFromBookIdGetsMostRecentNonAddedToRead() throws SQLException {
        String bookId = "99999";
        String started = "2022-08-01";
        HashMap<String, String> startedReadParams = new HashMap<>();
        startedReadParams.put("book_id", bookId);
        startedReadParams.put("started", started);
        StartedRead startedRead = new StartedRead(startedReadParams);
        this.startsTable.insertRecord(startedRead);
        Integer expectedResult = this.startsTable.getTableId(startedRead.getBookId(), started);
        Integer actualResult = this.startsTable.getTableId(startedRead.getBookId());
        assertEquals(expectedResult, actualResult);
        this.startsTable.deleteRecord(actualResult);
    }

    @Test
    public void testUpdateRecord() throws SQLException {
        String bookId = "99999";
        Integer startId = this.startsTable.getTableId(bookId);
        if (startId == 0) {
            String started = "2022-05-06";
            HashMap<String, String> startedReadParams = new HashMap<>();
            startedReadParams.put("book_id", bookId);
            startedReadParams.put("started", started);
            StartedRead startedRead = new StartedRead(startedReadParams);
            this.startsTable.insertRecord(startedRead);
            startId = this.startsTable.getTableId(bookId);
        }
        this.startsTable.updateRecord(startId, "added_to_reads", "1");
        StartedRead updatedStartedRead = this.startsTable.getRecord(startId);
        Integer expectedValue = 1;
        Integer actualValue = updatedStartedRead.getAddedToReads();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testDeleteRecord() throws SQLException {
        Integer bookId = 99999;
        Integer startId = this.startsTable.getTableId(bookId);
        this.startsTable.deleteRecord(startId);
        Integer expectedResult = 0;
        Integer actualResult = this.startsTable.getTableId(bookId);
        if (actualResult > 0) {
            startId = this.startsTable.getTableId(bookId);
            this.startsTable.deleteRecord(startId);
        }
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testNonExistingRecordGivesNullDate() throws SQLException {
        String bookId = "0";
        Integer startId = this.startsTable.getTableId(bookId);
        StartedRead startedRead = this.startsTable.getRecord(startId);
        String expectedValue = null;
        String actualValue = startedRead.getStarted();
        assertEquals(expectedValue, actualValue);
    }
}
