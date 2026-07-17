package net.vami.zoe.item.custom.implant;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;

public class SubdermalWeaveItem extends ImplantItem {
    public SubdermalWeaveItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public ImplantData data() {
        return ImplantData.build(
                ImplantData.attributes(
                        ImplantData.add(ModAttributes.PLATING.get(),
                                0.5d,
                                AttributeModifier.Operation.ADDITION)),
                10f);
    }
}
