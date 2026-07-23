package net.vami.zoe.event.custom;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ZoeEntityImpactEvent extends Event {
    private final LivingEntity entity;
    private final LivingEntity source;
    private final Entity impacted;
    private float power;
    private float damage;

    public ZoeEntityImpactEvent(LivingEntity entity, Entity impacted, LivingEntity source, float power, float damage) {
        this.entity = entity;
        this.source = source;
        this.impacted = impacted;
        this.power = power;
        this.damage = damage;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public LivingEntity getSource() {
        return source;
    }

    public Entity getImpacted() {
        return impacted;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}
