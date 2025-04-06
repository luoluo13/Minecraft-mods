package top.nefeli.minecraft.nefeli_gun.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import top.nefeli.minecraft.nefeli_gun.items.BulletCasingItem;
import top.nefeli.minecraft.nefeli_gun.items.BulletCoreItem;
import top.nefeli.minecraft.nefeli_gun.items.BulletPrimerItem;

import static top.nefeli.minecraft.nefeli_gun.Nefeli_gun.MODID;

public class ModCreativeModeTabs {
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "nefeli_gun" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final RegistryObject<CreativeModeTab> GUN_MOD_TAB = CREATIVE_MODE_TABS.register("gun_mod_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mod.gun_mod")) // 语言文件中的名称
                    .icon(() -> new ItemStack(ModItems.BULLET.get())) // 标签页图标（用步枪作为图标）
                    .displayItems((params, output) -> {
                        // 添加所有模组物品到该标签页
                        output.accept(ModBlocks.RELOADING_TABLE_ITEM.get());
                        output.accept(BulletCoreItem.createStack(7.62F, "铜制"));
                        output.accept(BulletCasingItem.createStack(7.62F, "铜制"));
                        output.accept(BulletPrimerItem.createStack("基础"));
                    })
                    .build());
}
