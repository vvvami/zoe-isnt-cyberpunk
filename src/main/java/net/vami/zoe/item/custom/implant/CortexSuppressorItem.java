package net.vami.zoe.item.custom.implant;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

public class CortexSuppressorItem extends ImplantItem {
    private static final int FRAMES = 20;

    public CortexSuppressorItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onHit(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        DamageSource damageSource = event.getSource();
        Player source = (Player) damageSource.getEntity();

        ItemStack item = ImplantUtil.getImplant(source, this);
        if (item.isEmpty()) return;

        target.invulnerableTime = 0;

        float quality = ImplantUtil.getQuality(item);
        event.setAmount((float) (event.getAmount() /
                        (FRAMES - (quality / 7.5))));
    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
