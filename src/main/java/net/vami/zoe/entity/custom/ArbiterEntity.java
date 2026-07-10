package net.vami.zoe.entity.custom;

import com.mojang.datafixers.types.templates.Tag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.vami.zoe.entity.SmoothBodyRotationControl;
import net.vami.zoe.entity.SmoothMoveControl;
import net.vami.zoe.init.ModAttributes;

public class ArbiterEntity extends Monster {

    public ArbiterEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new SmoothMoveControl(this);
    }

    public final AnimationState animation_idle1 = new AnimationState();
    public final AnimationState animation_idle2 = new AnimationState();
    private int idleAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) return;

        setupAnimationStates();

    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.IS_PROJECTILE)) {
            return false;
        }

        return super.hurt(pSource, pAmount);
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
        boolean moving = this.walkAnimation.speed() > 0.05F;

        if (moving) {
            this.animation_idle1.stop();
            this.animation_idle2.stop();
            this.idleAnimationTimeout = 0;
            return;
        }

        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(100) + 480;
            if (Math.random() > 0.5f) {
                this.animation_idle1.start(this.tickCount);
            } else {
                this.animation_idle2.start(this.tickCount);
            }
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1,
                new MeleeAttackGoal(this, 1d, true));
        this.goalSelector.addGoal(1, new MoveTowardsTargetGoal(this, 1.0D, 1.0F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(ModAttributes.PLATING.get(), 20D)
                .add(Attributes.ATTACK_DAMAGE, 20D)
                .add(Attributes.ATTACK_KNOCKBACK, 2D);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmoothBodyRotationControl(this);
    }

}
