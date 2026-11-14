package dev.eclipse.client.util;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Original "frosted glass" look: translucent body, bright top rim, soft violet edge. Not a copy of any OS style. */
public final class GlassUi {
    private GlassUi() {}

    public static final int PURPLE = 0xFF8B5CF6;
    public static final int PURPLE_DARK = 0xFF4C1D95;

    public static void panel(GuiGraphicsExtractor g, int x, int y, int w, int h) {
        g.fillGradient(x, y, x + w, y + h, 0x70FFFFFF & 0x70E9E0FF | 0x30000000, 0x50301A5E);
        // rounded-ish corners: cut 1px corners by overdrawing edges
        g.fill(x + 1, y, x + w - 1, y + 1, 0xB0FFFFFF);          // top rim highlight
        g.fill(x, y + 1, x + 1, y + h - 1, 0x40C4B5FD);          // left edge
        g.fill(x + w - 1, y + 1, x + w, y + h - 1, 0x408B5CF6);  // right edge
        g.fill(x + 1, y + h - 1, x + w - 1, y + h, 0x608B5CF6);  // bottom edge
    }

    public static void button(GuiGraphicsExtractor g, int x, int y, int w, int h, boolean hover, boolean on) {
        int top = hover ? 0x90FFFFFF : 0x60FFFFFF;
        int bot = on ? 0xA08B5CF6 : 0x504C1D95;
        g.fillGradient(x, y, x + w, y + h, top & 0x40FFFFFF | 0x20000000 | (top & 0x00FFFFFF), bot);
        g.fill(x + 1, y, x + w - 1, y + 1, 0xA0FFFFFF);
        g.outline(x, y, w, h, on ? 0xC0C4B5FD : 0x60C4B5FD);
    }
}
