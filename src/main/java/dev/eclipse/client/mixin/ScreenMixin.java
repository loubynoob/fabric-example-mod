package dev.eclipse.client.mixin;

import dev.eclipse.client.util.Branding;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    /** Eclipse branding in the bottom corners of every GUI. */
    @Inject(method = "extractRenderState", at = @At("TAIL"), require = 0)
    private void eclipse$branding(GuiGraphicsExtractor g, int mx, int my, float delta, CallbackInfo ci) {
        Screen self = (Screen) (Object) this;
        Branding.draw(g, self.width, self.height);
    }
}
