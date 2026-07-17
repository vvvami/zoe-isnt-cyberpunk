package net.vami.zoe.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.vami.zoe.entity.client.renderer.OpticChitinPlayerRenderer;

public final class OpticChitinRenderRegistry {
    public static OpticChitinPlayerRenderer NORMAL;
    public static OpticChitinPlayerRenderer SLIM;

    private OpticChitinRenderRegistry() {
    }

    public static void rebuild(EntityRendererProvider.Context context) {
        NORMAL = new OpticChitinPlayerRenderer(context, false);
        SLIM = new OpticChitinPlayerRenderer(context, true);
    }
}
