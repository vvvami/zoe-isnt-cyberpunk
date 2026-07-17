package net.vami.zoe.layer.renderer.implant;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class ClientImplantRenderState {

    private static final Map<UUID, Map<ResourceLocation, Integer>>
            LAYERS_BY_PLAYER = new HashMap<>();

    private ClientImplantRenderState() {
    }

    public static void setLayers(
            UUID playerId,
            Map<ResourceLocation, Integer> layers
    ) {
        LAYERS_BY_PLAYER.put(
                playerId,
                Map.copyOf(layers)
        );
    }

    public static boolean hasLayer(
            UUID playerId,
            ResourceLocation layer
    ) {
        return getLayerCount(playerId, layer) > 0;
    }

    public static int getLayerCount(
            UUID playerId,
            ResourceLocation layer
    ) {
        if (layer == null) {
            return 0;
        }

        return LAYERS_BY_PLAYER
                .getOrDefault(playerId, Map.of())
                .getOrDefault(layer, 0);
    }

    public static void clear(UUID playerId) {
        LAYERS_BY_PLAYER.remove(playerId);
    }

    public static void clearAll() {
        LAYERS_BY_PLAYER.clear();
    }
}