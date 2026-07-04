package net.vami.zoe.item.custom.implants;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.init.ModDamageTypes;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.ImplantUtil;

public class CortexSupressorItem extends ImplantItem {
    public CortexSupressorItem(Properties pProperties) {
        super(pProperties);

    }

    @Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
    private static class Events {

        @SubscribeEvent
        public static void onHit(LivingHurtEvent event) {
            LivingEntity entity = event.getEntity();
            Entity source = event.getSource().getEntity();
            if (!(source instanceof Player player)) return;

            for (ItemStack implant : ImplantUtil.implants(player)) {
                if (implant.getItem() != ModItems.CORTEX_SUPRESSOR.get()) continue;

                if (entity.invulnerableTime - 10 > 0) {
                    event.setAmount(event.getAmount() / entity.invulnerableTime);
                    entity.invulnerableTime = 0;
                    break;
                }
            }
        }
    }

    @Override
    public void register() {
        ImplantUtil.registerImplant(this,
                ImplantUtil.Builder.create(), 10f);
    }
}
