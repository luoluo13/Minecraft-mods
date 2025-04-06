package top.nefeli.minecraft.nefeli_gun.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class BulletItem extends Item {
    public BulletItem() {
        super(new Properties().stacksTo(64));
    }

    // 从NBT获取子弹属性
    public static Float getCaliber(ItemStack stack) {
        return stack.getOrCreateTag().getFloat("Caliber");
    }
    public static String getCoreMaterial(ItemStack stack) {
        return stack.getOrCreateTag().getString("CoreMaterial");
    }
    public static String getCasingMaterial(ItemStack stack) {
        return stack.getOrCreateTag().getString("CasingMaterial");
    }
    public static String getPrimer(ItemStack stack) {
        return stack.getOrCreateTag().getString("Primer");
    }
    public static int getPowderCharge(ItemStack stack) {
        return stack.getOrCreateTag().getInt("Powder");
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        stack.getOrCreateTag().putString("CoreMaterial", "铜制");
        stack.getOrCreateTag().putString("CasingMaterial", "铜制");
        stack.getOrCreateTag().putString("Primer", "基础");
        stack.getOrCreateTag().putFloat("Caliber", 7.62F);
        stack.getOrCreateTag().putInt("Powder", 1);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        // 从NBT中获取数据
        String caliber = getCaliber(stack)+"mm";
        // 添加 Lore 描述（灰色斜体）
        tooltip.add(Component.translatable("lore.nefeli_gun.bullet.core", caliber.toLowerCase(), getCoreMaterial(stack))
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.translatable("lore.nefeli_gun.bullet.casing", caliber.toLowerCase(), getCasingMaterial(stack))
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.translatable("lore.nefeli_gun.bullet.primer",getPrimer(stack))
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.translatable("lore.nefeli_gun.bullet.power",getPowderCharge(stack))
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
    @Override
    public Component getName(ItemStack stack) {
        String caliber = getCaliber(stack) + "mm";
        int powder = getPowderCharge(stack);
        return Component.translatable("item.nefeli_gun.bullet", caliber.toUpperCase());
    }
}