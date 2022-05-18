package maven_book_proj.database;

import java.sql.SQLException;
import java.util.List;

interface DataTable<T> {

    String getTableName();

    List<T> getRecords() throws SQLException;

    T getRecord(Integer Id) throws SQLException;

    Integer getTableId(String string) throws SQLException;

    void insertRecord(T dbObject) throws SQLException;

    void deleteRecord(Integer id) throws SQLException;
}
