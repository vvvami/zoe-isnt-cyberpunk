package net.vami.zoe.event.custom;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ImplantOnDeathEvent extends Event {
    private ItemStack implant;
    private final LivingEntity entity;
    private final DamageSource source;


    public ImplantOnDeathEvent(ItemStack implant, LivingEntity entity,
                               DamageSource source) {
        this.implant = implant;
        this.entity = entity;
        this.source = source;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public DamageSource getSource() {
        return this.source;
    }

    public Player getPlayer() {
        return (Player) this.entity;
    }

    public ItemStack getImplant() {
        return implant;
    }

    public Item getItem() {
        return implant.getItem();
    }
}
