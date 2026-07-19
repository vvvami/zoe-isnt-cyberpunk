package net.vami.zoe.item.custom.implant;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class TwinMotorItem extends ImplantItem {
    public TwinMotorItem(Properties pProperties) {
        super(pProperties);

    }

    /**{@link net.vami.zoe.mixin.ImplantPotionMixin#motorTwins$removeEffect(MobEffectInstance, Entity, CallbackInfoReturnable) removes slowness} */

    @Override
    public ImplantData data() {
        return ImplantData.build(10f,
                ImplantData.add(Attributes.MOVEMENT_SPEED,
                        0.15d,
                        AttributeModifier.Operation.ADDITION));
    }
}
