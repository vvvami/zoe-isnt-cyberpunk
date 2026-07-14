package net.vami.zoe.entity.client.renderer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.vami.zoe.entity.client.model.OpticChitinPlayerModel;

import javax.annotation.Nullable;

public class OpticChitinPlayerRenderer extends PlayerRenderer {
    private float opacity = 1.0F;

    public OpticChitinPlayerRenderer(EntityRendererProvider.Context context, boolean slim) {
        super(context, slim);

        ModelLayerLocation layer = slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER;
        this.model = new OpticChitinPlayerModel(context.bakeLayer(layer), slim, this);
    }

    public void setOpacity(float opacity) {
        this.opacity = Mth.clamp(opacity, 0.0F, 1.0F);
    }

    @Override
    @Nullable
    protected RenderType getRenderType(
            AbstractClientPlayer player,
            boolean bodyVisible,
            boolean translucent,
            boolean glowing
    ) {
        ResourceLocation texture = getTextureLocation(player);

        if (glowing) {
            return RenderType.outline(texture);
        }

        if (!bodyVisible && !translucent) {
            return null;
        }

        return RenderType.entityTranslucent(texture);
    }

    public float getOpacity() {
        return this.opacity;
    }
}
