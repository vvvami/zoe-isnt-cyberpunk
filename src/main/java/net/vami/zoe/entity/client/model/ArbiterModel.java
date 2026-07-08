package net.vami.zoe.entity.client.model;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.entity.animations.custom.ArbiterAnimations;
import net.vami.zoe.entity.custom.ArbiterEntity;

public class ArbiterModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ZoeIsntCyberpunk.MOD_ID, "arbiter"), "main");

	private final ModelPart body;
	private final ModelPart neck;
	private final ModelPart head;
	private final ModelPart leg1;
	private final ModelPart leg1_lower;
	private final ModelPart leg2;
	private final ModelPart leg2_lower;
	private final ModelPart leg3;
	private final ModelPart leg3_lower;
	private final ModelPart leg4;
	private final ModelPart leg4_lower;

	public ArbiterModel(ModelPart root) {
		this.body = root.getChild("body");
		this.neck = this.body.getChild("neck");
		this.head = this.neck.getChild("head");
		this.leg1 = this.body.getChild("leg1");
		this.leg1_lower = this.leg1.getChild("leg1_lower");
		this.leg2 = this.body.getChild("leg2");
		this.leg2_lower = this.leg2.getChild("leg2_lower");
		this.leg3 = this.body.getChild("leg3");
		this.leg3_lower = this.leg3.getChild("leg3_lower");
		this.leg4 = this.body.getChild("leg4");
		this.leg4_lower = this.leg4.getChild("leg4_lower");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.192F, -3.3412F, -17.5369F, 14.0F, 11.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(88, 43).addBox(1.798F, 1.4603F, -6.6158F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(1.192F, -6.6588F, 1.5369F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(32, 87).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.202F, 5.4603F, 13.3842F, 0.2182F, 0.0F, -0.2618F));

		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(80, 63).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.202F, -1.5397F, 2.3842F, -0.2618F, 0.0F, 0.3491F));

		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 43).addBox(-5.0F, -25.0F, -5.0F, 10.0F, 24.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.192F, -2.3412F, -11.5369F));

		PartDefinition cube_r3 = neck.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(80, 79).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.99F, -9.1985F, -0.0788F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r4 = neck.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(40, 43).addBox(-7.0F, 0.0F, -5.0F, 12.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.99F, -38.6545F, -10.9588F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r5 = neck.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(40, 63).addBox(-5.0F, -6.0F, -5.0F, 10.0F, 14.0F, 10.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -30.4776F, -2.6809F, 0.3927F, 0.0F, 0.0F));

		PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 77).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.01F, -35.1985F, -10.0788F));

		PartDefinition leg1 = body.addOrReplaceChild("leg1", CubeListBuilder.create(), PartPose.offset(-7.692F, -1.2434F, -16.0148F));

		PartDefinition cube_r6 = leg1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(64, 87).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 19.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 2.9022F, -1.0222F, 0.0F, -0.1963F, 0.0F));

		PartDefinition leg1_lower = leg1.addOrReplaceChild("leg1_lower", CubeListBuilder.create(), PartPose.offset(-1.0F, 19.9022F, -1.0222F));

		PartDefinition cube_r7 = leg1_lower.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(100, 95).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.1955F, 0.0443F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r8 = leg1_lower.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(64, 87).addBox(-1.5F, 3.0F, -1.5F, 3.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, -0.1963F, 0.0F));

		PartDefinition leg2 = body.addOrReplaceChild("leg2", CubeListBuilder.create(), PartPose.offset(-7.6155F, -1.2108F, 13.7104F));

		PartDefinition cube_r9 = leg2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(92, 0).addBox(-2.0F, -16.0F, -1.0F, 3.0F, 16.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5765F, 16.8697F, -0.2473F, 0.0F, 0.3927F, 0.0F));

		PartDefinition leg2_lower = leg2.addOrReplaceChild("leg2_lower", CubeListBuilder.create(), PartPose.offset(-1.0765F, 15.0652F, 1.297F));

		PartDefinition cube_r10 = leg2_lower.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(104, 18).addBox(-2.5F, -0.1522F, -1.7346F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r11 = leg2_lower.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(92, 0).addBox(-2.0F, -15.0F, -1.0F, 3.0F, 15.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 16.8045F, -1.5443F, 0.0F, 0.3927F, 0.0F));

		PartDefinition cube_r12 = leg2_lower.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(20, 103).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition leg3 = body.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(76, 95).addBox(-0.5F, -0.1303F, -2.5296F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(5.308F, -1.2108F, -16.0074F));

		PartDefinition leg3_lower = leg3.addOrReplaceChild("leg3_lower", CubeListBuilder.create().texOffs(76, 95).addBox(-1.5F, -0.1955F, -1.5443F, 3.0F, 23.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 9.0652F, -0.9852F));

		PartDefinition cube_r13 = leg3_lower.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(104, 9).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r14 = leg3_lower.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 101).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 19.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition leg4 = body.addOrReplaceChild("leg4", CubeListBuilder.create(), PartPose.offset(5.3437F, 0.7892F, 14.3553F));

		PartDefinition cube_r15 = leg4.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(88, 95).addBox(-2.0F, -15.0F, -1.0F, 3.0F, 15.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4643F, 14.8697F, 0.1078F, 0.0F, 0.1963F, 0.0F));

		PartDefinition cube_r16 = leg4.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(40, 103).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9643F, 10.0652F, -0.3479F, -0.3927F, 0.0F, 0.0F));

		PartDefinition leg4_lower = leg4.addOrReplaceChild("leg4_lower", CubeListBuilder.create(), PartPose.offset(1.4643F, 15.8697F, 0.1078F));

		PartDefinition cube_r17 = leg4_lower.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(104, 0).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.1955F, 0.5443F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r18 = leg4_lower.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(88, 95).addBox(-2.0F, -15.0F, -1.0F, 3.0F, 15.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.0F, 0.0F, 0.0F, 0.1963F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.animateWalk(ArbiterAnimations.walk, limbSwing, limbSwingAmount, 16f, 20f);
		this.animate(((ArbiterEntity) entity).animation_idle1, ArbiterAnimations.idle1, ageInTicks, 1f);
		this.animate(((ArbiterEntity) entity).animation_idle2, ArbiterAnimations.idle2, ageInTicks, 1f);

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.body;
	}
}