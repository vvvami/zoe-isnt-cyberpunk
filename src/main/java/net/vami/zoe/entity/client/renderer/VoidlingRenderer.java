package net.vami.zoe.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.vami.zoe.entity.client.layer.ArbiterGlowLayer;
import net.vami.zoe.entity.client.layer.ArbiterVibrateLayer;
import net.vami.zoe.entity.client.model.ArbiterModel;
import net.vami.zoe.entity.client.model.VoidlingModel;
import net.vami.zoe.entity.custom.ArbiterEntity;
import net.vami.zoe.entity.custom.VoidlingEntity;
import net.vami.zoe.util.ResUtil;

public class VoidlingRenderer extends MobRenderer<VoidlingEntity, VoidlingModel<VoidlingEntity>> {

    public VoidlingRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new VoidlingModel<>(pContext.bakeLayer(VoidlingModel.LAYER_LOCATION)),
                0f);
    }

    @Override
    public ResourceLocation getTextureLocation(VoidlingEntity pEntity) {
        return ResUtil.entity("voidling");
    }

    @Override
    public void render(VoidlingEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        int oldHurt = pEntity.hurtTime;
        pEntity.hurtTime = 0;

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, LightTexture.FULL_BRIGHT);

        pEntity.hurtTime = oldHurt;
    }

    @Override
    protected void scale(VoidlingEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(VoidlingEntity.SCALE, VoidlingEntity.SCALE, VoidlingEntity.SCALE);
        poseStack.translate(0d, 0.1d, 0d);
    }
}
