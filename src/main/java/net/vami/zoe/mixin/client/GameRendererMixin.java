package net.vami.zoe.mixin.client;
import net.minecraft.world.item.ItemStack;
import net.vami.zoe.init.ModAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.implant.ImplantUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "pick", at = @At("TAIL"), require = 1)
    private void zoe$wiredTendonAttack(float partialTicks, CallbackInfo ci) {
        if (minecraft.player == null || minecraft.level == null) return;

        HitResult current = minecraft.hitResult;
        if (current != null && current.getType() == HitResult.Type.ENTITY) return;
        if (current != null && current.getType() == HitResult.Type.BLOCK) return;

        Player player = minecraft.player;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.WIRED_TENDONS.get());
        if (implant.isEmpty()) return;

        float quality = ImplantUtil.getQuality(implant);

        double attackSpan = quality / 50;

        double range = player.getAttributes().getValue(ForgeMod.ENTITY_REACH.get()) - (attackSpan / 2d);
        double halfWidth = attackSpan / 2d;
        double halfHeight = attackSpan / 2d;

        Vec3 eye = player.getEyePosition(partialTicks);
        Vec3 look = player.getViewVector(partialTicks);
        Vec3 end = eye.add(look.scale(range));

        // making the scanner work with original attack span to ensure hit
        AABB scanner = new AABB(eye, end).inflate(attackSpan, attackSpan, attackSpan);

        List<Entity> candidates = minecraft.level.getEntities(
                player,
                scanner,
                e -> e instanceof LivingEntity
                        && e.isAlive()
                        && e != player
                        && !e.isSpectator()
                        && e.isPickable());

        Entity bestEntity = null;
        Vec3 bestHitPos = null;
        double d = Double.MAX_VALUE;

        for (Entity e : candidates) {
            // we r inflating the entity's bounding box and then checking to see if that hits our ray
            AABB expandedBox = e.getBoundingBox().inflate(halfWidth, halfHeight, halfWidth);

            Optional<Vec3> hit = expandedBox.clip(eye, end);
            if (hit.isEmpty()) continue;

            Vec3 hitPos = hit.get();
            double t = eye.distanceToSqr(hitPos);

            // wall collision
            HitResult blockHit = minecraft.level.clip(new ClipContext(
                    eye,
                    hitPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player));

            if (blockHit.getType() != HitResult.Type.MISS) continue;

            // progressive checking for best entity
            if (t < d) {
                d = t;
                bestEntity = e;
                bestHitPos = hitPos;
            }
        }

        if (bestEntity != null) {
            // this makes the attack indicator show up (this was literally it.)
            minecraft.crosshairPickEntity = bestEntity;
            minecraft.hitResult = new EntityHitResult(bestEntity, bestHitPos);
        }
    }
}