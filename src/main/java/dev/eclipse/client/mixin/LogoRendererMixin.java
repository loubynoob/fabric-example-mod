package dev.eclipse.client.mixin;

import dev.eclipse.client.config.EclipseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Hide the vanilla MINECRAFT logo on the Eclipse title screen. Method names are guesses; harmless if they don't match. */
@Mixin(targets = "net.minecraft.client.gui.components.LogoRenderer")
public abstract class LogoRendererMixin {
    @Inject(method = {"extractRenderState", "renderLogo", "extractLogo"}, at = @At("HEAD"), cancellable = true, require = 0)
    private void eclipse$hide(CallbackInfo ci) {
        if (EclipseConfig.get().eclipseTitle) ci.cancel();
    }
}
