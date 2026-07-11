package net.vami.zoe.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.entity.custom.ArbiterEntity;
import net.vami.zoe.entity.custom.VoidlingEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ZoeIsntCyberpunk.MOD_ID);

    public static final RegistryObject<EntityType<ArbiterEntity>> ARBITER =
            ENTITY_TYPES.register("arbiter", () -> EntityType.Builder.of(ArbiterEntity::new, MobCategory.MONSTER)
                    .sized(2.5f, 4f).build("arbiter"));

    public static final RegistryObject<EntityType<VoidlingEntity>> VOIDLING =
            ENTITY_TYPES.register("voidling", () -> EntityType.Builder.of(VoidlingEntity::new, MobCategory.MONSTER)
                    .sized(2f, 2f)
                    .clientTrackingRange(8192)
                    .build("voidling"));

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
