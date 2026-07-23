package net.vami.zoe.event.custom;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ZoePreCritEvent extends Event {
    private final LivingEntity entity;
    private final DamageSource source;
    private boolean condition;

    public ZoePreCritEvent(LivingEntity entity, DamageSource source, boolean condition) {
        this.entity = entity;
        this.source = source;
        this.condition = condition;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public DamageSource getSource() {
        return this.source;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public boolean getCondition() {
        return this.condition;
    }
}
