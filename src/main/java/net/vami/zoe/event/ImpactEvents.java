package net.vami.zoe.event;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ZoeImpactEvent;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public final class ImpactEvents {

    private static final int IMPACT_CD_TICKS = 0;
    private static final double MIN_VELOCITY = 1d;

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

        Vec3 currentVel = entity.getDeltaMovement();

        if (!state.init) {
            state.prevVel = currentVel;
            state.init = true;
            return;
        }

        if (state.cooldown > 0) {
            state.cooldown--;
            state.prevVel = currentVel;
            return;
        }


        if (entity.horizontalCollision) {
            onHorizontalCollide(
                    entity,
                    state.prevVel,
                    currentVel);
        }

        state.prevVel = currentVel;
    }

    private static void onHorizontalCollide(LivingEntity entity, Vec3 oVelocity, Vec3 velocityAfter) {
        double oSpeed = oVelocity.horizontalDistance();

        if (oSpeed < MIN_VELOCITY) return;

        double speedAfter = velocityAfter.horizontalDistance();
        double lostSpeed = oSpeed - speedAfter;
        double minLostSpeed = 0.3d;

        if (lostSpeed < minLostSpeed) return;

        float impactPwr = (float) (lostSpeed * 10 - 3);
        if (impactPwr <= 0) return;

        Vec3 impactDirection = new Vec3(
                oVelocity.x,
                0,
                oVelocity.z);

        if (impactDirection.lengthSqr() < 1.0e-8d) return;

        impactDirection = impactDirection.normalize();

        Vec3 impactPosition = new Vec3(
                entity.getX(),
                entity.getY(0.5d),
                entity.getZ())
                .add(impactDirection.scale(entity.getBbWidth() * 0.5d + 0.1d));


        float explosionPower = (float) Mth.clamp(
                Math.sqrt(impactPwr) / 2,
                1,
                (entity.getBbWidth() + entity.getBbHeight()) / 1.75f);

        ZoeImpactEvent impactEvent = new ZoeImpactEvent(entity, entity.getLastAttacker(), explosionPower);
        MinecraftForge.EVENT_BUS.post(impactEvent);

        if (impactEvent.isCanceled()) return;

        entity.invulnerableTime = 0;
        entity.level().explode(
                impactEvent.getSource(),
                impactPosition.x,
                impactPosition.y,
                impactPosition.z,
                impactEvent.getPower(),
                false,
                Level.ExplosionInteraction.MOB);

        entity.setDeltaMovement(oVelocity);
        entity.hurtMarked = true;

        ImpactState state = STATES.get(entity);

        if (state != null) {
            state.cooldown = IMPACT_CD_TICKS;

            state.prevVel = entity.getDeltaMovement();
        }
    }

    private static final class ImpactState {

        private Vec3 prevVel = Vec3.ZERO;
        private boolean init;
        private int cooldown;
    }
}