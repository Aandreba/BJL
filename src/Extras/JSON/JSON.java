package Extras.JSON;

import Extras.JSON.JSONEntry;
import Extras.Regex;

import java.util.*;

public class JSON extends ArrayList<JSONEntry> {
    public JSON (String string) {
        super();

        if (string.charAt(0) == '{') {
            string = string.substring(1);
        }

        if (string.charAt(string.length() - 1) == '}') {
            string = string.substring(0, string.length() - 1);
        }

        StringBuilder builder = new StringBuilder(string.strip());

        while (true) {
            String key = stringFinder(builder);
            if (key == null) {
                break;
            }

            Regex.replaceFirst(builder, "\\s*\\:\\s*", "");
            JSONEntry<?> entry = processValue(builder, key);
            System.out.println(entry);
            add(entry);
        }
    }

    private JSONEntry<?> processValue (StringBuilder builder, String key) {
        if (builder.charAt(0) == '"') { // String
            return new JSONEntry<String>(key, stringFinder(builder));
        } else if (builder.charAt(0) == '.' || builder.substring(0,1).matches("\\d")) { // Number
            String num = numberFinder(builder);

            if (!num.contains(".")) { // Integer
                try {
                    return new JSONEntry<Integer>(key, Integer.parseInt(num));
                } catch (Exception e) {
                    return new JSONEntry<Long>(key, Long.parseLong(num));
                }
            } else { // Decimal
                try {
                    return new JSONEntry<Float>(key, Float.parseFloat(num));
                } catch (Exception e) {
                    return new JSONEntry<Double>(key, Double.parseDouble(num));
                }
            }
        } else if (builder.charAt(0) == '[') { // Array
            StringBuilder group = new StringBuilder(Regex.firstMatchAndDelete(builder, "\\[[^\\[]+\\]").substring(1).strip());
            ArrayList<JSONEntry<?>> entries = new ArrayList<>();
            while (true) {
                JSONEntry<?> entry = processValue(group, "");
                System.out.println(entry);
                break;
            }

        } else if (builder.charAt(0) == '{') { // Map
            String group = Regex.firstMatchAndDelete(builder, "\\{[^\\{]+\\}").strip();
            JSON value = new JSON(group);
            return new JSONEntry<JSON>(key, value);
        }

        return null;
    }

    private String numberFinder (StringBuilder builder) {
        String result = "";

        while (true) {
            String c = builder.substring(0,1);
            builder.deleteCharAt(0);

            if (!c.equals(".") && !c.matches("\\d")) {
                break;
            }

            result += c;
        }

        return result;
    }

    private String stringFinder (StringBuilder builder) {
        if (builder.length() <= 0) {
            return null;
        }
        //TODO
        String result = null;

        while (true) {
            char c = builder.charAt(0);
            builder.deleteCharAt(0);

            if (c == '"' && result == null) {
                result = "";
            } else if (c == '"') {
                break;
            } else if (result != null) {
                result += c;
            }
        }

        return result;
    }

    private int indexOf (String key) {
        for (int i=0;i<size();i++) {
            if (get(i).key.equals(key)) {
                return i;
            }
        }

        return -1;
    }
}