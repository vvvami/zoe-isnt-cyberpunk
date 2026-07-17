package net.vami.zoe.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.ZoeIsntCyberpunk;

public class ResUtil {

    public static ResourceLocation entity(String name) {
        return new ResourceLocation(ZoeIsntCyberpunk.MOD_ID, "textures/entity/" + name + ".png");
    }

    public static ResourceLocation layer(String name) {
        return new ResourceLocation(ZoeIsntCyberpunk.MOD_ID, "textures/layer/" + name + ".png");
    }

    public static ResourceLocation entity(Entity entity) {
        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        String name = id.getPath();
        return new ResourceLocation(ZoeIsntCyberpunk.MOD_ID, "textures/entity/" + name + ".png");
    }
}
