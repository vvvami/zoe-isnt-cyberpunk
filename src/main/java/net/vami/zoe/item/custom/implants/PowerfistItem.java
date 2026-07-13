package net.vami.zoe.item.custom.implants;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ImplantOnHitEvent;
import net.vami.zoe.init.ModDamageTypes;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantConfig;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;
public class PowerfistItem extends ImplantItem {
    public PowerfistItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public void onHit(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        DamageSource damageSource = event.getSource();
        Player source = (Player) damageSource.getEntity();

        ItemStack item = ImplantUtil.getImplant(source, this);
        if (item.isEmpty()) return;

        CompoundTag tag = item.getOrCreateTag();

        if (!damageSource.is(DamageTypes.PLAYER_ATTACK)) return;
        if (!source.getMainHandItem().isEmpty()) return;

        if (tag.getBoolean("zPowerfist")) {
            return;
        }

        tag.putBoolean("zPowerfist", true);

        try {
            target.hurt(
                    ModDamageTypes.get(source, ModDamageTypes.VOLT),
                    (float) Math.sqrt(Math.max(1, ImplantUtil.getQuality(item)))
            );
        } finally {
            tag.putBoolean("zPowerfist", false);
        }
    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
