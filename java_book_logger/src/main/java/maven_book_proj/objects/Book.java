package maven_book_proj.objects;

import java.util.HashMap;

public class Book extends BookDBObject {

    private Integer bookId;
    private String title;
    private Integer year;
    private Genre genre;
    private Format format;
    private Integer pages;
    private Integer duration;
    private Integer authorId;
    private Integer imprintId;
    private Integer translatorId;
    private Integer narratorId;
    private Integer illustratorId;

    public Book(HashMap<String, String> bookData) {
        this.bookId = setIntegerVariable(bookData.get("book_id"));
        this.title = setVariable(bookData.get("title"));
        this.year = setIntegerVariable(bookData.get("year"));
        for (Genre genreValue : Genre.values()) {
            String genreValueString = genreValue.toString();
            if (genreValueString.equalsIgnoreCase(bookData.get("genre"))) {
                this.genre = genreValue;
            }
        }
        for (Format formatValue : Format.values()) {
            String formatValueString = formatValue.toString();
            if (formatValueString.equalsIgnoreCase(bookData.get("format"))) {
                this.format = formatValue;
            }
        }
        this.authorId = setIntegerVariable(bookData.get("author_id"));
        this.imprintId = setIntegerVariable(bookData.get("imprint_id"));

        this.pages = setIntegerVariable(bookData.get("pages"));
        this.duration = setIntegerVariable(bookData.get("duration"));
        this.translatorId = setIntegerVariable(bookData.get("translator_id"));
        this.narratorId = setIntegerVariable(bookData.get("narrator_id"));
        this.illustratorId = setIntegerVariable(bookData.get("illustrator_id"));
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public void setId(String tableName, Integer value) {
        switch (tableName) {
            case "translator":
                this.translatorId = value;
                break;
            case "narrator":
                this.narratorId = value;
                break;
            case "illustrator":
                this.illustratorId = value;
                break;
            default:
                System.out.println("Not a valid table");
        }
    }

    public void setPages(String pages) {
        this.pages = setIntegerVariable(pages);
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getTitle() {
        return this.title;
    }

    public Integer getBookId() {
        return this.bookId;
    }

    public Integer getYear() {
        return this.year;
    }

    public String getGenre() {
        try {
            return this.genre.toString().toLowerCase();
        } catch (Exception e) {
            System.out.println(this.title + "\n");
            e.printStackTrace();
        }
        return null;
    }

    public String getFormat() {
        try {
            return this.format.toString().toLowerCase();
        } catch (Exception e) {
            System.out.println(this.title + "\n");
            e.printStackTrace();
        }
        return null;
    }

    public Integer getPages() {
        return this.pages;
    }

    public String getDuration() {
        if (this.duration == null) {
            return null;
        }
        return this.duration.toString();
    }

    public String getTranslatorId() {
        if (this.translatorId == null) {
            return null;
        }
        return this.translatorId.toString();
    }

    public String getAuthorId() {
        return this.authorId.toString();
    }

    public String getImprintId() {
        return this.imprintId.toString();
    }

    public String getNarratorId() {
        if (this.narratorId == null) {
            return null;
        }
        return this.narratorId.toString();
    }

    public String getIllustratorId() {
        if (this.illustratorId == null) {
            return null;
        }
        return this.illustratorId.toString();
    }
}
