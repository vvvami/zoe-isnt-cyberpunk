package net.vami.zoe.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImplantRenderUtil {
    public static Set<ResourceLocation> fromImplants(List<ItemStack> implants) {
        Set<ResourceLocation> layers = new HashSet<>();

        for (ItemStack stack : implants) {
            if (stack.isEmpty()) continue;

            ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());

            if (id != null) {
                layers.add(id);
            }
        }

        return layers;
    }
}
