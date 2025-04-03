package top.nefeli.minecraft;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.nefeli.minecraft.entities.BulletEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, NefeliMod.MODID);

    public static final RegistryObject<EntityType<BulletEntity>> BULLET = ENTITIES.register(
            "bullet",
            () -> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F) // 设置碰撞箱大小
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("bullet")
    );
}
