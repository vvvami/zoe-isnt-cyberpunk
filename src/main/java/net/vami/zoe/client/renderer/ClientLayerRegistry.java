package net.vami.zoe.client.renderer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.client.model.EaraLayer;
import net.vami.zoe.client.model.PowerfistLayer;
import net.vami.zoe.client.model.ReinforcedTibiaLayer;
import net.vami.zoe.client.renderer.implant.EaraRender;
import net.vami.zoe.client.renderer.implant.PowerfistRender;
import net.vami.zoe.client.renderer.implant.ReinforcedTibiaRender;

import java.util.List;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientLayerRegistry {

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                ReinforcedTibiaLayer.LAYER_LOCATION,
                ReinforcedTibiaLayer::createBodyLayer
        );
        event.registerLayerDefinition(
                PowerfistLayer.LAYER_LOCATION,
                PowerfistLayer::createBodyLayer
        );
        event.registerLayerDefinition(
                EaraLayer.LAYER_LOCATION,
                EaraLayer::createBodyLayer
        );
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        List<EntityType<? extends LivingEntity>> typesToAdd = List.of(
                EntityType.PLAYER, EntityType.ZOMBIE, EntityType.SKELETON, EntityType.DROWNED, EntityType.HUSK, EntityType.STRAY, EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN,
                EntityType.PIGLIN_BRUTE, EntityType.ARMOR_STAND
                // add more known types or mod types here if you want
        );
        for (EntityType<? extends LivingEntity> type : typesToAdd) {
            addIfLivingEntityRenderer(event, type);
        }
        for (String skin : event.getSkins()) {
            PlayerRenderer playerRenderer = event.getSkin(skin);
            playerRenderer.addLayer(new ReinforcedTibiaRender<>(playerRenderer));
            playerRenderer.addLayer(new PowerfistRender<>(playerRenderer));
            playerRenderer.addLayer(new EaraRender<>(playerRenderer));


        }
    }

    private static <T extends LivingEntity> void addIfLivingEntityRenderer(EntityRenderersEvent.AddLayers event, EntityType<T> type) {
        EntityRenderer<T> renderer = event.getRenderer(type);
        if (renderer instanceof LivingEntityRenderer<T, ?> livingRenderer) {
            if (livingRenderer.getModel() instanceof HumanoidModel<?>) {
                @SuppressWarnings("unchecked")
                LivingEntityRenderer<T, HumanoidModel<T>> castRenderer = (LivingEntityRenderer<T, HumanoidModel<T>>) livingRenderer;
                castRenderer.addLayer(new ReinforcedTibiaRender<>(castRenderer));
                castRenderer.addLayer(new PowerfistRender<>(castRenderer));
                castRenderer.addLayer(new EaraRender<>(castRenderer));
            }
        }
    }
}

