package maven_book_proj.database;

import java.sql.SQLException;
import java.util.*;

import maven_book_proj.objects.Book;

public class BookTable extends BookDB implements DataTable<Book> {

    private String bookTable;
    private String[] tableColumns = new String[] {
            "book_id",
            "title",
            "year",
            "genre",
            "format",
            "pages",
            "duration",
            "author_id",
            "imprint_id",
            "translator_id",
            "narrator_id",
            "illustrator_id"
    };

    public BookTable(String schema) throws SQLException {
        super(schema, "books");
        this.bookTable = String.format("library%s.books", this.schema);
    }

    public String getTableName() {
        return this.bookTable;
    }

    public List<Book> getRecords() throws SQLException {
        List<Book> bookList = new ArrayList<Book>();
        String sql = QueryFormatter.getSelectQueryString(this.tableColumns, this.bookTable, "book_id");
        List<HashMap<String, String>> records = getRecords(sql, this.tableColumns);
        for (HashMap<String, String> bookParams : records) {
            Book book = new Book(bookParams);
            bookList.add(book);
        }
        return bookList;
    }

    public Book getRecord(Integer bookId) throws SQLException {
        return new Book(new HashMap<String, String>());
    }

    public Integer getTableId(String title) throws SQLException {
        title = title.replace("'", "''");
        String whereClause = String.format("where title = '%s' order by book_id desc limit 1", title);
        String sql = QueryFormatter.getSelectQueryStringFiltered(this.tableColumns, this.bookTable, whereClause);
        return getId(sql, "book_id");
    }

    public void insertRecord(Book book) throws SQLException {
        String[] columnsToInsert = new String[] {
                "title",
                "year",
                "genre",
                "format",
                "pages",
                "duration",
                "author_id",
                "imprint_id",
                "narrator_id",
                "translator_id",
                "illustrator_id"
        };
        String sql = QueryFormatter.getInsertQueryString(columnsToInsert, this.bookTable);
        String[] args = new String[] {
                book.getTitle(),
                book.getYear().toString(),
                book.getGenre(),
                book.getFormat(),
                book.getPages().toString(),
                book.getDuration(),
                book.getAuthorId(),
                book.getImprintId(),
                book.getNarratorId(),
                book.getTranslatorId(),
                book.getIllustratorId()
        };
        runInsertQuery(sql, args);
    }

    public void deleteRecord(Integer bookId) throws SQLException {
        String sql = QueryFormatter.getDeleteQueryString("book_id", this.bookTable);
        runDeleteQuery(sql, bookId);
    }
}
