package net.vami.zoe.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.vami.zoe.entity.ai.CycrawlerAttackGoal;
import net.vami.zoe.init.ModAttributes;

public class CycrawlerEntity extends Monster {
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(CycrawlerEntity.class, EntityDataSerializers.BOOLEAN);

    public CycrawlerEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public final AnimationState animation_idle1 = new AnimationState();
    public final AnimationState animation_idle2 = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState animation_attack = new AnimationState();
    public int attackAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) return;

        setupAnimationStates();

    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float animationSpeed = 0.0f;

        if (this.getPose() == Pose.STANDING) {
            animationSpeed = Math.min(pPartialTick * 6.0f, 1.0f);
        }

        this.walkAnimation.update(animationSpeed, 0.2f);
    }

    private void setupAnimationStates() {
        if (this.walkAnimation.speed() > 0.05F) {
            this.animation_idle1.stop();
        }

        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 60;
            if (Math.random() > 0.5f) {
                this.animation_idle2.start(this.tickCount);
            }
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.animation_idle1.isStarted()) {
            this.animation_idle1.updateTime(tickCount, 1f);
        } else {
            this.animation_idle1.start(this.tickCount);
        }

        // attack anim

        if (attackAnimationTimeout <= 0) {
            if (!this.isAttacking()) return;
            attackAnimationTimeout = 20;
            animation_attack.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new CycrawlerAttackGoal(this, 1d, false));
        this.goalSelector.addGoal(1, new MoveTowardsTargetGoal(this, 1d, 1f));
        this.goalSelector.addGoal(3, new FloatGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10d)
                .add(Attributes.MOVEMENT_SPEED, 0.5d)
                .add(ModAttributes.PLATING.get(), 10d)
                .add(Attributes.ATTACK_DAMAGE, 4d)
                .add(Attributes.FOLLOW_RANGE, 64d);
    }

}
