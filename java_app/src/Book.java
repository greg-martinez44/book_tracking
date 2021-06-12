class Book {

    private String title;
    private Author author;
    private Integer pages;
    private Integer pubYear;
    private Double price;
    private Format format;
    private Genre genre;
    private String source;

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
}