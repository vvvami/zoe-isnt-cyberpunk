package net.vami.zoe.layer.renderer.implant;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.layer.model.PowerfistLayer;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.ResUtil;

public class PowerfistRender<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> implements ImplantRenderer {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
            ResUtil.entity("powerfist_layer_1"),
            ResUtil.entity("powerfist_layer_2"),
            ResUtil.entity("powerfist_layer_3"),
            ResUtil.entity("powerfist_layer_4"),
            ResUtil.entity("powerfist_layer_3"),
            ResUtil.entity("powerfist_layer_2"),
    };

    private final PowerfistLayer implantLayer;

    public PowerfistRender(RenderLayerParent<T, M> parent) {
        super(parent);
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        this.implantLayer = new PowerfistLayer<>(modelSet.bakeLayer(PowerfistLayer.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (!(player instanceof Player realPlayer)) return;

        if (!ClientImplantRenderState.hasLayer(player.getUUID(), getImplant())) return;

        int frame = ((int)(ageInTicks / 2.0F)) % TEXTURES.length;

        ResourceLocation texture = TEXTURES[frame];
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));

//        implantLayer.prepareMobModel(player, limbSwing, limbSwingAmount, partialTicks);
//        implantLayer.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        implantLayer.rightArm.copyFrom(this.getParentModel().rightArm);
        implantLayer.leftArm.copyFrom(this.getParentModel().leftArm);

        poseStack.pushPose();

        implantLayer.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    public ResourceLocation getImplant() {
        return ForgeRegistries.ITEMS.getKey(ModItems.POWERFIST.get());
    }
}
