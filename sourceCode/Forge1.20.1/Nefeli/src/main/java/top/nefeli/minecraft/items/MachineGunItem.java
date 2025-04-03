package top.nefeli.minecraft.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.nefeli.minecraft.ModItems;
import top.nefeli.minecraft.entities.BulletEntity;

public class MachineGunItem extends Item {
    // 射击冷却时间(ticks)
    private static final int COOLDOWN = 2;
    // 每次射击消耗的弹药
    private static final int AMMO_PER_SHOT = 1;

    public MachineGunItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand); // 必须调用此方法才能触发onUseTick
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level level, @NotNull LivingEntity shooter, @NotNull ItemStack stack, int remainingUseDuration) {
        if (!level.isClientSide && shooter instanceof Player player) {
            // 检查弹药
            if (hasAmmo(player)) {
                // 消耗弹药
                consumeAmmo(player);

                // 发射子弹
                BulletEntity bullet = new BulletEntity(level, shooter);
                Vec3 look = shooter.getLookAngle();

                // 设置子弹位置和方向
                bullet.setPos(
                        shooter.getX() + look.x * 1.5,
                        shooter.getEyeY() - 0.1,
                        shooter.getZ() + look.z * 1.5
                );

                // 添加一些随机散布
                float spread = 0.5f; // 散布角度
                Vec3 spreadVec = look.add(
                        (level.random.nextFloat() - 0.5f) * spread,
                        (level.random.nextFloat() - 0.5f) * spread,
                        (level.random.nextFloat() - 0.5f) * spread
                ).normalize();

                bullet.shoot(spreadVec.x, spreadVec.y, spreadVec.z, 4.0f, 0.5f);
                bullet.setDamage(4.0f);
                level.addFreshEntity(bullet);
//                player.sendSystemMessage(Component.literal("我射射射射射"));

                // 播放射击音效
                level.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(),
                        SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 0.5f, 1.5f);

                // 添加后坐力
                if (shooter instanceof Player) {
                    Vec3 recoil = look.reverse().scale(0.05);
                    shooter.push(recoil.x, recoil.y, recoil.z);
                }

                // 设置冷却
                player.getCooldowns().addCooldown(this, COOLDOWN);
            } else {
                // 没有弹药时停止使用
                shooter.stopUsingItem();
            }
        }
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000; // 最大使用时间
    }

    // 检查玩家是否有弹药
    private boolean hasAmmo(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == ModItems.BULLET.get()) {
                return true;
            }
        }
//        player.sendSystemMessage(Component.literal("你没子弹了！"));
        return false;
    }

    // 消耗弹药
    private void consumeAmmo(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() == ModItems.BULLET.get()) {
                stack.shrink(AMMO_PER_SHOT);
                if (stack.isEmpty()) {
                    player.getInventory().setItem(i, ItemStack.EMPTY);
                }
                break;
            }
        }
    }
}
