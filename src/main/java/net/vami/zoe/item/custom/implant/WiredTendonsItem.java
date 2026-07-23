package net.vami.zoe.item.custom.implant;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.effect.ModEffects;
import net.vami.zoe.event.custom.ZoeCritEvent;
import net.vami.zoe.init.ModDamageTypes;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.HurtUtil;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class WiredTendonsItem extends ImplantItem {
    public WiredTendonsItem(Properties pProperties) {
        super(pProperties);

    }

    /** {@link net.vami.zoe.mixin.client.GameRendererMixin mixins for attack span}
     *
     *
     */

    @Override
    public ImplantData data() {
        return ImplantData.build(10f,
                ImplantData.add(ForgeMod.ENTITY_REACH.get(),
                        0.25,
                        AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}
