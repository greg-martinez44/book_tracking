package maven_book_proj.database;

import java.sql.SQLException;

interface CommonTests {

    void testGetTableName();

    void testOnlyOneConnectionAllowed() throws SQLException;

    void testGetRecords() throws SQLException;

    void testGetRecord() throws SQLException;

    void testGetTableIdFromTableIdentifier() throws SQLException;

    void testInsertRecord() throws SQLException;

    void testDeleteRecord() throws SQLException;
}
