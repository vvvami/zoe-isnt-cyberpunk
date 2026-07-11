package net.vami.zoe.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.vami.zoe.entity.client.renderer.ClientVoidlingRender;

import java.util.UUID;
import java.util.function.Supplier;

public record VoidlingPacket(
        UUID uuid,
        boolean active,
        double x,
        double y,
        double z,
        float yaw,
        float pitch,
        float bodyYaw,
        int tickCount
) {
    public static VoidlingPacket active(
            UUID uuid,
            double x,
            double y,
            double z,
            float yaw,
            float pitch,
            float bodyYaw,
            int tickCount
    ) {
        return new VoidlingPacket(
                uuid,
                true,
                x,
                y,
                z,
                yaw,
                pitch,
                bodyYaw,
                tickCount
        );
    }

    public static VoidlingPacket removed(UUID uuid) {
        return new VoidlingPacket(
                uuid,
                false,
                0.0D,
                0.0D,
                0.0D,
                0.0F,
                0.0F,
                0.0F,
                0
        );
    }

    public static void encode(
            VoidlingPacket packet,
            FriendlyByteBuf buffer
    ) {
        buffer.writeUUID(packet.uuid());
        buffer.writeBoolean(packet.active());

        if (packet.active()) {
            buffer.writeDouble(packet.x());
            buffer.writeDouble(packet.y());
            buffer.writeDouble(packet.z());

            buffer.writeFloat(packet.yaw());
            buffer.writeFloat(packet.pitch());
            buffer.writeFloat(packet.bodyYaw());

            buffer.writeVarInt(packet.tickCount());
        }
    }

    public static VoidlingPacket decode(FriendlyByteBuf buffer) {
        UUID uuid = buffer.readUUID();
        boolean active = buffer.readBoolean();

        if (!active) {
            return removed(uuid);
        }

        return active(
                uuid,
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readVarInt()
        );
    }

    public static void handle(
            VoidlingPacket packet,
            Supplier<NetworkEvent.Context> contextSupplier
    ) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(
                        Dist.CLIENT,
                        () -> () -> ClientVoidlingRender.accept(packet)
                )
        );

        context.setPacketHandled(true);
    }
}
