package net.vami.zoe.item.custom.implants;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class ImplantItem extends Item {

    public ImplantItem(Properties pProperties) {
        super(pProperties);
    }

    public void onTick(LivingEntity entity, ItemStack item) {}
    public void onEquip(LivingEntity entity, ItemStack item) {}
    public void onUnequip(LivingEntity entity, ItemStack item) {}
    public void onHit(LivingEntity entity, ItemStack item) {}
    public void onHurt(LivingEntity entity, ItemStack item) {}
    public void onDeath(LivingEntity entity, ItemStack item) {}
    public void onKill(LivingEntity entity, ItemStack item) {}

}
