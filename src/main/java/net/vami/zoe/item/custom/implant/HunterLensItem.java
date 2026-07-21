package net.vami.zoe.item.custom.implant;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;

public class HunterLensItem extends ImplantItem {
    public HunterLensItem(Properties pProperties) {
        super(pProperties);

    }

    // allows crits

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
