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
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.vami.zoe.entity.client.model.ArbiterModel;
import net.vami.zoe.entity.client.renderer.ArbiterRenderer;
import net.vami.zoe.util.ResUtil;

public class ArbiterVibrateLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private static final float ALPHA = 0.35f;
    private static final float OFFSET_AMOUNT = 0.1f;

    public ArbiterVibrateLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        ResourceLocation texture = ArbiterGlowLayer.GLOW_TEXTURE;

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.eyes(texture));

        long baseSeed = entity.getId() * 31L + (entity.tickCount);

        RandomSource random = RandomSource.create(baseSeed + 997L);

        double x = randomOffset(random);
        double y = randomOffset(random) * 0.5d;
        double z = randomOffset(random);

        poseStack.pushPose();

        poseStack.translate(x, y, z);

        this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY,
                1f,
                1f,
                1f,
                ALPHA);

        poseStack.popPose();
    }

    private static double randomOffset(RandomSource random) {
        return (random.nextDouble() - 0.5d) * 2.0d * OFFSET_AMOUNT;
    }
}
