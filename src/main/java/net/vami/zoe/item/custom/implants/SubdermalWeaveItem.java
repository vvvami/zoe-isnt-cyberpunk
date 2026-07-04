package net.vami.zoe.item.custom.implants;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.vami.zoe.util.ImplantUtil;

public class SubdermalWeaveItem extends ImplantItem {
    public SubdermalWeaveItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public void onEquip(LivingEntity entity, ItemStack item) {
        super.onEquip(entity, item);
    }

    @Override
    public void onUnequip(LivingEntity entity, ItemStack item) {
        super.onUnequip(entity, item);
    }

    @Override
    public void register() {
        ImplantUtil.registerImplant(this,
                ImplantUtil.Builder.create(
                        ImplantUtil.Builder.add
                                ("zoe:plating",
                                        0.5d, "addition")
                ), 10f);
    }
}
