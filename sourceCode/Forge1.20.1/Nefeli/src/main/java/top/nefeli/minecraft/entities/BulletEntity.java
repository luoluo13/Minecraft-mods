package top.nefeli.minecraft.entities;


import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import top.nefeli.minecraft.ModEntities;

public class BulletEntity extends ThrowableItemProjectile {
    // 伤害值
    private float damage = 5.0F;

    // 用于实体注册的构造方法
    public BulletEntity(EntityType<? extends BulletEntity> type, Level world) {
        super(type, world);
    }

    // 用于从世界创建的构造方法
    public BulletEntity(Level world, LivingEntity shooter) {
        super(ModEntities.BULLET.get(), shooter, world);
    }

    // 用于网络同步的构造方法
    public BulletEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(ModEntities.BULLET.get(), world);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return Items.AIR; // 子弹本身不显示物品
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            // 击中后造成伤害等效果
//            if (result.getType() == HitResult.Type.ENTITY) {
                // 对击中的实体造成伤害
//                 ((HitResult.Entity)result).getEntity().hurt(...);
//            }
            this.discard(); // 移除子弹实体
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        Entity owner = this.getOwner();

        // 造成伤害
        if (target instanceof LivingEntity livingTarget) {
            // 获取 DamageSources
            DamageSources damageSources = this.level().damageSources();

            // 创建伤害来源
            DamageSource damageSource;

            if (owner instanceof LivingEntity livingOwner) {
                damageSource = damageSources.mobProjectile(this, livingOwner);
            } else {
                damageSource = damageSources.thrown(this, owner);
            }

            // 造成伤害
            livingTarget.hurt(damageSource, this.damage);

        }

        // 击退效果
        if (owner instanceof LivingEntity) {
            if (target instanceof LivingEntity) {
                ((LivingEntity)target).knockback(
                        0.5f, // 击退力度
                        owner.getX() - target.getX(),
                        owner.getZ() - target.getZ()
                );
            }
        }
    }

    // 设置子弹速度
    public void shoot(Vec3 direction, float velocity, float inaccuracy) {
        super.shoot(direction.x, direction.y, direction.z, velocity, inaccuracy);
    }

    // 设置伤害值
    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }
}
