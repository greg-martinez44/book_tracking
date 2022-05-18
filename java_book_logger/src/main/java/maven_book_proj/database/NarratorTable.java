package maven_book_proj.database;

import maven_book_proj.objects.Narrator;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class NarratorTable extends BookDB implements DataTable<Narrator> {

    private String narratorTable;
    private String[] tableColumns = new String[] {
            "narrator_id",
            "narrator"
    };

    public NarratorTable(String schema) throws SQLException {
        super(schema, "narrators");
        this.narratorTable = String.format("library%s.narrators", this.schema);
    }

    public String getTableName() {
        return this.narratorTable;
    }

    public List<Narrator> getRecords() throws SQLException {
        List<Narrator> narratorList = new ArrayList<>();
        String sql = QueryFormatter.getSelectQueryString(this.tableColumns, this.narratorTable, "narrator_id");
        List<HashMap<String, String>> records = getRecords(sql, this.tableColumns);
        for (HashMap<String, String> narratorParams : records) {
            Narrator narrator = new Narrator(narratorParams);
            narratorList.add(narrator);
        }
        return narratorList;
    }

    public Narrator getRecord(Integer narratorId) throws SQLException {
        return new Narrator(new HashMap<String, String>());
    }

    public Integer getTableId(String narratorName) throws SQLException {
        String whereClause = String.format("where narrator = '%s'", narratorName);
        String sql = QueryFormatter.getSelectQueryStringFiltered(this.tableColumns, this.narratorTable, whereClause);
        return getId(sql, "narrator_id");
    }

    public void insertRecord(Narrator narrator) throws SQLException {
        String sql = QueryFormatter.getInsertQueryString(new String[] { "narrator" }, this.narratorTable);
        runInsertQuery(sql, new String[] { narrator.getNarrator() });
    }

    public void deleteRecord(Integer narratorId) throws SQLException {
        String sql = QueryFormatter.getDeleteQueryString("narrator_id", this.narratorTable);
        runDeleteQuery(sql, narratorId);
    }
}
