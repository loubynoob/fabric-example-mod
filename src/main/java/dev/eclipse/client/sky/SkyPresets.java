package dev.eclipse.client.sky;

public final class SkyPresets {
    private SkyPresets() {}

    public record Sky(String name, int rgb, boolean fixed) {}

    /** index 0 = vanilla. "fixed" ignores day/night dimming. */
    public static final Sky[] ALL = {
            new Sky("Vanilla", 0x000000, false),
            new Sky("Violet Dusk", 0x7C3AED, false),
            new Sky("Blood Moon", 0x9F1239, false),
            new Sky("Aurora", 0x10B981, false),
            new Sky("Nebula", 0xC026D3, false),
            new Sky("Midnight Blue", 0x1E3A8A, false),
            new Sky("Sakura", 0xF9A8D4, false),
            new Sky("Golden Hour", 0xF59E0B, false),
            new Sky("Deep Ocean", 0x0E7490, false),
            new Sky("Ember", 0xEA580C, false),
            new Sky("Mint Fog", 0x99F6E4, false),
            new Sky("Lavender", 0xC4B5FD, false),
            new Sky("Storm Slate", 0x64748B, false),
            new Sky("Neon Cyber", 0x22D3EE, false),
            new Sky("Twilight (always)", 0x4C1D95, true),
            new Sky("Eclipse (always)", 0x2A0A1E, true),
            new Sky("Void (always)", 0x05030A, true),
            new Sky("Sunset (always)", 0xFB7185, true),
    };
}
