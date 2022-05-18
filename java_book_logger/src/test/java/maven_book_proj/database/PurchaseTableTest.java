package maven_book_proj.database;

import maven_book_proj.objects.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

public class PurchaseTableTest implements CommonTests {

    private PurchaseTable purchaseTable;

    @BeforeEach
    void setUp() throws SQLException {
        this.purchaseTable = new PurchaseTable("dev");
    }

    @AfterEach
    void tearDown() throws SQLException {
        this.purchaseTable.disconnect();
    }

    @Test
    public void testGetTableName() {
        String expectedResult = "library_dev.purchases";
        String actualResult = this.purchaseTable.getTableName();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testOnlyOneConnectionAllowed() throws SQLException {
        assertThrows(SQLException.class, () -> new PurchaseTable("dev"));
    }

    @Test
    public void testGetRecords() throws SQLException {
        List<Purchase> purchaseList = this.purchaseTable.getRecords();
        Integer numberOfRecordsAtTimeOfTest = 414;
        Integer numberOfRecordsReturned = purchaseList.size();
        assertTrue(numberOfRecordsReturned >= numberOfRecordsAtTimeOfTest);
    }

    @Test
    public void testGetRecord() throws SQLException {
        //
    }

    @Test
    void testGetPurchaseDataFromSpecificBookIdHasCorrectSource() throws SQLException {
        Integer bookId = 74;
        Purchase purchase = this.purchaseTable.getRecord(bookId);
        String expectedResult = "Skylight Books";
        String actualResult = purchase.getSource();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetPurchaseDataFromSpecificBookIdHasCorrectPurchaseDate() throws SQLException {
        Integer bookId = 74;
        Purchase purchase = this.purchaseTable.getRecord(bookId);
        String expectedResult = "2017-09-01";
        String actualResult = purchase.getDatePurchased();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetPurchaseDataFromSpecificBookIdHasCorrectPrice() throws SQLException {
        Integer bookId = 74;
        Purchase purchase = this.purchaseTable.getRecord(bookId);
        Double expectedResult = 16.95;
        Double actualResult = purchase.getPrice();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetPurchaseDataFromSpecificBookIdHasZeroPrice() throws SQLException {
        Integer bookId = 73;
        Purchase purchase = this.purchaseTable.getRecord(bookId);
        Double expectedResult = 0.0;
        Double actualResult = purchase.getPrice();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testInsertRecord() throws SQLException {
        HashMap<String, String> purchaseParams = new HashMap<>();
        purchaseParams.put("book_id", "99999");
        purchaseParams.put("source", "new source");
        purchaseParams.put("price", "9.99");
        purchaseParams.put("purchase_date", "2022-05-04");
        Purchase purchase = new Purchase(purchaseParams);
        this.purchaseTable.insertRecord(purchase);
        Integer bookId = 99999;
        Purchase purchaseData = this.purchaseTable.getRecord(bookId);
        String expectedResult = "new source";
        String actualResult = purchaseData.getSource();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetTableIdFromTableIdentifier() throws SQLException {
        Integer bookId = 797;
        Integer expectedResult = 412;
        Integer actualResult = this.purchaseTable.getTableId(bookId);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeleteRecord() throws SQLException {
        Integer bookId = 99999;
        Integer purchaseId = this.purchaseTable.getTableId(bookId);
        if (purchaseId > 0) {
            this.purchaseTable.deleteRecord(purchaseId);
        }
        Integer expectedResult = 0;
        Integer actualResult = this.purchaseTable.getTableId(bookId);
        assertEquals(expectedResult, actualResult);
    }
}
