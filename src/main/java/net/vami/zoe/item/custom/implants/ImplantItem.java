package net.vami.zoe.item.custom.implants;

import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

public abstract class ImplantItem extends Item {
    public ArrayList<AttributeModifier> attributeList = new ArrayList<>();

    public ImplantItem(Properties pProperties) {
        super(pProperties);
//        ForgeRegistries.ITEMS.getKey(this);
    }

    public void onTick(LivingEntity entity, ItemStack item) {}
    public void onEquip(LivingEntity entity, ItemStack item) {}
    public void onUnequip(LivingEntity entity, ItemStack item) {}
    public void onHit(LivingEntity entity, ItemStack item) {}
    public void onHurt(LivingEntity entity, ItemStack item) {}
    public void onDeath(LivingEntity entity, ItemStack item) {}
    public void onKill(LivingEntity entity, ItemStack item) {}

}
