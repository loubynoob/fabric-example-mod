package dev.eclipse.client.mixin;

import dev.eclipse.client.config.EclipseConfig;
import dev.eclipse.client.screen.EclipseTitleScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    /** Swap the vanilla title screen for the Eclipse one. */
    @ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true, require = 0)
    private Screen eclipse$swapTitle(Screen screen) {
        if (EclipseConfig.get().eclipseTitle && screen != null
                && screen.getClass() == TitleScreen.class) {
            return new EclipseTitleScreen();
        }
        return screen;
    }
}
