package database;

import java.sql.*;

public class BookData {

    private final String CONN_URL = "jdbc:mysql://localhost:3306/Library";
    private final Connection conn;
    private final Statement statement;

    private ResultSet resultSet;
    private ResultSetMetaData metaData;

    private boolean connectedToDatabase = false;

    public BookData(String query) throws SQLException {
        conn = DriverManager.getConnection(CONN_URL, "jbooks", "jbooks");
        statement = conn.createStatement();
        connectedToDatabase = true;
        setQuery(query);
    }

    public int getColumnCount() throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not connected to the database");
        }
        try {
            return metaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void printTitles() throws SQLException {
        String query = "Select title from fullrecords";
        resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            System.out.println(resultSet.getString("title"));
        }
    }

    public void setQuery(String query) throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not connected to database");
        }
        try {
            resultSet = statement.executeQuery(query);
            metaData  = resultSet.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printResults() throws SQLException {
        int columns = getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columns; i++) {
                System.out.printf("%-8s\t", resultSet.getObject(i));
            }
            System.out.println();
        }
    }
}
