package net.vami.zoe.item.custom.implants;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.vami.zoe.init.ModDamageTypes;
import net.vami.zoe.util.ImplantUtil;

public class PowerfistItem extends ImplantItem {
    public PowerfistItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public void onHit(LivingEntity source, LivingEntity target, ItemStack item) {
        CompoundTag tag = item.getOrCreateTag();

        if (tag.getBoolean("zPowerfist")) {
            return;
        }

        tag.putBoolean("zPowerfist", true);

        try {
            target.hurt(
                    ModDamageTypes.get(source, ModDamageTypes.VOLT),
                    (float) Math.sqrt(Math.max(1, ImplantUtil.getQuality(item)))
            );
        } finally {
            tag.putBoolean("zPowerfist", false);
        }
    }

    @Override
    public void register() {
        ImplantUtil.registerImplant(this,
                ImplantUtil.Builder.create(), 10f);
    }
}
