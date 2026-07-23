package net.vami.zoe.event.custom;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ZoeImpactEvent extends Event {
    private final LivingEntity entity;
    private final LivingEntity source;
    private float power;

    public ZoeImpactEvent(LivingEntity entity, LivingEntity source, float power) {
        this.entity = entity;
        this.source = source;
        this.power = power;
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
}
