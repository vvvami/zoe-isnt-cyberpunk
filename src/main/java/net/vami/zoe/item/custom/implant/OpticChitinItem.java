package net.vami.zoe.item.custom.implant;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;

public class OpticChitinItem extends ImplantItem {
    public OpticChitinItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f,
                ImplantData.add(ForgeMod.NAMETAG_DISTANCE.get(),
                        -1,
                        AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}
