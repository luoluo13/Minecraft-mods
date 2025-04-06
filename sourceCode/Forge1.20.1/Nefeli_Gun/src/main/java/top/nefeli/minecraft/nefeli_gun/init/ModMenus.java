package top.nefeli.minecraft.nefeli_gun.init;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.nefeli.minecraft.nefeli_gun.Nefeli_gun;
import top.nefeli.minecraft.nefeli_gun.blocks.Reloading.ReloadingMenu;
import top.nefeli.minecraft.nefeli_gun.blocks.Reloading.ReloadingTableBlockEntity;

// 文件路径: src/main/java/com/example/gunmod/init/ModMenus.java
public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Nefeli_gun.MODID); // 替换 YourMod 为你的主类名

    // 注册装弹台的菜单类型
    public static final RegistryObject<MenuType<ReloadingMenu>> RELOADING_MENU =
            MENUS.register("reloading_menu",
                    () -> IForgeMenuType.create( // 使用 Forge 提供的工厂方法
                            (containerId, playerInv, data) ->
                            {
                                BlockPos pos = data != null ? data.readBlockPos() : BlockPos.ZERO;
                                // 获取 BlockEntity
                                ReloadingTableBlockEntity blockEntity = (ReloadingTableBlockEntity) playerInv.player.level().getBlockEntity(pos);
                                return new ReloadingMenu(containerId, playerInv, blockEntity);
                            }
                    )
            );
}