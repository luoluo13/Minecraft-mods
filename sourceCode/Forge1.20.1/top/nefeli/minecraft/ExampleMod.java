package top.nefeli.minecraft;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.nefeli.minecraft.enchantment.ModEnchantments;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "examplemod";
    // Directly reference a slf4j logger
    public ExampleMod() {
//        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册附魔
        ModEnchantments.ENCHANTMENTS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}