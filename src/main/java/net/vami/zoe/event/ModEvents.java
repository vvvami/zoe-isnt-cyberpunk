package net.vami.zoe.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;


@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onImplantTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
    }

    @SubscribeEvent
    public static void onImplantHit(LivingHurtEvent event) {
        DamageSource damageSource = event.getSource();
        Entity sourceEntity = event.getSource().getEntity();
        Entity targetEntity = event.getEntity();
    }

    @SubscribeEvent
    public static void onImplantHurt(LivingHurtEvent event) {
        DamageSource damageSource = event.getSource();
        Entity sourceEntity = event.getSource().getEntity();
        Entity targetEntity = event.getEntity();
    }

    @SubscribeEvent
    public static void onImplantKill(LivingDeathEvent event) {
        DamageSource damageSource = event.getSource();
        Entity sourceEntity = event.getSource().getEntity();
        Entity targetEntity = event.getEntity();
    }

    @SubscribeEvent
    public static void onImplantDeath(LivingDeathEvent event) {
        DamageSource damageSource = event.getSource();
        Entity sourceEntity = event.getSource().getEntity();
        Entity targetEntity = event.getEntity();
    }
}
