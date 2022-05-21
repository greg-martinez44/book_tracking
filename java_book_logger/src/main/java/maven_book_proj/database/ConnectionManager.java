package maven_book_proj.database;

import java.util.HashMap;

class ConnectionManager {

    private String table;
    private static HashMap<String, Boolean> connectionStatusMap = new HashMap<>() {
        {
            put("books", false);
            put("authors", false);
            put("publishers", false);
            put("purchases", false);
            put("narrators", false);
            put("illustrators", false);
            put("translators", false);
            put("completed_reads", false);
            put("started_reads", false);
            put("reviews", false);
        }
    };

    ConnectionManager(String table) {
        this.table = table;
    }

    Boolean isConnectionLive() {
        return ConnectionManager.connectionStatusMap.get(this.table);
    }

    void toggleConnectionStatus() {
        Boolean currentConnectionStatus = ConnectionManager.connectionStatusMap.get(this.table);
        ConnectionManager.connectionStatusMap.put(this.table, !currentConnectionStatus);
    }
}
