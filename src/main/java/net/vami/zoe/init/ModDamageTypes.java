package net.vami.zoe.init;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.vami.zoe.ZoeIsntCyberpunk;

import javax.annotation.Nullable;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> VOLT = create("volt");


    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(
                Registries.DAMAGE_TYPE,
                new ResourceLocation(ZoeIsntCyberpunk.MOD_ID, name)
        );
    }
}
