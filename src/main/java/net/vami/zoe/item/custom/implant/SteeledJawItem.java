package net.vami.zoe.item.custom.implant;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;

public class SteeledJawItem extends ImplantItem {
    public SteeledJawItem(Properties pProperties) {
        super(pProperties);

    }

    // has functionality in ImplantLivingEntityMixin, makes eating faster

    @Override
    public ImplantData data() {
        return ImplantData.build(10f,
                ImplantData.add(ModAttributes.PLATING.get(),
                        5f,
                        AttributeModifier.Operation.ADDITION),

                ImplantData.add(Attributes.ARMOR,
                        5,
                        AttributeModifier.Operation.ADDITION));
    }
}
