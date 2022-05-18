package maven_book_proj.objects;

import java.util.HashMap;

public class Publisher {

    private Integer imprintId;
    private String imprint;
    private String publishingHouse;

    public Publisher(HashMap<String, String> publisherData) {
        this.imprint = publisherData.get("imprint");
        this.publishingHouse = publisherData.get("publishing_house");

        if (publisherData.get("imprint_id") != null) {
            this.imprintId = Integer.parseInt(publisherData.get("imprint_id"));
        }
    }

    public Integer getImprintId() {
        return this.imprintId;
    }

    public String getImprint() {
        return this.imprint;
    }

    public String getPublishingHouse() {
        return this.publishingHouse;
    }

}
