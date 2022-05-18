package maven_book_proj.database;

import maven_book_proj.objects.Illustrator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

public class IllustratorTableTest implements CommonTests {

    private IllustratorTable illustratorTable;

    @BeforeEach
    void setUp() throws SQLException {
        this.illustratorTable = new IllustratorTable("dev");
    }

    @AfterEach
    void tearDown() throws SQLException {
        this.illustratorTable.disconnect();
    }

    @Test
    public void testOnlyOneConnectionAllowed() {
        assertThrows(SQLException.class, () -> new IllustratorTable("dev"));
    }

    @Test
    public void testGetTableName() {
        String expectedResult = "library_dev.illustrators";
        String actualResult = this.illustratorTable.getTableName();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetRecords() throws SQLException {
        List<Illustrator> illustrators = this.illustratorTable.getRecords();
        Integer numberOfRecordsAtTimeOfTest = 13;
        Integer numberOfRecordsReturned = illustrators.size();
        assertTrue(numberOfRecordsReturned >= numberOfRecordsAtTimeOfTest);
    }

    @Test
    public void testGetRecord() throws SQLException {
        //
    }

    @Test
    public void testGetTableIdFromTableIdentifier() throws SQLException {
        String illustrator = "Inio Asano";
        Integer expectedResult = 2;
        Integer actualResult = this.illustratorTable.getTableId(illustrator);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testInsertRecord() throws SQLException {
        String illustratorName = "test illustrator";
        HashMap<String, String> illustratorParams = new HashMap<>() {
            {
                put("illustrator", illustratorName);
            }
        };
        Illustrator illustrator = new Illustrator(illustratorParams);
        this.illustratorTable.insertRecord(illustrator);
        Integer actualResult = this.illustratorTable.getTableId(illustratorName);
        assertTrue(actualResult > 0);
    }

    @Test
    public void testDeleteRecord() throws SQLException {
        String illustratorName = "test illustrator";
        Integer illustratorId = this.illustratorTable.getTableId(illustratorName);
        if (illustratorId == 0) {
            HashMap<String, String> illustratorParams = new HashMap<>() {
                {
                    put("illustrator", illustratorName);
                }
            };
            this.illustratorTable.insertRecord(new Illustrator(illustratorParams));
            illustratorId = this.illustratorTable.getTableId(illustratorName);
        }
        this.illustratorTable.deleteRecord(illustratorId);
        Integer expectedResult = 0;
        Integer actualResult = this.illustratorTable.getTableId(illustratorName);
        assertEquals(expectedResult, actualResult);
    }
}
