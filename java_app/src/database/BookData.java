package database;

import base.*;
import java.sql.*;
import java.util.*;

public class BookData {

    private final String CONN_URL = "jdbc:mysql://localhost:3306/Library?zeroDateTimeBehavior=convertToNull";
    private final String USERNAME = "jbooks";
    private final String PASSWORD = "jbooks";

    private final Connection conn;
    private PreparedStatement insertNewBook;
    private PreparedStatement selectAllBooks;

    private boolean connectedToDatabase = false;

    public BookData() throws SQLException {
        conn = DriverManager.getConnection(CONN_URL, USERNAME, PASSWORD);
        insertNewBook = conn.prepareStatement(
                "INSERT INTO fullrecords "
                + "(title, author, started, imprint, publishing_house,"
                + "year, pages, duration, format, source, other_authors, f_nf,"
                + "price, genre, narrator, illustrator, translator)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?)"
                );
        selectAllBooks = conn.prepareStatement(
                "SELECT * FROM fullrecords ORDER BY started"
                );
        connectedToDatabase = true;
    }

    public List<Book> getAllBooks() {
        try (ResultSet resultSet = selectAllBooks.executeQuery()) {
            List<Book> results = new ArrayList<Book>();
            while (resultSet.next()) {
                Book book = new Book(
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getInt("pages"),
                    resultSet.getInt("pubYear"),
                    resultSet.getDouble("price"),
                    resultSet.getString("format"),
                    resultSet.getString("genre"),
                    resultSet.getString("source"),
                    resultSet.getString("imprint"),
                    resultSet.getString("publishing_house"),
                    resultSet.getString("f_nf"),
                    resultSet.getString("started"),
                    resultSet.getString("finished")
                );
                results.add(book);
            }
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertBook(Book book) throws SQLException {
        insertNewBook.setString(1, book.getTitle());
        insertNewBook.setString(2, book.getAuthorName());
        insertNewBook.setString(3, book.getDateStarted());
        insertNewBook.setString(4, book.getImprint());
        insertNewBook.setString(5, book.getPublishingHouse());
        insertNewBook.setString(6, book.getPubYear().toString());
        insertNewBook.setString(7, book.getPages().toString());
        insertNewBook.setString(8, null);
        insertNewBook.setString(9, book.getFormat());
        insertNewBook.setString(10, book.getSource());
        insertNewBook.setString(11, null);
        insertNewBook.setString(12, book.getFictionOrNonFiction());
        insertNewBook.setString(13, book.getPrice().toString());
        insertNewBook.setString(14, book.getGenre().toString());
        insertNewBook.setString(15, null);
        insertNewBook.setString(16, null);
        insertNewBook.setString(17, null);

        insertNewBook.executeUpdate();
    }

    public void disconnectFromDatabase() {
        if (connectedToDatabase) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connectedToDatabase = false;
            }
        }
    }
}
