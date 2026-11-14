package dev.eclipse.client.hud;

import dev.eclipse.client.config.EclipseConfig;
import dev.eclipse.client.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.List;

/** One movable, configurable HUD element. */
public abstract class HudModule {
    public record Option(String key, String label, String[] choices) {}

    public final String id;
    public final String name;
    public final String group; // "PvP", "Essentials", "Survival"
    public final EclipseConfig.HudSettings s;

    // last drawn bounds (screen pixels), used by the editor for hit-testing
    public int lastX, lastY, lastW, lastH;

    protected HudModule(String id, String name, String group, float defX, float defY) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.s = EclipseConfig.get().hud(id, defX, defY);
    }

    public List<Option> options() { return List.of(); }

    public String opt(String key, String def) {
        return s.extra.getOrDefault(key, def);
    }

    public boolean optOn(String key, boolean def) {
        return opt(key, def ? "on" : "off").equals("on");
    }

    /** Text lines for simple text HUDs. */
    protected List<String> lines(Minecraft mc, boolean preview) { return List.of(); }

    protected int textColor() { return ColorUtil.parse(s.textColor, 0xFFFFFFFF); }
    protected int bgColor() { return ColorUtil.parse(s.bgColor, 0x66000000); }

    /** Unscaled content size. */
    public int contentWidth(Minecraft mc, boolean preview) {
        int w = 0;
        for (String l : lines(mc, preview)) w = Math.max(w, mc.font.width(l));
        return w + 6;
    }

    public int contentHeight(Minecraft mc, boolean preview) {
        return lines(mc, preview).size() * (mc.font.lineHeight + 1) + 5;
    }

    /** Draw the content at 0,0 (already translated and scaled). */
    protected void drawContent(GuiGraphicsExtractor g, Minecraft mc, boolean preview) {
        List<String> ls = lines(mc, preview);
        int w = contentWidth(mc, preview), h = contentHeight(mc, preview);
        if (s.background) g.fill(0, 0, w, h, bgColor());
        int y = 3;
        for (String l : ls) {
            g.text(mc.font, l, 3, y, textColor(), s.shadow);
            y += mc.font.lineHeight + 1;
        }
    }

    /** Extra vertical offset applied after scaling (armor HUD uses it to grow upward). */
    protected int yShift(Minecraft mc, boolean preview) { return 0; }

    public final void render(GuiGraphicsExtractor g, Minecraft mc, boolean preview) {
        int cw = contentWidth(mc, preview), ch = contentHeight(mc, preview);
        int sw = mc.getWindow().getGuiScaledWidth(), sh = mc.getWindow().getGuiScaledHeight();
        int w = Math.round(cw * s.scale), h = Math.round(ch * s.scale);
        int x = Math.round(s.x * Math.max(0, sw - w));
        int y = Math.round(s.y * Math.max(0, sh - h)) + Math.round(yShift(mc, preview) * s.scale);
        lastX = x; lastY = y; lastW = w; lastH = h;
        g.pose().pushMatrix();
        g.pose().translate(x, y);
        g.pose().scale(s.scale, s.scale);
        drawContent(g, mc, preview);
        g.pose().popMatrix();
    }

    /** Move so the top-left corner of the box is at (px, py). */
    public void moveTo(Minecraft mc, int px, int py) {
        int sw = mc.getWindow().getGuiScaledWidth(), sh = mc.getWindow().getGuiScaledHeight();
        int freeW = Math.max(1, sw - lastW), freeH = Math.max(1, sh - lastH);
        s.x = Math.max(0f, Math.min(1f, px / (float) freeW));
        s.y = Math.max(0f, Math.min(1f, py / (float) freeH));
    }
}
