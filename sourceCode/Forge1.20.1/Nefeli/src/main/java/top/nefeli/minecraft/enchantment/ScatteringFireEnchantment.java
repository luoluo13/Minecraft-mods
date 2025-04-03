package top.nefeli.minecraft.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ScatteringFireEnchantment extends Enchantment {
    public ScatteringFireEnchantment() {
        super(
                Rarity.RARE,
                EnchantmentCategory.BOW,
                new EquipmentSlot[] { EquipmentSlot.MAINHAND }
        );
    }

    @Override
    public int getMaxLevel() {
        return 20; // 最大等级为 III
    }

    // 设置附魔仅对弓生效
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof BowItem;
    }

    // 避免与“无限”附魔冲突
//    @Override
//    public boolean checkCompatibility(Enchantment other) {
//        return !(other instanceof Enchantment.);
//    }
}