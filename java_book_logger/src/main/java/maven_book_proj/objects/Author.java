package maven_book_proj.objects;

import java.util.HashMap;

public class Author extends BookDBObject {
    private Integer authorId;
    private String authorName;
    private String otherAuthorName;

    public Author(HashMap<String, String> authorData) {
        this.authorId = setIntegerVariable(authorData.get("author_id"));
        this.authorName = setVariable(authorData.get("author"));
        this.otherAuthorName = setVariable(authorData.get("other_authors"));
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getAuthorId() {
        return this.authorId;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public String getOtherAuthorName() {
        return this.otherAuthorName;
    }
}
