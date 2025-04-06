package top.nefeli.minecraft.nefeli_gun.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.nefeli.minecraft.nefeli_gun.Nefeli_gun;
import top.nefeli.minecraft.nefeli_gun.blocks.Reloading.ReloadingTableBlock;
import top.nefeli.minecraft.nefeli_gun.blocks.Reloading.ReloadingTableBlockEntity;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Nefeli_gun.MODID); // 替换 YourMod 为你的主类名

    // 注册装弹台方块
    public static final RegistryObject<Block> RELOADING_TABLE = BLOCKS.register("reloading_table",
            () -> new ReloadingTableBlock());

    // 注册方块的物品形式（关键！否则无法通过物品获取）
    public static final RegistryObject<Item> RELOADING_TABLE_ITEM = ModItems.ITEMS.register(
            "reloading_table",
            () -> new BlockItem(RELOADING_TABLE.get(), new Item.Properties())
    );
    // 注册装弹台的方块实体类型（BlockEntityType）
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Nefeli_gun.MODID);

    public static final RegistryObject<BlockEntityType<ReloadingTableBlockEntity>> RELOADING_TABLE_BE =
            BLOCK_ENTITIES.register("reloading_table_be",
                    () -> BlockEntityType.Builder.of(
                            ReloadingTableBlockEntity::new,
                            ModBlocks.RELOADING_TABLE.get() // 关联方块实例
                    ).build(null));
}