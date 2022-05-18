package maven_book_proj.objects;

import java.util.HashMap;

public class Purchase extends BookDBObject {
    private Integer purchaseId;
    private Integer bookId;
    private Double price;
    private String source;
    private String datePurchased;

    public Purchase(HashMap<String, String> purchaseData) {
        this.purchaseId = setIntegerVariable(purchaseData.get("purchase_id"));
        this.bookId = setIntegerVariable(purchaseData.get("book_id"));
        this.price = setDoubleVariable(purchaseData.get("price"));
        this.source = setVariable(purchaseData.get("source"));
        this.datePurchased = setVariable(purchaseData.get("purchase_date"));
    }

    public Integer getPurchaseId() {
        return this.purchaseId;
    }

    public Integer getBookId() {
        return this.bookId;
    }

    public Double getPrice() {
        return this.price;
    }

    public String getSource() {
        return this.source;
    }

    public String getDatePurchased() {
        return this.datePurchased;
    }
}
