package net.vami.zoe.entity.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.EnumSet;

public class DistantWalkGoal extends Goal {
    private final PathfinderMob mob;
    private final double destinationX;
    private final double destinationZ;
    private final double speed;

    private final double waypointDistance;
    private final double arrivalDistanceSqr;

    private int repathCooldown;

    public DistantWalkGoal(
            PathfinderMob mob,
            double destinationX,
            double destinationZ,
            double speed,
            double waypointDistance,
            double arrivalDistance
    ) {
        this.mob = mob;
        this.destinationX = destinationX;
        this.destinationZ = destinationZ;
        this.speed = speed;
        this.waypointDistance = waypointDistance;
        this.arrivalDistanceSqr = arrivalDistance * arrivalDistance;

        this.setFlags(EnumSet.of(
                Goal.Flag.MOVE,
                Goal.Flag.LOOK
        ));
    }

    @Override
    public boolean canUse() {
        return !hasArrived();
    }

    @Override
    public boolean canContinueToUse() {
        return !hasArrived();
    }

    @Override
    public void start() {
        moveToNextWaypoint();
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(
                destinationX,
                this.mob.getEyeY(),
                destinationZ
        );

        if (--this.repathCooldown <= 0
                || this.mob.getNavigation().isDone()) {
            this.repathCooldown = 20;
            moveToNextWaypoint();
        }
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
    }

    private void moveToNextWaypoint() {
        double dx = destinationX - this.mob.getX();
        double dz = destinationZ - this.mob.getZ();

        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        if (horizontalDistance < 0.001D) {
            return;
        }

        double step = Math.min(
                waypointDistance,
                horizontalDistance
        );

        double waypointX =
                this.mob.getX() + dx / horizontalDistance * step;

        double waypointZ =
                this.mob.getZ() + dz / horizontalDistance * step;

        BlockPos waypointColumn = BlockPos.containing(
                waypointX,
                this.mob.getY(),
                waypointZ
        );

        BlockPos groundWaypoint = this.mob.level()
                .getHeightmapPos(
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        waypointColumn
                );

        this.mob.getNavigation().moveTo(
                groundWaypoint.getX() + 0.5D,
                groundWaypoint.getY(),
                groundWaypoint.getZ() + 0.5D,
                speed
        );
    }

    private boolean hasArrived() {
        double dx = this.mob.getX() - destinationX;
        double dz = this.mob.getZ() - destinationZ;

        return dx * dx + dz * dz <= arrivalDistanceSqr;
    }
}