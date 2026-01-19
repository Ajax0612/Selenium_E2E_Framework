package config;

import io.github.cdimascio.dotenv.Dotenv;

public final class Config {

    private static final Dotenv ENV = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    private Config() {
        // Prevent instantiation
    }

    private static String get(String key, String def) {

        // 1️⃣ Try Java system property (-DKEY=value)
        String value = System.getProperty(key);

        // 2️⃣ If not found or blank, try OS environment variable
        if (value == null || value.isBlank()) value = System.getenv(key);

        // 3️⃣ If still not found, try from .env file
        if (value == null || value.isBlank()) value = ENV.get(key);

        // 4️⃣ Return whatever we got, or fallback to default
        return value != null ? value : def;
    }

    public static String baseUrl() {
        return get("BASE_URL", "https://example.com");
    }

    public static String username() {
        return get("APP_USERNAME", "");
    }

    public static char[] password() {
        return get("APP_PASSWORD", "").toCharArray();
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("HEADLESS", "true"));
    }
}
