package maven_book_proj.objects;

import java.util.HashMap;

public class StartedRead extends BookDBObject {
    private Integer startId;
    private Integer bookId;
    private String started;
    private Integer addedToReads;

    public StartedRead(HashMap<String, String> startedReadData) {
        this.startId = setIntegerVariable(startedReadData.get("start_id"));
        this.bookId = setIntegerVariable(startedReadData.get("book_id"));
        this.started = setVariable(startedReadData.get("started"));
        this.addedToReads = setIntegerVariable(startedReadData.get("added_to_reads"));
    }

    public Integer getStartId() {
        return this.startId;
    }

    public Integer getBookId() {
        return this.bookId;
    }

    public String getStarted() {
        return this.started;
    }

    public Integer getAddedToReads() {
        return this.addedToReads;
    }
}
