package top.nefeli.minecraft.enchantment;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.nefeli.minecraft.ModEnchantments;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static top.nefeli.minecraft.ExampleMod.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class EnchantmentHandler {
    private static final Set<AbstractArrow> trackedArrows = ConcurrentHashMap.newKeySet();

    @SubscribeEvent
    public static void onArrowShot(EntityJoinLevelEvent event) {
        // 检查实体是否是箭矢
        if (event.getEntity() instanceof Arrow arrow) {
            // 检查箭矢的发射者是否是玩家
            if (arrow.getOwner() instanceof Player player) {
                ItemStack bow = player.getMainHandItem();
                int level = bow.getEnchantmentLevel(ModEnchantments.STIFFNESS.get());
                if (level > 0) {
                    arrow.getPersistentData().putBoolean("StiffnessEnchanted", true);
                    // 设置物理参数
                    arrow.setNoGravity(true);
//                    arrow.setNoPhysics(true);
//                    arrow.setPierceLevel((byte) 999); // 防止碰撞影响
//                    player.sendSystemMessage(Component.literal("你射出了一支附魔箭！"));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.level().isClientSide || event.phase != TickEvent.Phase.END) return;

        ItemStack heldItem = player.getMainHandItem();
        int rapidEnchantLevel = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.RAPID_FIRE.get(), heldItem);
        int scatteringEnchantLevel = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.SCATTERING_FIRE.get(), heldItem);

        if (rapidEnchantLevel > 0 && player.isUsingItem() && heldItem.getItem() instanceof BowItem) {
            handleRapidFire(player, heldItem);
        }
    }

    private static final Map<UUID, Integer> SHOOT_COOLDOWN = new HashMap<>();

    private void handleRapidFire(Player player, ItemStack heldItem) {
        UUID playerId = player.getUUID();
        int cooldown = SHOOT_COOLDOWN.getOrDefault(playerId, 0);
        int rapid = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.RAPID_FIRE.get(), heldItem);

        if (cooldown <= 0) {
            shootArrow(player, heldItem);
            SHOOT_COOLDOWN.put(playerId, 20/rapid); // 20 ticks = 1秒
        } else {
            SHOOT_COOLDOWN.put(playerId, cooldown - 1);
        }
    }

    private void shootArrow(Player player) {
        shootArrow(player, player.getMainHandItem());
    }
    private void shootArrow(Player player, float speed) {
        shootArrow(player, player.getMainHandItem(), speed);
    }
    private void shootArrow(Player player, ItemStack heldItem) {
        shootArrow(player, player.getMainHandItem(), 3.0f);
    }
    private void shootArrow(Player player, ItemStack heldItem, float speed) {

        int scattering = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.SCATTERING_FIRE.get(), heldItem);
        if (scattering == 0) {
            Arrow arrow = new Arrow(player.level(), player);
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, speed, 0.0F);
            player.level().addFreshEntity(arrow);
        }else {
            for (int i = 0; i < scattering; i++) {
                Arrow arrow = new Arrow(player.level(), player);
                arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, speed, 5.0F);
//                arrow.setNoGravity(true);
                player.level().addFreshEntity(arrow);
            }
        }

        // 播放音效或粒子效果（可选）
    }

    @SubscribeEvent
    public void onArrowLaunch(ArrowLooseEvent event) {
        ItemStack bow = event.getBow();
        int level = bow.getEnchantmentLevel(ModEnchantments.SCATTERING_FIRE.get());

        if (level > 0) {
            shootArrow(event.getEntity());
        }
    }

//    @SubscribeEvent
//    public static void onArrowTick(TickEvent.LevelTickEvent event) {
//        if (event.phase == TickEvent.Phase.START || event.level.isClientSide()) return;
//
//        for (AbstractArrow arrow : event.level.getEntitiesOfClass(AbstractArrow.class,
//                new AABB(event.level.getSharedSpawnPos()).inflate(100))) {
//            CompoundTag data = arrow.getPersistentData();
//
//            // 初始化标记（只在第一次tick时执行）
//            if (!data.contains("StiffnessInitialized")) {
//                if (data.getCompound("StiffnessEnchanted").getBoolean("active")) {
//                    // 记录初始运动向量
//                    Vec3 initialMotion = arrow.getDeltaMovement();
//                    CompoundTag motionData = new CompoundTag();
//                    motionData.putDouble("mX", initialMotion.x);
//                    motionData.putDouble("mY", initialMotion.y);
//                    motionData.putDouble("mZ", initialMotion.z);
//                    data.put("StiffnessMotion", motionData);
//
//                    // 设置物理参数
//                    arrow.setNoGravity(true);
//                    arrow.setNoPhysics(true);
//                    arrow.setPierceLevel((byte) 999); // 防止碰撞影响
//
//                    data.putBoolean("StiffnessInitialized", true); // 标记初始化完成
//                }
//                continue;
//            }
//
//            // 后续tick保持运动状态
//            if (data.contains("StiffnessMotion")) {
//                CompoundTag motionData = data.getCompound("StiffnessMotion");
//                Vec3 preservedMotion = new Vec3(
//                        motionData.getDouble("mX"),
//                        motionData.getDouble("mY"),
//                        motionData.getDouble("mZ")
//                );
//
//                // 保持初始运动向量
//                arrow.setDeltaMovement(preservedMotion);
//
//                // 锁定旋转角度（保持发射时的原始角度）
//                arrow.setXRot(arrow.xRotO);
//                arrow.setYRot(arrow.yRotO);
//            }
//        }
//    }

    // 处理箭矢击中目标后的清理
    @SubscribeEvent
    public static void onArrowHit(ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof AbstractArrow arrow) {
            arrow.getPersistentData().remove("StiffnessEnchanted");
        }
    }
}
