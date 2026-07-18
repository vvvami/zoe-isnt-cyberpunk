package net.vami.zoe.layer.model;
// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.vami.zoe.ZoeIsntCyberpunk;

public class EaraLayer<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION =
			new ModelLayerLocation(new ResourceLocation(ZoeIsntCyberpunk.MOD_ID, "eara_layer"), "main");

	private final ModelPart root;

	public final ModelPart body;
	private final ModelPart right_wing;
	private final ModelPart right_wing_inner;
	private final ModelPart right_wing_outer;
	private final ModelPart left_wing;
	private final ModelPart left_wing_inner;
	private final ModelPart left_wing_outer;

	public EaraLayer(ModelPart root) {
		this.root = root;
		this.body = root.getChild("body");
		this.right_wing = this.body.getChild("right_wing");
		this.right_wing_inner = this.right_wing.getChild("right_wing_inner");
		this.right_wing_outer = this.right_wing_inner.getChild("right_wing_outer");
		this.left_wing = this.body.getChild("left_wing");
		this.left_wing_inner = this.left_wing.getChild("left_wing_inner");
		this.left_wing_outer = this.left_wing_inner.getChild("left_wing_outer");
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(-1.0F, 1.0F, 0.0F));

		PartDefinition right_wing = body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(37, 8).addBox(4.3344F, -3.4687F, -5.8475F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.3344F, 5.2187F, 7.8475F));

		PartDefinition right_wing_inner = right_wing.addOrReplaceChild("right_wing_inner", CubeListBuilder.create(), PartPose.offset(3.9892F, -1.0937F, -3.3904F));

		PartDefinition right_wing3_r1 = right_wing_inner.addOrReplaceChild("right_wing3_r1", CubeListBuilder.create().texOffs(32, 27).addBox(-0.4375F, -6.75F, -0.5625F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8645F, 4.9375F, 1.0821F, 0.0F, -0.7854F, 0.0F));

		PartDefinition right_wing2_r1 = right_wing_inner.addOrReplaceChild("right_wing2_r1", CubeListBuilder.create().texOffs(25, 35).addBox(-0.4375F, -3.75F, -0.5625F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5497F, 0.9375F, -0.3321F, 0.0F, -0.7854F, 0.0F));

		PartDefinition right_wing_outer = right_wing_inner.addOrReplaceChild("right_wing_outer", CubeListBuilder.create(), PartPose.offset(-7.6076F, 1.1042F, 4.63F));

		PartDefinition right_wing6_r1 = right_wing_outer.addOrReplaceChild("right_wing6_r1", CubeListBuilder.create().texOffs(25, 19).addBox(-0.4375F, -9.5F, -1.125F, 1.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.4452F, 4.5833F, 1.2213F, 0.0F, -1.1781F, 0.0F));

		PartDefinition right_wing5_r1 = right_wing_outer.addOrReplaceChild("right_wing5_r1", CubeListBuilder.create().texOffs(1, 19).addBox(-0.4375F, -9.5F, -1.125F, 1.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2503F, 1.5833F, -0.3094F, 0.0F, -1.1781F, 0.0F));

		PartDefinition right_wing4_r1 = right_wing_outer.addOrReplaceChild("right_wing4_r1", CubeListBuilder.create().texOffs(1, 0).addBox(-0.4375F, -9.5F, -1.125F, 1.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.8697F, 0.5833F, -2.2228F, 0.0F, -1.1781F, 0.0F));

		PartDefinition left_wing = body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(1, 37).addBox(-5.8211F, -3.9688F, -3.5683F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(7.1961F, 5.7188F, 5.5371F));

		PartDefinition left_wing_inner = left_wing.addOrReplaceChild("left_wing_inner", CubeListBuilder.create(), PartPose.offset(-4.5004F, -1.5938F, -1.1701F));

		PartDefinition left_wing3_r1 = left_wing_inner.addOrReplaceChild("left_wing3_r1", CubeListBuilder.create().texOffs(33, 16).addBox(-0.4375F, -6.75F, -0.5625F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7639F, 4.9375F, 1.141F, 0.0F, 0.7854F, 0.0F));

		PartDefinition left_wing2_r1 = left_wing_inner.addOrReplaceChild("left_wing2_r1", CubeListBuilder.create().texOffs(37, 0).addBox(-0.4375F, -3.75F, -0.5625F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6503F, 0.9375F, -0.2732F, 0.0F, 0.7854F, 0.0F));

		PartDefinition left_wing_outer = left_wing_inner.addOrReplaceChild("left_wing_outer", CubeListBuilder.create(), PartPose.offset(7.6676F, 0.8542F, 4.4924F));

		PartDefinition left_wing6_r1 = left_wing_outer.addOrReplaceChild("left_wing6_r1", CubeListBuilder.create().texOffs(29, 0).addBox(-0.4375F, -9.5F, -1.125F, 1.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3251F, 4.8333F, 1.445F, 0.0F, 1.1781F, 0.0F));

		PartDefinition left_wing5_r1 = left_wing_outer.addOrReplaceChild("left_wing5_r1", CubeListBuilder.create().texOffs(13, 19).addBox(-0.4375F, -9.5F, -1.125F, 1.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3704F, 1.8333F, -0.0858F, 0.0F, 1.1781F, 0.0F));

		PartDefinition left_wing4_r1 = left_wing_outer.addOrReplaceChild("left_wing4_r1", CubeListBuilder.create().texOffs(15, 0).addBox(-0.4375F, -9.5F, -1.125F, 1.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.9898F, 0.8333F, -1.9992F, 0.0F, 1.1781F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}