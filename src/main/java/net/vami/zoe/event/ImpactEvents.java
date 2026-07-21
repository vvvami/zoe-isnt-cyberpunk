package net.vami.zoe.event;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.util.HurtUtil;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public final class ImpactEvents {

    private static final int IMPACT_COOLDOWN_TICKS = 10;
    private static final double MIN_IMPACT_VELOCITY = 0.7d;

    private static final Map<LivingEntity, ImpactState> STATES =
            new WeakHashMap<>();

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();

        if (level.isClientSide()) return;

//        if (!entity.getPersistentData().getBoolean("zCanImpact")) {
//            STATES.remove(entity);
//            return;
//        }

        ImpactState state = STATES.computeIfAbsent(
                entity,
                ignored -> new ImpactState());

        Vec3 currentVelocity = entity.getDeltaMovement();

        if (!state.initialized) {
            state.previousVelocity = currentVelocity;
            state.initialized = true;
            return;
        }

        if (state.cooldown > 0) {
            state.cooldown--;
            state.previousVelocity = currentVelocity;
            return;
        }


        if (entity.horizontalCollision) {
            handleHorizontalImpact(
                    entity,
                    state.previousVelocity,
                    currentVelocity);
        }

        state.previousVelocity = currentVelocity;
    }

    private static void handleHorizontalImpact(LivingEntity entity, Vec3 velocityBefore, Vec3 velocityAfter) {
        double speedBefore = velocityBefore.horizontalDistance();

        if (speedBefore < MIN_IMPACT_VELOCITY) return;

        double speedAfter = velocityAfter.horizontalDistance();
        double lostSpeed = speedBefore - speedAfter;
        double minimumLostSpeed = 0.3d;

        if (lostSpeed < minimumLostSpeed) return;

        float impactSeverity = (float) (lostSpeed * 10d - 3d);
        if (impactSeverity <= 0) return;

        Vec3 impactDirection = new Vec3(
                velocityBefore.x,
                0.0D,
                velocityBefore.z);

        if (impactDirection.lengthSqr() < 1.0E-8D) return;

        impactDirection = impactDirection.normalize();

        Vec3 impactPosition = new Vec3(
                entity.getX(),
                entity.getY(0.5d),
                entity.getZ())
                .add(impactDirection.scale(entity.getBbWidth() * 0.5d + 0.1d));

        float explosionPower = (float) Mth.clamp(
                Math.sqrt(impactSeverity) / 2f,
                1f,
                entity.getBbWidth() + entity.getBbHeight()
        );

        entity.invulnerableTime = 0;
        entity.level().explode(
                entity.getLastAttacker(),
                impactPosition.x,
                impactPosition.y,
                impactPosition.z,
                explosionPower,
                false,
                Level.ExplosionInteraction.MOB);

        ImpactState state = STATES.get(entity);

        if (state != null) {
            state.cooldown = IMPACT_COOLDOWN_TICKS;


            state.previousVelocity = entity.getDeltaMovement();
            entity.setDeltaMovement(0,0,0);
        }
    }

    private static final class ImpactState {

        private Vec3 previousVelocity = Vec3.ZERO;
        private boolean initialized;
        private int cooldown;
    }
}