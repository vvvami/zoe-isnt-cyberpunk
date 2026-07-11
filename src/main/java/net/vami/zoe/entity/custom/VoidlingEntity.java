package net.vami.zoe.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.entity.ModEntities;
import net.vami.zoe.entity.SmoothBodyRotationControl;
import net.vami.zoe.entity.SmoothMoveControl;
import net.vami.zoe.entity.goal.DistantWalkGoal;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.network.ModPackets;
import net.vami.zoe.network.packet.VoidlingPacket;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

public class VoidlingEntity extends Monster {
    private final VoidlingPart head;
    private final VoidlingPart body;
    private final VoidlingPart leg1;
    private final VoidlingPart leg2;
    private final VoidlingPart leg3;
    private final VoidlingPart leg4;
    private final VoidlingPart leg5;
    private final VoidlingPart leg6;

    private final PartEntity<?>[] parts;

    public static final float SCALE = 110f;

    @Nullable
    private ChunkPos forcedChunk;

    public VoidlingEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);

        this.head = new VoidlingPart(this, "head", 0.5F, 0.5F,
                0d, 2d, 0.75d);
        this.body = new VoidlingPart(this, "body", 1.4F, 0.6F,
                0d, 2d, -0.25d);
        this.leg1 = new VoidlingPart(this, "leg1", 0.6F, 0.5F,
                -1d, 2.65d, 0.7d);
        this.leg2 = new VoidlingPart(this, "leg2", 0.6F, 0.5F,
                -1d, 2.65d, 0d);
        this.leg3 = new VoidlingPart(this, "leg3", 0.6F, 0.5F,
                -1d, 2.7d, -0.75d);
        this.leg4 = new VoidlingPart(this, "leg4", 0.6F, 0.5F,
                1d, 2.65d, 0.7d);
        this.leg5 = new VoidlingPart(this, "leg5", 0.6F, 0.5F,
                1d, 2.65d, 0d);
        this.leg6 = new VoidlingPart(this, "leg6", 0.6F, 0.5F,
                1d, 2.7d, -0.75d);

        this.parts = new PartEntity<?>[] {
                this.head,
                this.body,
                this.leg1,
                this.leg2,
                this.leg3,
                this.leg4,
                this.leg5,
                this.leg6
        };

        this.noCulling = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            setupAnimationStates();
        } else {
            if (this.tickCount % 20 == 0) {
                updateForcedChunk();
                sendDistantRenderSnapshot();
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        Vec3[] oldPos = new Vec3[this.parts.length];

        for (int i = 0; i < this.parts.length; i++) {
            oldPos[i] = this.parts[i].position();
        }

        partsTick();

        for (int i = 0; i < this.parts.length; i++) {
            PartEntity<?> part = this.parts[i];
            Vec3 prev = oldPos[i];

            part.xo = prev.x;
            part.yo = prev.y;
            part.zo = prev.z;

            part.xOld = prev.x;
            part.yOld = prev.y;
            part.zOld = prev.z;
        }
    }

    @Override
    public void setId(int id) {
        super.setId(id);

        if (this.parts != null) {
            for (int i = 0; i < this.parts.length; i++) {
                this.parts[i].setId(id + i + 1);
            }
        }
    }

    private void partsTick() {
        for (PartEntity<?> part : parts) {
            if (!(part instanceof VoidlingPart voidPart)) return;
            movePart(voidPart, voidPart.getOffset().x, voidPart.getOffset().y, voidPart.getOffset().z);
        }
    }

    private void movePart(VoidlingPart part, double x, double y, double z) {
        x *= SCALE;
        y *= SCALE;
        z *= SCALE;

        float yawRad = this.getYRot() * Mth.DEG_TO_RAD;

        double sin = Mth.sin(yawRad);
        double cos = Mth.cos(yawRad);

        double rotX = x * cos - z * sin;
        double rotZ = x * sin + z * cos;

        part.setPos(this.getX() + rotX, this.getY() + y, this.getZ() + rotZ);
    }

    public boolean hurtPart(VoidlingPart part, DamageSource source, float amount) {
        if (this.level().isClientSide) {
            return false;
        }

        return this.hurt(source, amount);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.IS_PROJECTILE)
        || pSource.is(DamageTypes.FALL)) {
            return false;
        }

        return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {

    }

    private void setupAnimationStates() {

    }

    @Override
    protected void registerGoals() {

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100000d)
                .add(Attributes.MOVEMENT_SPEED, 0.5d)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0d)
                .add(ModAttributes.PLATING.get(), 1000d)
                .add(Attributes.ATTACK_DAMAGE, 1d)
                .add(Attributes.ATTACK_KNOCKBACK, 20d)
                .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 100d);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.body.getBoundingBox().inflate(500d);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }


    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public PartEntity<?>[] getParts() {
        return this.parts;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    private void updateForcedChunk() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        ChunkPos currentChunk = this.chunkPosition();

        if (currentChunk.equals(this.forcedChunk)) {
            return;
        }

        if (this.forcedChunk != null) {
            serverLevel.setChunkForced(
                    this.forcedChunk.x,
                    this.forcedChunk.z,
                    false
            );
        }

        serverLevel.setChunkForced(
                currentChunk.x,
                currentChunk.z,
                true
        );

        this.forcedChunk = currentChunk;
    }

    private void releaseForcedChunk() {
        if (this.level() instanceof ServerLevel serverLevel
                && this.forcedChunk != null) {

            serverLevel.setChunkForced(
                    this.forcedChunk.x,
                    this.forcedChunk.z,
                    false
            );

            this.forcedChunk = null;
        }
    }

    private void sendDistantRenderSnapshot() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        VoidlingPacket packet =
                VoidlingPacket.active(
                        this.getUUID(),
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        this.getYRot(),
                        this.getXRot(),
                        this.yBodyRot,
                        this.tickCount
                );

        ModPackets.INSTANCE.send(
                PacketDistributor.DIMENSION.with(
                        serverLevel::dimension
                ),
                packet
        );
    }

    private boolean shouldRemoveDistantGhost(RemovalReason reason) {
        return reason != RemovalReason.UNLOADED_TO_CHUNK;
    }

    @Override
    public void remove(RemovalReason reason) {
        if (this.level() instanceof ServerLevel serverLevel
                && shouldRemoveDistantGhost(reason)) {

            ModPackets.INSTANCE.send(
                    PacketDistributor.DIMENSION.with(
                            serverLevel::dimension
                    ),
                    VoidlingPacket.removed(
                            this.getUUID()
                    )
            );

            releaseForcedChunk();
        }

        super.remove(reason);
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

        if (!this.level().isClientSide) {
            sendDistantRenderSnapshot();
        }
    }

    @Mod.EventBusSubscriber(
            modid = ZoeIsntCyberpunk.MOD_ID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )

    private final class Spawn {

        @SubscribeEvent
        public static void onWorldCreated(LevelEvent.CreateSpawnPosition event) {
            if (!(event.getLevel() instanceof ServerLevel level)) {
                return;
            }

            if (level.dimension() != Level.OVERWORLD) {
                return;
            }

            VoidlingEntity entity = ModEntities.VOIDLING.get().create(level);

            if (entity == null) {
                return;
            }

            Random random = new Random();

            int x = random.nextInt(-200, 200);
            int z = random.nextInt(-200, 200);

            level.getChunk(x >> 4, z >> 4);

            int y = level.getHeight(
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    x,
                    z
            );

            entity.moveTo(
                    x + 0.5,
                    y,
                    z + 0.5,
                    0.0F,
                    0.0F
            );

            level.addFreshEntity(entity);
        }
    }
}
