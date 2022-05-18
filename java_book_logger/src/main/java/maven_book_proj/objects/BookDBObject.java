package maven_book_proj.objects;

public class BookDBObject {

    Integer setIntegerVariable(String value) {
        if (value != null && value != "") {
            return Integer.parseInt(value);
        }
        return null;
    }

    Double setDoubleVariable(String value) {
        if (value != null && value != "") {
            return Double.parseDouble(value);
        }
        return null;
    }

    String setVariable(String value) {
        return value;
    }
}
