package net.vami.zoe.layer.renderer.implant;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.entity.client.OpticChitinRenderRegistry;
import net.vami.zoe.entity.client.renderer.OpticChitinPlayerRenderer;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.layer.animations.LegsawAnimations;
import net.vami.zoe.layer.model.LegsawLayer;
import net.vami.zoe.util.ResUtil;
import org.joml.Vector3f;

public class LegsawRender<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> implements ImplantRenderer {
    private static final ResourceLocation TEXTURE = ResUtil.layer("legsaw");
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
    private final LegsawLayer implantLayer;

    public LegsawRender(RenderLayerParent<T, M> parent) {
        super(parent);
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        this.implantLayer = new LegsawLayer(modelSet.bakeLayer(LegsawLayer.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (!(player instanceof Player realPlayer)) return;

        int implantCount = ClientImplantRenderState.getLayerCount(player.getUUID(), getImplant());
        if (implantCount == 0) return;

        implantLayer.root().getAllParts().forEach(ModelPart::resetPose);

        float speed = player.getDeltaMovement().x >= 0.001f
                || player.getDeltaMovement().z >= 0.001f ? 2f : 0.5f;

        long animationTime;

        animationTime = (long) (ageInTicks * 1000.0F * speed / 20.0F);

        implantLayer.root().getAllParts().forEach(ModelPart::resetPose);

        renderLegsaw(poseStack, buffer, packedLight, this.getParentModel().rightLeg, animationTime);

        if (implantCount > 1) {
            renderLegsaw(poseStack, buffer, packedLight, this.getParentModel().leftLeg, animationTime);
        }
    }

    private void renderLegsaw(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ModelPart parentLeg, long animationTime) {

        KeyframeAnimations.animate(
                implantLayer,
                LegsawAnimations.idle,
                animationTime,
                1.0F,
                ANIMATION_VECTOR_CACHE);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));

        poseStack.pushPose();
        parentLeg.translateAndRotate(poseStack);

        float opacity = OpticChitinRenderRegistry.NORMAL.getOpacity() < 1 ? OpticChitinRenderRegistry.NORMAL.getOpacity() : OpticChitinRenderRegistry.SLIM.getOpacity();

        implantLayer.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, opacity);
        poseStack.popPose();
    }

    public ResourceLocation getImplant() {
        return ForgeRegistries.ITEMS.getKey(ModItems.LEGSAW.get());
    }
}
