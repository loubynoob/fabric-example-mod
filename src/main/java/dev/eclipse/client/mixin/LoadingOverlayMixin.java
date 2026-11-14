package dev.eclipse.client.mixin;

import dev.eclipse.client.config.EclipseConfig;
import dev.eclipse.client.util.Branding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Draws the Eclipse loading screen over the vanilla one (logo + animated bar). */
@Mixin(targets = "net.minecraft.client.gui.screens.LoadingOverlay")
public abstract class LoadingOverlayMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"), require = 0)
    private void eclipse$loading(GuiGraphicsExtractor g, int mx, int my, float delta, CallbackInfo ci) {
        if (!EclipseConfig.get().eclipseLoading) return;
        Minecraft mc = Minecraft.getInstance();
        int w = mc.getWindow().getGuiScaledWidth(), h = mc.getWindow().getGuiScaledHeight();
        g.fill(0, 0, w, h, 0xFF0B0716);
        g.blit(RenderPipelines.GUI_TEXTURED, Branding.LOGO64, w / 2 - 32, h / 2 - 50, 0f, 0f, 64, 64, 64, 64);
        int bw = 120, bx = w / 2 - bw / 2, by = h / 2 + 30;
        g.fill(bx - 1, by - 1, bx + bw + 1, by + 5, 0xFF3B1F6B);
        g.fill(bx, by, bx + bw, by + 4, 0xFF150C2B);
        long t = System.currentTimeMillis();
        int seg = 40, pos = (int) ((t / 8) % (bw + seg)) - seg;
        int x0 = Math.max(0, pos), x1 = Math.min(bw, pos + seg);
        if (x1 > x0) g.fill(bx + x0, by, bx + x1, by + 4, 0xFFA78BFA);
        String s = "Loading Eclipse...";
        g.text(mc.font, s, w / 2 - mc.font.width(s) / 2, by + 10, 0xFFE9D5FF, true);
    }
}
