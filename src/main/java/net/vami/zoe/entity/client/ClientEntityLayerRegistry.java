package net.vami.zoe.entity.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.entity.ModEntities;
import net.vami.zoe.entity.client.model.ArbiterModel;
import net.vami.zoe.entity.client.renderer.ArbiterRenderer;
import net.vami.zoe.entity.custom.ArbiterEntity;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEntityLayerRegistry {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.ARBITER.get(), ArbiterRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ArbiterModel.LAYER_LOCATION, ArbiterModel::createBodyLayer);
    }

}
