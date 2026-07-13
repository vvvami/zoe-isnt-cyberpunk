package net.vami.zoe.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.vami.zoe.entity.client.layer.ArbiterGlowLayer;
import net.vami.zoe.entity.client.layer.ArbiterVibrateLayer;
import net.vami.zoe.entity.client.layer.CycrawlerGlowLayer;
import net.vami.zoe.entity.client.model.ArbiterModel;
import net.vami.zoe.entity.client.model.CycrawlerModel;
import net.vami.zoe.entity.custom.ArbiterEntity;
import net.vami.zoe.entity.custom.CycrawlerEntity;
import net.vami.zoe.entity.custom.VoidlingEntity;
import net.vami.zoe.util.ResUtil;

import javax.annotation.Nullable;

public class CycrawlerRenderer extends MobRenderer<CycrawlerEntity, CycrawlerModel<CycrawlerEntity>> {

    public CycrawlerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new CycrawlerModel<>(pContext.bakeLayer(CycrawlerModel.LAYER_LOCATION)),
                0f);
        this.addLayer(new CycrawlerGlowLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(CycrawlerEntity pEntity) {
        return ResUtil.entity(pEntity);
    }

    @Override
    public void render(CycrawlerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float opacity = getOpacity(entity);

        this.model.setOpacity(opacity);

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected void scale(CycrawlerEntity pLivingEntity, PoseStack pPoseStack, float pPartialTickTime) {
        pPoseStack.scale(0.5f, 0.5f, 0.5f);
    }

    @Override
    @Nullable
    protected RenderType getRenderType(CycrawlerEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        ResourceLocation texture = getTextureLocation(entity);

        if (showOutline) {
            return RenderType.outline(texture);
        }

        if (!showBody && !translucent) {
            return null;
        }

        return RenderType.entityTranslucent(texture);
    }

    public static float getOpacity(CycrawlerEntity entity) {
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        float startFade = 1f;
        float endFade = 10f;

        float distance = (float) entity.position().distanceTo(cameraPos);

        float opacity = 1.0F - (distance - startFade) / (endFade - startFade);

        opacity = Mth.clamp(opacity, 0.0F, 1.0F);

        return opacity;
    }
}
