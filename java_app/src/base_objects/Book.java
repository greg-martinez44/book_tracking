package base;

import base.constants.*;

public class Book {

    private String title;
    private Author author;
    private Integer pages;
    private Integer pubYear;
    private Double price;
    private Format format;
    private Genre genre;
    private String source;
    private String imprint;
    private String publishingHouse;
    private String fictionOrNonFiction;

    // These should be in a different object...
    // Something like a BookRecord object rather
    // than the book object.
    private String dateStarted;
    private String dateFinished;

    public Book(String title, String author) {
        this.title = title;
        this.author = new Author(author);
        this.genre = Genre.GENERIC;
    }

    public Book(
            String title,
            String author,
            Integer pages,
            Integer pubYear,
            Double price,
            String format,
            String genre,
            String source,
            String imprint,
            String publishingHouse,
            String fictionOrNonFiction,
            String dateStarted,
            String dateFinished
            )
    {
        this.title = title;
        this.author = new Author(author);
        this.pages = pages;
        this.pubYear = pubYear;
        this.price = price;
        this.source = source;
        this.imprint = imprint;
        this.publishingHouse = publishingHouse;
        this.fictionOrNonFiction = fictionOrNonFiction;

        this.genre = Genre.GENERIC;
        for (Genre enumeratedGenre : Genre.values()) {
            String nextGenre = enumeratedGenre.toString().toLowerCase();
            genre = genre.toLowerCase();
            if (nextGenre.equals(genre)) {
                this.genre = enumeratedGenre;
                break;
            }
        }
        this.format = Format.HARDCOVER;
        for (Format enumeratedFormat : Format.values()) {
            String nextFormat = enumeratedFormat.toString().toLowerCase();
            format = format.toLowerCase();
            if (nextFormat.equals(format)) {
                this.format = enumeratedFormat;
                break;
            }
        }

    }
    // Getters
    public String getTitle() {
        return this.title;
    }

    public String getAuthorName() {
        return this.author.name();
    }

    public Integer getPages() {
        return this.pages;
    }

    public String getFormat() {
        return this.format.toString().toLowerCase();
    }

    public Integer getPubYear() {
        return this.pubYear;
    }

    public Double getPrice() {
        return this.price;
    }

    public String getGenre() {
        return this.genre.toString().toLowerCase();
    }

    public String getDateStarted() {
        return this.dateStarted;
    }

    public String getDateFinished() {
        return this.dateFinished;
    }

    public String getSource() {
        return this.source;
    }

    public String getImprint() {
        return this.imprint;
    }

    public String getPublishingHouse() {
        return this.publishingHouse;
    }

    public String getFictionOrNonFiction() {
        return this.fictionOrNonFiction;
    }

    // Setters
    public void setPages(Integer pages) throws Exception {
        if (pages < 0) {
            throw new Exception("Pages cannot be negative");
        }
        this.pages = pages;
    }

    public void setFormat(String newFormat) {
        for (Format format : Format.values()) {
            String nextFormat = format.toString().toLowerCase();
            newFormat = newFormat.toLowerCase();
            if (nextFormat.equals(newFormat)) {
                this.format = format;
                break;
            }
        }
        this.format = Format.HARDCOVER;
    }

    public void setPubYear(Integer year) {
        this.pubYear = year;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setGenre(String newGenre) {
        for (Genre genre : Genre.values()) {
            String nextGenre = genre.toString().toLowerCase();
            newGenre = newGenre.toLowerCase();
            if (nextGenre.equals(newGenre)) {
                this.genre = genre;
                break;
            }
        }
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public void setDateFinished(String dateFinished) {
        this.dateFinished = dateFinished;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setImprint(String imprint) {
        this.imprint = imprint;
    }

    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse;
    }

    public void setFictionOrNonFiction(String fictionOrNonFiction) {
        this.fictionOrNonFiction = fictionOrNonFiction;
    }
}
