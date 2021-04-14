package Extras.JSON;

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
        return key+": "+value.toString();
    }
}
