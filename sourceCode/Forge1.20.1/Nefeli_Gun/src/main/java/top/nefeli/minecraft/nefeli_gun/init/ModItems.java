package top.nefeli.minecraft.nefeli_gun.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.nefeli.minecraft.nefeli_gun.Nefeli_gun;
import top.nefeli.minecraft.nefeli_gun.items.BulletCasingItem;
import top.nefeli.minecraft.nefeli_gun.items.BulletCoreItem;
import top.nefeli.minecraft.nefeli_gun.items.BulletItem;
import top.nefeli.minecraft.nefeli_gun.items.BulletPrimerItem;

// 在 ModItems 类中注册物品
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Nefeli_gun.MODID);

    public static final RegistryObject<Item> BULLET_CASING = ITEMS.register("bullet_casing",
            BulletCasingItem::new);

    // 弹头
    public static final RegistryObject<Item> BULLET_CORE = ITEMS.register("bullet_core",
            BulletCoreItem::new);

    // 底火
    public static final RegistryObject<Item> BULLET_PRIMER = ITEMS.register("bullet_primer",
            BulletPrimerItem::new);

    // 子弹成品
    public static final RegistryObject<Item> BULLET = ITEMS.register("bullet",
            BulletItem::new);
}
