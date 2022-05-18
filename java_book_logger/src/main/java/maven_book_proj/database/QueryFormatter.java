package maven_book_proj.database;

class QueryFormatter {
    static String getSelectQueryString(String[] tableColumns, String table, String idColumn) {
        String columnString = String.join(", ", tableColumns);
        return String.format("select %s from %s order by %s", columnString, table, idColumn);
    }

    static String getSelectQueryStringFiltered(String[] tableColumns, String table, String whereClause) {
        String columnString = String.join(", ", tableColumns);
        return String.format("select %s from %s %s", columnString, table, whereClause);
    }

    static String getInsertQueryString(String[] columns, String table) {
        String[] valueString = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            valueString[i] = "?";
        }
        return String.format("insert into %s (%s) values (%s)", table, String.join(", ", columns),
                String.join(", ", valueString));
    }

    static String getDeleteQueryString(String idColumn, String table) {
        return String.format("delete from %s where %s = ?", table, idColumn);
    }

    static String getUpdateQueryString(String idColumn, String table, String columnToUpdate) {
        return String.format("update %s set %s = ? where %s = ?", table, columnToUpdate, idColumn);
    }
}
