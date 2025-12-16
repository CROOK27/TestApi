package utils;

import java.util.HashMap;
import java.util.Map;

public class TokenManager {
    private static final Map<String, String> tokens = new HashMap<>();

    public static void saveToken(String key, String token) {
        tokens.put(key, token);
    }

    public static String getToken(String key) {
        return tokens.get(key);
    }

    public static void clearTokens() {
        tokens.clear();
    }

    public static String getDefaultToken() {
        return tokens.get("default");
    }

    public static void saveDefaultToken(String token) {
        tokens.put("default", token);
    }

    public static boolean hasDefaultToken() {
        return tokens.containsKey("default") &&
                tokens.get("default") != null &&
                !tokens.get("default").isEmpty();
    }
}