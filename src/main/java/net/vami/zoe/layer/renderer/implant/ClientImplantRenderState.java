package net.vami.zoe.layer.renderer.implant;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ClientImplantRenderState {
    private static final Map<UUID, Set<ResourceLocation>> LAYERS_BY_PLAYER = new HashMap<>();

    public static void setLayers(UUID playerId, Set<ResourceLocation> layers) {
        LAYERS_BY_PLAYER.put(playerId, Set.copyOf(layers));
    }

    public static boolean hasLayer(UUID playerId, ResourceLocation layer) {
        return layer != null &&
                LAYERS_BY_PLAYER
                        .getOrDefault(playerId, Set.of())
                        .contains(layer);
    }

    public static void clear(UUID playerId) {
        LAYERS_BY_PLAYER.remove(playerId);
    }

    public static void clearAll() {
        LAYERS_BY_PLAYER.clear();
    }
}