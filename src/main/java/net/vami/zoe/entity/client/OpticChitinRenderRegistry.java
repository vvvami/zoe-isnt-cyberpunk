package net.vami.zoe.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.entity.client.renderer.OpticChitinPlayerRenderer;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class OpticChitinRenderRegistry {
    public static OpticChitinPlayerRenderer NORMAL;
    public static OpticChitinPlayerRenderer SLIM;

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        EntityRendererProvider.Context context = event.getContext();

        NORMAL = new OpticChitinPlayerRenderer(context, false);
        SLIM = new OpticChitinPlayerRenderer(context, true);
    }
}
