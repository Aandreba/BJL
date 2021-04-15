package Extras.JSON;

import Extras.Regex;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.*;

public class JSONObject extends ArrayList<JSONEntry> {
    public JSONObject () {
        super();
    }

    public JSONObject (Map<? extends String, ?> map) {
        super();
        setAll(map);
    }

    public JSONObject (String string) {
        super();

        if (string == null || string.length() <= 0) {
            return;
        }

        // Clean up text
        string = string.strip();
        string = string.replaceAll("\\s*\\n\\s*", "");
        string = string.replaceAll("\"\\s*:\\s*", "\":");
        string = string.replaceAll("\\,\\n*\\s*\"", ",\"");

        if (string.charAt(0) == '[') {
            string = "{\"\":"+string+"}";
        }

        // Generate String Builder
        StringBuilder builder = new StringBuilder(string);

        while (builder.length() > 0) {
            String key = stringFinder(builder);
            if (key == null) {
                break;
            }

            builder.deleteCharAt(0);
            JSONEntry entry = entryFinder(builder, key);
            add(entry);
        }
    }

    public Object get (String key) {
        return get(indexOf(key)).value;
    }

    public <T> T getAs (String key) {
        return (T) get(key);
    }

    public String getString (String key) {
        return getAs(key);
    }

    public JSONObject getObject (String key) {
        return getAs(key);
    }

    public ArrayList getArray (String key) {
        return getAs(key);
    }

    public <T> ArrayList<T> getAsArray (String key) {
        ArrayList base = getArray(key);
        ArrayList<T> result = new ArrayList<>();
        base.forEach(x -> result.add((T) x));

        return result;
    }

    public boolean getBool (String key) {
        return getAs(key);
    }

    public Number getNumber (String key) {
        return getAs(key);
    }

    public double getDouble (String key) {
        return getNumber(key).doubleValue();
    }

    public float getFloat (String key) {
        return getNumber(key).floatValue();
    }

    public long getLong (String key) {
        return getNumber(key).longValue();
    }

    public int getInt (String key) {
        return getNumber(key).intValue();
    }

    public void set (String key, Object value) {
        JSONEntry entry;
        try {
            entry = new JSONEntry(key, new JSONObject((Map<String,Object>) value));
        } catch (Exception e) {
            entry = new JSONEntry(key, value);
        }

        int index = indexOf(key);
        if (index == -1) {
            add(entry);
        } else {
            set(index, entry);
        }
    }

    public void set (String key, List values) {
        JSONEntry entry = new JSONEntry(key, values);

        int index = indexOf(key);
        if (index == -1) {
            add(entry);
        } else {
            set(index, entry);
        }
    }

    public void set (String key, Object... values) {
        ArrayList objects = new ArrayList();
        Collections.addAll(objects, values);

        set(key, objects);
    }

    public void setAll (Map<? extends String, ?> map) {
        for (Map.Entry<? extends String, ?> entry: map.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    public boolean isNull (String key) {
        return get(key) == null;
    }

    public boolean containsKey (String key) {
        return indexOf(key) != -1;
    }

    public boolean containsValue (Object value) {
        for (JSONEntry entry: this) {
            if (entry.value.equals(value)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        for (JSONEntry entry: this) {
            builder.append(entry.toString()+", ");
        }

        return builder.substring(0, builder.length() - 2) + "}";
    }

    // Byte JSON
    public ByteBuffer toBJSON () {
        // TODO
        return null;
    }

    private JSONEntry entryFinder (StringBuilder builder, String key) {
        if (builder.length() <= 0) {
            return null;
        }

        // String
        if (builder.charAt(0) == '"') {
            String value = stringFinder(builder);
            return new JSONEntry(key, value);
        }

        // Number
        else if (builder.substring(0,1).matches("\\d")) {
            String value = numberFinder(builder);

            if (value.contains(".")) { // Decimal
                try {
                    return new JSONEntry(key, Float.parseFloat(value)); // Float
                } catch (Exception e) {
                    return new JSONEntry(key, Double.parseDouble(value)); // Double
                }
            } else { // Integer
                try {
                    return new JSONEntry(key, Integer.parseInt(value)); // Int
                } catch (Exception e) {
                    return new JSONEntry(key, Long.parseLong(value)); // Long
                }
            }
        }

        // Boolean (true)
        else if (builder.length() >= 4 && builder.substring(0, 4).equals("true")) {
            builder.delete(0, 4);
            return new JSONEntry(key, true);
        }

        // Boolean (false)
        else if (builder.length() >= 5 && builder.substring(0, 5).equals("false")) {
            builder.delete(0, 5);
            return new JSONEntry(key, false);
        }

        // Null
        else if (builder.length() >= 4 && builder.substring(0, 4).equals("null")) {
            builder.delete(0, 4);
            return new JSONEntry(key, null);
        }

        // Array
        else if (builder.charAt(0) == '[') {
            String inside = arrayFinder(builder);
            if (inside == null) {
                return new JSONEntry(key, new ArrayList<>());
            }

            StringBuilder value = new StringBuilder(inside);
            ArrayList entries = new ArrayList<>();

            while (value.length() > 0) {
                JSONEntry entry = entryFinder(value, "");
                if (entry == null) {
                    break;
                }

                if (value.length() > 0 && value.charAt(0) == ',') {
                    value.deleteCharAt(0);
                }

                entries.add(entry.value);
            }

            return new JSONEntry(key, entries);
        }

        // Object
        else if (builder.charAt(0) == '{') {
            String value = objectFinder(builder);
            return new JSONEntry(key, new JSONObject(value));
        }

        return null;
    }

    private String objectFinder (StringBuilder builder) {
        String result = null;
        int skips = 0;

        builder.deleteCharAt(0);
        while (true) {
            char c = builder.charAt(0);
            builder.deleteCharAt(0);

            if (c == '{') {
                skips++;
            } else if (c == '}' && skips <= 0) {
                break;
            } else if (c == '}' && skips > 0) {
                skips--;
            }

            if (result == null) {
                result = ""+c;
            } else {
                result += c;
            }
        }

        return result;
    }

    private String arrayFinder (StringBuilder builder) {
        String result = null;
        int skips = 0;

        builder.deleteCharAt(0);
        while (true) {
            char c = builder.charAt(0);
            builder.deleteCharAt(0);

            if (c == '[') {
                skips++;
            } else if (c == ']' && skips <= 0) {
                break;
            } else if (c == ']' && skips > 0) {
                skips--;
            }

            if (result == null) {
                result = ""+c;
            } else {
                result += c;
            }
        }

        return result;
    }

    private String numberFinder (StringBuilder builder) {
        String result = "";

        while (builder.length() > 0) {
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

        String result = null;

        while (builder.length() > 0) {
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