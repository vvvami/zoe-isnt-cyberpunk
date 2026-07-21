package net.vami.zoe.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.effect.custom.ShockEffect;

import java.util.UUID;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ZoeIsntCyberpunk.MOD_ID);

    public static final RegistryObject<MobEffect> SHOCK =
            MOB_EFFECTS.register("shock",
                    () -> new ShockEffect(MobEffectCategory.HARMFUL, 0x000000)
                            .addAttributeModifier(Attributes.MOVEMENT_SPEED,
                                    UUID.randomUUID().toString(),
                                    -0.5f, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
