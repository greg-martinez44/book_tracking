package maven_book_proj.database;

import maven_book_proj.objects.Translator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

public class TranslatorTableTest implements CommonTests {

    private TranslatorTable translatorTable;

    @BeforeEach
    void setUp() throws SQLException {
        this.translatorTable = new TranslatorTable("dev");
    }

    @AfterEach
    void tearDown() throws SQLException {
        this.translatorTable.disconnect();
    }

    @Test
    public void testGetTableName() {
        String expectedResult = "library_dev.translators";
        String actualResult = this.translatorTable.getTableName();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testOnlyOneConnectionAllowed() {
        assertThrows(SQLException.class, () -> new TranslatorTable("dev"));
    }

    @Test
    public void testGetRecords() throws SQLException {
        List<Translator> translatorList = this.translatorTable.getRecords();
        Integer numberOfRecordsAtTimeOfTesting = 29;
        Integer numberOfRecordsReturned = translatorList.size();
        assertTrue(numberOfRecordsReturned >= numberOfRecordsAtTimeOfTesting);
    }

    @Test
    public void testGetRecord() throws SQLException {
        //
    }

    @Test
    public void testGetTableIdFromTableIdentifier() throws SQLException {
        String translatorName = "John Werry";
        Integer expectedResult = 6;
        Integer actualResult = this.translatorTable.getTableId(translatorName);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testInsertRecord() throws SQLException {
        String translatorName = "test translator";
        HashMap<String, String> translatorParams = new HashMap<>();
        translatorParams.put("translator", translatorName);
        Translator translator = new Translator(translatorParams);
        this.translatorTable.insertRecord(translator);
        Integer newTranslatorId = this.translatorTable.getTableId(translatorName);
        assertTrue(newTranslatorId > 0);
    }

    @Test
    public void testDeleteRecord() throws SQLException {
        String translatorName = "test translator";
        Integer translatorId = this.translatorTable.getTableId(translatorName);
        this.translatorTable.deleteRecord(translatorId);
        Integer expectedResult = 0;
        Integer actualResult = this.translatorTable.getTableId(translatorName);
        assertEquals(expectedResult, actualResult);
    }
}
