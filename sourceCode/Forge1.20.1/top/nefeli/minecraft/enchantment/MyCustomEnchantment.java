package top.nefeli.minecraft.enchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class MyCustomEnchantment extends Enchantment {
    // 附魔的唯一 ID（Forge 会自动分配）
    public MyCustomEnchantment() {
        super(
                Rarity.RARE,                 // 稀有度（COMMON, UNCOMMON, RARE, VERY_RARE）
                EnchantmentCategory.WEAPON,  // 附魔适用的物品类型（WEAPON, ARMOR, DIGGER 等）
                new EquipmentSlot[] { EquipmentSlot.MAINHAND } // 可附魔的装备槽
        );
    }

    @Override
    public int getMaxLevel() {
        return 3; // 最大等级
    }

    @Override
    public int getMinCost(int level) {
        return 10 + (level - 1) * 10; // 附魔台最低等级需求
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 30; // 附魔台最高等级需求
    }

    // 是否与其他附魔冲突（如“锋利”和“亡灵杀手”）
    @Override
    public boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other) && other != Enchantments.SHARPNESS;
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int level) {
        if (!attacker.level().isClientSide()) {
            // 3级附魔有 30% 概率点燃目标
            if (level == 3 && attacker.getRandom().nextFloat() < 1f) {
                target.setSecondsOnFire(3);
            }
        }
        super.doPostAttack(attacker, target, level);
    }
}
