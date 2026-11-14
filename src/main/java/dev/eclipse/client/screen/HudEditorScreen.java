package dev.eclipse.client.screen;

import dev.eclipse.client.config.EclipseConfig;
import dev.eclipse.client.hud.HudManager;
import dev.eclipse.client.hud.HudModule;
import dev.eclipse.client.util.ColorUtil;
import dev.eclipse.client.util.GlassUi;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

/** Drag HUDs anywhere, select one to configure. Works from the title screen too (preview mode). */
public class HudEditorScreen extends Screen {
    private final Screen parent;
    private HudModule selected, dragging;
    private int grabX, grabY;
    private EditBox textColor, bgColor, numColor;

    public HudEditorScreen(Screen parent) {
        super(Component.literal("Eclipse HUD"));
        this.parent = parent;
        selected = HudManager.MODULES.get(0);
    }

    @Override
    protected void init() {
        clearWidgets();
        int px = width - 170;
        int y = 28;
        addRenderableWidget(Button.builder(Component.literal("Enabled: " + (selected.s.enabled ? "ON" : "OFF")), b -> {
            selected.s.enabled = !selected.s.enabled; init();
        }).bounds(px, y, 160, 18).build()); y += 22;
        addRenderableWidget(Button.builder(Component.literal("-"), b -> { selected.s.scale = Math.max(0.5f, selected.s.scale - 0.1f); }).bounds(px, y, 30, 18).build());
        addRenderableWidget(Button.builder(Component.literal("+"), b -> { selected.s.scale = Math.min(3f, selected.s.scale + 0.1f); }).bounds(px + 130, y, 30, 18).build());
        y += 22;
        textColor = new EditBox(font, px, y, 160, 16, Component.literal("Text #hex"));
        textColor.setMaxLength(9); textColor.setValue(selected.s.textColor);
        textColor.setResponder(v -> { if (ColorUtil.isValid(v)) selected.s.textColor = v; });
        addRenderableWidget(textColor); y += 20;
        bgColor = new EditBox(font, px, y, 160, 16, Component.literal("Background #hex"));
        bgColor.setMaxLength(9); bgColor.setValue(selected.s.bgColor);
        bgColor.setResponder(v -> { if (ColorUtil.isValid(v)) selected.s.bgColor = v; });
        addRenderableWidget(bgColor); y += 20;
        addRenderableWidget(Button.builder(Component.literal("Background: " + (selected.s.background ? "ON" : "OFF")), b -> {
            selected.s.background = !selected.s.background; init();
        }).bounds(px, y, 160, 18).build()); y += 22;
        addRenderableWidget(Button.builder(Component.literal("Shadow: " + (selected.s.shadow ? "ON" : "OFF")), b -> {
            selected.s.shadow = !selected.s.shadow; init();
        }).bounds(px, y, 160, 18).build()); y += 22;
        for (HudModule.Option o : selected.options()) {
            final HudModule.Option opt = o;
            String cur = selected.opt(o.key(), o.choices()[0]);
            addRenderableWidget(Button.builder(Component.literal(o.label() + ": " + cur), b -> {
                String c = selected.opt(opt.key(), opt.choices()[0]);
                int i = 0;
                for (int k = 0; k < opt.choices().length; k++) if (opt.choices()[k].equals(c)) i = k;
                selected.s.extra.put(opt.key(), opt.choices()[(i + 1) % opt.choices().length]);
                init();
            }).bounds(px, y, 160, 18).build()); y += 20;
        }
        if (selected.id.equals("armor")) {
            numColor = new EditBox(font, px, y, 160, 16, Component.literal("Number #hex"));
            numColor.setMaxLength(9); numColor.setValue(selected.opt("numColorHex", "#FFFFFF"));
            numColor.setResponder(v -> { if (ColorUtil.isValid(v)) selected.s.extra.put("numColorHex", v); });
            addRenderableWidget(numColor); y += 20;
        }
        // module list (left)
        int ly = 28;
        for (HudModule m : HudManager.MODULES) {
            final HudModule mod = m;
            addRenderableWidget(Button.builder(Component.literal(m.name), b -> { selected = mod; init(); })
                    .bounds(8, ly, 90, 18).build());
            ly += 20;
        }
        EclipseConfig c = EclipseConfig.get();
        addRenderableWidget(Button.builder(Component.literal("FPS Guard: " + (c.fpsGuard ? "ON" : "OFF")), b -> { c.fpsGuard = !c.fpsGuard; init(); })
                .bounds(8, height - 66, 120, 18).build());
        addRenderableWidget(Button.builder(Component.literal("Toggle Sprint: " + (c.toggleSprint ? "ON" : "OFF")), b -> { c.toggleSprint = !c.toggleSprint; init(); })
                .bounds(8, height - 44, 120, 18).build());
        addRenderableWidget(Button.builder(Component.literal("Zoom FOV: " + c.zoomFov), b -> {
            c.zoomFov = c.zoomFov >= 60 ? 30 : c.zoomFov + 10; init();
        }).bounds(8, height - 176, 120, 18).build());
        addRenderableWidget(Button.builder(Component.literal("Sky..."), b -> minecraft.setScreen(new SkyScreen(this)))
                .bounds(8, height - 154, 120, 18).build());
        addRenderableWidget(Button.builder(Component.literal("View bobbing: " + c.bobMode), b -> {
            c.bobMode = c.bobMode.equals("vanilla") ? "on" : c.bobMode.equals("on") ? "off" : "vanilla"; init();
        }).bounds(8, height - 132, 120, 18).build());
        addRenderableWidget(Button.builder(Component.literal("Bright inv: " + (c.brightInventory ? "ON" : "OFF")), b -> { c.brightInventory = !c.brightInventory; init(); })
                .bounds(8, height - 110, 120, 18).build());
        addRenderableWidget(Button.builder(Component.literal("Name logo: " + (c.selfLogo ? "ON" : "OFF")), b -> { c.selfLogo = !c.selfLogo; init(); })
                .bounds(8, height - 88, 120, 18).build());
        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
                .bounds(width / 2 - 40, height - 28, 80, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        g.fill(0, 0, width, height, 0x90140A28);
        GlassUi.panel(g, width - 176, 20, 172, height - 40);
        GlassUi.panel(g, 4, 20, 98, HudManager.MODULES.size() * 20 + 14);
        var mc = minecraft;
        for (HudModule m : HudManager.MODULES) {
            m.render(g, mc, true);
            if (m == selected) g.outline(m.lastX - 1, m.lastY - 1, m.lastW + 2, m.lastH + 2, GlassUi.PURPLE);
        }
        g.text(font, "Eclipse HUD Editor - drag to move", width / 2 - 80, 8, 0xFFE9D5FF, true);
        super.extractRenderState(g, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent e, boolean dbl) {
        if (super.mouseClicked(e, dbl)) return true;
        for (HudModule m : HudManager.MODULES) {
            if (e.x() >= m.lastX && e.x() <= m.lastX + m.lastW && e.y() >= m.lastY && e.y() <= m.lastY + m.lastH) {
                dragging = m; grabX = (int) e.x() - m.lastX; grabY = (int) e.y() - m.lastY;
                if (selected != m) { selected = m; init(); }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent e, double dx, double dy) {
        if (dragging != null) {
            dragging.moveTo(minecraft, (int) e.x() - grabX, (int) e.y() - grabY);
            return true;
        }
        return super.mouseDragged(e, dx, dy);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent e) {
        dragging = null;
        return super.mouseReleased(e);
    }

    @Override
    public void onClose() {
        EclipseConfig.save();
        minecraft.setScreen(parent);
    }
}
