package top.nefeli.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.nefeli.minecraft.items.MachineGunItem;

@Mod.EventBusSubscriber(modid = "nefeli", value = Dist.CLIENT)
public class ClientEvents {

    private static boolean isFiring = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                // 检查玩家是否持有机枪
                ItemStack heldItem = mc.player.getMainHandItem();
                if (heldItem.getItem() instanceof MachineGunItem) {
                    // 处理自动射击
                    if (mc.options.keyAttack.isDown()) {
                        if (!isFiring) {
                            // 开始射击
                            if (mc.gameMode != null) {
                                mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
                            }
                            isFiring = true;
                        }
                    } else {
                        // 停止射击
                        if (isFiring && mc.player.isUsingItem()) {
                            mc.player.stopUsingItem();
                        }
                        isFiring = false;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        // 可以在这里添加其他键盘输入处理
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton event) {
        // 可以在这里添加鼠标输入处理
    }
}