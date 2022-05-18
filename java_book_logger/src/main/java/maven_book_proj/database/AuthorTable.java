package maven_book_proj.database;

import maven_book_proj.objects.Author;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.sql.SQLException;

public class AuthorTable extends BookDB implements DataTable<Author> {

    private String authorTable;
    private String[] tableColumns = new String[] {
            "author_id",
            "author",
            "other_authors"
    };

    public AuthorTable(String schema) throws SQLException {
        super(schema, "authors");
        this.authorTable = String.format("library%s.authors", this.schema);
    }

    public String getTableName() {
        return this.authorTable;
    }

    public List<Author> getRecords() throws SQLException {
        List<Author> authorList = new ArrayList<>();
        String sql = QueryFormatter.getSelectQueryString(this.tableColumns, this.authorTable, "author_id");
        List<HashMap<String, String>> records = getRecords(sql, this.tableColumns);
        for (HashMap<String, String> authorParams : records) {
            Author author = new Author(authorParams);
            authorList.add(author);
        }
        return authorList;
    }

    public Author getRecord(Integer authorId) throws SQLException {
        return new Author(new HashMap<String, String>());
    }

    public Integer getTableId(String author) throws SQLException {
        String whereClause = String.format("where author = '%s' and coalesce(other_authors, '') = ''", author);
        String sql = QueryFormatter.getSelectQueryStringFiltered(this.tableColumns, this.authorTable, whereClause);
        return getId(sql, "author_id");
    }

    public Integer getTableId(String author, String otherAuthor) throws SQLException {
        String whereClause = String.format("where author = '%s' and other_authors = '%s'", author, otherAuthor);
        String sql = QueryFormatter.getSelectQueryStringFiltered(this.tableColumns, this.authorTable, whereClause);
        return getId(sql, "author_id");
    }

    public void insertRecord(Author author) throws SQLException {
        String authorName = author.getAuthorName();
        String otherAuthorName = author.getOtherAuthorName();
        String[] columnsToInsert = new String[] { "author", "other_authors" };
        String sql = QueryFormatter.getInsertQueryString(columnsToInsert, this.authorTable);
        String[] queryParams = new String[] { authorName, otherAuthorName };
        runInsertQuery(sql, queryParams);
    }

    public void deleteRecord(Integer authorId) throws SQLException {
        String sql = QueryFormatter.getDeleteQueryString("author_id", this.authorTable);
        runDeleteQuery(sql, authorId);
    }
}
