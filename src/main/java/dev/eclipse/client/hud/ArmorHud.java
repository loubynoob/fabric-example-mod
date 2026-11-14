package dev.eclipse.client.hud;

import dev.eclipse.client.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

/** Armor + held tool/weapon durability HUD. */
public class ArmorHud extends HudModule {
    private static final EquipmentSlot[] SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public ArmorHud() { super("armor", "Armor", "PvP", 0.97f, 0.55f); }

    @Override
    public List<Option> options() {
        return List.of(
                new Option("orient", "Layout", new String[]{"vertical", "horizontal"}),
                new Option("mode", "Number", new String[]{"left", "damage", "percent", "none"}),
                new Option("bar", "Vanilla bar", new String[]{"off", "on"}),
                new Option("held", "Held item row", new String[]{"on", "off"}),
                new Option("grow", "Move when holding tool", new String[]{"up", "down"}),
                new Option("numcolor", "Number color", new String[]{"auto", "custom"}),
                new Option("hotbar", "Match hotbar bars", new String[]{"off", "on"}));
    }

    private List<ItemStack> stacks(Minecraft mc, boolean preview) {
        List<ItemStack> out = new ArrayList<>();
        if (preview || mc.player == null) {
            out.add(damaged(Items.DIAMOND_HELMET, 40));
            out.add(damaged(Items.DIAMOND_CHESTPLATE, 60));
            out.add(damaged(Items.DIAMOND_LEGGINGS, 30));
            out.add(damaged(Items.DIAMOND_BOOTS, 80));
            if (optOn("held", true)) out.add(damaged(Items.DIAMOND_SWORD, 150));
            return out;
        }
        for (EquipmentSlot e : SLOTS) out.add(mc.player.getItemBySlot(e));
        if (optOn("held", true) && isToolOrWeapon(mc.player.getMainHandItem()))
            out.add(mc.player.getMainHandItem());
        return out;
    }

    private static ItemStack damaged(net.minecraft.world.item.Item item, int dmg) {
        ItemStack st = new ItemStack(item);
        st.setDamageValue(dmg);
        return st;
    }

    private static boolean isToolOrWeapon(ItemStack st) {
        return !st.isEmpty() && (st.has(DataComponents.TOOL) || st.has(DataComponents.WEAPON)) && st.isDamageableItem();
    }

    private boolean holdingTool(Minecraft mc, boolean preview) {
        return preview || (mc.player != null && isToolOrWeapon(mc.player.getMainHandItem()));
    }

    private String number(ItemStack st) {
        if (!st.isDamageableItem()) return "";
        int max = st.getMaxDamage(), left = max - st.getDamageValue();
        return switch (opt("mode", "left")) {
            case "damage" -> String.valueOf(st.getDamageValue());
            case "percent" -> Math.round(left * 100f / max) + "%";
            case "none" -> "";
            default -> String.valueOf(left);
        };
    }

    private int numColor(ItemStack st) {
        if (opt("numcolor", "auto").equals("custom")) return ColorUtil.parse(opt("numColorHex", s.textColor), 0xFFFFFFFF);
        float f = st.isDamageableItem() ? (st.getMaxDamage() - st.getDamageValue()) / (float) st.getMaxDamage() : 1f;
        return ColorUtil.healthColor(f);
    }

    @Override
    public int contentWidth(Minecraft mc, boolean preview) {
        List<ItemStack> l = stacks(mc, preview);
        if (opt("orient", "vertical").equals("horizontal")) return Math.max(1, l.size()) * 36 + 4;
        int w = 0;
        for (ItemStack st : l) w = Math.max(w, 22 + mc.font.width(number(st)));
        return w + 6;
    }

    @Override
    public int contentHeight(Minecraft mc, boolean preview) {
        List<ItemStack> l = stacks(mc, preview);
        if (opt("orient", "vertical").equals("horizontal")) return 30;
        return Math.max(1, l.size()) * 18 + 4;
    }

    /** Held-tool row appears at the end of the list; "up" makes the stack grow upward so armor shifts up. */
    @Override
    protected int yShift(Minecraft mc, boolean preview) {
        if (!holdingTool(mc, preview) || !optOn("held", true)) return 0;
        if (opt("orient", "vertical").equals("horizontal")) return 0;
        return opt("grow", "up").equals("up") ? -18 : 0;
    }

    @Override
    protected void drawContent(GuiGraphicsExtractor g, Minecraft mc, boolean preview) {
        List<ItemStack> l = stacks(mc, preview);
        int w = contentWidth(mc, preview), h = contentHeight(mc, preview);
        if (s.background) g.fill(0, 0, w, h, bgColor());
        boolean horiz = opt("orient", "vertical").equals("horizontal");
        for (int i = 0; i < l.size(); i++) {
            ItemStack st = l.get(i);
            int x = horiz ? 2 + i * 36 : 3;
            int y = horiz ? 2 : 2 + i * 18;
            if (st.isEmpty()) continue;
            g.item(st, x, y);
            if (optOn("bar", false)) g.itemDecorations(mc.font, st, x, y);
            String n = number(st);
            if (!n.isEmpty()) {
                if (horiz) g.text(mc.font, n, x + 17 - mc.font.width(n) / 2 + 0, y + 18, numColor(st), s.shadow);
                else g.text(mc.font, n, x + 20, y + 4, numColor(st), s.shadow);
            }
        }
    }
}
