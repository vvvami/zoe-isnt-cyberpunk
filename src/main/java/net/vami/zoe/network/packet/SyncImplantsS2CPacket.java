package net.vami.zoe.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.vami.zoe.capability.CapUtil;
import net.vami.zoe.capability.PlayerCapability;
import net.vami.zoe.layer.renderer.implant.ClientImplantRenderState;
import net.vami.zoe.util.implant.ImplantRenderUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class SyncImplantsS2CPacket {
    private final UUID playerId;
    private final List<ItemStack> implants;

    public SyncImplantsS2CPacket(UUID playerId, List<ItemStack> implants) {
        this.playerId = playerId;
        this.implants = implants;
    }

    public SyncImplantsS2CPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readUUID();

        int size = buf.readVarInt();
        this.implants = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            this.implants.add(buf.readItem());
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(playerId);

        buf.writeVarInt(implants.size());

        for (ItemStack stack : implants) {
            buf.writeItem(stack);
        }
    }

    public static void handle(SyncImplantsS2CPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();

            if (mc.level == null) return;

            Player targetPlayer = mc.level.getPlayerByUUID(msg.playerId);

            // even if the entity is not found yet, the render cache can still be updated by uuid
            ClientImplantRenderState.setLayers(
                    msg.playerId,
                    ImplantRenderUtil.fromImplants(msg.implants)
            );

            if (targetPlayer != null && CapUtil.hasCapability(targetPlayer)) {
                PlayerCapability capability = CapUtil.getCap(targetPlayer);
                capability.implants.set(new ArrayList<>(msg.implants));
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
