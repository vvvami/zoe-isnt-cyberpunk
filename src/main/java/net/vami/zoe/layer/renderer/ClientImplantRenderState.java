package net.vami.zoe.layer.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.layer.renderer.implant.PlayerModelPart;
import net.vami.zoe.layer.renderer.implant.PlayerPartOverrideProvider;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class ClientImplantRenderState {

    // for the render layers of each implant
    private static final Map<UUID, Map<ResourceLocation, Integer>> PLAYER_LAYERS = new HashMap<>();

    // for the canceled rendering of body parts in the .Pre render event
    private static final Map<UUID, Set<PlayerModelPart>> PLAYER_PARTS = new HashMap<>();

    private ClientImplantRenderState() {
    }

    public static void setLayers(UUID playerId, Map<ResourceLocation, Integer> layers) {
        Map<ResourceLocation, Integer> layerCopy = Map.copyOf(layers);

        PLAYER_LAYERS.put(playerId, layerCopy);

        PLAYER_PARTS.put(playerId, getOverrides(layerCopy));
    }

    private static Set<PlayerModelPart> getOverrides(Map<ResourceLocation, Integer> layers) {
        EnumSet<PlayerModelPart> overrides = EnumSet.noneOf(PlayerModelPart.class);

        for (Map.Entry<ResourceLocation, Integer> entry : layers.entrySet()) {
            int count = entry.getValue();

            if (count <= 0) continue;

            Item item = ForgeRegistries.ITEMS.getValue(entry.getKey());

            if (item instanceof PlayerPartOverrideProvider provider) {
                provider.addParts(count, overrides);
            }
        }

        return Set.copyOf(overrides);
    }

    public static boolean hasLayer(UUID playerId, ResourceLocation layer) {
        return getLayerCount(playerId, layer) > 0;
    }

    public static int getLayerCount(UUID playerId, ResourceLocation layer) {
        if (layer == null) return 0;

        return PLAYER_LAYERS
                .getOrDefault(playerId, Map.of())
                .getOrDefault(layer, 0);
    }

    public static Set<PlayerModelPart> getOverrides(UUID playerId) {
        return PLAYER_PARTS.getOrDefault(playerId, Set.of());
    }

    public static boolean overridesPart(UUID playerId, PlayerModelPart part) {
        return PLAYER_PARTS
                .getOrDefault(playerId, Set.of())
                .contains(part);
    }

    public static void clear(UUID playerId) {
        PLAYER_LAYERS.remove(playerId);
        PLAYER_PARTS.remove(playerId);
    }

    public static void clearAll() {
        PLAYER_LAYERS.clear();
        PLAYER_PARTS.clear();
    }
}