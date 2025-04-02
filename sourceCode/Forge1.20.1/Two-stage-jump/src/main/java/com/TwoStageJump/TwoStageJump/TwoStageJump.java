package com.TwoStageJump.TwoStageJump;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.SimpleParticleType;
import org.slf4j.Logger;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraft.core.particles.ParticleTypes;
import java.util.HashMap;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
// The value here should match an entry in the META-INF/mods.toml file
@Mod(com.TwoStageJump.TwoStageJump.TwoStageJump.MODID)
public class TwoStageJump {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "twostagejump";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // 添加新的玩家跳跃处理器
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class PlayerJumpHandler {
        // 使用哈希表记录玩家最后跳跃时间，键为玩家对象，值为游戏刻时间戳
        private static final HashMap<Player, Long> lastJumpTimes = new HashMap<>();
        // 新增跳跃次数记录表（键：玩家对象，值：当前空中已使用二段跳次数）
        private static final HashMap<Player, Integer> jumpCounts = new HashMap<>();
        // 二段跳的垂直初速度（单位：米/刻），约为原版跳跃力的1/3
        private static final double JUMP_POWER = 0.5;

        // 监听生物跳跃事件（LivingJumpEvent）
        @SubscribeEvent
        public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
            // 仅处理玩家跳跃且客户端侧的触发（避免服务端重复处理）
            if (event.getEntity() instanceof Player player && player.level().isClientSide()) {
                // 记录跳跃时间戳（使用游戏刻而非系统时间保证跨客户端同步）
                lastJumpTimes.put(player, player.level().getGameTime());
                // 初始化跳跃次数（允许一次二段跳）
                jumpCounts.put(player, 0);
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent event) {
            Player player = event.player;
            long currentTime = player.level().getGameTime();

            // 二段跳触发条件判断：
            // 1. 玩家正按下跳跃键（使用Minecraft的输入配置检测）
            // 2. 玩家处于空中状态（onGround()返回false）
            // 3. 玩家存在于跳跃记录表中（表示已经完成初次跳跃）
            // 4. 距离上次跳跃超过5游戏刻（防止连跳过快）
            if (Minecraft.getInstance().options.keyJump.isDown()
                    && !player.onGround()
                    && lastJumpTimes.containsKey(player)
                    && (currentTime - lastJumpTimes.get(player)) > 5 // 初次起跳和二段跳之间的间隔（tick）
                    // 新增次数检查：当前空中尚未使用过二段跳
                    && jumpCounts.getOrDefault(player, 0) < 1) {

                // 获取当前Y轴速度（单位：米/刻）
                double motionY = player.getDeltaMovement().y;

                // 物理计算：清除下落速度并施加跳跃力
                if(motionY < 0) motionY = 0; // 消除重力加速度带来的下落速度
                motionY += JUMP_POWER;       // 应用基础跳跃加速度（与游戏刻时间无关的瞬时加速）

                // 更新运动矢量（保留原有水平速度）
                player.setDeltaMovement(
                        player.getDeltaMovement().x, // X轴速度（水平前后）
                        motionY,                     // 修正后的Y轴速度
                        player.getDeltaMovement().z  // Z轴速度（水平左右）
                );

                spawnDoubleJumpParticles(player);
                // 标记已使用二段跳
                jumpCounts.put(player, 1);
                LOGGER.info("二段跳触发: {}", player.getName().getString());
            }

            // 状态重置：当玩家接触地面时移除跳跃记录
            if (player.onGround()) {
                lastJumpTimes.remove(player);
                // 落地后清除跳跃次数记录
                jumpCounts.remove(player);
            }
        }

        // 生成二段跳特效粒子
        private static void spawnDoubleJumpParticles(Player player) {
            // 使用云粒子类型，生成30个粒子，基础速度0.8
            spawnParticles(player, ParticleTypes.CLOUD, 30, 0.8);
        }

        // 通用粒子生成方法
        private static void spawnParticles(Player player, SimpleParticleType type, int count, double speed) {
            // 计算粒子生成中心点（使用玩家碰撞箱底部Y坐标）
            double centerY = player.getBoundingBox().minY;
            for (int i = 0; i < count; i++) {
                // 随机生成球面坐标（转换为水平圆形分布）
                double angle = Math.toRadians(player.getRandom().nextInt(360)); // 0-360度随机角度
                double radius = player.getRandom().nextDouble(); // 0-1米随机半径

                // 将极坐标转换为笛卡尔坐标
                double dx = Math.cos(angle) * radius;
                double dz = Math.sin(angle) * radius;
                double dy = player.getRandom().nextDouble() * 0.5; // 垂直方向随机偏移

                // 添加粒子到游戏世界
                player.level().addParticle(type,
                        player.getX() + dx,  // 世界X坐标（玩家位置+水平偏移）
                        centerY,             // 世界Y坐标（脚部位置）
                        player.getZ() + dz,  // 世界Z坐标（玩家位置+水平偏移）
                        dx * speed,          // X轴速度分量（水平扩散）
                        speed,               // Y轴速度分量（向上运动）
                        dz * speed);         // Z轴速度分量（水平扩散）
            }
        }
    }
}
