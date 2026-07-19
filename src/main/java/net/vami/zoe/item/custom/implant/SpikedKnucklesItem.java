package net.vami.zoe.item.custom.implant;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;

public class SpikedKnucklesItem extends ImplantItem {
    public SpikedKnucklesItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f,
                ImplantData.add(Attributes.ATTACK_DAMAGE,
                        0.1d,
                        AttributeModifier.Operation.MULTIPLY_TOTAL),

                ImplantData.add(ModAttributes.SABOTAGE.get(),
                        0.1d,
                        AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}
