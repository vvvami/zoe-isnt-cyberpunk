package net.vami.zoe.layer.renderer.implant;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.layer.model.ReinforcedTibiaLayer;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.ResUtil;

public class ReinforcedTibiaRender<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> implements ImplantRenderer {
    private static final ResourceLocation TEXTURE = ResUtil.entity("reinforced_tibia_layer");
    private final ReinforcedTibiaLayer implantLayer;

    public ReinforcedTibiaRender(RenderLayerParent<T, M> parent) {
        super(parent);
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        this.implantLayer = new ReinforcedTibiaLayer<>(modelSet.bakeLayer(ReinforcedTibiaLayer.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (!(player instanceof Player realPlayer)) return;

        if (!ClientImplantRenderState.hasLayer(player.getUUID(), getImplant())) return;

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

//        implantLayer.prepareMobModel(player, limbSwing, limbSwingAmount, partialTicks);
//        implantLayer.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        implantLayer.rightLeg.copyFrom(this.getParentModel().rightLeg);
        implantLayer.leftLeg.copyFrom(this.getParentModel().leftLeg);

        poseStack.pushPose();

        implantLayer.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    public ResourceLocation getImplant() {
        return ForgeRegistries.ITEMS.getKey(ModItems.REINFORCED_TIBIA.get());
    }
}
