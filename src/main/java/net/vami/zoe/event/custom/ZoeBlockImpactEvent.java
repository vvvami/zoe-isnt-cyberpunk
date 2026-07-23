package net.vami.zoe.event.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ZoeBlockImpactEvent extends Event {
    private final LivingEntity entity;
    private final LivingEntity source;
    private float power;
    private float damage;

    public ZoeBlockImpactEvent(LivingEntity entity, LivingEntity source, float power, float damage) {
        this.entity = entity;
        this.source = source;
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

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}
