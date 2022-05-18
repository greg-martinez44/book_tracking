package maven_book_proj.database;

import maven_book_proj.objects.Publisher;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class PublisherTable extends BookDB implements DataTable<Publisher> {

    private String publisherTable;
    private String[] tableColumns = new String[] {
            "imprint_id",
            "imprint",
            "publishing_house"
    };

    public PublisherTable(String schema) throws SQLException {
        super(schema, "publishers");
        this.publisherTable = String.format("library%s.publishers", this.schema);
    }

    public String getTableName() {
        return this.publisherTable;
    }

    public List<Publisher> getRecords() throws SQLException {
        List<Publisher> publisherList = new ArrayList<>();
        String sql = QueryFormatter.getSelectQueryString(this.tableColumns, this.publisherTable, "imprint_id");
        List<HashMap<String, String>> records = getRecords(sql, this.tableColumns);
        for (HashMap<String, String> publisherParams : records) {
            Publisher publisher = new Publisher(publisherParams);
            publisherList.add(publisher);
        }
        return publisherList;
    }

    public Publisher getRecord(Integer publisherId) throws SQLException {
        return new Publisher(new HashMap<String, String>());
    }

    public Integer getTableId(String imprintName) throws SQLException {
        String whereClause = String.format("where imprint = '%s'", imprintName);
        String sql = QueryFormatter.getSelectQueryStringFiltered(this.tableColumns, this.publisherTable, whereClause);
        return getId(sql, "imprint_id");
    }

    public void insertRecord(Publisher publisher) throws SQLException {
        String imprint = publisher.getImprint();
        String publishingHouse = publisher.getPublishingHouse();
        String[] columnsToInsert = new String[] { "imprint", "publishing_house" };
        String sql = QueryFormatter.getInsertQueryString(columnsToInsert, this.publisherTable);
        String[] queryParams = new String[] { imprint, publishingHouse };
        runInsertQuery(sql, queryParams);
    }

    public void deleteRecord(Integer imprintId) throws SQLException {
        String sql = QueryFormatter.getDeleteQueryString("imprint_id", this.publisherTable);
        runDeleteQuery(sql, imprintId);
    }
}
