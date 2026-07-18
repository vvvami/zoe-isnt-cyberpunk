package net.vami.zoe.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class HurtUtil {
    private static Holder.Reference<DamageType> holder(Level level, ResourceKey<DamageType> key) {
        return level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(key);
    }

    public static DamageSource get(Level level, ResourceKey<DamageType> key) {
        return new DamageSource(holder(level, key));
    }

    public static DamageSource get(Entity attacker, ResourceKey<DamageType> key) {
        return new DamageSource(holder(attacker.level(), key), attacker);
    }

    public static DamageSource get(Entity directEntity, @Nullable Entity causingEntity, ResourceKey<DamageType> key) {
        return new DamageSource(
                holder(directEntity.level(), key),
                directEntity,
                causingEntity
        );
    }
}
