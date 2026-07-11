package net.vami.zoe.network;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.network.packet.DoubleJumpC2SPacket;
import net.vami.zoe.network.packet.SyncImplantsS2CPacket;
import net.vami.zoe.network.packet.VoidlingPacket;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class ModPackets {
    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static SimpleChannel INSTANCE;


    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ZoeIsntCyberpunk.MOD_ID, "main"))
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions("1"::equals)
                .serverAcceptedVersions("1"::equals)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(DoubleJumpC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(DoubleJumpC2SPacket::encode)
                .decoder(DoubleJumpC2SPacket::new)
                .consumerMainThread(DoubleJumpC2SPacket::handle)
                .add();

        net.messageBuilder(SyncImplantsS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncImplantsS2CPacket::encode)
                .decoder(SyncImplantsS2CPacket::new)
                .consumerMainThread(SyncImplantsS2CPacket::handle)
                .add();

        net.messageBuilder(VoidlingPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(VoidlingPacket::encode)
                .decoder(VoidlingPacket::decode)
                .consumerMainThread(VoidlingPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToClient(MSG message, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {

            ModPackets.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), message);
        }
    }

    public static <MSG> void sendToTrackingAndSelf(MSG message, ServerPlayer player) {
        ModPackets.INSTANCE.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                message
        );
    }

    public static <MSG> void sendToPlayer(MSG message, Entity player) {
        if (player instanceof ServerPlayer serverPlayer) {

            INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), message);
        }
    }

    public static <MSG> void sendToClients(Entity entity, MSG message) {
        if (entity instanceof LocalPlayer serverPlayer)

            INSTANCE.send(PacketDistributor.SERVER.noArg(), message);

    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        new ModPackets();
        register();
    }
}
