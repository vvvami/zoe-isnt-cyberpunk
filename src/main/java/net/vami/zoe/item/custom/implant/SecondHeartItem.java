package net.vami.zoe.item.custom.implant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class SecondHeartItem extends ImplantItem {
    public SecondHeartItem(Properties pProperties) {
        super(pProperties);

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.SECOND_HEART.get());
        if (implant.isEmpty()) return;

        CompoundTag tag = implant.getOrCreateTag();

        if (tag.getBoolean("zSecondHeart")) {
            tag.putBoolean("zSecondHeart", false);
            return;
        }

        float quality = ImplantUtil.getQuality(implant);

        player.setHealth(1 + quality / 10);
        event.setCanceled(true);

        serverLevel.sendParticles(ParticleTypes.HEART,
                (player.getX()), (player.getY() + player.getBbHeight() / 2), (player.getZ()),
                20,
                (player.getBbWidth() / 2), (player.getBbHeight() / 2), (player.getBbWidth() / 2),
                0.05);

        serverLevel.playSound(
                null,
                BlockPos.containing(
                        player.getX(),
                        player.getY() + player.getBbHeight() / 2,
                        player.getZ()),
                SoundEvents.TOTEM_USE, SoundSource.PLAYERS,
                0.45f, (float) Mth.nextDouble(RandomSource.create(), 2, 3));

        player.invulnerableTime = 20;
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, Integer.MAX_VALUE, (int) (quality / 5)));

        tag.putBoolean("zSecondHeart", true);
    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
