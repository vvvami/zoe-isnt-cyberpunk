package net.vami.zoe.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RangedAttribute.class)
public abstract class RangedAttributeMixin {

    @Unique
    private static final double ZOE$MAX_ATTACK_KNOCKBACK = 1024;

    @Inject(method = "sanitizeValue", at = @At("HEAD"), cancellable = true)
    private void zoe$increaseKBLimit(double value, CallbackInfoReturnable<Double> cir) {
        Attribute attribute = (Attribute) (Object) this;
        if (attribute == Attributes.ATTACK_KNOCKBACK) {
            cir.setReturnValue(
                    Mth.clamp(
                            value,
                            1,
                            ZOE$MAX_ATTACK_KNOCKBACK));
        }
    }

    @Inject(method = "getMaxValue", at = @At("HEAD"), cancellable = true)
    private void zoe$getMaxKB(CallbackInfoReturnable<Double> cir) {
        Attribute attribute = (Attribute) (Object) this;
        if (attribute == Attributes.ATTACK_KNOCKBACK) {
            cir.setReturnValue(ZOE$MAX_ATTACK_KNOCKBACK);
        }
    }
}
