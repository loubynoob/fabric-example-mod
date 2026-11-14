package dev.eclipse.client.hud;

import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CoordsHud extends HudModule {
    public CoordsHud() { super("coords", "Coordinates", "Essentials", 0.01f, 0.14f); }

    @Override
    public List<Option> options() {
        return List.of(
                new Option("decimals", "Decimals", new String[]{"0", "1", "2"}),
                new Option("facing", "Facing", new String[]{"on", "off"}));
    }

    @Override
    protected List<String> lines(Minecraft mc, boolean preview) {
        double x = 128.5, y = 64, z = -302.25;
        String facing = "North";
        if (!preview && mc.player != null) {
            x = mc.player.getX();
            y = mc.player.getY();
            z = mc.player.getZ();
            String n = mc.player.getDirection().getName();
            facing = n.substring(0, 1).toUpperCase(Locale.ROOT) + n.substring(1);
        }
        int d = Integer.parseInt(opt("decimals", "0"));
        String f = "%." + d + "f";
        List<String> out = new ArrayList<>();
        out.add("X: " + String.format(f, x));
        out.add("Y: " + String.format(f, y));
        out.add("Z: " + String.format(f, z));
        if (optOn("facing", true)) out.add(facing);
        return out;
    }
}
