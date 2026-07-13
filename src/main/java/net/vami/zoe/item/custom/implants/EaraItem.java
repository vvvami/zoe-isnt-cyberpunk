package net.vami.zoe.item.custom.implants;

import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ImplantOnEquipEvent;
import net.vami.zoe.event.custom.ImplantOnHurtEvent;
import net.vami.zoe.event.custom.ImplantOnUnequipEvent;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class EaraItem extends ImplantItem {
    public EaraItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public void onEquip(ImplantOnEquipEvent event) {
        Player player = event.getEntity();
        if (player.isCreative() || player.isSpectator()) return;
        EaraItem.updateFlying(player, event.getImplant(), true);
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.isCreative() || player.isSpectator()) return;

        ArrayList<ItemStack> implants = ImplantUtil.implants(player);
        for (ItemStack implant : implants) {
            if (implant.getItem() == ModItems.EARA.get()) {
                EaraItem.updateFlyingJump(player, implant, true);
                break;
            }
        }
    }

    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player.isCreative() || player.isSpectator()) {
            player.getAbilities().setFlyingSpeed(0.05f);
            player.onUpdateAbilities();
            return;
        }

        ArrayList<ItemStack> implants = ImplantUtil.implants(player);
        for (ItemStack implant : implants) {
            if (implant.getItem() == ModItems.EARA.get()) {
                EaraItem.updateFlying(player, implant, true);
                break;
            }
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();
        if (oldPlayer.isCreative() || oldPlayer.isSpectator()) {
            newPlayer.getAbilities().setFlyingSpeed(0.05f);
            newPlayer.onUpdateAbilities();
            return;
        }

        ArrayList<ItemStack> implants = ImplantUtil.implants(oldPlayer);
        for (ItemStack implant : implants) {
            if (implant.getItem() == ModItems.EARA.get()) {
                EaraItem.updateFlying(newPlayer, implant, false);
                break;
            }
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (player.isCreative() || player.isSpectator()) {
            player.getAbilities().setFlyingSpeed(0.05f);
            player.onUpdateAbilities();
            return;
        }

        ArrayList<ItemStack> implants = ImplantUtil.implants(player);
        for (ItemStack implant : implants) {
            if (implant.getItem() == ModItems.EARA.get()) {
                EaraItem.updateFlying(player, implant, true);
                break;
            }
        }
    }

    @Override
    public void onUnequip(ImplantOnUnequipEvent event) {
        Player player = event.getEntity();
        if (player.isCreative() || player.isSpectator()) {
            player.getAbilities().setFlyingSpeed(0.05f);
            player.onUpdateAbilities();
            return;
        }

        EaraItem.updateFlying(player, event.getImplant(), false);
    }

    @Override
    public void onHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.isCreative() || player.isSpectator()) return;
        ArrayList<ItemStack> implants = ImplantUtil.implants(player);
        for (ItemStack implant : implants) {
            if (implant.getItem() == this) {
                EaraItem.updateFlying(player, implant, false);
            }
        }
    }

    private static void updateFlying(Player player, ItemStack implant, boolean canFly) {
        Abilities abilities = player.getAbilities();
        abilities.mayfly = canFly;
        abilities.flying = canFly;
        float quality = ImplantUtil.getQuality(implant);
        abilities.setFlyingSpeed(canFly ? quality / 1000 : 0.05f);
        player.onUpdateAbilities();
    }

    private static void updateFlyingJump(Player player, ItemStack implant, boolean canFly) {
        Abilities abilities = player.getAbilities();
        abilities.mayfly = canFly;
        float quality = ImplantUtil.getQuality(implant);
        abilities.setFlyingSpeed(canFly ? quality / 1000 : 0.05f);
        player.onUpdateAbilities();
    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
