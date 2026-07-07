package net.vami.zoe.item.custom.implants;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ImplantOnHitEvent;
import net.vami.zoe.init.ModDamageTypes;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.ImplantUtil;
@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class PowerfistItem extends ImplantItem {
    public PowerfistItem(Properties pProperties) {
        super(pProperties);

    }

    @SubscribeEvent
    public static void onImplantHit(ImplantOnHitEvent event) {
        ItemStack item = event.getImplant();
        LivingEntity target = event.getEntity();
        DamageSource damageSource = event.getSource();
        Entity source = damageSource.getEntity();
        CompoundTag tag = item.getOrCreateTag();

        if (!damageSource.is(DamageTypes.PLAYER_ATTACK)) return;
        if (!(item.getItem() == ModItems.POWERFIST.get())) return;

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
        System.out.println("triggered powerfist");
    }

    @Override
    public void register() {
        ImplantUtil.registerImplant(this,
                ImplantUtil.Builder.create(), 10f);
    }
}
