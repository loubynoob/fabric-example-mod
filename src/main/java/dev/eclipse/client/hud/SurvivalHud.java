package dev.eclipse.client.hud;

import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Survival info: day count, in-game time, light level, direction. */
public class SurvivalHud extends HudModule {
    public SurvivalHud() { super("survival", "Survival Info", "Survival", 0.01f, 0.35f); }

    @Override
    public List<Option> options() {
        return List.of(
                new Option("day", "Day count", new String[]{"on", "off"}),
                new Option("time", "Time", new String[]{"on", "off"}),
                new Option("light", "Light level", new String[]{"on", "off"}),
                new Option("direction", "Direction", new String[]{"on", "off"}));
    }

    @Override
    protected List<String> lines(Minecraft mc, boolean preview) {
        long dayTime = 6000;
        int light = 15;
        String dir = "South";
        if (!preview && mc.level != null && mc.player != null) {
            dayTime = mc.level.getGameTime();
            light = mc.level.getMaxLocalRawBrightness(mc.player.blockPosition());
            String n = mc.player.getDirection().getName();
            dir = n.substring(0, 1).toUpperCase(Locale.ROOT) + n.substring(1);
        }
        long day = dayTime / 24000L + 1;
        long t = dayTime % 24000L;
        int hh = (int) (((t / 1000) + 6) % 24);
        int mm = (int) ((t % 1000) * 60 / 1000);
        List<String> out = new ArrayList<>();
        if (optOn("day", true)) out.add("Day " + day);
        if (optOn("time", true)) out.add(String.format("Time %02d:%02d", hh, mm));
        if (optOn("light", true)) out.add("Light " + light);
        if (optOn("direction", true)) out.add("Facing " + dir);
        if (out.isEmpty()) out.add("(nothing selected)");
        return out;
    }
}
