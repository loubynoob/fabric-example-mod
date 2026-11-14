package dev.eclipse.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.eclipse.client.config.EclipseConfig;
import dev.eclipse.client.fps.FpsGuard;
import dev.eclipse.client.hud.CpsHud;
import dev.eclipse.client.hud.HudManager;
import dev.eclipse.client.screen.HudEditorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class EclipseClient implements ClientModInitializer {
    public static final String MOD_ID = "eclipse_client";
    private static KeyMapping openEditor;
    private static boolean sprintLatch;
    private static KeyMapping zoomKey;
    private static int savedFov = -1;

    @Override
    public void onInitializeClient() {
        EclipseConfig.load();
        HudManager.register();

        openEditor = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.eclipse_client.editor", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"))));

        zoomKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.eclipse_client.zoom", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C,
                KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"))));

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            // smooth hold-to-zoom by easing the FOV option
            var fov = mc.options.fov();
            boolean zooming = zoomKey.isDown() && mc.screen == null;
            if (zooming) {
                if (savedFov < 0) savedFov = fov.get();
                int target = EclipseConfig.get().zoomFov, cur = fov.get();
                if (cur != target) fov.set(cur + Math.max(-12, Math.min(12, target - cur)));
            } else if (savedFov >= 0) {
                int cur = fov.get();
                if (cur >= savedFov) { fov.set(savedFov); savedFov = -1; }
                else fov.set(Math.min(savedFov, cur + 12));
            }
            if (openEditor.consumeClick() && mc.screen == null) mc.setScreen(new HudEditorScreen(null));
            CpsHud.tick(mc);
            FpsGuard.tick(mc);
            String bm = EclipseConfig.get().bobMode;
            if (!bm.equals("vanilla")) mc.options.bobView().set(bm.equals("on"));
            if (EclipseConfig.get().toggleSprint && mc.player != null && mc.screen == null) {
                if (mc.options.keySprint.isDown()) { if (!sprintLatch) { sprintLatch = true; } }
                else if (sprintLatch) { sprintLatch = false; mc.options.toggleSprint().set(true); }
            }
        });

        ScreenEvents.AFTER_INIT.register((mc, screen, w, h) -> {
            if (screen instanceof PauseScreen) {
                Screens.getWidgets(screen).add(Button.builder(Component.literal("Eclipse HUD"),
                        b -> mc.setScreen(new HudEditorScreen(screen))).bounds(6, 6, 80, 20).build());
            }
        });
    }
}
