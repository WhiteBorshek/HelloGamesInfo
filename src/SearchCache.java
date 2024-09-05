import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchCache {
    private static Map<String, JSONObject> searchCache = new HashMap<>();

    public static JSONObject get(String key) {
        return searchCache.get(key);
    }

    public static void put(String key, JSONObject value) {
        searchCache.put(key, value);
    }

    public static boolean containsKey(String key) {
        return searchCache.containsKey(key);
    }
}
