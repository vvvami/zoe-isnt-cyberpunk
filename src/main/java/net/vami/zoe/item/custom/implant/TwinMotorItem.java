package net.vami.zoe.item.custom.implant;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.vami.zoe.event.custom.ImplantOnEquipEvent;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class TwinMotorItem extends ImplantItem {
    public TwinMotorItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public void onEquip(ImplantOnEquipEvent event) {
        if (event.getEntity().hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            event.getEntity().removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        }
    }

    /**{@link net.vami.zoe.mixin.ImplantPotionMixin#motorTwins$removeEffect(MobEffectInstance, Entity, CallbackInfoReturnable) removes slowness}
     * also removes cobweb slow and increases swim speed
     * */



    @Override
    public ImplantData data() {
        return ImplantData.build(10f,
                ImplantData.add(ForgeMod.SWIM_SPEED.get(),
                        2f,
                        AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}
