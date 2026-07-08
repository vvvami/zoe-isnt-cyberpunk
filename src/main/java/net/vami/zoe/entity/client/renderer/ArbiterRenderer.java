package net.vami.zoe.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.vami.zoe.entity.client.layer.ArbiterGlowLayer;
import net.vami.zoe.entity.client.layer.ArbiterVibrateLayer;
import net.vami.zoe.entity.client.model.ArbiterModel;
import net.vami.zoe.entity.custom.ArbiterEntity;
import net.vami.zoe.util.ResUtil;

public class ArbiterRenderer extends MobRenderer<ArbiterEntity, ArbiterModel<ArbiterEntity>> {

    public ArbiterRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ArbiterModel<>(pContext.bakeLayer(ArbiterModel.LAYER_LOCATION)),
                1f);
        this.addLayer(new ArbiterGlowLayer<>(this));
        this.addLayer(new ArbiterVibrateLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ArbiterEntity pEntity) {
        return ResUtil.entity("arbiter");
    }

    @Override
    public void render(ArbiterEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
