package net.vami.zoe.item.custom.implant;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ModTags;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ZoeCritEvent;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class PendulockItem extends ImplantItem {
    public PendulockItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public void onHit(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (!event.getSource().is(ModTags.DamageTypes.IS_MELEE)) return;
        if (!(source.getEntity() instanceof Player player)) return;
        if (player.fallDistance <= 0f) return;

        ItemStack implant = ImplantUtil.getImplant(player, this);
        if (implant.isEmpty()) return;
        float quality = ImplantUtil.getQuality(implant);

        float multiplier = 1 + (quality / 100);

        event.setAmount(event.getAmount() * multiplier);
    }

    @SubscribeEvent
    public static void onCrit(ZoeCritEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        if (player.fallDistance < 4) return;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.PENDULOCK.get());
        if (implant.isEmpty()) return;

        float explosion =  2 + ImplantUtil.getQuality(implant) / 100;

        LivingEntity target = event.getEntity();
        target.invulnerableTime = 0;
        player.level().explode(player,
                target.getX(), target.getY(), target.getZ(),
                explosion, Level.ExplosionInteraction.MOB);
    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f,
                ImplantData.add(ForgeMod.ENTITY_GRAVITY.get(),
                        0.25d,
                        AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}
