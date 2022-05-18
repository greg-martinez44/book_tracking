package maven_book_proj.database;

import maven_book_proj.objects.Purchase;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class PurchaseTable extends BookDB implements DataTable<Purchase> {

    private String purchaseTable;
    private String[] tableColumns = new String[] {
            "purchase_id",
            "book_id",
            "source",
            "price",
            "purchase_date"
    };

    public PurchaseTable(String schema) throws SQLException {
        super(schema, "purchases");
        this.purchaseTable = String.format("library%s.purchases", this.schema);
    }

    public String getTableName() {
        return this.purchaseTable;
    }

    public List<Purchase> getRecords() throws SQLException {
        List<Purchase> purchaseList = new ArrayList<>();
        String sql = QueryFormatter.getSelectQueryString(this.tableColumns, this.purchaseTable, "book_id");
        List<HashMap<String, String>> records = getRecords(sql, this.tableColumns);
        for (HashMap<String, String> purchaseParams : records) {
            Purchase purchase = new Purchase(purchaseParams);
            purchaseList.add(purchase);
        }
        return purchaseList;
    }

    public Integer getTableId(String bookId) throws SQLException {
        return getTableId(Integer.parseInt(bookId));
    }

    public Purchase getRecord(Integer bookId) throws SQLException {
        String whereClause = String.format("where book_id = '%s'", bookId);
        String sql = QueryFormatter.getSelectQueryStringFiltered(this.tableColumns, this.purchaseTable, whereClause);
        List<HashMap<String, String>> records = getRecords(sql, this.tableColumns);
        for (HashMap<String, String> purchaseParams : records) {
            return new Purchase(purchaseParams);
        }
        return new Purchase(new HashMap<String, String>());
    }

    public Integer getTableId(Integer bookId) throws SQLException {
        String whereClause = String.format("where book_id = '%s'", bookId);
        String sql = QueryFormatter.getSelectQueryStringFiltered(this.tableColumns, this.purchaseTable, whereClause);
        return getId(sql, "purchase_id");
    }

    public void insertRecord(Purchase purchase) throws SQLException {
        String bookId = purchase.getBookId().toString();
        String source = purchase.getSource();
        String purchaseDate = purchase.getDatePurchased();
        String price = purchase.getPrice().toString();
        String[] columnsToInsert = new String[] { "book_id", "source", "price", "purchase_date" };
        String sql = QueryFormatter.getInsertQueryString(columnsToInsert, this.purchaseTable);
        String[] queryParams = new String[] { bookId, source, price, purchaseDate };
        runInsertQuery(sql, queryParams);
    }

    public void deleteRecord(Integer bookId) throws SQLException {
        String sql = QueryFormatter.getDeleteQueryString("purchase_id", this.purchaseTable);
        runDeleteQuery(sql, bookId);
    }
}
