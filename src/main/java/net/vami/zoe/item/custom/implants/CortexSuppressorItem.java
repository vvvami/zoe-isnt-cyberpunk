package net.vami.zoe.item.custom.implants;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ImplantOnHitEvent;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.ImplantUtil;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class CortexSuppressorItem extends ImplantItem {
    private static final int FRAMES = 20;

    public CortexSuppressorItem(Properties pProperties) {
        super(pProperties);
    }

    @SubscribeEvent
    public static void onImplantHit(ImplantOnHitEvent event) {
        // Checks that the implant is the Cortex Suppressor
        if (!(event.getItem() instanceof CortexSuppressorItem)) return;
        // Gets the quality of the equipped Cortex Suppressor
        float quality = ImplantUtil.getQuality(event.getImplant());
        // Removes the target's i-frames
        event.getEntity().invulnerableTime = 0;
        // Reduces the damage of the attack
        event.setAmount((float) (event.getAmount() /
                        (FRAMES - (quality / 7.5))));
    }

    @Override
    public void register() {
        ImplantUtil.registerImplant(this,
                ImplantUtil.Builder.create(), 10f);
    }
}
