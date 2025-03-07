package net.vami.zoe.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.implants.ReinforcedTibiaItem;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onImplantTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.getMainHandItem().is(ModItems.REINFORCED_TIBIA.get())) {
                ModItems.REINFORCED_TIBIA.get().onTick(player, player.getMainHandItem());
        }
    }
}
