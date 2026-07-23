package net.vami.zoe.item.custom.implant;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ZoeCritEvent;
import net.vami.zoe.init.ModDamageTypes;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.HurtUtil;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

import java.util.Optional;
@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class DoubleFluxItem extends ImplantItem {

    public DoubleFluxItem(Properties pProperties) {
        super(pProperties);
    }

    @SubscribeEvent(priority =  EventPriority.HIGHEST)
    public static void onEntityHit(LivingAttackEvent event) {
        DamageSource damageSource = event.getSource();
        if (!(damageSource.getEntity() instanceof Player player)) return;

        Entity target = event.getEntity();

        ItemStack item = ImplantUtil.getImplant(player, ModItems.DOUBLE_FLUX.get());
        if (item.isEmpty()) return;

        float quality = ImplantUtil.getQuality(item) / 100;

        CompoundTag tag = item.getOrCreateTag();
        if (tag.getBoolean("zDoubleFlux")) return;

        event.setCanceled(true);

        tag.putBoolean("zDoubleFlux", true);

        Registry<DamageType> registry = player.level().registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE);

        Optional<DamageSource> randSource = registry.getRandom(player.level().random)
                .map(DamageSource::new);

        randSource.ifPresent(source -> {
            DamageSource newSource = new DamageSource(source.typeHolder(), player);
            target.hurt(newSource,
                    event.getAmount() / (2 - quality));
        });

        tag.putBoolean("zDoubleFlux", false);
    }

    @SubscribeEvent
    public static void onCrit(ZoeCritEvent event) {
        DamageSource damageSource = event.getSource();
        if (!(damageSource.getEntity() instanceof Player player)) return;

        ItemStack item = ImplantUtil.getImplant(player, ModItems.DOUBLE_FLUX.get());
        if (item.isEmpty()) return;

        event.setKnockback(event.getKnockback() * 22);
        event.setMultiplier(event.getMultiplier() * 22);
    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
