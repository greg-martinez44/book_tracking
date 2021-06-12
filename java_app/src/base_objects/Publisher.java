import java.util.ArrayList;

class Publisher {
    private String publisherName;
    private ArrayList<String> imprints = new ArrayList<>();

    public Publisher(String publisherName) {
        this.publisherName = publisherName;
    }

    public void addImprint(String imprint) {
        this.imprints.add(imprint);
    }
}