package util;

public class JsonBuilder {

    private StringBuilder stringBuilder;

    public JsonBuilder() {
        stringBuilder = new StringBuilder("{");
    }

    public JsonBuilder put(String key, String value) {
        stringBuilder.append(addMore())
                .append("\"")
                .append(key)
                .append("\": \"")
                .append(value)
                .append("\",");
        return this;
    }

    public JsonBuilder put(String key, int value) {
        stringBuilder.append(addMore())
                .append("\"")
                .append(key)
                .append("\": ")
                .append(value)
                .append(",");
        return this;
    }

    public String getJson() {
        return this.toString();
    }

    @Override
    public String toString() {
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        stringBuilder.append(newLine())
                .append("}");
        return stringBuilder.toString();
    }

    private String addMore() {
        return newLine() + indentation();
    }

    private String newLine() {
        return "\n";
    }

    private String indentation() {
        return "    ";
    }
}
