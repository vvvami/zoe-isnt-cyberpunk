package net.vami.zoe.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.ModTags;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ZoeCritEvent;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.implant.ImplantUtil;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class ModCombatEvents {

    @SubscribeEvent
    public static void cancelVanillaCrit(CriticalHitEvent event) {
        event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void zoeCrit(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (!source.is(ModTags.DamageTypes.IS_MELEE)) return;
        if (!(source.getEntity() instanceof Player player)) return;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.HUNTER_LENS.get());
        if (implant.isEmpty()) return;

        double value = player.getAttribute(ModAttributes.SABOTAGE.get()).getValue();
        // sabotage - 1 > num bigger than 0 smaller than 1 for crit
        if ((value - 1) <= Math.random()) return;

        LivingEntity entity = event.getEntity();

        float multiplier = ImplantUtil.getQuality(implant) / 100;

        ZoeCritEvent critEvent = new ZoeCritEvent(entity, source,
                1 + multiplier, 1);
        MinecraftForge.EVENT_BUS.post(critEvent);

        double attackKB = ((2 + player.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue())
                * critEvent.getKnockback());

        AttributeInstance instance = entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        double kbResist = instance != null ? (1 + instance.getValue()) * 4 : 1;

        entity.hurtMarked = true;
        entity.setDeltaMovement(new Vec3(
                (entity.getDeltaMovement().x() + (player.getLookAngle().x * attackKB) / kbResist),
                (entity.getDeltaMovement().y() + (player.getLookAngle().y * attackKB) / kbResist),
                (entity.getDeltaMovement().z() + (player.getLookAngle().z * attackKB) / kbResist)));

        event.setAmount(event.getAmount() * critEvent.getMultiplier());

        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.CRIT,
                    (entity.getX()), (entity.getY() + entity.getBbHeight() / 2), (entity.getZ()),
                    20, (entity.getBbWidth() / 4), (entity.getBbHeight() / 4), (entity.getBbWidth() / 4),
                    0.55);

                serverLevel.playSound(
                        null,
                        BlockPos.containing(entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ()),
                        SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS,
                        0.45f, (float) Mth.nextDouble(RandomSource.create(), 3, 3));
        }
    }

    @SubscribeEvent
    public static void zoeDamage(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        if (source.is(ModTags.DamageTypes.BYPASSES_PLATING)) return;

        float originalAmount = event.getAmount();

        float plating = (float) event.getEntity().getAttributeValue(ModAttributes.PLATING.get());
        if (plating == 0) return;
        float newAmount =
                (float) Math.max(originalAmount /
                        Math.sqrt(plating), originalAmount - (2 * (plating)));

        event.setAmount(newAmount);
    }
}
