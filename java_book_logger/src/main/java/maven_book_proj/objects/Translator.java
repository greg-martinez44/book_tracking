package maven_book_proj.objects;

import java.util.HashMap;

public class Translator extends BookDBObject {

    private Integer translatorId;
    private String translator;

    public Translator(HashMap<String, String> translatorData) {
        this.translatorId = setIntegerVariable(translatorData.get("translator_id"));
        this.translator = setVariable(translatorData.get("translator"));
    }

    public Integer getTranslatorId() {
        return this.translatorId;
    }

    public String getTranslator() {
        return this.translator;
    }
}
