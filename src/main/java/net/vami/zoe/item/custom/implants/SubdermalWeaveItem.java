package net.vami.zoe.item.custom.implants;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

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
