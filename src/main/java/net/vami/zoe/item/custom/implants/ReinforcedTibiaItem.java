package net.vami.zoe.item.custom.implants;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.vami.zoe.util.ImplantUtil;

public class ReinforcedTibiaItem extends ImplantItem {
    public ReinforcedTibiaItem(Properties pProperties) {
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
                                ("minecraft:generic.movement_speed",
                                        0.1d, "percentage")
                ), 10f);
    }
}
