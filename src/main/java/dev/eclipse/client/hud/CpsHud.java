package dev.eclipse.client.hud;

import net.minecraft.client.Minecraft;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class CpsHud extends HudModule {
    private static final Deque<Long> LEFT = new ArrayDeque<>();
    private static final Deque<Long> RIGHT = new ArrayDeque<>();
    private static boolean lastLeft, lastRight;

    public CpsHud() { super("cps", "CPS", "PvP", 0.01f, 0.08f); }

    /** Called every client tick to detect new clicks. */
    public static void tick(Minecraft mc) {
        boolean l = mc.options.keyAttack.isDown();
        boolean r = mc.options.keyUse.isDown();
        long now = System.currentTimeMillis();
        if (l && !lastLeft) LEFT.addLast(now);
        if (r && !lastRight) RIGHT.addLast(now);
        lastLeft = l;
        lastRight = r;
        while (!LEFT.isEmpty() && now - LEFT.peekFirst() > 1000) LEFT.pollFirst();
        while (!RIGHT.isEmpty() && now - RIGHT.peekFirst() > 1000) RIGHT.pollFirst();
    }

    @Override
    public List<Option> options() {
        return List.of(new Option("mode", "Show", new String[]{"both", "left", "right"}));
    }

    @Override
    protected List<String> lines(Minecraft mc, boolean preview) {
        int l = preview ? 8 : LEFT.size();
        int r = preview ? 5 : RIGHT.size();
        return switch (opt("mode", "both")) {
            case "left" -> List.of("CPS: " + l);
            case "right" -> List.of("CPS: " + r);
            default -> List.of("CPS: " + l + " | " + r);
        };
    }
}
