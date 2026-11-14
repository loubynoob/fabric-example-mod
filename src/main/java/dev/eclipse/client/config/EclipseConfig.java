package dev.eclipse.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EclipseConfig {
    public static class HudSettings {
        public boolean enabled = true;
        public float x = 0.02f;          // 0..1 position across the free screen space
        public float y = 0.20f;
        public float scale = 1.0f;
        public String textColor = "#FFFFFF";
        public String bgColor = "#00000066"; // RRGGBBAA
        public boolean background = true;
        public boolean shadow = true;
        public Map<String, String> extra = new HashMap<>();
    }

    public Map<String, HudSettings> huds = new LinkedHashMap<>();
    public boolean fpsGuard = true;
    public int fpsTarget = 40;
    public boolean toggleSprint = false;
    public int skyPreset = 0;               // 0 = vanilla
    public String bobMode = "vanilla";     // vanilla | on | off
    public boolean brightInventory = true;
    public boolean eclipseTitle = true;
    public boolean eclipseLoading = true;
    public boolean selfLogo = true;
    public int zoomFov = 30;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static EclipseConfig instance;

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("eclipse-client.json");
    }

    public static EclipseConfig get() {
        if (instance == null) load();
        return instance;
    }

    public static void load() {
        Path p = file();
        if (Files.exists(p)) {
            try (Reader r = Files.newBufferedReader(p)) {
                instance = GSON.fromJson(r, EclipseConfig.class);
            } catch (Exception e) {
                instance = null;
            }
        }
        if (instance == null) instance = new EclipseConfig();
        if (instance.huds == null) instance.huds = new LinkedHashMap<>();
    }

    public static void save() {
        try (Writer w = Files.newBufferedWriter(file())) {
            GSON.toJson(get(), w);
        } catch (IOException ignored) {
        }
    }

    public HudSettings hud(String id, float defX, float defY) {
        return huds.computeIfAbsent(id, k -> {
            HudSettings s = new HudSettings();
            s.x = defX;
            s.y = defY;
            return s;
        });
    }
}
