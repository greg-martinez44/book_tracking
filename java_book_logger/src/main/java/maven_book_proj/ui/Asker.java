package maven_book_proj.ui;

import java.util.Scanner;
import java.io.InputStream;
import java.io.PrintStream;

public class Asker {
    private Scanner scanner;
    private PrintStream out;

    public Asker(InputStream in, PrintStream out) {
        this.scanner = new Scanner(in);
        this.out = out;
    }

    public String ask(String message) {
        out.println(message);
        return scanner.nextLine();
    }
}
