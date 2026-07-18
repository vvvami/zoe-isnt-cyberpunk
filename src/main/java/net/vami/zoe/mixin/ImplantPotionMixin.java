package net.vami.zoe.mixin;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.implant.ImplantUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public class ImplantPotionMixin {

    @Inject(method = "forceAddEffect", at = @At("HEAD"), cancellable = true)
    private void rotorJoints$removeForceEffect(
            MobEffectInstance pInstance, Entity pEntity, CallbackInfo ci) {
        if (pInstance.getEffect() != MobEffects.DIG_SLOWDOWN) return;

        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof Player player)) return;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.ROTOR_JOINTS.get());
        if (implant.isEmpty()) return;

        ci.cancel();
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void rotorJoints$removeEffect(
            MobEffectInstance pEffectInstance, Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        if (pEffectInstance.getEffect() != MobEffects.DIG_SLOWDOWN) return;

        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof Player player)) return;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.ROTOR_JOINTS.get());
        if (implant.isEmpty()) return;

        cir.setReturnValue(false);
    }

    @Inject(method = "forceAddEffect", at = @At("HEAD"), cancellable = true)
    private void motorTwins$removeForceEffect(
            MobEffectInstance pInstance, Entity pEntity, CallbackInfo ci) {
        if (pInstance.getEffect() != MobEffects.MOVEMENT_SLOWDOWN) return;

        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof Player player)) return;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.TWIN_MOTOR.get());
        if (implant.isEmpty()) return;

        ci.cancel();
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void motorTwins$removeEffect(
            MobEffectInstance pEffectInstance, Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        if (pEffectInstance.getEffect() != MobEffects.MOVEMENT_SLOWDOWN) return;

        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof Player player)) return;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.TWIN_MOTOR.get());
        if (implant.isEmpty()) return;

        cir.setReturnValue(false);
    }
}
