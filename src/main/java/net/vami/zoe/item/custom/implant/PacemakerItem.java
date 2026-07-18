package net.vami.zoe.item.custom.implant;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class PacemakerItem extends ImplantItem {
    public PacemakerItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f,
                ImplantData.add(Attributes.MAX_HEALTH,
                        10f,
                        AttributeModifier.Operation.ADDITION));
    }
}
