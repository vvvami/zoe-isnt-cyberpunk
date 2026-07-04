package net.vami.zoe.event;

import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.init.ModDamageTypes;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class ModCombatEvents {

    @SubscribeEvent
    public static void zoeCrit(CriticalHitEvent event) {
        event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public static void zoeDamage(LivingDamageEvent event) {
        float originalAmount = event.getAmount();

        float plating = (float) event.getEntity().getAttributeValue(ModAttributes.PLATING.get());
        if (plating == 0) return;
        float newAmount =
                (float) Math.max(originalAmount /
                        Math.sqrt(plating), originalAmount - (2 * (plating)));

        event.setAmount(newAmount);
    }
}
