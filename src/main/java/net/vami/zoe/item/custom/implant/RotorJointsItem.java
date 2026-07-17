package net.vami.zoe.item.custom.implant;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ImplantOnEquipEvent;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

public class RotorJointsItem extends ImplantItem {
    public RotorJointsItem(Properties pProperties) {
        super(pProperties);
    }

    @Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static final class RotorJointsEvents {
        @SubscribeEvent(priority = EventPriority.LOW)
        public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();

            ItemStack item = ImplantUtil.getImplant(player, ModItems.ROTOR_JOINTS.get());
            if (item.isEmpty()) return;
            float quality = ImplantUtil.getQuality(item);
            event.setNewSpeed(event.getNewSpeed() * quality / 10);
        }
    }

    @Override
    public void onEquip(ImplantOnEquipEvent event) {
        Player player = event.getEntity();
        player.removeEffect(MobEffects.DIG_SLOWDOWN);
    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
