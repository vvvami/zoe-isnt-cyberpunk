package net.vami.zoe.entity.client.model;// Made with Blockbench 5.1.4
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
import net.vami.zoe.entity.animations.custom.ArbiterAnimations;
import net.vami.zoe.entity.animations.custom.CycrawlerAnimations;
import net.vami.zoe.entity.custom.ArbiterEntity;
import net.vami.zoe.entity.custom.CycrawlerEntity;

public class CycrawlerModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation(ZoeIsntCyberpunk.MOD_ID, "cycrawler"), "main");

	public ModelPart root;
	private final ModelPart body;
	private final ModelPart leftfrill;
	private final ModelPart rightfrill;
	private final ModelPart head;
	private final ModelPart rightleg1;
	private final ModelPart leftleg1;
	private final ModelPart rightleg2;
	private final ModelPart leftleg2;

	private float opacity = 1f;

	public CycrawlerModel(ModelPart root) {
		this.root = root;
		this.body = root.getChild("body");
		this.leftfrill = this.body.getChild("leftfrill");
		this.rightfrill = this.body.getChild("rightfrill");
		this.head = this.body.getChild("head");
		this.rightleg1 = root.getChild("rightleg1");
		this.leftleg1 = root.getChild("leftleg1");
		this.rightleg2 = root.getChild("rightleg2");
		this.leftleg2 = root.getChild("leftleg2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 21).addBox(-8.0F, -11.5F, -10.0F, 16.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 73).addBox(-5.0F, 3.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(72, 43).addBox(3.0F, 3.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -10.6063F, 10.8667F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -3.75F, -7.0F, 14.0F, 8.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.8246F, 5.7262F, -0.2618F, 0.0F, 0.0F));

		PartDefinition leftfrill = body.addOrReplaceChild("leftfrill", CubeListBuilder.create(), PartPose.offset(8.0F, -10.75F, -5.0F));

		PartDefinition cube_r3 = leftfrill.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 41).mirror().addBox(1.75F, -10.0F, -10.0F, 0.0F, 12.0F, 20.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -1.75F, 7.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition rightfrill = body.addOrReplaceChild("rightfrill", CubeListBuilder.create(), PartPose.offset(-8.0F, -10.75F, -5.0F));

		PartDefinition cube_r4 = rightfrill.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 41).addBox(-1.75F, -10.0F, -10.0F, 0.0F, 12.0F, 20.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(3.0F, -1.75F, 7.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(40, 41).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -10.0F));

		PartDefinition rightleg1 = partdefinition.addOrReplaceChild("rightleg1", CubeListBuilder.create().texOffs(64, 67).addBox(-1.8907F, -1.238F, -2.2314F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.1093F, 16.238F, -5.7686F));

		PartDefinition cube_r5 = rightleg1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(52, 21).addBox(-0.9844F, -0.5F, -3.5F, 2.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0771F, 4.1769F, 1.7167F, -0.2182F, 0.0F, 0.2182F));

		PartDefinition cube_r6 = rightleg1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(40, 67).addBox(-1.0F, -3.0F, -2.0F, 2.0F, 8.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-1.3473F, 0.9311F, -0.2314F, 0.0F, 0.0F, 0.2182F));

		PartDefinition leftleg1 = partdefinition.addOrReplaceChild("leftleg1", CubeListBuilder.create().texOffs(72, 35).addBox(-0.1093F, -1.238F, -2.2314F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(8.1093F, 16.238F, -5.7686F));

		PartDefinition cube_r7 = leftleg1.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(52, 67).addBox(-1.0F, -3.0F, -2.0F, 2.0F, 8.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(1.3473F, 0.9311F, -0.2314F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cube_r8 = leftleg1.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(54, 0).addBox(-1.0313F, -0.5F, -3.5F, 2.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0924F, 4.1735F, 1.7167F, -0.2182F, 0.0F, -0.2182F));

		PartDefinition rightleg2 = partdefinition.addOrReplaceChild("rightleg2", CubeListBuilder.create(), PartPose.offset(-7.1093F, 19.238F, 6.2314F));

		PartDefinition cube_r9 = rightleg2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(40, 57).addBox(-1.2046F, 1.0478F, -3.2649F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5222F, 1.6654F, 2.6157F, 0.0873F, 0.0F, 0.2182F));

		PartDefinition cube_r10 = rightleg2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(12, 73).addBox(-1.2046F, -3.7311F, -3.8471F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-1.2261F, 1.7134F, 2.5765F, 0.3054F, 0.0F, 0.2182F));

		PartDefinition cube_r11 = rightleg2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(54, 14).addBox(-0.3685F, -3.9033F, -3.8471F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5222F, 1.6654F, 2.6157F, 0.3054F, 0.0F, 0.0F));

		PartDefinition leftleg2 = partdefinition.addOrReplaceChild("leftleg2", CubeListBuilder.create(), PartPose.offset(7.1093F, 19.238F, 6.2314F));

		PartDefinition cube_r12 = leftleg2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(60, 57).addBox(-0.7954F, 1.0478F, -3.2649F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5222F, 1.6654F, 2.6157F, 0.0873F, 0.0F, -0.2182F));

		PartDefinition cube_r13 = leftleg2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(22, 73).addBox(-0.7954F, -3.7311F, -3.8471F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(1.2261F, 1.7134F, 2.5765F, 0.3054F, 0.0F, -0.2182F));

		PartDefinition cube_r14 = leftleg2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(64, 14).addBox(-1.6315F, -3.9033F, -3.8471F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5222F, 1.6654F, 2.6157F, 0.3054F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animateWalk(CycrawlerAnimations.walk, limbSwing, limbSwingAmount, 2f, 1f);
		this.animate(((CycrawlerEntity) entity).animation_idle1, CycrawlerAnimations.idle1, ageInTicks, 1f);
		this.animate(((CycrawlerEntity) entity).animation_idle2, CycrawlerAnimations.idle2, ageInTicks, 1f);
		this.animate(((CycrawlerEntity) entity).animation_attack, CycrawlerAnimations.attack, ageInTicks, 1f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.root().render(poseStack, vertexConsumer, packedLight, packedOverlay,
				red, green, blue, alpha * this.opacity);
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}