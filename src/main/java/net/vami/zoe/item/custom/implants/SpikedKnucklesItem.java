package net.vami.zoe.item.custom.implants;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vami.zoe.util.ImplantUtil;

public class SpikedKnucklesItem extends ImplantItem {
    public SpikedKnucklesItem(Properties pProperties) {
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
                        ImplantUtil.Builder.add("minecraft:generic.attack_damage",
                                0.1d, "percent")
                ), 10f);
    }
}
