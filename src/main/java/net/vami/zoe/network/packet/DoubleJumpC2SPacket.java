package net.vami.zoe.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.vami.zoe.item.custom.implants.FootspringsItem;

import java.util.function.Supplier;

public class DoubleJumpC2SPacket {
    public final int playerId;

    public DoubleJumpC2SPacket(int playerId) {
        this.playerId = playerId;
    }

    public DoubleJumpC2SPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.playerId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {
            if (!context.getDirection().getReceptionSide().isServer()) return;

            Player player = (Player) context.getSender().level().getEntity(playerId);
            if (player == null) return;

            FootspringsItem.doubleJump(player);
        });

        context.setPacketHandled(true);
    }
}
