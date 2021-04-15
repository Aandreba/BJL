package Extras.JSON;

import java.util.List;
import java.util.Map;

public class JSONEntry<V> {
    final public String key;
    final public V value;

    public JSONEntry (String key, V value) {
        this.key = key;
        this.value = value;
    }

    public Class<?> getType () {
        return value.getClass();
    }

    @Override
    public String toString () {
        return "\""+key+"\": "+(value == null ? "null" : toString(value));
    }

    private String toString (Object object) {
        if (object instanceof CharSequence) {
            return "\"" + object + "\"";
        } else if (object instanceof List && !(object instanceof JSONObject)) {
            StringBuilder result = new StringBuilder();
            List list = (List) object;
            list.forEach(x -> result.append(", "+toString(x)));

            return "[" + (result.length() >= 2 ? result.substring(2) : result) + "]";
        } else {
            return object.toString();
        }
    }
}
