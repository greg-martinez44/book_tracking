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
    private PreparedStatement bookToUpdate;

    private boolean connectedToDatabase = false;

    public BookData() throws SQLException {
        conn = DriverManager.getConnection(CONN_URL, USERNAME, PASSWORD);

        bookToUpdate = conn.prepareStatement(
                "SELECT b.book_id FROM books b "
                + "inner join authors a on b.author_id = a.author_id"
                + "where b.title = ? and a.author = ?"
                );

        insertNewBook = conn.prepareStatement(
                "INSERT INTO books "
                + "(title, pages, duration, year, format, genre, "
                + "imprint_id, author_id, translator_id, narrator_id, illustrator_id)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );

        selectAllBooks = conn.prepareStatement(
                "SELECT b.title, a.author, s.started, rc.finished, b.pages, b.duration, "
                + "rc.rating, b.year, b.format, b.genre, pub.imprint, src.source, src.price, t.translator, "
                + "n.narrator, i.illustrator "
                + "FROM books b "
                + "INNER JOIN authors a on b.author_id = a.author_id "
                + "INNER JOIN publishers pub on b.imprint_id = pub.imprint_id "
                + "LEFT JOIN purchases src on b.book_id = src.book_id "
                + "LEFT JOIN starts s on b.book_id = s.book_id "
                + "LEFT JOIN completed_reads rc on rc.book_id = b.book_id "
                + "LEFT JOIN translators t on b.translator_id = t.translator_id "
                + "LEFT JOIN narrators n on b.narrator_id = n.narrator_id "
                + "LEFT JOIN illustrators i on b.illustrator_id = i.illustrator_id"
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
                    resultSet.getInt("year"),
                    resultSet.getDouble("price"),
                    resultSet.getString("format"),
                    resultSet.getString("genre"),
                    resultSet.getString("source"),
                    resultSet.getString("imprint"),
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
