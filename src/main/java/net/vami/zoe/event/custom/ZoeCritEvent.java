package net.vami.zoe.event.custom;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ZoeCritEvent extends Event {
    private final LivingEntity entity;
    private final DamageSource source;
    private float multiplier;
    private float knockback;

    public ZoeCritEvent(LivingEntity entity, DamageSource source, float multiplier, float knockback) {
        this.entity = entity;
        this.source = source;
        this.multiplier = multiplier;
        this.knockback = knockback;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public DamageSource getSource() {
        return this.source;
    }

    public float getMultiplier() {
        return this.multiplier;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }

    public float getKnockback() {
        return this.knockback;
    }

    public void setKnockback(float knockback) {
        this.knockback = knockback;
    }
}
