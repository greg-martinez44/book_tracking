import database.*;

import java.sql.*;

public class BookDataTest {
    public static void main(String[] args) {
        try{
            BookData db = new BookData("Select * from fullrecords where started > '2021-03-01'");
            db.printResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
