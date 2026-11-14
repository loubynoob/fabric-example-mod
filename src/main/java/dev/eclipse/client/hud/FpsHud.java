package dev.eclipse.client.hud;

import net.minecraft.client.Minecraft;
import java.util.List;

public class FpsHud extends HudModule {
    public FpsHud() { super("fps", "FPS", "Essentials", 0.01f, 0.02f); }

    @Override
    protected List<String> lines(Minecraft mc, boolean preview) {
        return List.of("FPS: " + (preview ? 120 : mc.getFps()));
    }
}
