package maven_book_proj.database;

import maven_book_proj.objects.Translator;

import java.sql.SQLException;
import java.util.List;

import java.util.ArrayList;
import java.util.HashMap;

public class TranslatorTable extends BookDB implements DataTable<Translator> {

    private String translatorTable;
    private String[] tableColumns = new String[] {
            "translator_id",
            "translator"
    };

    public TranslatorTable(String schema) throws SQLException {
        super(schema, "translators");
        this.translatorTable = String.format("library%s.translators", this.schema);
    }

    public String getTableName() {
        return this.translatorTable;
    }

    public List<Translator> getRecords() throws SQLException {
        List<Translator> translatorList = new ArrayList<>();
        String sql = QueryFormatter.getSelectQueryString(this.tableColumns, this.translatorTable, "translator_id");
        List<HashMap<String, String>> records = getRecords(sql, this.tableColumns);
        for (HashMap<String, String> translatorParams : records) {
            Translator translator = new Translator(translatorParams);
            translatorList.add(translator);
        }
        return translatorList;
    }

    public Translator getRecord(Integer translatorId) throws SQLException {
        return new Translator(new HashMap<String, String>());
    }

    public Integer getTableId(String translatorName) throws SQLException {
        String whereClause = String.format("where translator = '%s'", translatorName);
        String sql = QueryFormatter.getSelectQueryStringFiltered(this.tableColumns, this.translatorTable, whereClause);
        return getId(sql, "translator_id");
    }

    public void insertRecord(Translator translator) throws SQLException {
        String translatorName = translator.getTranslator();
        String sql = QueryFormatter.getInsertQueryString(new String[] { "translator" }, this.translatorTable);
        runInsertQuery(sql, new String[] { translatorName });
    }

    public void deleteRecord(Integer translatorId) throws SQLException {
        String sql = QueryFormatter.getDeleteQueryString("translator_id", this.translatorTable);
        runDeleteQuery(sql, translatorId);
    }
}
