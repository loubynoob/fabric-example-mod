package dev.eclipse.client.util;

/** Parses hex colors typed or pasted by the user: "#RGB", "#RRGGBB" or "#RRGGBBAA" (letters allowed, # optional). */
public final class ColorUtil {
    private ColorUtil() {}

    public static boolean isValid(String s) {
        return parse(s, Integer.MIN_VALUE) != Integer.MIN_VALUE;
    }

    /** Returns ARGB. 6 digits = opaque, 8 digits = RRGGBBAA. */
    public static int parse(String s, int fallback) {
        if (s == null) return fallback;
        s = s.trim();
        if (s.startsWith("#")) s = s.substring(1);
        try {
            if (s.length() == 3) {
                s = "" + s.charAt(0) + s.charAt(0) + s.charAt(1) + s.charAt(1) + s.charAt(2) + s.charAt(2);
            }
            if (s.length() == 6) {
                return 0xFF000000 | Integer.parseInt(s, 16);
            }
            if (s.length() == 8) {
                long v = Long.parseLong(s, 16);
                int rgb = (int) (v >> 8) & 0xFFFFFF;
                int a = (int) (v & 0xFF);
                return (a << 24) | rgb;
            }
        } catch (NumberFormatException ignored) {
        }
        return fallback;
    }

    /** Green (full) to red (empty). */
    public static int healthColor(float fraction) {
        fraction = Math.max(0f, Math.min(1f, fraction));
        int r = (int) (255 * Math.min(1f, (1f - fraction) * 2f));
        int g = (int) (255 * Math.min(1f, fraction * 2f));
        return 0xFF000000 | (r << 16) | (g << 8) | 0x20;
    }
}
