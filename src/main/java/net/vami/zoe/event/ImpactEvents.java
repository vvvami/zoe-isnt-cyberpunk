package net.vami.zoe.event;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ZoeBlockImpactEvent;
import net.vami.zoe.event.custom.ZoeEntityImpactEvent;
import net.vami.zoe.util.HurtUtil;

import java.util.*;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public final class ImpactEvents {

    private static final int IMPACT_CD_TICKS = 0;
    private static final double MIN_VELOCITY = 1.2d;

    private static final double CONTACT_PADDING = 0.05d;
    private static final double SEARCH_PADDING = 0.2d;
    private static final double COLLISION_EPSILON = 1e-8d;

    private static final double BLOCK_PROBE_PADDING = 0.05d;
    private static final double BLOCK_PROBE_EPSILON = 1e-3d;
    private static final double MAX_BLOCK_PROBE_DISTANCE = 2d;

    private static final Map<LivingEntity, ImpactState> STATES =
            new WeakHashMap<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        ImpactState state = STATES.computeIfAbsent(
                event.getEntity(),
                ignored -> new ImpactState());
        state.cooldown = 20;
    }

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();

        if (level.isClientSide()) return;

        ImpactState state = STATES.computeIfAbsent(
                entity,
                ignored -> new ImpactState());

        Vec3 currentVel = entity.getDeltaMovement();
        Vec3 currentCenter = entity.getBoundingBox().getCenter();

        if (!state.init) {
            state.prevVel = currentVel;
            state.prevCenter = currentCenter;
            state.init = true;


//            updateEntityContacts(
//                    entity,
//                    state,
//                    false,
//                    Vec3.ZERO);
//
//            return;
        }

        Vec3 actualMovement = currentCenter.subtract(state.prevCenter);

        if (state.cooldown > 0) {
            state.cooldown--;

//            updateEntityContacts(
//                    entity,
//                    state,
//                    false,
//                    actualMovement);

            state.prevVel = currentVel;
            state.prevCenter = currentCenter;
            state.prevActualMovement = actualMovement;
            return;
        }

        Vec3 collisionVelocity = state.prevVel;

        if (state.prevActualMovement.horizontalDistanceSqr()
                > collisionVelocity.horizontalDistanceSqr()) {
            collisionVelocity = state.prevActualMovement;
        }

        boolean hitHorizontalBlock =
                entity.horizontalCollision
                        || isPlayerColliding(entity, collisionVelocity);


        if (hitHorizontalBlock) {
            onHorizontalCollide(
                    entity,
                    collisionVelocity,
                    actualMovement
            );
        }

//        updateEntityContacts(
//                entity,
//                state,
//                true,
//                actualMovement);

        state.prevActualMovement = actualMovement;
        state.prevVel = entity.getDeltaMovement();
        state.prevCenter = entity.getBoundingBox().getCenter();
    }

    private static boolean isPlayerColliding(LivingEntity entity, Vec3 movement) {
        double horizontalSpeed = movement.horizontalDistance();

        if (horizontalSpeed < 1e-8d) return false;

        double directionX = movement.x / horizontalSpeed;
        double directionZ = movement.z / horizontalSpeed;

        double probeDistance = Mth.clamp(
                horizontalSpeed + BLOCK_PROBE_PADDING,
                0.2d,
                MAX_BLOCK_PROBE_DISTANCE);

        AABB box = entity.getBoundingBox();

        double minY = box.minY + BLOCK_PROBE_EPSILON;
        double maxY = box.maxY - BLOCK_PROBE_EPSILON;

        if (Math.abs(directionX) > 1e-6d) {
            double xDistance =
                    Math.abs(directionX) * probeDistance
                            + BLOCK_PROBE_EPSILON;

            AABB xProbe;

            if (directionX > 0) {
                xProbe = new AABB(
                        box.maxX - BLOCK_PROBE_EPSILON,
                        minY,
                        box.minZ + BLOCK_PROBE_EPSILON,

                        box.maxX + xDistance,
                        maxY,
                        box.maxZ - BLOCK_PROBE_EPSILON
                );
            } else {
                xProbe = new AABB(
                        box.minX - xDistance,
                        minY,
                        box.minZ + BLOCK_PROBE_EPSILON,

                        box.minX + BLOCK_PROBE_EPSILON,
                        maxY,
                        box.maxZ - BLOCK_PROBE_EPSILON
                );
            }

            if (hasBlockCollision(entity, xProbe)) return true;
        }

        if (Math.abs(directionZ) > 1e-6d) {
            double zDistance =
                    Math.abs(directionZ) * probeDistance
                            + BLOCK_PROBE_EPSILON;

            AABB zProbe;

            if (directionZ > 0) {
                zProbe = new AABB(
                        box.minX + BLOCK_PROBE_EPSILON,
                        minY,
                        box.maxZ - BLOCK_PROBE_EPSILON,

                        box.maxX - BLOCK_PROBE_EPSILON,
                        maxY,
                        box.maxZ + zDistance);
            } else {
                zProbe = new AABB(
                        box.minX + BLOCK_PROBE_EPSILON,
                        minY,
                        box.minZ - zDistance,

                        box.maxX - BLOCK_PROBE_EPSILON,
                        maxY,
                        box.minZ + BLOCK_PROBE_EPSILON);
            }
            return hasBlockCollision(entity, zProbe);
        }

        return false;
    }

    private static boolean hasBlockCollision(LivingEntity entity, AABB box) {
        return entity.level()
                .getBlockCollisions(entity, box)
                .iterator()
                .hasNext();
    }

    private static void onHorizontalCollide(LivingEntity entity, Vec3 oVelocity, Vec3 velocityAfter) {
        double oSpeed = oVelocity.horizontalDistance();

        if (oSpeed < MIN_VELOCITY) return;

        double speedAfter = velocityAfter.horizontalDistance();
        double lostSpeed = oSpeed - speedAfter;
        double minLostSpeed = 0.3;

        if (lostSpeed < minLostSpeed) return;

        float impactPwr = (float) (lostSpeed * 10 - 3);
        if (impactPwr <= 0) return;

        Vec3 impactDir = new Vec3(
                oVelocity.x,
                0,
                oVelocity.z);

        if (impactDir.lengthSqr() < 1e-8) return;

        double distance = Math.max(
                1,
                Math.max(entity.getBbWidth(), entity.getBbHeight()) * 0.5 + 0.1);

        Vec3 movementDir = oVelocity.normalize();

        Vec3 impactPos = new Vec3(
                entity.getX(),
                entity.getY(0.5),
                entity.getZ())
                .add(movementDir.scale(distance));


        float explosionPower = (float) Mth.clamp(
                Math.sqrt(impactPwr) / 2,
                1,
                (entity.getBbWidth() + entity.getBbHeight()) / 1.75f);

        ZoeBlockImpactEvent impactEvent = new ZoeBlockImpactEvent(
                entity, entity.getLastAttacker(),
                explosionPower, 4f);
        MinecraftForge.EVENT_BUS.post(impactEvent);

        if (impactEvent.isCanceled()) return;

        DamageSource damageSource = impactEvent.getSource() != null ?
                HurtUtil.get(impactEvent.getSource(), DamageTypes.FLY_INTO_WALL) :
                HurtUtil.get(entity.level(), DamageTypes.FLY_INTO_WALL);

        LivingEntity sourceEntity = impactEvent.getSource() == null ? entity : impactEvent.getSource();

        entity.level().explode(sourceEntity,
                damageSource,
                new ExplosionDamageCalculator(),
                impactPos.x,
                impactPos.y,
                impactPos.z,
                impactEvent.getPower(),
                false,
                Level.ExplosionInteraction.MOB);

        entity.invulnerableTime = 0;

        entity.hurt(damageSource,
                impactEvent.getDamage());

        entity.setDeltaMovement(oVelocity.scale(0.75f));
        entity.hasImpulse = true;
        entity.hurtMarked = true;

        ImpactState state = STATES.get(entity);

        if (state != null) {
            state.cooldown = IMPACT_CD_TICKS;

            state.prevVel = entity.getDeltaMovement();
        }
    }

    private static final class ImpactState {

        private Vec3 prevVel = Vec3.ZERO;
        private Vec3 prevCenter = Vec3.ZERO;

        private boolean init;
        private int cooldown;

        private Vec3 prevActualMovement = Vec3.ZERO;

        // Reused and swapped instead of allocating a new HashSet every tick.
        private IntSet entityContacts = new IntOpenHashSet();
        private IntSet nextEntityContacts = new IntOpenHashSet();
    }

    private static void updateEntityContacts(
            LivingEntity entity,
            ImpactState state,
            boolean allowImpact,
            Vec3 actualMovement) {

        AABB currentBox = entity.getBoundingBox();
        AABB contactBox = currentBox.inflate(CONTACT_PADDING);

        /*
         * When impacts are disabled, crossed entities do not matter because only
         * entities touching at the end of the tick are retained as contacts.
         */
        AABB searchBox;

        if (allowImpact) {
            searchBox = new AABB(
                    Math.min(currentBox.minX, currentBox.minX - actualMovement.x),
                    Math.min(currentBox.minY, currentBox.minY - actualMovement.y),
                    Math.min(currentBox.minZ, currentBox.minZ - actualMovement.z),

                    Math.max(currentBox.maxX, currentBox.maxX - actualMovement.x),
                    Math.max(currentBox.maxY, currentBox.maxY - actualMovement.y),
                    Math.max(currentBox.maxZ, currentBox.maxZ - actualMovement.z)
            ).inflate(SEARCH_PADDING);
        } else {
            searchBox = currentBox.inflate(SEARCH_PADDING);
        }

        // Create this once, rather than once per candidate.
        Predicate<Entity> pushable = EntitySelector.pushableBy(entity);

        List<Entity> candidates = entity.level().getEntities(
                entity,
                searchBox,
                other ->
                        other.isAlive()
                                && !other.isSpectator()
                                && !entity.isPassengerOfSameVehicle(other)
                                && pushable.test(other)
        );

        IntSet previousContacts = state.entityContacts;
        IntSet currentContacts = state.nextEntityContacts;
        currentContacts.clear();

        Entity strongestCollision = null;
        double strongestClosingSpeed = 0.0D;

        Vec3 originalVelocity = state.prevVel;

        Vec3 entityCurrentCenter = currentBox.getCenter();
        Vec3 entityPreviousCenter =
                entityCurrentCenter.subtract(actualMovement);

        double entityHalfWidthX =
                currentBox.getXsize() * 0.5D + CONTACT_PADDING;
        double entityHalfHeight =
                currentBox.getYsize() * 0.5D + CONTACT_PADDING;
        double entityHalfWidthZ =
                currentBox.getZsize() * 0.5D + CONTACT_PADDING;

        for (Entity other : candidates) {
            AABB otherBox = other.getBoundingBox();
            int otherId = other.getId();

            boolean touchingNow = contactBox.intersects(otherBox);

            if (touchingNow) {
                currentContacts.add(otherId);
            }

            if (!allowImpact) {
                continue;
            }

            Vec3 otherMovement = other.getDeltaMovement();

            double otherCurrentX = (otherBox.minX + otherBox.maxX) * 0.5D;
            double otherCurrentY = (otherBox.minY + otherBox.maxY) * 0.5D;
            double otherCurrentZ = (otherBox.minZ + otherBox.maxZ) * 0.5D;

            double otherPreviousX = otherCurrentX - otherMovement.x;
            double otherPreviousY = otherCurrentY - otherMovement.y;
            double otherPreviousZ = otherCurrentZ - otherMovement.z;

            if (!touchingNow && !sweptIntersects(
                    otherBox,
                    otherCurrentX,
                    otherCurrentY,
                    otherCurrentZ,
                    otherPreviousX,
                    otherPreviousY,
                    otherPreviousZ,
                    entityCurrentCenter,
                    entityPreviousCenter,
                    entityHalfWidthX,
                    entityHalfHeight,
                    entityHalfWidthZ)) {

                continue;
            }

            // It was already touching last tick, so this is not a new impact.
            if (previousContacts.contains(otherId)) {
                continue;
            }

            double closingSpeed = getClosingSpeed(
                    originalVelocity,
                    otherMovement,
                    entityPreviousCenter,
                    otherPreviousX,
                    otherPreviousY,
                    otherPreviousZ
            );

            if (closingSpeed > strongestClosingSpeed) {
                strongestClosingSpeed = closingSpeed;
                strongestCollision = other;
            }
        }

        state.entityContacts = currentContacts;
        state.nextEntityContacts = previousContacts;

        if (strongestCollision != null) {
            onEntityCollide(
                    entity,
                    strongestCollision,
                    originalVelocity,
                    strongestClosingSpeed
            );
        }
    }

    private static boolean sweptIntersects(
            AABB otherBox,
            double otherCurrentX,
            double otherCurrentY,
            double otherCurrentZ,
            double otherPreviousX,
            double otherPreviousY,
            double otherPreviousZ,
            Vec3 entityCurrentCenter,
            Vec3 entityPreviousCenter,
            double entityHalfWidthX,
            double entityHalfHeight,
            double entityHalfWidthZ) {

        double startX = entityPreviousCenter.x - otherPreviousX;
        double startY = entityPreviousCenter.y - otherPreviousY;
        double startZ = entityPreviousCenter.z - otherPreviousZ;

        double endX = entityCurrentCenter.x - otherCurrentX;
        double endY = entityCurrentCenter.y - otherCurrentY;
        double endZ = entityCurrentCenter.z - otherCurrentZ;

        double minX =
                otherBox.minX - otherCurrentX - entityHalfWidthX;
        double minY =
                otherBox.minY - otherCurrentY - entityHalfHeight;
        double minZ =
                otherBox.minZ - otherCurrentZ - entityHalfWidthZ;

        double maxX =
                otherBox.maxX - otherCurrentX + entityHalfWidthX;
        double maxY =
                otherBox.maxY - otherCurrentY + entityHalfHeight;
        double maxZ =
                otherBox.maxZ - otherCurrentZ + entityHalfWidthZ;

        return segmentIntersectsAabb(
                startX, startY, startZ,
                endX, endY, endZ,
                minX, minY, minZ,
                maxX, maxY, maxZ
        );
    }

    private static boolean segmentIntersectsAabb(
            double startX, double startY, double startZ,
            double endX, double endY, double endZ,
            double minX, double minY, double minZ,
            double maxX, double maxY, double maxZ) {

        double tMin = 0.0D;
        double tMax = 1.0D;

        double deltaX = endX - startX;

        if (Math.abs(deltaX) < COLLISION_EPSILON) {
            if (startX < minX || startX > maxX) {
                return false;
            }
        } else {
            double inverse = 1.0D / deltaX;
            double t1 = (minX - startX) * inverse;
            double t2 = (maxX - startX) * inverse;

            if (t1 > t2) {
                double swap = t1;
                t1 = t2;
                t2 = swap;
            }

            tMin = Math.max(tMin, t1);
            tMax = Math.min(tMax, t2);

            if (tMin > tMax) {
                return false;
            }
        }

        double deltaY = endY - startY;

        if (Math.abs(deltaY) < COLLISION_EPSILON) {
            if (startY < minY || startY > maxY) {
                return false;
            }
        } else {
            double inverse = 1.0D / deltaY;
            double t1 = (minY - startY) * inverse;
            double t2 = (maxY - startY) * inverse;

            if (t1 > t2) {
                double swap = t1;
                t1 = t2;
                t2 = swap;
            }

            tMin = Math.max(tMin, t1);
            tMax = Math.min(tMax, t2);

            if (tMin > tMax) {
                return false;
            }
        }

        double deltaZ = endZ - startZ;

        if (Math.abs(deltaZ) < COLLISION_EPSILON) {
            return startZ >= minZ && startZ <= maxZ;
        }

        double inverse = 1.0D / deltaZ;
        double t1 = (minZ - startZ) * inverse;
        double t2 = (maxZ - startZ) * inverse;

        if (t1 > t2) {
            double swap = t1;
            t1 = t2;
            t2 = swap;
        }

        tMin = Math.max(tMin, t1);
        tMax = Math.min(tMax, t2);

        return tMin <= tMax;
    }

    private static double getClosingSpeed(
            Vec3 originalVelocity,
            Vec3 otherVelocity,
            Vec3 entityPreviousCenter,
            double otherPreviousX,
            double otherPreviousY,
            double otherPreviousZ) {

        double relativeX = originalVelocity.x - otherVelocity.x;
        double relativeY = originalVelocity.y - otherVelocity.y;
        double relativeZ = originalVelocity.z - otherVelocity.z;

        double relativeSpeedSqr =
                relativeX * relativeX
                        + relativeY * relativeY
                        + relativeZ * relativeZ;

        if (relativeSpeedSqr
                < (MIN_VELOCITY * MIN_VELOCITY) * 0.5) {

            return 0;
        }

        double directionX =
                otherPreviousX - entityPreviousCenter.x;
        double directionY =
                otherPreviousY - entityPreviousCenter.y;
        double directionZ =
                otherPreviousZ - entityPreviousCenter.z;

        double directionLengthSqr =
                directionX * directionX
                        + directionY * directionY
                        + directionZ * directionZ;

        if (directionLengthSqr < COLLISION_EPSILON) {
            return Math.sqrt(relativeSpeedSqr);
        }

        double closingSpeed =
                (relativeX * directionX
                        + relativeY * directionY
                        + relativeZ * directionZ)
                        / Math.sqrt(directionLengthSqr);

        return Math.max(0, closingSpeed);
    }

    private static void onEntityCollide(LivingEntity entity, Entity collidedEntity, Vec3 originalVelocity, double closingSpeed) {

        if (closingSpeed < MIN_VELOCITY) return;

        float impactPwr = (float) (closingSpeed * 10 - 3);

        if (impactPwr <= 0) return;

        Vec3 movementDir = originalVelocity.normalize();

        if (movementDir.lengthSqr() < 1e-8) return;

        double distanceBehindTarget = Math.max(
                0.25,
                collidedEntity.getBbWidth() * 0.5 + 0.1);

        Vec3 impactPos = collidedEntity.getBoundingBox()
                .getCenter()
                .add(movementDir.scale(distanceBehindTarget));

        float explosionPower = (float) Mth.clamp(
                Math.sqrt(impactPwr) / 2,
                1,
                (entity.getBbWidth() + entity.getBbHeight()) / 1.75f);

        ZoeEntityImpactEvent impactEvent = new ZoeEntityImpactEvent(
                entity,
                collidedEntity,
                entity.getLastAttacker(),
                explosionPower,
                2);
        MinecraftForge.EVENT_BUS.post(impactEvent);

        if (impactEvent.isCanceled()) {
            return;
        }

        entity.level().explode(
                impactEvent.getSource(),
                impactPos.x,
                impactPos.y,
                impactPos.z,
                impactEvent.getPower(),
                false,
                Level.ExplosionInteraction.MOB);

        DamageSource damageSource = impactEvent.getSource() != null ?
                HurtUtil.get(impactEvent.getSource(), DamageTypes.FLY_INTO_WALL) :
                HurtUtil.get(entity.level(), DamageTypes.FLY_INTO_WALL);

        entity.invulnerableTime = 0;
        entity.hurt(damageSource, impactEvent.getDamage());

        entity.setDeltaMovement(originalVelocity.scale(0.75f));
        entity.hasImpulse = true;
        entity.hurtMarked = true;

        ImpactState state = STATES.get(entity);

        if (state != null) {
            state.cooldown = IMPACT_CD_TICKS;
            state.prevVel = entity.getDeltaMovement();
        }

    }
}