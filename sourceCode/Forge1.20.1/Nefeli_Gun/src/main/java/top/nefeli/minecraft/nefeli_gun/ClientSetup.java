package top.nefeli.minecraft.nefeli_gun;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.nefeli.minecraft.nefeli_gun.blocks.Reloading.ReloadingScreen;
import top.nefeli.minecraft.nefeli_gun.init.ModMenus;

@Mod.EventBusSubscriber(modid = Nefeli_gun.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // 绑定菜单类型到屏幕
        MenuScreens.register(ModMenus.RELOADING_MENU.get(), ReloadingScreen::new);
    }
}