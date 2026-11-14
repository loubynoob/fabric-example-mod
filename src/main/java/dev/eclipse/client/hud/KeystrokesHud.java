package dev.eclipse.client.hud;

import dev.eclipse.client.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.KeyMapping;

import java.util.List;

public class KeystrokesHud extends HudModule {
    private static final int K = 18, GAP = 2;

    public KeystrokesHud() { super("keystrokes", "Keystrokes", "PvP", 0.01f, 0.55f); }

    @Override
    public List<Option> options() {
        return List.of(
                new Option("mouse", "Mouse buttons", new String[]{"on", "off"}),
                new Option("space", "Space bar", new String[]{"on", "off"}),
                new Option("pressed", "Pressed color", new String[]{"purple", "white", "red", "green", "custom"}));
    }

    private int pressedColor() {
        return switch (opt("pressed", "purple")) {
            case "white" -> 0xB0FFFFFF;
            case "red" -> 0xB0E03030;
            case "green" -> 0xB030C050;
            case "custom" -> ColorUtil.parse(opt("pressedHex", "#8A4DFF"), 0xB08A4DFF);
            default -> 0xB08A4DFF;
        };
    }

    @Override
    public int contentWidth(Minecraft mc, boolean preview) { return K * 3 + GAP * 2; }

    @Override
    public int contentHeight(Minecraft mc, boolean preview) {
        int rows = 2;
        if (optOn("mouse", true)) rows++;
        if (optOn("space", true)) rows++;
        return rows * K + (rows - 1) * GAP;
    }

    private void key(GuiGraphicsExtractor g, Minecraft mc, String label, KeyMapping km, int x, int y, int w, boolean preview, boolean fakeDown) {
        boolean down = preview ? fakeDown : (km != null && km.isDown());
        int bg = down ? pressedColor() : (s.background ? bgColor() : 0x00000000);
        g.fill(x, y, x + w, y + K, bg);
        int tw = mc.font.width(label);
        g.text(mc.font, label, x + (w - tw) / 2, y + (K - mc.font.lineHeight) / 2 + 1, textColor(), s.shadow);
    }

    @Override
    protected void drawContent(GuiGraphicsExtractor g, Minecraft mc, boolean preview) {
        var o = mc.options;
        int col = K + GAP;
        key(g, mc, "W", o.keyUp, col, 0, K, preview, true);
        int row2 = col;
        key(g, mc, "A", o.keyLeft, 0, row2, K, preview, false);
        key(g, mc, "S", o.keyDown, col, row2, K, preview, false);
        key(g, mc, "D", o.keyRight, col * 2, row2, K, preview, false);
        int y = row2 + col;
        if (optOn("mouse", true)) {
            int half = (K * 3 + GAP * 2 - GAP) / 2;
            key(g, mc, "LMB", o.keyAttack, 0, y, half, preview, true);
            key(g, mc, "RMB", o.keyUse, half + GAP, y, half, preview, false);
            y += col;
        }
        if (optOn("space", true)) {
            key(g, mc, "---", o.keyJump, 0, y, K * 3 + GAP * 2, preview, false);
        }
    }
}
