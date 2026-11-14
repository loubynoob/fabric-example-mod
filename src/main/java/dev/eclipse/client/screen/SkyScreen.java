package dev.eclipse.client.screen;

import dev.eclipse.client.config.EclipseConfig;
import dev.eclipse.client.sky.SkyPresets;
import dev.eclipse.client.util.GlassUi;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Pick a sky preset (click to apply, see it live behind the screen when in a world). */
public class SkyScreen extends Screen {
    private final Screen parent;

    public SkyScreen(Screen parent) {
        super(Component.literal("Eclipse Sky"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cols = 3, bw = 110, bh = 20, gap = 6;
        int x0 = width / 2 - (cols * bw + (cols - 1) * gap) / 2, y0 = 40;
        for (int i = 0; i < SkyPresets.ALL.length; i++) {
            final int idx = i;
            int x = x0 + (i % cols) * (bw + gap), y = y0 + (i / cols) * (bh + gap);
            String label = (EclipseConfig.get().skyPreset == i ? "> " : "") + SkyPresets.ALL[i].name();
            addRenderableWidget(Button.builder(Component.literal(label), b -> {
                EclipseConfig.get().skyPreset = idx; init();
            }).bounds(x, y, bw, bh).build());
        }
        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
                .bounds(width / 2 - 40, height - 28, 80, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float delta) {
        g.fill(0, 0, width, height, 0x90140A28);
        GlassUi.panel(g, width / 2 - 190, 28, 380, height - 62);
        g.text(font, "Sky presets", width / 2 - 28, 14, 0xFFE9D5FF, true);
        super.extractRenderState(g, mx, my, delta);
    }

    @Override
    public void onClose() {
        EclipseConfig.save();
        minecraft.setScreen(parent);
    }
}
