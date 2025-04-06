package top.nefeli.minecraft.nefeli_gun.init;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import top.nefeli.minecraft.nefeli_gun.Nefeli_gun;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Nefeli_gun.MODID);

//    public static final RegistryObject<EntityType<BulletEntity>> BULLET = ENTITIES.register(
//            "bullet",
//            () -> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
//                    .sized(0.25F, 0.25F) // 设置碰撞箱大小
//                    .clientTrackingRange(4)
//                    .updateInterval(10)
//                    .build("bullet")
//    );
}
