package maven_book_proj.database;

import maven_book_proj.objects.Illustrator;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class IllustratorTable extends BookDB implements DataTable<Illustrator> {

    private String illustratorTable;
    private String[] tableColumns = new String[] {
            "illustrator_id",
            "illustrator"
    };

    public IllustratorTable(String schema) throws SQLException {
        super(schema, "illustrators");
        this.illustratorTable = String.format("library%s.illustrators", this.schema);
    }

    public String getTableName() {
        return this.illustratorTable;
    }

    public List<Illustrator> getRecords() throws SQLException {
        List<Illustrator> illustratorList = new ArrayList<>();
        String sql = QueryFormatter.getSelectQueryString(this.tableColumns, this.illustratorTable, "illustrator_id");
        List<HashMap<String, String>> records = getRecords(sql, this.tableColumns);
        for (HashMap<String, String> illustratorParams : records) {
            Illustrator illustrator = new Illustrator(illustratorParams);
            illustratorList.add(illustrator);
        }
        return illustratorList;
    }

    public Illustrator getRecord(Integer illustratorId) throws SQLException {
        return new Illustrator(new HashMap<String, String>());
    }

    public Integer getTableId(String illustratorName) throws SQLException {
        String whereClause = String.format("where illustrator = '%s'", illustratorName);
        String sql = QueryFormatter.getSelectQueryStringFiltered(this.tableColumns, this.illustratorTable, whereClause);
        return getId(sql, "illustrator_id");
    }

    public void insertRecord(Illustrator illustrator) throws SQLException {
        String sql = QueryFormatter.getInsertQueryString(new String[] { "illustrator" }, this.illustratorTable);
        runInsertQuery(sql, new String[] { illustrator.getIllustrator() });
    }

    public void deleteRecord(Integer illustratorId) throws SQLException {
        String sql = QueryFormatter.getDeleteQueryString("illustrator_id", this.illustratorTable);
        runDeleteQuery(sql, illustratorId);
    }
}
