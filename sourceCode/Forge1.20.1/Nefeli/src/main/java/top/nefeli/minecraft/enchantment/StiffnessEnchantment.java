package top.nefeli.minecraft.enchantment;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.ArrowInfiniteEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

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
        return 1;
    }

    // 禁止与其他弓附魔共存
    @Override
    protected boolean checkCompatibility(Enchantment other) {
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