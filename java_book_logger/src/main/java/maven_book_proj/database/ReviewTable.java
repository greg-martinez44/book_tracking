package maven_book_proj.database;

import maven_book_proj.objects.Review;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class ReviewTable extends BookDB implements DataTable<Review> {

    private String reviewTable;
    private String[] tableColumns = new String[] {
            "review_id",
            "read_id",
            "review"
    };

    public ReviewTable(String schema) throws SQLException {
        super(schema, "reviews");
        this.reviewTable = String.format("library%s.reviews", this.schema);
    }

    public String getTableName() {
        return this.reviewTable;
    }

    public List<Review> getRecords() throws SQLException {
        List<Review> reviewList = new ArrayList<>();
        String sql = QueryFormatter.getSelectQueryString(this.tableColumns, this.reviewTable, "review_id");
        List<HashMap<String, String>> records = getRecords(sql, this.tableColumns);
        for (HashMap<String, String> reviewParams : records) {
            reviewList.add(new Review(reviewParams));
        }
        return reviewList;
    }

    public Review getRecord(Integer reviewId) throws SQLException {
        return new Review(new HashMap<String, String>());
    }

    public Integer getTableId(String readId) throws SQLException {
        return getTableId(Integer.parseInt(readId));
    }

    public Integer getTableId(Integer readId) throws SQLException {
        return 0;
    }

    public void insertRecord(Review review) throws SQLException {
        //
    }

    public void deleteRecord(Integer reviewId) throws SQLException {
        //
    }
}
