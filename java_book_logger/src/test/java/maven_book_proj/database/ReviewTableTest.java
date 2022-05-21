package maven_book_proj.database;

import maven_book_proj.objects.Review;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

public class ReviewTableTest implements CommonTests {

    private ReviewTable reviewTable;

    @BeforeEach
    void setUp() throws SQLException {
        reviewTable = new ReviewTable("dev");
    }

    @AfterEach
    void tearDown() throws SQLException {
        reviewTable.disconnect();
    }

    @Test
    public void testOnlyOneConnectionAllowed() {
        assertThrows(SQLException.class, () -> new ReviewTable("dev"));
    }

    @Test
    public void testGetTableName() {
        String expectedValue = "library_dev.reviews";
        String actualValue = this.reviewTable.getTableName();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetRecords() throws SQLException {
        List<Review> reviews = this.reviewTable.getRecords();
        Integer numberOfRecordsAtTimeOfTest = 2;
        Integer numberOfRecordsReturned = reviews.size();
        assertTrue(numberOfRecordsReturned >= numberOfRecordsAtTimeOfTest);
    }

    @Test
    public void testGetRecord() throws SQLException {
        //
    }

    @Test
    public void testGetTableIdFromTableIdentifier() throws SQLException {
        //
    }

    @Test
    public void testInsertRecord() throws SQLException {
        //
    }

    @Test
    public void testDeleteRecord() throws SQLException {
        //
    }
}
