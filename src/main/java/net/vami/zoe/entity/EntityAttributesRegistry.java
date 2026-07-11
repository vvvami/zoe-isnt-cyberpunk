package net.vami.zoe.entity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.entity.custom.ArbiterEntity;
import net.vami.zoe.entity.custom.VoidlingEntity;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributesRegistry {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ARBITER.get(), ArbiterEntity.createAttributes().build());
        event.put(ModEntities.VOIDLING.get(), VoidlingEntity.createAttributes().build());
    }
}
