package net.vami.zoe.item.custom.implant;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.HurtUtil;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

public class BurningPalmItem extends ImplantItem {

    public BurningPalmItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onHit(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        DamageSource damageSource = event.getSource();
        if (damageSource.is(DamageTypes.ON_FIRE)) return;

        Player source = (Player) damageSource.getEntity();
        ItemStack item = ImplantUtil.getImplant(source, this);
        if (item.isEmpty()) return;

        float quality = ImplantUtil.getQuality(item);
        target.setRemainingFireTicks(target.getRemainingFireTicks() + (int) quality * 2);
        target.invulnerableTime = 0;
        target.hurt(HurtUtil.get(source, DamageTypes.ON_FIRE), quality / 20);
    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
