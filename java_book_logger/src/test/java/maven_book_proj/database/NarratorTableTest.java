package maven_book_proj.database;

import maven_book_proj.objects.Narrator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

public class NarratorTableTest implements CommonTests {

    private NarratorTable narratorTable;

    @BeforeEach
    void setUp() throws SQLException {
        this.narratorTable = new NarratorTable("dev");
    }

    @AfterEach
    void tearDown() throws SQLException {
        this.narratorTable.disconnect();
    }

    @Test
    public void testOnlyOneConnectionAllowed() {
        assertThrows(SQLException.class, () -> new NarratorTable("dev"));
    }

    @Test
    public void testGetTableName() {
        String expectedResult = "library_dev.narrators";
        String actualResult = this.narratorTable.getTableName();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetRecords() throws SQLException {
        List<Narrator> narrators = this.narratorTable.getRecords();
        Integer numberOfRecordsAtTimeOfTest = 8;
        Integer numberOfRecordsReturned = narrators.size();
        assertTrue(numberOfRecordsReturned >= numberOfRecordsAtTimeOfTest);
    }

    @Test
    public void testGetRecord() throws SQLException {
        //
    }

    @Test
    public void testGetTableIdFromTableIdentifier() throws SQLException {
        String narrator = "Frankie Corzo";
        Integer expectedResult = 4;
        Integer actualResult = this.narratorTable.getTableId(narrator);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testInsertRecord() throws SQLException {
        HashMap<String, String> narratorParams = new HashMap<>() {
            {
                put("narrator", "test narrator");
            }
        };
        Narrator narrator = new Narrator(narratorParams);
        this.narratorTable.insertRecord(narrator);
        Integer actualResult = this.narratorTable.getTableId(narrator.getNarrator());
        assertTrue(actualResult > 0);
    }

    @Test
    public void testDeleteRecord() throws SQLException {
        String narratorName = "test narrator";
        Integer narratorId = this.narratorTable.getTableId(narratorName);
        if (narratorId == 0) {
            HashMap<String, String> narratorParams = new HashMap<>() {
                {
                    put("narrator", narratorName);
                }
            };
            Narrator narrator = new Narrator(narratorParams);
            this.narratorTable.insertRecord(narrator);
            narratorId = this.narratorTable.getTableId(narratorName);
        }
        this.narratorTable.deleteRecord(narratorId);
        Integer expectedResult = 0;
        Integer actualResult = this.narratorTable.getTableId(narratorName);
        assertEquals(expectedResult, actualResult);
    }
}
