package net.vami.zoe.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SmoothMoveControl extends MoveControl {
    private final Mob mob;

    public SmoothMoveControl(Mob mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;

            double dx = this.wantedX - this.mob.getX();
            double dz = this.wantedZ - this.mob.getZ();

            if (dx * dx + dz * dz < 0.0001D) {
                this.mob.setZza(0.0F);
                return;
            }

            float targetYaw = (float) (Mth.atan2(dz, dx) * (180.0F / Math.PI)) - 90.0F;

            // This is the important smoothing value.
            // Try 5–10.
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), targetYaw, 8.0F));

            this.mob.setSpeed(
                    (float) (
                            this.speedModifier *
                                    this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)
                    )
            );
        } else {
            this.mob.setZza(0.0F);
        }
    }
}
