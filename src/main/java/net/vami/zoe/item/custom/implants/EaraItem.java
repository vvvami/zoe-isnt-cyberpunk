package net.vami.zoe.item.custom.implants;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ImplantOnHurtEvent;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantConfig;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class EaraItem extends ImplantItem {
    public EaraItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public void onEquip(LivingEntity entity, ItemStack item) {
        if (!(entity instanceof Player player)) return;
        if (player.isCreative() || player.isSpectator()) return;

        player.getAbilities().mayfly = true;
        player.getAbilities().flying = true;
        player.onUpdateAbilities();
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.isCreative() || player.isSpectator()) return;

        ArrayList<ItemStack> implants = ImplantUtil.implants(player);
        for (ItemStack implant : implants) {
            if (implant.getItem() == ModItems.EARA.get()) {
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
                break;
            }
        }
    }

    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player.isCreative() || player.isSpectator()) return;

        ArrayList<ItemStack> implants = ImplantUtil.implants(player);
        for (ItemStack implant : implants) {
            if (implant.getItem() == ModItems.EARA.get()) {
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
                break;
            }
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();
        if (newPlayer.isCreative() || newPlayer.isSpectator()) return;

        ArrayList<ItemStack> implants = ImplantUtil.implants(oldPlayer);
        for (ItemStack implant : implants) {
            if (implant.getItem() == ModItems.EARA.get()) {
                newPlayer.getAbilities().mayfly = true;
                newPlayer.onUpdateAbilities();
                break;
            }
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (player.isCreative() || player.isSpectator()) return;

        ArrayList<ItemStack> implants = ImplantUtil.implants(player);
        for (ItemStack implant : implants) {
            if (implant.getItem() == ModItems.EARA.get()) {
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
                break;
            }
        }
    }


    @Override
    public void onUnequip(LivingEntity entity, ItemStack item) {
        if (!(entity instanceof Player player)) return;
        if (player.isCreative() || player.isSpectator()) return;

        player.getAbilities().mayfly = false;
        player.getAbilities().flying = false;
        player.onUpdateAbilities();
    }

    @SubscribeEvent
    public static void onHurt(ImplantOnHurtEvent event) {
        Player player = event.getPlayer();
        if (player.isCreative() || player.isSpectator()) return;

        if (event.getItem() == ModItems.EARA.get()) {
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
        }
    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
