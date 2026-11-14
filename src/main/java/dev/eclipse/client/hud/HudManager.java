package dev.eclipse.client.hud;

import dev.eclipse.client.EclipseClient;
import dev.eclipse.client.screen.HudEditorScreen;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

import java.util.List;

public final class HudManager {
    private HudManager() {}

    public static final List<HudModule> MODULES = List.of(
            new FpsHud(), new CpsHud(), new KeystrokesHud(), new ArmorHud(),
            new CoordsHud(), new SurvivalHud());

    public static void register() {
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath(EclipseClient.MOD_ID, "huds"),
                (graphics, delta) -> {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.options.hideGui || mc.screen instanceof HudEditorScreen) return;
                    for (HudModule m : MODULES) if (m.s.enabled) m.render(graphics, mc, false);
                });
    }
}
