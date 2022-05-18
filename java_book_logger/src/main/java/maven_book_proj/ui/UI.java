package maven_book_proj.ui;

/*
A container for all input/output operations given to the user.

Static functions here mean there is only one place that needs to
be updated when input/output needs to change; the actual format
of the responses can be kept constant so the rest of the program
does not run into issues.
*/

import java.util.HashMap;
import java.util.regex.*;

import maven_book_proj.objects.*;

public class UI {

    private final HashMap<String, String> mainMenuOptionsMap = new HashMap<String, String>() {
        {
            put("1", "Add new book");
            put("2", "Start new read");
            put("3", "Finish read");
        }
    };

    Asker asker;

    public UI() {
        this.asker = new Asker(System.in, System.out);
    }

    public UI(Asker asker) {
        this.asker = asker;
    }

    public void printWelcomeMessage() {
        String welcomeMessage = "*******************\n"
                + "  Book DB Manager  \n"
                + "*******************\n";
        System.out.print(welcomeMessage);
    }

    public String readMainMenuResponse() {
        printMenuOptions(this.mainMenuOptionsMap);
        String response = readResponse(this.mainMenuOptionsMap);
        return response;
    }

    public HashMap<String, String> collectBookData() {
        HashMap<String, String> bookData = new HashMap<>();
        System.out.println("Enter book data:");
        String title = readString("Title");
        String year = readInteger("Year");
        String genre = readGenre("Select a genre from the list above");
        String format = readFormat("Select a format from the list above");
        String pages = readInteger("Pages");
        String duration = "";
        if (pages == null || pages.equals("")) {
            duration = readInteger("Duration");
        }
        String authorName = readString("Author name");
        String otherAuthorName = readString("Other author name (Empty if NA)");
        String imprintName = readString("Imprint name");
        String translatorName = readString("Translator name");
        String illustratorName = "";
        if (format.equals("gn")) {
            illustratorName = readString("Illustrator name");
        }
        String narratorName = "";
        if (format.equals("audio")) {
            narratorName = readString("Narrator name");
        }

        bookData.put("title", title);
        bookData.put("year", year);
        bookData.put("genre", genre);
        bookData.put("format", format);
        bookData.put("pages", pages);
        bookData.put("duration", duration);
        bookData.put("authorName", authorName);
        bookData.put("otherAuthorName", otherAuthorName);
        bookData.put("imprintName", imprintName);
        bookData.put("translatorName", translatorName);
        bookData.put("illustratorName", illustratorName);
        bookData.put("narratorName", narratorName);
        return bookData;
    }

    public String collectPublishingHouse() {
        System.out.print("\nThis is a new imprint.\n");
        return readString("Publishing House");
    }

    public HashMap<String, String> collectPurchaseData() {
        HashMap<String, String> purchaseData = new HashMap<>();
        String source = readString("Source");
        String price = readDouble("Price");
        String purchaseDate = readDate("Purchase Date");
        purchaseData.put("source", source);
        purchaseData.put("price", price);
        purchaseData.put("purchase_date", purchaseDate);
        return purchaseData;
    }

    public HashMap<String, String> collectStartedReadData() {
        HashMap<String, String> startedReadData = new HashMap<>();
        String bookTitle = readString("Book title");
        String startedDate = readDate("Started date");
        startedReadData.put("bookTitle", bookTitle);
        startedReadData.put("started", startedDate);
        return startedReadData;
    }

    public HashMap<String, String> collectCompletedReadData() {
        HashMap<String, String> completedReadData = new HashMap<>();
        String bookTitle = readString("Book title");
        String completedDate = readString("Completed date");
        String rating = readInteger("Rating");
        completedReadData.put("bookTitle", bookTitle);
        completedReadData.put("finished", completedDate);
        completedReadData.put("rating", rating);
        return completedReadData;
    }

    private String readString(String prompt) {
        String formattedPrompt = String.format("%s: ", prompt);
        return asker.ask(formattedPrompt);
    }

    private String readInteger(String prompt) {
        String formattedPrompt = String.format("%s: ", prompt);
        String response = asker.ask(formattedPrompt);
        Boolean isDigit = response == "" || isResponseADigit(response);
        if (isDigit) {
            return response;
        }
        return readInteger("Invalid entry - please enter an integer value");
    }

    private String readDouble(String prompt) {
        String formattedPrompt = String.format("%s: ", prompt);
        String response = asker.ask(formattedPrompt);
        Boolean isDouble = isResponseDouble(response) || isResponseADigit(response);
        if (isDouble) {
            return response;
        }
        return readDouble("Invalid entry - please enter a double value");
    }

    private String readDate(String prompt) {
        String formattedPrompt = String.format("%s: ", prompt);
        String response = asker.ask(formattedPrompt);
        Boolean isDate = isResponseDate(response);
        if (isDate) {
            return response;
        }
        return readDate("Invalid entry - please enter a date value (%Y-%m-%d)");
    }

    private String readGenre(String prompt) {
        printGenres();
        String genreEntry = readString(prompt);
        if (isGenre(genreEntry)) {
            return genreEntry;
        }
        return readGenre("Invalid genre - Please select an option in the list");
    }

    private String readFormat(String prompt) {
        printFormats();
        String formatEntry = readString(prompt);
        if (isFormat(formatEntry)) {
            return formatEntry;
        }
        return readFormat("Invalid format - Please select an option in the list");
    }

    private String readResponse(HashMap<String, String> menuOptionsMap) {
        String[] acceptedResponses = new String[] { "1", "2", "3" };
        String response = "";
        while (true) {
            response = asker.ask("");
            for (String acceptableResponse : acceptedResponses) {
                if (response.equals(acceptableResponse)) {
                    return response;
                }
            }
            System.out.println("Invalid response - select from the below:");
            printMenuOptions(menuOptionsMap);
        }
    }

    private void printMenuOptions(HashMap<String, String> menuOptionsMap) {
        String menuOptions = "Please select from the menu below:\n"
                + String.format("1. %s\n", menuOptionsMap.get("1"))
                + String.format("2. %s\n", menuOptionsMap.get("2"))
                + String.format("3. %s\n", menuOptionsMap.get("3"));
        System.out.print(menuOptions);
    }

    private Boolean isResponseADigit(String response) {
        if (response == "") {
            return false;
        }
        Pattern digitPattern = Pattern.compile("\\d");
        Matcher matcher = digitPattern.matcher(response);
        return matcher.find();
    }

    private Boolean isResponseDouble(String response) {
        if (response == "") {
            return false;
        }
        Pattern doublePattern = Pattern.compile("\\d\\.?\\d");
        Matcher matcher = doublePattern.matcher(response);
        return matcher.find();
    }

    private Boolean isResponseDate(String response) {
        if (response == "") {
            return false;
        }
        Pattern datePattern = Pattern.compile("^....-..-..$");
        Matcher matcher = datePattern.matcher(response);
        return matcher.find();
    }

    private Boolean isGenre(String response) {
        Boolean isMatch = false;
        for (Genre genre : Genre.values()) {
            isMatch = response.equalsIgnoreCase(genre.toString());
            if (isMatch) {
                return isMatch;
            }
        }
        return isMatch;
    }

    private Boolean isFormat(String response) {
        Boolean isMatch = false;
        for (Format format : Format.values()) {
            isMatch = response.equalsIgnoreCase(format.toString());
            if (isMatch) {
                return isMatch;
            }
        }
        return isMatch;
    }

    private void printGenres() {
        System.out.println("[");
        for (Genre genre : Genre.values()) {
            System.out.println(genre.toString().toLowerCase());
        }
        System.out.println("]");
    }

    private void printFormats() {
        System.out.println("[");
        for (Format format : Format.values()) {
            System.out.println(format.toString().toLowerCase());
        }
        System.out.println("]");
    }
}
