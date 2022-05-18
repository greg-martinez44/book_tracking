package maven_book_proj.objects;

import java.util.HashMap;

public class CompletedRead extends BookDBObject {
    private Integer readId;
    private Integer bookId;
    private String started;
    private String finished;
    private Integer rating;

    public CompletedRead(HashMap<String, String> completedReadData) {
        this.readId = setIntegerVariable(completedReadData.get("read_id"));
        this.bookId = setIntegerVariable(completedReadData.get("book_id"));
        this.started = setVariable(completedReadData.get("started"));
        this.finished = setVariable(completedReadData.get("finsihed"));
        this.rating = setIntegerVariable(completedReadData.get("rating"));

    }

    public Integer getReadId() {
        return this.readId;
    }

    public Integer getBookId() {
        return this.bookId;
    }

    public String getStarted() {
        return this.started;
    }

    public String getFinished() {
        return this.finished;
    }

    public Integer getRating() {
        return this.rating;
    }
}
