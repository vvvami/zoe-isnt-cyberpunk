package net.vami.zoe.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ModTags;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ZoeCritEvent;
import net.vami.zoe.event.custom.ZoePreCritEvent;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.CombatUtil;
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
        if (!(source.getEntity() instanceof Player player)) return;

        double sabotage = player.getAttribute(ModAttributes.SABOTAGE.get()).getValue();

        if (sabotage + Math.random() < 2) return;

        LivingEntity entity = event.getEntity();

        boolean zoeCritCondition = source.is(ModTags.DamageTypes.IS_MELEE)
                && CombatUtil.isFullSweep(player);

        ZoePreCritEvent preCritEvent = new ZoePreCritEvent(entity, source, zoeCritCondition);
        MinecraftForge.EVENT_BUS.post(preCritEvent);

        if (!preCritEvent.getCondition()) return;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.HUNTER_LENS.get());
        if (implant.isEmpty()) return;

        float multiplier = ImplantUtil.getQuality(implant) / 100;

        ZoeCritEvent critEvent = new ZoeCritEvent(entity, source,
                1 + multiplier, 1);
        MinecraftForge.EVENT_BUS.post(critEvent);

        double attackKB = ((2 + player.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue())
                * critEvent.getKnockback());

        AttributeInstance instance = entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        double kbResist = instance != null ? (1 + instance.getValue()) : 1;

        Entity directEntity = source.getDirectEntity() == null ? entity : source.getDirectEntity();
        Vec3 direction = directEntity instanceof Projectile ?
                directEntity.getDeltaMovement().scale(0.5f) :
                directEntity.getLookAngle().scale(attackKB);

        entity.hurtMarked = true;
        entity.setDeltaMovement(new Vec3(
                (entity.getDeltaMovement().x() + (direction.x) / kbResist),
                (entity.getDeltaMovement().y() + (direction.y) / kbResist),
                (entity.getDeltaMovement().z() + (direction.z) / kbResist)));

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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void zoeGetKnockback(LivingDamageEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        DamageSource source = event.getSource();
        if (!(source.getEntity() instanceof LivingEntity sourceEntity)) return;

        Entity directEntity = source.getDirectEntity() == null ? sourceEntity : source.getDirectEntity();

        Vec3 direction = directEntity instanceof Projectile ?
                directEntity.getDeltaMovement().scale(0.5f) :
                directEntity.getLookAngle();

        LivingEntity entity = event.getEntity();

        entity.getPersistentData().putDouble("zoeKnockbackX",
                direction.x);
        entity.getPersistentData().putDouble("zoeKnockbackY",
                direction.y);
        entity.getPersistentData().putDouble("zoeKnockbackZ",
                direction.z);
    }

    @SubscribeEvent(priority =  EventPriority.LOWEST)
    public static void zoeDealKnockback(LivingKnockBackEvent event) {
        LivingEntity entity = event.getEntity();

        float strength = event.getStrength();
        event.setCanceled(true);

        AttributeInstance instance = entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        double kbResist = instance != null ? (1 + instance.getValue()) : 1;

        Vec3 direction = new Vec3(
                entity.getPersistentData().getDouble("zoeKnockbackX"),
                entity.getPersistentData().getDouble("zoeKnockbackY"),
                entity.getPersistentData().getDouble("zoeKnockbackZ"))
                .scale(strength);

        double extraY = direction.y > -0.2 && direction.y < 0.2 ? (0.2 * Math.sqrt(1 + strength)) - (direction.y / 2) : 0; // for direct hits, add small y bonus for vanilla kb effect

        entity.setDeltaMovement(new Vec3(
                (entity.getDeltaMovement().x() + direction.x / kbResist),
                (entity.getDeltaMovement().y() + extraY + (direction.y / kbResist)),
                (entity.getDeltaMovement().z() + direction.z / kbResist)));

    }
}
