package net.vami.zoe.capability;

import net.minecraft.world.entity.Entity;

public class CapabilityUtil {
    // Gets a capability
    public static PlayerCapability getCapability(Entity entity){
        return entity.getCapability(PlayerCapabilityProvider.CAPABILITY).orElse(null);
    }

    // Check a capability before attempting to get or modify its value
    public static boolean checkCapability(Entity entity){
        return entity.getCapability(PlayerCapabilityProvider.CAPABILITY).isPresent();
    }
}
