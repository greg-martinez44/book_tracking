class BookTest {
    public static void main(String[] args) {
        Book book = new Book("Dark Age", "Pierce Brown");
        try{
            book.setPages(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        book.setSource("Bookshop.org");
        book.setGenre("Sci-fi");
        book.setFormat("hardcover");
        System.out.printf("Stats for %s by %s:\n", book.getTitle(), book.getAuthorName());
        System.out.println(book.getPages());
        System.out.println(book.getSource());
        System.out.println(book.getGenre());
        System.out.println(book.getFormat());

        Book book2 = new Book("A Different Book", "Someone Else");
        System.out.println(book2.getTitle());
    }
}