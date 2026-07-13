package net.vami.zoe.item.custom.implants;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantConfig;
import net.vami.zoe.util.implant.ImplantData;

public class ReinforcedTibiaItem extends ImplantItem {
    public ReinforcedTibiaItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public ImplantData data() {
        return ImplantData.build(
                ImplantData.attributes(
                        ImplantData.add(Attributes.MOVEMENT_SPEED,
                                0.5d,
                                AttributeModifier.Operation.MULTIPLY_TOTAL),

                        ImplantData.add(ModAttributes.PLATING.get(),
                                5,
                                AttributeModifier.Operation.ADDITION)),
                10f);
    }
}
