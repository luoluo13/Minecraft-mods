package top.nefeli.minecraft.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.ArrowInfiniteEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class StiffnessEnchantment extends Enchantment {
    public StiffnessEnchantment() {
        super(
                Rarity.RARE,
                EnchantmentCategory.BOW,
                new EquipmentSlot[] { EquipmentSlot.MAINHAND }
        );
    }

    @Override
    public int getMaxLevel() {
        return super.getMaxLevel();
    }

    // 禁止与其他弓附魔共存
    @Override
    protected boolean checkCompatibility(@NotNull Enchantment other) {
        return !(other instanceof ArrowInfiniteEnchantment) &&
                super.checkCompatibility(other);
    }

    // 设置附魔权重（出现概率）
    @Override
    public int getMinCost(int level) {
        return 30;
    }

    @Override
    public int getMaxCost(int level) {
        return 60;
    }
}