package net.vami.zoe.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.capability.CapabilityUtil;
import net.vami.zoe.capability.PlayerCapability;
import net.vami.zoe.util.ImplantUtil;


@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!(event.player instanceof ServerPlayer player)) return;
            if (!CapabilityUtil.checkCapability(player)) return;
            if ( player.tickCount % 20 == 0) {
                System.out.println(ImplantUtil.implants(player));
            }
        }
    }

}
