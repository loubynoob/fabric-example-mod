package dev.eclipse.client.fps;

import dev.eclipse.client.config.EclipseConfig;
import net.minecraft.client.Minecraft;

/** Lowers render distance when average FPS is under target, restores it when there is headroom. */
public final class FpsGuard {
    private FpsGuard() {}

    private static int ticks, sum, samples;
    private static int original = -1;

    public static void tick(Minecraft mc) {
        EclipseConfig c = EclipseConfig.get();
        if (!c.fpsGuard || mc.level == null) return;
        sum += mc.getFps(); samples++;
        if (++ticks < 100) return; // ~5 s
        int avg = sum / Math.max(1, samples);
        ticks = sum = samples = 0;
        int rd = mc.options.renderDistance().get();
        if (avg < c.fpsTarget && rd > 4) {
            if (original < 0) original = rd;
            mc.options.renderDistance().set(rd - 1);
        } else if (avg > c.fpsTarget + 25 && original > 0 && rd < original) {
            mc.options.renderDistance().set(rd + 1);
        }
    }
}
