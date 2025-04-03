package top.nefeli.minecraft;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.nefeli.minecraft.enchantments.LifeStealEnchantment;
import top.nefeli.minecraft.enchantments.RapidFireEnchantment;
import top.nefeli.minecraft.enchantments.ScatteringFireEnchantment;
import top.nefeli.minecraft.enchantments.StiffnessEnchantment;


public class ModEnchantments {
    // 1. 创建 DeferredRegister
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, NefeliMod.MODID);

    // 2. 注册附魔
    public static final RegistryObject<Enchantment> STIFFNESS =
            ENCHANTMENTS.register("stiffness", StiffnessEnchantment::new);
    public static final RegistryObject<Enchantment> LIFE_STEAL =
            ENCHANTMENTS.register("life_steal", LifeStealEnchantment::new);
    public static final RegistryObject<Enchantment> RAPID_FIRE  =
            ENCHANTMENTS.register("rapid_fire", RapidFireEnchantment::new);
    public static final RegistryObject<Enchantment> SCATTERING_FIRE  =
            ENCHANTMENTS.register("scattering_fire", ScatteringFireEnchantment::new);

    // 3. 在 Mod 主类中调用注册
    // 见下一步
}