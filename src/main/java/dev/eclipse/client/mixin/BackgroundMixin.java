package dev.eclipse.client.mixin;

import dev.eclipse.client.config.EclipseConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Brighter, lightly purple-tinted backdrop behind inventories instead of the heavy dark one. */
@Mixin(Screen.class)
public abstract class BackgroundMixin {
    @Inject(method = {"extractBackground", "extractTransparentBackground"}, at = @At("HEAD"), cancellable = true, require = 0)
    private void eclipse$bright(GuiGraphicsExtractor g, int mx, int my, float delta, CallbackInfo ci) {
        if (!EclipseConfig.get().brightInventory) return;
        Screen self = (Screen) (Object) this;
        if (!(self instanceof AbstractContainerScreen<?>) || Minecraft.getInstance().level == null) return;
        g.fillGradient(0, 0, self.width, self.height, 0x30C4B5FD, 0x55312E81);
        ci.cancel();
    }
}
