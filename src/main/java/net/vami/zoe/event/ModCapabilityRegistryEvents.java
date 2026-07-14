package net.vami.zoe.event;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.capability.PlayerCapability;
import net.vami.zoe.capability.PlayerCapabilityProvider;
import net.vami.zoe.util.implant.ImplantUtil;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class ModCapabilityRegistryEvents {
    // Add capabilities when spawn  //
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerCapabilityProvider.CAPABILITY).isPresent()) {
                event.addCapability(ResourceLocation.tryBuild(ZoeIsntCyberpunk.MOD_ID, "properties"), new PlayerCapabilityProvider());
            }
        }
    }

    // Add capabilities back on death
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        Player original = event.getOriginal();
        Player clone = event.getEntity();

        original.reviveCaps();

        try {
            original.getCapability(PlayerCapabilityProvider.CAPABILITY)
                    .ifPresent(oldStore ->
                            clone.getCapability(PlayerCapabilityProvider.CAPABILITY)
                                    .ifPresent(newStore ->
                                            newStore.copyFrom(oldStore)));
        } finally {
            original.invalidateCaps();
        }
    }

    // Register player capabilities
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerCapability.class);
    }
}
