package maven_book_proj.database;

import maven_book_proj.objects.StartedRead;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class StartsTable extends BookDB implements DataTable<StartedRead> {

    String startsTable;
    String[] tableColumns = new String[] {
            "start_id",
            "book_id",
            "started",
            "added_to_reads"
    };

    public StartsTable(String schema) throws SQLException {
        super(schema, "started_reads");
        this.startsTable = String.format("library%s.started_reads", this.schema);
    }

    public String getTableName() {
        return this.startsTable;
    }

    public List<StartedRead> getRecords() throws SQLException {
        String sql = QueryFormatter.getSelectQueryString(this.tableColumns, this.startsTable, "book_id");
        return buildStartedReadsList(sql);
    }

    public List<StartedRead> getRecords(Integer bookId) throws SQLException {
        String whereClause = String.format("where book_id = %s", bookId);
        String sql = QueryFormatter.getSelectQueryStringFiltered(this.tableColumns, this.startsTable, whereClause);
        return buildStartedReadsList(sql);
    }

    public StartedRead getRecord(Integer startId) throws SQLException {
        String whereClause = String.format("where start_id = %s", startId);
        String sql = QueryFormatter.getSelectQueryStringFiltered(this.tableColumns, this.startsTable, whereClause);
        List<HashMap<String, String>> records = getRecords(sql, this.tableColumns);
        if (records.size() == 0) {
            return new StartedRead(new HashMap<String, String>() {
                {
                    put("book_id", null);
                    put("started", null);
                    put("added_to_reads", null);
                }
            });
        }
        return new StartedRead(records.get(0));
    }

    public Integer getTableId(String bookId) throws SQLException {
        return getTableId(Integer.parseInt(bookId));
    }

    public Integer getTableId(Integer bookId, String started) throws SQLException {
        String whereClause = String.format("where book_id = %s and started = '%s'", bookId, started);
        String sql = QueryFormatter.getSelectQueryStringFiltered(this.tableColumns, this.startsTable, whereClause);
        return getId(sql, "start_id");
    }

    public Integer getTableId(Integer bookId) throws SQLException {
        String whereClause = String.format("where book_id = %s order by started desc limit 1",
                bookId);
        String sql = QueryFormatter.getSelectQueryStringFiltered(this.tableColumns, this.startsTable, whereClause);
        return getId(sql, "start_id");
    }

    public void insertRecord(StartedRead startedRead) throws SQLException {
        String bookId = startedRead.getBookId().toString();
        String started = startedRead.getStarted();
        String[] columnsToInsert = new String[] { "book_id", "started" };
        String sql = QueryFormatter.getInsertQueryString(columnsToInsert, this.startsTable);
        String[] queryParams = new String[] { bookId, started };
        runInsertQuery(sql, queryParams);
    }

    public void deleteRecord(Integer startId) throws SQLException {
        String sql = QueryFormatter.getDeleteQueryString("start_id", this.startsTable);
        runDeleteQuery(sql, startId);
    }

    public void updateRecord(Integer startId, String columnToUpdate, String newValue) throws SQLException {
        String sql = QueryFormatter.getUpdateQueryString("start_id", this.startsTable, columnToUpdate);
        runUpdateQuery(sql, newValue, startId);
    }

    private List<StartedRead> buildStartedReadsList(String sql) throws SQLException {
        List<StartedRead> startedReadList = new ArrayList<>();
        List<HashMap<String, String>> records = getRecords(sql, this.tableColumns);
        for (HashMap<String, String> startedReadParams : records) {
            StartedRead startedRead = new StartedRead(startedReadParams);
            startedReadList.add(startedRead);
        }
        return startedReadList;
    }
}
