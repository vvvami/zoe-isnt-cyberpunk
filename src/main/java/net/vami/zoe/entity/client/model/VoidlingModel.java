package net.vami.zoe.entity.client.model;
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
import net.vami.zoe.entity.animations.custom.VoidlingAnimations;
import net.vami.zoe.entity.custom.VoidlingEntity;

public class VoidlingModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation(ZoeIsntCyberpunk.MOD_ID, "voidling"), "main");

	private final ModelPart root;
	public final ModelPart body;
	private final ModelPart head;
	private final ModelPart leg1;
	private final ModelPart leg4;
	private final ModelPart leg2;
	private final ModelPart leg5;
	private final ModelPart leg3;
	private final ModelPart leg6;

	public VoidlingModel(ModelPart root) {
		this.root = root;
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.leg1 = this.body.getChild("leg1");
		this.leg4 = this.body.getChild("leg4");
		this.leg2 = this.body.getChild("leg2");
		this.leg5 = this.body.getChild("leg5");
		this.leg3 = this.body.getChild("leg3");
		this.leg6 = this.body.getChild("leg6");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(44, 48).addBox(-5.0F, -9.5F, -10.0F, 10.0F, 5.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 48).addBox(-5.0F, -12.5F, 2.0F, 10.0F, 8.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(34, 85).addBox(-4.0F, -13.5F, 4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(96, 97).addBox(3.25F, -9.0F, -9.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 100).addBox(3.25F, -9.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(102, 75).addBox(3.25F, -10.5F, 6.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(96, 105).addBox(-7.25F, -10.5F, 6.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 106).addBox(-7.25F, -9.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 106).addBox(-7.25F, -9.0F, -9.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(34, 68).addBox(-8.25F, -9.0F, -9.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 108).addBox(-8.25F, -9.0F, -2.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(48, 109).addBox(-8.25F, -10.5F, 6.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(58, 109).addBox(7.25F, -10.5F, 6.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(68, 109).addBox(7.25F, -9.0F, -2.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(78, 109).addBox(7.25F, -9.0F, -9.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(66, 85).addBox(-6.75F, -5.75F, -5.0F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(3.25F, -4.5F, -9.5F));

		PartDefinition leg1 = body.addOrReplaceChild("leg1", CubeListBuilder.create(), PartPose.offset(-5.5F, -7.0F, -6.0F));

		PartDefinition cube_r1 = leg1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-53.6302F, -4.1605F, -2.0F, 56.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.8373F, -5.9504F, -6.0F, 0.2849F, -0.274F, -0.8249F));

		PartDefinition cube_r2 = leg1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 88).addBox(-5.2767F, -8.193F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(44, 65).addBox(0.5674F, -7.7233F, -2.5F, 12.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.8373F, -5.9504F, -6.0F, -0.2849F, -0.274F, 0.8249F));

		PartDefinition leg4 = body.addOrReplaceChild("leg4", CubeListBuilder.create(), PartPose.offset(5.5F, -7.0F, -6.0F));

		PartDefinition cube_r3 = leg4.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.3698F, -4.1605F, -2.0F, 56.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(13.8373F, -5.9504F, -6.0F, 0.2849F, 0.274F, 0.8249F));

		PartDefinition cube_r4 = leg4.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(24, 94).addBox(-0.7233F, -8.193F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(68, 75).addBox(-12.5674F, -7.7233F, -2.5F, 12.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.8373F, -5.9504F, -6.0F, -0.2849F, 0.274F, -0.8249F));

		PartDefinition leg2 = body.addOrReplaceChild("leg2", CubeListBuilder.create(), PartPose.offset(-5.5F, -7.0F, 0.0F));

		PartDefinition cube_r5 = leg2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 24).addBox(-53.0F, -1.0F, -3.0F, 56.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.5178F, -7.7396F, 1.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r6 = leg2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(88, 48).addBox(0.0F, -3.5F, -2.5F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.25F, -13.0F, -0.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r7 = leg2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 68).addBox(-6.0F, -2.5F, -2.5F, 12.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, -5.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition leg5 = body.addOrReplaceChild("leg5", CubeListBuilder.create(), PartPose.offset(5.5F, -7.0F, 0.0F));

		PartDefinition cube_r8 = leg5.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 24).mirror().addBox(-3.0F, -1.0F, -3.0F, 56.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(16.5178F, -7.7396F, 1.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r9 = leg5.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(48, 97).addBox(-6.0F, -3.5F, -2.5F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.25F, -13.0F, -0.5F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r10 = leg5.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 78).addBox(-6.0F, -2.5F, -2.5F, 12.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -5.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition leg3 = body.addOrReplaceChild("leg3", CubeListBuilder.create(), PartPose.offset(-5.5F, -8.0F, 8.0F));

		PartDefinition cube_r11 = leg3.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-55.6302F, -4.1605F, -2.0F, 58.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.8373F, -5.9504F, 5.0F, -0.2849F, 0.274F, -0.8249F));

		PartDefinition cube_r12 = leg3.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(92, 85).addBox(-5.2767F, -8.193F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(34, 75).addBox(0.5674F, -7.7233F, -2.5F, 12.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.8373F, -5.9504F, 5.0F, 0.2849F, 0.274F, 0.8249F));

		PartDefinition leg6 = body.addOrReplaceChild("leg6", CubeListBuilder.create(), PartPose.offset(5.5F, -8.0F, 8.0F));

		PartDefinition cube_r13 = leg6.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.3698F, -4.1605F, -2.0F, 58.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(13.8373F, -5.9504F, 5.0F, -0.2849F, -0.274F, 0.8249F));

		PartDefinition cube_r14 = leg6.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(72, 97).addBox(-0.7233F, -8.193F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(78, 65).addBox(-12.5674F, -7.7233F, -2.5F, 12.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.8373F, -5.9504F, 5.0F, 0.2849F, -0.274F, -0.8249F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(((VoidlingEntity) entity).idle1, VoidlingAnimations.idle1, ageInTicks, 1f);
		this.animate(((VoidlingEntity) entity).idle2, VoidlingAnimations.idle2, ageInTicks, 1f);
		this.animate(((VoidlingEntity) entity).idle3, VoidlingAnimations.idle3, ageInTicks, 1f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}