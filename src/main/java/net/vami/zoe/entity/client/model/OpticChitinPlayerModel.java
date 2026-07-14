package net.vami.zoe.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.vami.zoe.entity.client.renderer.OpticChitinPlayerRenderer;

public class OpticChitinPlayerModel extends PlayerModel<AbstractClientPlayer> {
    private final OpticChitinPlayerRenderer renderer;

    public OpticChitinPlayerModel(
            ModelPart root,
            boolean slim,
            OpticChitinPlayerRenderer renderer
    ) {
        super(root, slim);
        this.renderer = renderer;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, red, green, blue,
                alpha * renderer.getOpacity());
    }
}