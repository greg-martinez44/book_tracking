import database.*;
import base.*;

import java.sql.*;
import java.util.*;

public class BookImportTest {

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        System.out.print("Enter book's title: ");
        String title = input.nextLine();
        System.out.print("Enter author name: ");
        String author = input.nextLine();

        Book book = new Book(title, author);

        System.out.print("Enter date started: ");
        String year = input.nextLine();
        if (year == "") {
            book.setDateStarted(null);
        } else {
            book.setDateStarted(year);
        }
        System.out.print("Enter imprint: ");
        book.setImprint(input.nextLine());
        System.out.print("Enter publishing house: ");
        book.setPublishingHouse(input.nextLine());
        System.out.print("Enter the year published: ");
        book.setPubYear(input.nextInt());
        System.out.print("Enter number of pages: ");
        book.setPages(input.nextInt());
        input.nextLine();
        System.out.print("Enter the format: ");
        book.setFormat(input.nextLine());
        System.out.print("Enter the source: ");
        book.setSource(input.nextLine());
        System.out.print("Enter fiction or non-fiction: ");
        book.setFictionOrNonFiction(input.nextLine());
        System.out.print("Enter the price: ");
        book.setPrice(input.nextDouble());
        input.nextLine();
        System.out.print("Enter the genre: ");
        book.setGenre(input.nextLine());

        try {
            BookData db = new BookData();
            db.insertBook(book);
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
}
