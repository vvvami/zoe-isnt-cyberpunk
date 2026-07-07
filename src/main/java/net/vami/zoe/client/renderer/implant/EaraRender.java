package net.vami.zoe.client.renderer.implant;

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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.client.animation.EaraLayerAnim;
import net.vami.zoe.client.model.EaraLayer;
import net.vami.zoe.client.model.ReinforcedTibiaLayer;
import net.vami.zoe.client.renderer.ClientImplantRenderState;
import net.vami.zoe.client.renderer.ImplantRenderer;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.ResrcUtil;
import org.joml.Vector3f;

public class EaraRender<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> implements ImplantRenderer {
    private static final ResourceLocation TEXTURE = ResrcUtil.entity("eara_layer");
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
    private final EaraLayer implantLayer;
    private long animationTime;
    float tickCount;

    public EaraRender(RenderLayerParent<T, M> parent) {
        super(parent);
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        this.implantLayer = new EaraLayer<>(modelSet.bakeLayer(EaraLayer.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (!(player instanceof Player realPlayer)) return;

        if (!ClientImplantRenderState.hasLayer(player.getUUID(), getImplant())) return;

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

        implantLayer.root().getAllParts().forEach(ModelPart::resetPose);
        implantLayer.body.copyFrom(this.getParentModel().body);

        animationTime = (long) (ageInTicks * 1000.0F / 20.0F);

        KeyframeAnimations.animate(
                implantLayer,
                EaraLayerAnim.eara_loop,
                animationTime,
                1.0F,
                ANIMATION_VECTOR_CACHE
        );


        poseStack.pushPose();

        implantLayer.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    public ResourceLocation getImplant() {
        return ForgeRegistries.ITEMS.getKey(ModItems.EARA.get());
    }
}
