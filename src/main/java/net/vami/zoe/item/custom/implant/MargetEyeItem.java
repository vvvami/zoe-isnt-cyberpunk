package net.vami.zoe.item.custom.implant;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ZoePreCritEvent;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class MargetEyeItem extends ImplantItem {
    public MargetEyeItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public void onHit(LivingHurtEvent event) {
        DamageSource damageSource = event.getSource();
        if (!(damageSource.getEntity() instanceof Player player)) return;

        if (!damageSource.is(DamageTypeTags.IS_PROJECTILE)) return;

        List<ItemStack> implantList = ImplantUtil.getImplants(player, this);

        for (ItemStack implant : implantList) {
            float quality = 1 + ImplantUtil.getQuality(implant) / 200;

            event.setAmount(event.getAmount() * quality);
        }
    }

    @SubscribeEvent
    public static void preCrit(ZoePreCritEvent event) {
        DamageSource damageSource = event.getSource();
        if (!(damageSource.getEntity() instanceof Player player)) return;
        ItemStack implant = ImplantUtil.getImplant(player, ModItems.MARGET_EYE.get());
        if (implant.isEmpty()) return;

        event.setCondition(event.getCondition()
                || damageSource.is(DamageTypeTags.IS_PROJECTILE));
    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
