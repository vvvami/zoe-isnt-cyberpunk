package net.vami.zoe.event.custom;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ImplantOnHitEvent extends Event {
    private ItemStack implant;
    private final LivingEntity entity;
    private final DamageSource source;
    private float amount;

    public ImplantOnHitEvent(ItemStack implant, LivingEntity entity,
                             DamageSource source, float amount) {
        this.implant = implant;
        this.entity = entity;
        this.source = source;
        this.amount = amount;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public DamageSource getSource() {
        return this.source;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Player getPlayer() {
        return (Player) this.getSource().getEntity();
    }

    public ItemStack getImplant() {
        return implant;
    }

    public Item getItem() {
        return implant.getItem();
    }
}
