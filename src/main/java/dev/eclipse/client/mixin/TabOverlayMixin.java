package dev.eclipse.client.mixin;

import dev.eclipse.client.config.EclipseConfig;
import dev.eclipse.client.util.Branding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Eclipse logo next to YOUR name in the tab list (client-side only, others can't see it). */
@Mixin(targets = "net.minecraft.client.gui.components.PlayerTabOverlay")
public abstract class TabOverlayMixin {
    @Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true, require = 0)
    private void eclipse$logo(PlayerInfo info, CallbackInfoReturnable<Component> cir) {
        if (!EclipseConfig.get().selfLogo) return;
        Minecraft mc = Minecraft.getInstance();
        Component name = cir.getReturnValue();
        if (mc.player != null && name != null && name.getString().contains(mc.player.getName().getString()))
            cir.setReturnValue(Branding.withLogo(name));
    }
}
