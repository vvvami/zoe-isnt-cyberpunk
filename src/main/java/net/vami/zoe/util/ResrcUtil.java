package net.vami.zoe.util;

import net.minecraft.resources.ResourceLocation;
import net.vami.zoe.ZoeIsntCyberpunk;

public class ResrcUtil {

    public static ResourceLocation entity(String name) {
        return new ResourceLocation(ZoeIsntCyberpunk.MOD_ID, "textures/entities/" + name + ".png");
    }
}
