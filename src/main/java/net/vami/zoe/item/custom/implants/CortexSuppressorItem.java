package net.vami.zoe.item.custom.implants;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ImplantOnHitEvent;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantConfig;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

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
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
