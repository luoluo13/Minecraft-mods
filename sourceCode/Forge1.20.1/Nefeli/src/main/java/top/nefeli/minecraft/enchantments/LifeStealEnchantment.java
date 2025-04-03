package top.nefeli.minecraft.enchantments;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;

public class LifeStealEnchantment extends Enchantment {
    public LifeStealEnchantment() {
        super(
                Rarity.RARE,    //附魔稀有度
                EnchantmentCategory.WEAPON, //附魔种类
                new EquipmentSlot[] { EquipmentSlot.MAINHAND }  //有效槽位
        );
    }

    @Override
    public int getMaxLevel() {
        return 3; // 最高 3 级
    }

    @Override
    public int getMinCost(int level) {
        return 15 + (level - 1) * 10; // 附魔台最低等级需求
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 30;
    }

    // 吸血逻辑
    @Override
    public void doPostAttack(LivingEntity attacker, @NotNull Entity target, int level) {
        if (!attacker.level().isClientSide() && target instanceof LivingEntity livingTarget) {

            // 计算吸血量（基础 1 心 + 每级 0.5 心）
            float healAmount = 2.0f + (level - 1) * 1.0f; // 1 心 = 2.0f HP

            // 恢复生命值（但不能超过最大生命值）
            attacker.heal(healAmount);

            // 可选：播放粒子效果或音效
             attacker.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5f, 1.0f);
        }
        if (attacker.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.HEART,
                    attacker.getX(), attacker.getY() + 1.0, attacker.getZ(),
                    5, // 粒子数量
                    0.5, 0.5, 0.5, // 偏移量
                    0.1 // 速度
            );
        }
        super.doPostAttack(attacker, target, level);
    }

    // 禁止与“火焰附加”冲突
    @Override
    public boolean checkCompatibility(@NotNull Enchantment other) {
        return super.checkCompatibility(other) && other != Enchantments.FIRE_ASPECT;
    }
}
