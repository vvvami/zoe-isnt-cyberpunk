package net.vami.zoe.item.custom.implant;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ZoeCritEvent;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class CellAccelerantItem extends ImplantItem {
    public CellAccelerantItem(Properties pProperties) {
        super(pProperties);

    }

    // increases healing by up to 50%

    @SubscribeEvent
    public static void onHeal(LivingHealEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack implant = ImplantUtil.getImplant(player, ModItems.CELL_ACCELERANT.get());
        if (implant.isEmpty()) return;

        float quality =  1 + ImplantUtil.getQuality(implant) / 200;
        event.setAmount(event.getAmount() * quality);
    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
