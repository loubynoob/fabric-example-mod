package dev.eclipse.client.util;

import com.google.gson.JsonParser;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.serialization.JsonOps;
import dev.eclipse.client.EclipseClient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;

public final class Branding {
    private Branding() {}

    public static final Identifier LOGO16 = Identifier.fromNamespaceAndPath(EclipseClient.MOD_ID, "textures/gui/logo16.png");
    public static final Identifier LOGO64 = Identifier.fromNamespaceAndPath(EclipseClient.MOD_ID, "textures/gui/logo64.png");

    private static String versionLine;
    private static Component logoInline;

    /** Bottom-right "logo + Eclipse Client", bottom-left "Minecraft x | Fabric y". */
    public static void draw(GuiGraphicsExtractor g, int w, int h) {
        Minecraft mc = Minecraft.getInstance();
        String name = "ECLIPSE CLIENT";
        Component t = Component.literal(name).withStyle(net.minecraft.ChatFormatting.BOLD);
        int tw = mc.font.width(t);
        int x = w - tw - 6, y = h - 12;
        g.text(mc.font, t, x, y, 0xFFE9D5FF, true);
        g.blit(RenderPipelines.GUI_TEXTURED, LOGO16, x - 18, y - 4, 0f, 0f, 16, 16, 16, 16);
        g.text(mc.font, versionLine(), 5, h - 12, 0xB0E9D5FF, true);
    }

    private static String versionLine() {
        if (versionLine == null) {
            FabricLoader fl = FabricLoader.getInstance();
            String mcv = fl.getModContainer("minecraft").map(c -> c.getMetadata().getVersion().getFriendlyString()).orElse("?");
            String fv = fl.getModContainer("fabricloader").map(c -> c.getMetadata().getVersion().getFriendlyString()).orElse("?");
            versionLine = "Minecraft " + mcv + "  |  Fabric Loader " + fv;
        }
        return versionLine;
    }

    /** Inline logo glyph (custom font) built from JSON so we avoid Style/font API changes. */
    public static Component logo() {
        if (logoInline == null) {
            try {
                var json = JsonParser.parseString("{\"text\":\"\\ue000\",\"font\":\"eclipse_client:logo\"}");
                logoInline = ComponentSerialization.CODEC.parse(JsonOps.INSTANCE, json).result().orElse(Component.empty());
            } catch (Throwable t) {
                logoInline = Component.empty();
            }
        }
        return logoInline;
    }

    public static Component withLogo(Component original) {
        return Component.empty().append(logo()).append(Component.literal(" ")).append(original);
    }
}
