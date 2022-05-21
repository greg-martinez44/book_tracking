package maven_book_proj.objects;

import java.util.HashMap;

public class Review extends BookDBObject {

    private Integer reviewId;
    private Integer readId;
    private String review;

    public Review(HashMap<String, String> reviewData) {
        this.reviewId = setIntegerVariable(reviewData.get("review_id"));
        this.readId = setIntegerVariable(reviewData.get("read_id"));
        this.review = setVariable(reviewData.get("review"));
    }

    public Integer getReviewId() {
        return this.reviewId;
    }

    public Integer getReadId() {
        return this.readId;
    }

    public String getReview() {
        return this.review;
    }
}
