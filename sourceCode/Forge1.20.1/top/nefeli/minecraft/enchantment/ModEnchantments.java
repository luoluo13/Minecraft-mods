package top.nefeli.minecraft.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.nefeli.minecraft.ExampleMod;


public class ModEnchantments {
    // 1. 创建 DeferredRegister
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ExampleMod.MODID);

    // 2. 注册附魔
    public static final RegistryObject<Enchantment> MY_CUSTOM_ENCHANT =
            ENCHANTMENTS.register("my_custom_enchant", MyCustomEnchantment::new);
    public static final RegistryObject<Enchantment> LIFE_STEAL =
            ENCHANTMENTS.register("life_steal", LifeStealEnchantment::new);

    // 3. 在 Mod 主类中调用注册
    // 见下一步
}