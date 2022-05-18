package maven_book_proj.objects;

import java.util.HashMap;

public class Illustrator extends BookDBObject {

    private Integer illustratorId;
    private String illustrator;

    public Illustrator(HashMap<String, String> illustratorData) {
        this.illustratorId = setIntegerVariable(illustratorData.get("illustrator_id"));
        this.illustrator = setVariable(illustratorData.get("illustrator"));
    }

    public Integer getIllustratorId() {
        return this.illustratorId;
    }

    public String getIllustrator() {
        return this.illustrator;
    }
}
