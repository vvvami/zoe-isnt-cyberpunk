package net.vami.zoe.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.entity.client.OpticChitinRenderRegistry;
import net.vami.zoe.entity.client.renderer.OpticChitinPlayerRenderer;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.implant.ImplantUtil;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class OpticChitinRenderEvent {
    private static UUID renderedPlayer;

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        UUID uuid = event.getEntity().getUUID();

        if (uuid.equals(renderedPlayer)) {
            return;
        }

        AbstractClientPlayer player =
                (AbstractClientPlayer) event.getEntity();

        if (!shouldFade(player)) {
            return;
        }

        OpticChitinPlayerRenderer renderer =
                player.getModelName().equals("slim")
                        ? OpticChitinRenderRegistry.SLIM
                        : OpticChitinRenderRegistry.NORMAL;

        if (renderer == null) return;

        float opacity = calculateOpacity(
                player,
                event.getPartialTick()
        );

        event.setCanceled(true);

        renderer.setOpacity(opacity);
        renderedPlayer = uuid;

        try {
            renderer.render(player, player.getYRot(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
        } finally {
            renderedPlayer = null;
            renderer.setOpacity(1f);
        }
    }

    private static boolean shouldFade(AbstractClientPlayer player) {
        ItemStack implant = ImplantUtil.getImplant(player, ModItems.OPTIC_CHITIN.get());
        return !implant.isEmpty();
    }

    private static float calculateOpacity(AbstractClientPlayer player, float partialTick) {
        Vec3 camPos = Minecraft.getInstance()
                .gameRenderer.getMainCamera().getPosition();

        double x = Mth.lerp(partialTick, player.xOld, player.getX());

        double y = Mth.lerp(partialTick, player.yOld, player.getY());

        double z = Mth.lerp(partialTick, player.zOld, player.getZ());

        float distance = (float) camPos.distanceTo(new Vec3(x, y, z));

        float quality = getFadeQuality(player);

        quality = Mth.clamp(quality, 1f, 100f);

        float qualLerp = (quality - 1f) / 99f;

        float fadeStart = Mth.lerp(qualLerp, 16f, 2f);

        float fadeEnd = Mth.lerp(qualLerp, 64f, 10f);

        return Mth.clamp(1f - (distance - fadeStart) / (fadeEnd - fadeStart),
                0f, 1f);
    }

    private static float getFadeQuality(AbstractClientPlayer player) {
        ItemStack implant = ImplantUtil.getImplant(player, ModItems.OPTIC_CHITIN.get());

        if (implant.isEmpty()) return 1f;

        return ImplantUtil.getQuality(implant);
    }
}
