package net.vami.zoe.entity.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.vami.zoe.entity.client.renderer.CycrawlerRenderer;
import net.vami.zoe.entity.custom.CycrawlerEntity;
import net.vami.zoe.util.ResUtil;

public class CycrawlerGlowLayer<T extends LivingEntity, M extends EntityModel<T>>
        extends RenderLayer<T, M> {

    public static final ResourceLocation GLOW_TEXTURE =
            ResUtil.entity("cycrawler_glow");

    public CycrawlerGlowLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(entity instanceof CycrawlerEntity cyc)) return;

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(GLOW_TEXTURE));

        float opacity = CycrawlerRenderer.getOpacity(cyc);

        this.getParentModel().renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, opacity);
    }
}
