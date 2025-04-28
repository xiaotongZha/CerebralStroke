package utils;

public class ParseUtils {

    public static Integer parseInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof String) {
            try {
                String s = ((String) value).trim();
                if (s.isEmpty()) {
                    return null;
                }
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }

    public static String parseString(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString().trim();
    }

    public static Double parseDouble(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof String) {
            try {
                String s = ((String) value).trim();
                if (s.isEmpty()) {
                    return null;
                }
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return null;
    }
}