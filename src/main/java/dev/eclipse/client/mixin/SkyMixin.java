package dev.eclipse.client.mixin;

import dev.eclipse.client.config.EclipseConfig;
import dev.eclipse.client.sky.SkyPresets;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Tints the sky/fog colour with a chosen preset. Handles both int-ARGB and Vec3 return types. */
@Mixin(targets = "net.minecraft.client.multiplayer.ClientLevel")
public abstract class SkyMixin {
    @Inject(method = "getSkyColor", at = @At("RETURN"), cancellable = true, require = 0)
    private void eclipse$sky(Vec3 pos, float pt, CallbackInfoReturnable<Object> cir) {
        int idx = EclipseConfig.get().skyPreset;
        if (idx <= 0 || idx >= SkyPresets.ALL.length) return;
        SkyPresets.Sky sky = SkyPresets.ALL[idx];
        Object orig = cir.getReturnValue();
        double lum;
        if (orig instanceof Integer i) {
            lum = (((i >> 16) & 255) * 0.3 + ((i >> 8) & 255) * 0.59 + (i & 255) * 0.11) / 255.0;
        } else if (orig instanceof Vec3 v) {
            lum = v.x * 0.3 + v.y * 0.59 + v.z * 0.11;
        } else return;
        double k = sky.fixed() ? 1.0 : Math.max(0.12, Math.min(1.0, lum / 0.55));
        double r = ((sky.rgb() >> 16) & 255) / 255.0 * k, g = ((sky.rgb() >> 8) & 255) / 255.0 * k, b = (sky.rgb() & 255) / 255.0 * k;
        if (orig instanceof Integer) {
            cir.setReturnValue(0xFF000000 | ((int) (r * 255) << 16) | ((int) (g * 255) << 8) | (int) (b * 255));
        } else {
            cir.setReturnValue(new Vec3(r, g, b));
        }
    }
}
