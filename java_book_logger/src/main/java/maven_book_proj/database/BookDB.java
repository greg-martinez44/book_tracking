package maven_book_proj.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class BookDB {
    private String CONN_URL = System.getenv("BOOKS_CONN");
    private String USERNAME = System.getenv("BOOKS_USERNAME");
    private String PASSWORD = System.getenv("BOOKS_PASSWORD");
    protected String schema;
    private Connection conn;
    private ConnectionManager connectionManger;

    public BookDB(String schema, String tableName) throws SQLException {
        this.connectionManger = new ConnectionManager(tableName);
        if (connectionManger.isConnectionLive()) {
            throw new SQLException("Connection is already live!");
        }
        if (schema.equals("dev")) {
            this.schema = "_dev";
        } else if (schema.equals("main")) {
            this.schema = "";
        } else {
            throw new SQLException("Invalid schema given");
        }
        if (this.CONN_URL == null
                || this.USERNAME == null
                || this.PASSWORD == null) {
            JSONObject systemEnv = getSystemEnvFromJSONFile();
            this.CONN_URL = systemEnv.getString("BOOKS_CONN");
            this.USERNAME = systemEnv.getString("USERNAME");
            this.PASSWORD = systemEnv.getString("PASSWORD");
        }
        this.conn = DriverManager.getConnection(CONN_URL, USERNAME, PASSWORD);
        this.connectionManger.toggleConnectionStatus();
    }

    private JSONObject getSystemEnvFromJSONFile() {
        Path envFilePath = Paths.get("externals/env.json");
        try {
            List<String> stringFromFile = Files.readAllLines(envFilePath, StandardCharsets.UTF_8);
            String joinedString = String.join("", stringFromFile);
            return new JSONObject(joinedString);
        } catch (IOException e) {
            return new JSONObject();
        }
    }

    protected Integer getId(String sql, String idCol) throws SQLException {
        ResultSet resultSet = runQuery(sql);
        while (resultSet.next()) {
            return resultSet.getInt(idCol);
        }
        return 0;
    }

    protected List<HashMap<String, String>> getRecords(String sql, String[] tableColumns) throws SQLException {
        List<HashMap<String, String>> recordsList = new ArrayList<>();
        ResultSet resultSet = runQuery(sql);
        while (resultSet.next()) {
            HashMap<String, String> recordParams = buildHashMap(resultSet, tableColumns);
            recordsList.add(recordParams);
        }
        return recordsList;
    }

    protected ResultSet runQuery(String sql) throws SQLException {
        PreparedStatement prepared_sql = this.conn.prepareStatement(sql);
        return prepared_sql.executeQuery();
    }

    protected void runInsertQuery(String baseSql, String[] args) {
        try {
            PreparedStatement prepared_sql = this.conn.prepareStatement(baseSql);
            for (int i = 0; i < args.length; i++) {
                prepared_sql.setString(i + 1, args[i]);
            }
            prepared_sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void runDeleteQuery(String baseSql, Integer id) throws SQLException {
        PreparedStatement preparedSql = this.conn.prepareStatement(baseSql);
        preparedSql.setInt(1, id);
        preparedSql.execute();
    }

    protected void runUpdateQuery(String baseSql, String newValue, Integer id) throws SQLException {
        PreparedStatement preparedSql = this.conn.prepareStatement(baseSql);
        preparedSql.setString(1, newValue);
        preparedSql.setInt(2, id);
        preparedSql.execute();
    }

    protected String[] getColumnNames(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        Integer columnCount = resultSetMetaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            columnNames[columnIndex] = resultSetMetaData.getColumnName(columnIndex + 1);
        }
        return columnNames;
    }

    protected Integer getColumnCount(String[] columnNames) {
        return columnNames.length;
    }

    protected HashMap<String, String> buildHashMap(ResultSet resultSet, String[] tableColumns) throws SQLException {
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < tableColumns.length; i++) {
            String columnName = tableColumns[i];
            hashMap.put(columnName, resultSet.getString(columnName));
        }
        return hashMap;
    }

    public void disconnect() throws SQLException {
        if (this.connectionManger.isConnectionLive()) {
            this.conn.close();
            this.connectionManger.toggleConnectionStatus();
        }
    }

}
