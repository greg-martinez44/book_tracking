package maven_book_proj.objects;

import java.util.HashMap;

public class Narrator extends BookDBObject {

    private Integer narratorId;
    private String narrator;

    public Narrator(HashMap<String, String> narratorData) {
        this.narratorId = setIntegerVariable(narratorData.get("narrator_id"));
        this.narrator = setVariable(narratorData.get("narrator"));
    }

    public Integer getNarratorId() {
        return this.narratorId;
    }

    public String getNarrator() {
        return this.narrator;
    }
}
