package maven_book_proj.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QueryFormatterTest {
    @Test
    void testGetInsertQueryString() {
        String expectedResult = "insert into library_dev.authors (author, other_authors) values (?, ?)";
        String actualResult = QueryFormatter.getInsertQueryString(new String[] { "author", "other_authors" },
                "library_dev.authors");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetDeleteQuery() {
        String expectedResult = "delete from library_dev.authors where author_id = ?";
        String actualResult = QueryFormatter.getDeleteQueryString("author_id", "library_dev.authors");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetUpdateQuery() {
        String expectedResult = "update library_dev.started_reads set added_to_reads = ? where start_id = ?";
        String actualResult = QueryFormatter.getUpdateQueryString("start_id", "library_dev.started_reads",
                "added_to_reads");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetSelectQueryString() {
        String[] tableColumns = new String[] { "author_id", "author", "other_authors" };
        String tableName = "library_dev.authors";
        String idColumn = "author_id";
        String expectedResult = "select author_id, author, other_authors from library_dev.authors order by author_id";
        String actualResult = QueryFormatter.getSelectQueryString(tableColumns, tableName, idColumn);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetSelectQueryStringFiltered() {
        String[] tableColumns = new String[] { "author_id", "author", "other_authors" };
        String tableName = "library_dev.authors";
        String whereClause = "where author_id = 10";
        String expectedResult = "select author_id, author, other_authors from library_dev.authors where author_id = 10";
        String actualResult = QueryFormatter.getSelectQueryStringFiltered(tableColumns, tableName, whereClause);
        assertEquals(expectedResult, actualResult);
    }
}
