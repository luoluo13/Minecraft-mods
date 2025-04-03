package top.nefeli.minecraft;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.nefeli.minecraft.enchantments.EnchantmentHandler;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NefeliMod.MODID)
public class NefeliMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "nefeli";

    // Directly reference a slf4j logger
    public NefeliMod() {
        MinecraftForge.EVENT_BUS.register(new EnchantmentHandler());

        IEventBus modEventBus = FMLJavaModLoadingContext .get().getModEventBus();

        // 注册
        ModEnchantments.ENCHANTMENTS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
    }
}