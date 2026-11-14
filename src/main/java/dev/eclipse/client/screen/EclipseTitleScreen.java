package dev.eclipse.client.screen;

import dev.eclipse.client.util.Branding;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import com.mojang.realmsclient.RealmsMainScreen;

/** Eclipse main menu: logo, Singleplayer / Multiplayer / Realms stacked, Options + HUD at the bottom. */
public class EclipseTitleScreen extends TitleScreen {
    public EclipseTitleScreen() { super(); }

    @Override
    protected void init() {
        // deliberately NOT calling super.init(): we build our own layout
        int cx = width / 2, bw = 200, bh = 22;
        int y = height / 2 - 14;
        addRenderableWidget(Button.builder(Component.translatable("menu.singleplayer"),
                b -> minecraft.setScreen(new SelectWorldScreen(this))).bounds(cx - bw / 2, y, bw, bh).build());
        addRenderableWidget(Button.builder(Component.translatable("menu.multiplayer"),
                b -> minecraft.setScreen(new JoinMultiplayerScreen(this))).bounds(cx - bw / 2, y + 26, bw, bh).build());
        addRenderableWidget(Button.builder(Component.translatable("menu.online"),
                b -> minecraft.setScreen(new RealmsMainScreen(this))).bounds(cx - bw / 2, y + 52, bw, bh).build());
        int by = height - 52;
        addRenderableWidget(Button.builder(Component.translatable("menu.options"),
                b -> minecraft.setScreen(new OptionsScreen(this, minecraft.options))).bounds(cx - 101, by, 100, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Eclipse UI / HUD"),
                b -> minecraft.setScreen(new HudEditorScreen(this))).bounds(cx + 1, by, 100, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("menu.quit"),
                b -> minecraft.stop()).bounds(cx - 50, by + 24, 100, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        super.extractRenderState(g, mouseX, mouseY, delta); // panorama + widgets + branding (via Screen mixin)
        int cx = width / 2, y = height / 2 - 14;
        g.blit(RenderPipelines.GUI_TEXTURED, Branding.LOGO64, cx - 32, y - 70, 0f, 0f, 64, 64, 64, 64);
        String t = "E C L I P S E";
        g.text(font, t, cx - font.width(t) / 2, y - 12, 0xFFE9D5FF, true);
    }
}
