package dev.eclipse.client.mixin;

import dev.eclipse.client.config.EclipseConfig;
import dev.eclipse.client.util.Branding;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Eclipse logo in front of your own name tag (client-side only). */
@Mixin(Player.class)
public abstract class PlayerNameMixin {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true, require = 0)
    private void eclipse$logo(CallbackInfoReturnable<Component> cir) {
        if (!EclipseConfig.get().selfLogo) return;
        if ((Object) this == Minecraft.getInstance().player && cir.getReturnValue() != null)
            cir.setReturnValue(Branding.withLogo(cir.getReturnValue()));
    }
}
