package net.vami.zoe.item.custom.implant;

import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ZoeBlockImpactEvent;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class MountainShatteringLaceItem extends ImplantItem {
    public MountainShatteringLaceItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack implant = ImplantUtil.getImplant(player, this);
        if (implant.isEmpty()) return;

        float quality = ImplantUtil.getQuality(implant);

        if (event.getSource().is(DamageTypes.FLY_INTO_WALL)) {
            event.setAmount(event.getAmount() * (1 / (1 + quality)));
        }
    }

    @SubscribeEvent
    public static void onImpact(ZoeBlockImpactEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.MOUNTAIN_SHATTERING_LACE.get());
        if (implant.isEmpty()) return;

        float quality = ImplantUtil.getQuality(implant);
        quality = 1 + quality / 33;
        event.setDamage(event.getDamage() * quality);
        event.setPower(event.getPower() * quality);
    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
