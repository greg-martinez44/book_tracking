package maven_book_proj;

import maven_book_proj.objects.*;
import maven_book_proj.database.*;
import maven_book_proj.ui.UI;

import java.sql.SQLException;
import java.util.*;

public class App {

    private static UI ui;
    private static String dbSchema = "main";

    public static void main(String[] args) throws SQLException {
        ui = new UI();
        ui.printWelcomeMessage();
        String response = ui.readMainMenuResponse();
        String bookId;
        switch (response) {
            case "1":
                /*
                 * The user wants to add a new book to the database.
                 * Before creating any book, the appropriate ID values should be retrieved from
                 * the database (author, imprint, etc.)
                 */
                Book book = buildBookData();
                bookId = addBook(book);
                System.out.printf("\n%s added to books table!\n\n", book.getTitle());
                Purchase purchase = buildPurchaseData(bookId);
                addPurchase(purchase);
                System.out.printf("\n%s added to purchases table!\n\n", book.getTitle());
                break;
            case "2":
                /*
                 * The user wants to add a book to the started_reads table.
                 */
                try {
                    StartedRead startedRead = buildStartedReadData();
                    addStartedRead(startedRead);
                    System.out.print("\nBook added to started_reads table!\n\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "3":
                /*
                 * The user wants to add a book to the completed_reads table.
                 * This should update started_reads (added_to_reads -> 1)
                 */
                try {
                    CompletedRead completedRead = buildCompletedReadData();
                    addCompletedRead(completedRead);
                    updateStartedReads(completedRead.getBookId());
                    System.out.print("\nBook added to completed_reads table!\n\n");
                } catch (Exception e) {
                    e.printStackTrace();

                }
                break;
            default:
                //
        }
    }

    private static Book buildBookData() throws SQLException {
        HashMap<String, String> bookData = ui.collectBookData();
        String authorName = bookData.get("authorName");
        String otherAuthorName = bookData.get("otherAuthorName");
        String authorId = getAuthorId(authorName, otherAuthorName);
        bookData.put("author_id", authorId);
        String imprintName = bookData.get("imprintName");
        String imprintId = getImprintId(imprintName);
        bookData.put("imprint_id", imprintId);
        String narratorName = bookData.get("narratorName");
        if (!narratorName.equals("")) {
            String narratorId = getNarratorId(narratorName);
            bookData.put("narrator_id", narratorId);
        }
        String illustratorName = bookData.get("illustratorName");
        if (!illustratorName.equals("")) {
            String illustratorId = getIllustratorId(illustratorName);
            bookData.put("illustrator_id", illustratorId);
        }
        String translatorName = bookData.get("translatorName");
        if (!translatorName.equals("")) {
            String translatorId = getTranslatorId(translatorName);
            bookData.put("translator_id", translatorId);
        }
        return new Book(bookData);
    }

    private static Purchase buildPurchaseData(String bookId) throws SQLException {
        HashMap<String, String> purchaseData = ui.collectPurchaseData();
        purchaseData.put("book_id", bookId);
        return new Purchase(purchaseData);
    }

    private static StartedRead buildStartedReadData() throws SQLException, Exception {
        HashMap<String, String> startedReadData = ui.collectStartedReadData();
        String bookId = getBookId(startedReadData.get("bookTitle"));
        isEntryValid(bookId, startedReadData.get("bookTitle"), "books");
        startedReadData.put("book_id", bookId);
        return new StartedRead(startedReadData);
    }

    private static CompletedRead buildCompletedReadData() throws SQLException, Exception {
        HashMap<String, String> completedReadData = ui.collectCompletedReadData();
        String bookId = getBookId(completedReadData.get("bookTitle"));
        isEntryValid(bookId, completedReadData.get("bookTitle"), "books");
        String startedDate = getStartedDate(bookId);
        isEntryValid(startedDate, completedReadData.get("bookTitle"), "started_reads");
        completedReadData.put("book_id", bookId);
        completedReadData.put("started", startedDate);
        return new CompletedRead(completedReadData);
    }

    private static String getBookId(String bookTitle) throws SQLException {
        BookTable bookTable = new BookTable(dbSchema);
        Integer bookId = bookTable.getTableId(bookTitle);
        bookTable.disconnect();
        return bookId.toString();
    }

    private static String getAuthorId(String author, String otherAuthors) throws SQLException {
        AuthorTable authorTable = new AuthorTable(dbSchema);
        Integer authorId = authorTable.getTableId(author, otherAuthors);
        if (authorId == 0) {
            HashMap<String, String> authorParams = new HashMap<>() {
                {
                    put("author", author);
                    put("other_authors", otherAuthors);
                }
            };
            authorTable.insertRecord(new Author(authorParams));
            authorId = authorTable.getTableId(author, otherAuthors);
        }
        authorTable.disconnect();
        return authorId.toString();
    }

    private static String getImprintId(String imprintName) throws SQLException {
        PublisherTable publisherTable = new PublisherTable(dbSchema);
        Integer imprintId = publisherTable.getTableId(imprintName);
        if (imprintId == 0) {
            String publishingHouse = ui.collectPublishingHouse();
            HashMap<String, String> publisherParams = new HashMap<>() {
                {
                    put("imprint", imprintName);
                    put("publishing_house", publishingHouse);
                }
            };
            publisherTable.insertRecord(new Publisher(publisherParams));
            imprintId = publisherTable.getTableId(imprintName);
        }
        publisherTable.disconnect();
        return imprintId.toString();
    }

    private static String getNarratorId(String narratorName) throws SQLException {
        NarratorTable narratorTable = new NarratorTable(dbSchema);
        Integer narratorId = narratorTable.getTableId(narratorName);
        if (narratorId == 0) {
            HashMap<String, String> narratorParams = new HashMap<>() {
                {
                    put("narrator", narratorName);
                }
            };
            narratorTable.insertRecord(new Narrator(narratorParams));
            narratorId = narratorTable.getTableId(narratorName);
        }
        narratorTable.disconnect();
        return narratorId.toString();
    }

    private static String getIllustratorId(String illustratorName) throws SQLException {
        IllustratorTable illustratorTable = new IllustratorTable(dbSchema);
        Integer illustratorId = illustratorTable.getTableId(illustratorName);
        if (illustratorId == 0) {
            HashMap<String, String> illustratorParams = new HashMap<>() {
                {
                    put("illustrator", illustratorName);
                }
            };
            illustratorTable.insertRecord(new Illustrator(illustratorParams));
            illustratorId = illustratorTable.getTableId(illustratorName);
        }
        illustratorTable.disconnect();
        return illustratorId.toString();
    }

    private static String getTranslatorId(String translatorName) throws SQLException {
        TranslatorTable translatorTable = new TranslatorTable(dbSchema);
        Integer translatorId = translatorTable.getTableId(translatorName);
        if (translatorId == 0) {
            HashMap<String, String> translatorParams = new HashMap<>() {
                {
                    put("translator", translatorName);
                }
            };
            translatorTable.insertRecord(new Translator(translatorParams));
            translatorId = translatorTable.getTableId(translatorName);
        }
        translatorTable.disconnect();
        return translatorId.toString();
    }

    private static String getStartedDate(String bookId) throws SQLException {
        StartsTable startsTable = new StartsTable(dbSchema);
        Integer startId = startsTable.getTableId(bookId);
        StartedRead startedRead = startsTable.getRecord(startId);
        startsTable.disconnect();
        return startedRead.getStarted();
    }

    private static String addBook(Book book) throws SQLException {
        BookTable bookTable = new BookTable(dbSchema);
        bookTable.insertRecord(book);
        Integer bookId = bookTable.getTableId(book.getTitle());
        bookTable.disconnect();
        return bookId.toString();
    }

    private static void addPurchase(Purchase purchase) throws SQLException {
        PurchaseTable purchaseTable = new PurchaseTable(dbSchema);
        purchaseTable.insertRecord(purchase);
        purchaseTable.disconnect();
    }

    private static void addStartedRead(StartedRead startedRead) throws SQLException {
        StartsTable startsTable = new StartsTable(dbSchema);
        startsTable.insertRecord(startedRead);
        startsTable.disconnect();
    }

    private static void addCompletedRead(CompletedRead completedRead) throws SQLException {
        CompletedReadTable completedReadTable = new CompletedReadTable(dbSchema);
        completedReadTable.insertRecord(completedRead);
        completedReadTable.disconnect();
    }

    private static void updateStartedReads(Integer bookId) throws SQLException {
        StartsTable startsTable = new StartsTable(dbSchema);
        Integer startId = startsTable.getTableId(bookId);
        startsTable.updateRecord(startId, "added_to_reads", "1");
        startsTable.disconnect();
    }

    private static void isEntryValid(String idValue, String missingValue, String sourceTable) throws Exception {
        String errorMessage = String.format("\n%s is missing from %s!\n", missingValue, sourceTable);
        if (idValue.equals("0") || idValue == null) {
            throw new Exception(errorMessage);
        }
    }
}
