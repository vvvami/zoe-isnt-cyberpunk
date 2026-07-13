package net.vami.zoe.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.vami.zoe.entity.custom.CycrawlerEntity;

import javax.swing.*;

public class CycrawlerAttackGoal extends MeleeAttackGoal {
    private CycrawlerEntity entity;

    public CycrawlerAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.entity =  (CycrawlerEntity) pMob;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        entity.setAttacking(pDistToEnemySqr <= this.getAttackReachSqr(pEnemy));
        super.checkAndPerformAttack(pEnemy, pDistToEnemySqr);
    }
}
