package net.vami.zoe.item.custom.implant;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.network.ModPackets;
import net.vami.zoe.network.packet.DoubleJumpC2SPacket;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;
import org.lwjgl.glfw.GLFW;

public class ThrustersItem extends ImplantItem {
    public ThrustersItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }

    @Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID,
            value = Dist.CLIENT,
            bus = Mod.EventBusSubscriber.Bus.FORGE)
    private static class ClientJumpHandler {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            // Get the minecraft instance, Also gets the player too. Can do mc.playerX //
            Minecraft mc = Minecraft.getInstance();
            // Listen for movement keybinds, Check if player isn't null before //

            Player player = mc.player;

            if (player == null) return;

            if (mc.options.keyJump.consumeClick() && event.getAction() == GLFW.GLFW_PRESS) {
                ModPackets.sendToServer(new DoubleJumpC2SPacket(player.getId()));
            }
        }
    }

    @Mod.EventBusSubscriber(
            modid = ZoeIsntCyberpunk.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    private static class ServerJumpHandler {
        @SubscribeEvent
        public static void landAirJumpReset(LivingFallEvent event) {
            if (event.getEntity().level().isClientSide) return;
            if (!(event.getEntity() instanceof Player player)) return;

            resetAirJumps
                    (ImplantUtil.getImplant(player, ModItems.THRUSTERS.get()));
        }

        @SubscribeEvent
        public static void joinAirJumpReset(EntityJoinLevelEvent event) {
            if (event.getLevel().isClientSide) return;
            if (!(event.getEntity() instanceof Player player)) return;

            resetAirJumps
                    (ImplantUtil.getImplant(player, ModItems.THRUSTERS.get()));
        }
    }

    private static final String AIR_JUMPS_KEY = "zAirJumps";
    private static final int MAX_AIR_JUMPS = 1;

    private static void resetAirJumps(ItemStack stack) {
        if (stack.isEmpty()) return;
        stack.getOrCreateTag().putInt(AIR_JUMPS_KEY, MAX_AIR_JUMPS);
    }

    public static void doubleJump(Player player) {
        if (player.level().isClientSide) return;

        if (player.onGround()) return;
        if (player.getAbilities().flying) return;
        if (player.isFallFlying()) return;
        if (player.isInWaterOrBubble()) return;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.THRUSTERS.get());

        CompoundTag data = implant.getOrCreateTag();

        int jumpsLeft = data.getInt(AIR_JUMPS_KEY);

        if (jumpsLeft <= 0) return;

        data.putInt(AIR_JUMPS_KEY, jumpsLeft - 1);

        Vec3 motion = player.getDeltaMovement();
        float quality = ImplantUtil.getQuality(implant) / 200;
        player.setDeltaMovement(
                motion.x + (player.getLookAngle().x * quality),
                Math.max(0, motion.y) + (0.8d * quality),
                motion.z + (player.getLookAngle().z * quality)
        );

        player.hasImpulse = true;
        player.hurtMarked = true;

        player.resetFallDistance();
    }
}
