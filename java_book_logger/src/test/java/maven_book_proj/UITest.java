package maven_book_proj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import maven_book_proj.objects.Format;
import maven_book_proj.objects.Genre;
import maven_book_proj.ui.Asker;
import maven_book_proj.ui.UI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.HashMap;

public class UITest {
    private Asker asker;
    private UI ui;

    void setAsker(String returnValue) {
        when(this.asker.ask(anyString())).thenReturn(returnValue);
    }

    void setSpecificAsker(String prompt, String returnValue) {
        when(this.asker.ask(prompt)).thenReturn(returnValue);
    }

    void setBookCollectionDefaults() {
        setSpecificAsker("Select a genre from the list above: ", Genre.GENERAL_FICTION.toString().toLowerCase());
        setSpecificAsker("Select a format from the list above: ", Format.PAPERBACK.toString().toLowerCase());
        setSpecificAsker("Year: ", "0");
        setSpecificAsker("Pages: ", "0");
    }

    void setPurchaseCollectionDefaults() {
        setSpecificAsker("Source: ", "Bookshop.org");
        setSpecificAsker("Purchase Date: ", "2022-05-14");
        setSpecificAsker("Price: ", "10.99");
    }

    @BeforeEach
    void setUp() {
        this.asker = mock(Asker.class);
        this.ui = new UI(asker);
    }

    @AfterEach
    void tearDown() {
        this.asker = new Asker(System.in, System.out);
        this.ui = new UI();
    }

    @Test
    void testCollectBookDataBuildsMapWithTitle() {
        setBookCollectionDefaults();
        setSpecificAsker("Title: ", "test_title");
        HashMap<String, String> bookData = this.ui.collectBookData();
        String expectedValue = "test_title";
        String actualValue = bookData.get("title");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void testCollectBookDataBuildsMapWithYear() {
        setBookCollectionDefaults();
        setSpecificAsker("Year: ", "2022");
        HashMap<String, String> bookData = this.ui.collectBookData();
        String expectedValue = "2022";
        String actualValue = bookData.get("year");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void testCollectBookDataBuildsMapWithNonBlankOtherAuthors() {
        setBookCollectionDefaults();
        setSpecificAsker("Other author name (Empty if NA): ", "some other author");
        HashMap<String, String> bookData = this.ui.collectBookData();
        String expectedValue = "some other author";
        String actualValue = bookData.get("otherAuthorName");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void testCollectBookDataBuildsMapWithBlankOtherAuthors() {
        setBookCollectionDefaults();
        setSpecificAsker("Other author name (Empty if NA): ", "");
        HashMap<String, String> bookData = this.ui.collectBookData();
        String expectedValue = "";
        String actualValue = bookData.get("otherAuthorName");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void testCollectBookDataOnlyDoesNotAcceptStringsForYearOrPages() {
        setBookCollectionDefaults();
        setSpecificAsker("Year: ", "abc");
        setSpecificAsker("Invalid entry - please enter an integer value: ", "2022");
        HashMap<String, String> bookData = this.ui.collectBookData();
        String expectedValue = "2022";
        String actualValue = bookData.get("year");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void testCollectBookDataOnlyAcceptsLegitimateGenres() {
        setBookCollectionDefaults();
        setSpecificAsker("Select a genre from the list above: ", "abc");
        setSpecificAsker("Invalid genre - Please select an option in the list: ", "sci_fi");
        HashMap<String, String> bookData = this.ui.collectBookData();
        String expectedValue = "sci_fi";
        String actualValue = bookData.get("genre");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void testCollectBookDataOnlyAcceptsLegitimateFormat() {
        setBookCollectionDefaults();
        setSpecificAsker("Select a format from the list above: ", "abc");
        setSpecificAsker("Invalid format - Please select an option in the list: ", "paperback");
        HashMap<String, String> bookData = this.ui.collectBookData();
        String expectedValue = "paperback";
        String actualValue = bookData.get("format");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void testCollectPurchaseDataOnlyAcceptsLegitimateDoubleValuesWithDecimal() {
        setPurchaseCollectionDefaults();
        setSpecificAsker("Price: ", "abc");
        setSpecificAsker("Invalid entry - please enter a double value: ", "12.99");
        HashMap<String, String> purchaseData = this.ui.collectPurchaseData();
        String expectedValue = "12.99";
        String actualValue = purchaseData.get("price");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void testCollectPurchaseDataOnlyAcceptsLegitimateDoubleValuesWithoutDecimal() {
        setPurchaseCollectionDefaults();
        setSpecificAsker("Price: ", "abc");
        setSpecificAsker("Invalid entry - please enter a double value: ", "25");
        HashMap<String, String> purchaseData = this.ui.collectPurchaseData();
        String expectedValue = "25";
        String actualValue = purchaseData.get("price");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void testCollectPurchaseDataOnlyAcceptsLegitimateDoubleValueWhenZero() {
        setPurchaseCollectionDefaults();
        setSpecificAsker("Price: ", "abc");
        setSpecificAsker("Invalid entry - please enter a double value: ", "0");
        HashMap<String, String> purchaseData = this.ui.collectPurchaseData();
        String expectedValue = "0";
        String actualValue = purchaseData.get("price");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void testCollectPurchaseDataOnlyAcceptsLegitimateDateValues() {
        setPurchaseCollectionDefaults();
        setSpecificAsker("Purchase Date: ", "abc");
        setSpecificAsker("Invalid entry - please enter a date value (%Y-%m-%d): ", "2022-03-01");
        HashMap<String, String> purchaseData = this.ui.collectPurchaseData();
        String expectedValue = "2022-03-01";
        String actualValue = purchaseData.get("purchase_date");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void testCollectPurchaseDataOnlyAcceptsLegitimateDateValuesTooManyDigits() {
        setPurchaseCollectionDefaults();
        setSpecificAsker("Purchase Date: ", "2022-045-01");
        setSpecificAsker("Invalid entry - please enter a date value (%Y-%m-%d): ", "2022-04-01");
        HashMap<String, String> purchaseData = this.ui.collectPurchaseData();
        String expectedValue = "2022-04-01";
        String actualValue = purchaseData.get("purchase_date");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void testCollectBookDataReadsIllustratorOnlyIfGenreIsGN() {
        setBookCollectionDefaults();
        setSpecificAsker("Select a format from the list above: ", "gn");
        setSpecificAsker("Illustrator name: ", "illustrator");
        HashMap<String, String> bookData = this.ui.collectBookData();
        String expectedValue = "illustrator";
        String actualValue = bookData.get("illustratorName");
        assertEquals(expectedValue, actualValue);
    }
}
