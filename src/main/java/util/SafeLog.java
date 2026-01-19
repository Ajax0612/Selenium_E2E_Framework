package util;

public final class SafeLog {
    private SafeLog() {}
    public static String mask(String s) {
        if (s == null || s.isBlank()) return "****";
        if (s.length() <= 4) return "****";
        return s.substring(0, 2) + "****" + s.substring(s.length() - 2);
    }
}
