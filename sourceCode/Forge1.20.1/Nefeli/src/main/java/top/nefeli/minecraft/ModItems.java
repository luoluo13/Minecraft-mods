package top.nefeli.minecraft;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.nefeli.minecraft.items.BulletItem;
import top.nefeli.minecraft.items.GunItem;
import top.nefeli.minecraft.items.MachineGunItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NefeliMod.MODID);

    public static final RegistryObject<Item> BULLET =
            ITEMS.register("bullet", BulletItem::new);
    public static final RegistryObject<Item> GUN =
            ITEMS.register("gun", GunItem::new);

    public static final RegistryObject<Item> MACHINEGUN =
            ITEMS.register("machinegun", MachineGunItem::new);
}
