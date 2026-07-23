package net.vami.zoe.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;

public class CombatUtil {

    public static boolean isVanillaSweep(Player player) {
        // Full attack strength
        if (player.getAttackStrengthScale(0.5F) <= 0.848F) return false;

        // Must not be sprinting
        if (player.isSprinting()) return false;

        // Must be on ground and moving slowly
        double speedSq = player.getDeltaMovement().horizontalDistanceSqr();
        if (speedSq >= 0.01D) return false;
        if (!player.onGround()) return false;

        return true;
    }

    public static boolean isFullSweep(Player player) {
        return player.getAttackStrengthScale(0.5F) > 0.848F;
    }
}
