package top.nefeli.minecraft.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.nefeli.minecraft.entities.BulletEntity;

public class GunItem extends Item {
    public GunItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!world.isClientSide) { // 确保只在服务端执行
            BulletEntity bullet = new BulletEntity(world, player);

            // 计算射击方向
            Vec3 look = player.getLookAngle();

            // 设置子弹位置（从玩家眼睛位置发射）
            bullet.setPos(
                    player.getX() + look.x * 1.5,
                    player.getEyeY() - 0.1,
                    player.getZ() + look.z * 1.5
            );

            // 设置子弹速度和伤害
            bullet.shoot(look, 3.0F, 1.0F); // 速度3.0，精度1.0
            bullet.setDamage(5.0F); // 设置伤害值

            world.addFreshEntity(bullet);

            // 播放射击音效（可选）
            player.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.0F);
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
