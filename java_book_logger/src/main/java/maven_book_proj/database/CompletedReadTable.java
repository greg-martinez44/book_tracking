package maven_book_proj.database;

import maven_book_proj.objects.CompletedRead;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class CompletedReadTable extends BookDB implements DataTable<CompletedRead> {

    private String completedReadTable;
    private String[] tableColumns = new String[] {
            "read_id",
            "book_id",
            "started",
            "finished",
            "rating"
    };

    public CompletedReadTable(String schema) throws SQLException {
        super(schema, "completed_reads");
        this.completedReadTable = String.format("library%s.completed_reads", this.schema);
    }

    public String getTableName() {
        return this.completedReadTable;
    }

    public List<CompletedRead> getRecords() throws SQLException {
        List<CompletedRead> completedReadList = new ArrayList<>();
        String sql = QueryFormatter.getSelectQueryString(this.tableColumns, this.completedReadTable, "read_id");
        List<HashMap<String, String>> records = getRecords(sql, this.tableColumns);
        for (HashMap<String, String> completedReadParams : records) {
            CompletedRead completedRead = new CompletedRead(completedReadParams);
            completedReadList.add(completedRead);
        }
        return completedReadList;
    }

    public Integer getTableId(String bookId) throws SQLException {
        return getTableId(Integer.parseInt(bookId));
    }

    public Integer getTableId(Integer bookId) throws SQLException {
        String whereClause = String.format("where book_id = %s order by finished desc", bookId);
        String sql = QueryFormatter.getSelectQueryStringFiltered(this.tableColumns, this.completedReadTable,
                whereClause);
        return getId(sql, "read_id");
    }

    public CompletedRead getRecord(Integer readId) throws SQLException {
        return new CompletedRead(new HashMap<String, String>());
    }

    public void insertRecord(CompletedRead completedRead) throws SQLException {
        String[] columnsToInsert = new String[] {
                "book_id",
                "started",
                "finished",
                "rating"
        };
        String sql = QueryFormatter.getInsertQueryString(columnsToInsert, this.completedReadTable);
        String[] args = new String[] {
                completedRead.getBookId().toString(),
                completedRead.getStarted(),
                completedRead.getFinished(),
                completedRead.getRating().toString()
        };
        runInsertQuery(sql, args);
    }

    public void deleteRecord(Integer readId) throws SQLException {
        String sql = QueryFormatter.getDeleteQueryString("read_id", this.completedReadTable);
        runDeleteQuery(sql, readId);
    }
}
