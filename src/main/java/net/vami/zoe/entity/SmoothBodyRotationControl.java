package net.vami.zoe.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class SmoothBodyRotationControl extends BodyRotationControl {
    private final Mob mob;

    public SmoothBodyRotationControl(Mob mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public void clientTick() {
        // How fast the body can turn per tick.
        // Lower = smoother, higher = snappier.
        float maxBodyTurn = 6.0F;

        // How far the head can differ from the body.
        float maxHeadBodyDifference = 35.0F;

        // Smooth body toward movement/entity yaw.
        this.mob.yBodyRot = Mth.approachDegrees(
                this.mob.yBodyRot,
                this.mob.getYRot(),
                maxBodyTurn
        );

        // Clamp head so it does not drag the body into wild spins.
        float headBodyDiff = Mth.wrapDegrees(this.mob.yHeadRot - this.mob.yBodyRot);

        if (headBodyDiff < -maxHeadBodyDifference) {
            this.mob.yHeadRot = this.mob.yBodyRot - maxHeadBodyDifference;
        }

        if (headBodyDiff > maxHeadBodyDifference) {
            this.mob.yHeadRot = this.mob.yBodyRot + maxHeadBodyDifference;
        }
    }
}
