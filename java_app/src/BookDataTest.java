import database.*;
import base.*;

import java.util.*;
import java.sql.*;

public class BookDataTest {
    public static void main(String[] args) {
        try{
            BookData db = new BookData();
            List<Book> results = db.getAllBooks();
            System.out.println(results);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
