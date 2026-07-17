package net.vami.zoe.util.implant;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class ImplantRenderUtil {
    public static Map<ResourceLocation, Integer> fromImplants(List<ItemStack> implants) {
        Map<ResourceLocation, Integer> counts = new HashMap<>();

        for (ItemStack implant : implants) {
            if (implant.isEmpty()) {
                continue;
            }

            ResourceLocation id =
                    ForgeRegistries.ITEMS.getKey(implant.getItem());

            if (id != null) {
                counts.merge(id, implant.getCount(), Integer::sum);
            }
        }

        return counts;
    }
}
