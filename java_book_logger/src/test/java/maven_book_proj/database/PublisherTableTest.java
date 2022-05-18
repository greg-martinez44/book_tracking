package maven_book_proj.database;

import maven_book_proj.objects.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

public class PublisherTableTest implements CommonTests {
    private PublisherTable publisherTable;

    @BeforeEach
    void setUp() throws SQLException {
        this.publisherTable = new PublisherTable("dev");
    }

    @AfterEach
    void tearDown() throws SQLException {
        this.publisherTable.disconnect();
    }

    @Test
    public void testGetTableName() {
        String expectedResult = "library_dev.publishers";
        String actualResult = this.publisherTable.getTableName();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetTableNameWithMain() throws SQLException {
        this.publisherTable.disconnect();
        PublisherTable mainPublisherTable = new PublisherTable("main");
        String expectedResult = "library.publishers";
        String actualResult = mainPublisherTable.getTableName();
        assertEquals(expectedResult, actualResult);
        mainPublisherTable.disconnect();
    }

    @Test
    public void testOnlyOneConnectionAllowed() throws SQLException {
        assertThrows(SQLException.class, () -> new PublisherTable("dev"));
    }

    @Test
    void testPublisherTableCanExistWithAuthorTable() throws SQLException {
        AuthorTable authorTable = new AuthorTable("dev");
        try {
            assertTrue(true);
        } finally {
            authorTable.disconnect();
        }
    }

    @Test
    void testPublisherTableCanExistWithBookTable() throws SQLException {
        BookTable bookTable = new BookTable("dev");
        try {
            assertTrue(true);
        } finally {
            bookTable.disconnect();
        }
    }

    @Test
    public void testGetRecords() throws SQLException {
        List<Publisher> publishers = this.publisherTable.getRecords();
        Integer numberOfPublishersAtTimeOfTest = 252;
        Integer numberOfRecordsReturned = publishers.size();
        assertTrue(numberOfRecordsReturned >= numberOfPublishersAtTimeOfTest);
    }

    @Test
    public void testGetRecord() throws SQLException {
        //
    }

    @Test
    public void testGetTableIdFromTableIdentifier() throws SQLException {
        String imprintName = "Henry Holt";
        Integer expectedResult = 3;
        Integer actualResult = this.publisherTable.getTableId(imprintName);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testInsertRecord() throws SQLException {
        HashMap<String, String> publisherData = new HashMap<>();
        String imprintName = "test imprint";
        String publishingHouseName = "test publishing house";
        publisherData.put("imprint", imprintName);
        publisherData.put("publishing_house", publishingHouseName);
        Publisher publisher = new Publisher(publisherData);
        this.publisherTable.insertRecord(publisher);
        Integer actualResult = this.publisherTable.getTableId(imprintName);
        assertTrue(actualResult > 0);
    }

    @Test
    public void testDeleteRecord() throws SQLException {
        String imprintName = "test imprint";
        Integer imprintId = this.publisherTable.getTableId(imprintName);
        if (imprintId > 0) {
            this.publisherTable.deleteRecord(imprintId);
        }
        Integer expectedResult = 0;
        Integer actualResult = this.publisherTable.getTableId(imprintName);
        assertEquals(expectedResult, actualResult);
    }
}
