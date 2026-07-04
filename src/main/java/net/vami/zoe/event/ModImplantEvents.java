package net.vami.zoe.event;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.capability.CapabilityUtil;
import net.vami.zoe.capability.PlayerCapability;
import net.vami.zoe.capability.PlayerCapabilityProvider;
import net.vami.zoe.event.custom.ImplantOnEquipEvent;
import net.vami.zoe.event.custom.ImplantOnUnequipEvent;
import net.vami.zoe.item.custom.implants.ImplantItem;
import net.vami.zoe.util.ImplantUtil;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class ModImplantEvents {

    @Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class implantRegistry {
        @SubscribeEvent
        public static void registerImplantConfigs(FMLLoadCompleteEvent event) {
            for (Item item : ForgeRegistries.ITEMS.getValues()) {
                if (item instanceof ImplantItem implantItem) {
                    implantItem.register();
                }
            }
        }
    }


    @SubscribeEvent
    public static void playerCloneApplyImplantAttributes(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        oldPlayer.reviveCaps();

        oldPlayer.getCapability(PlayerCapabilityProvider.CAPABILITY).ifPresent(oldCap -> {
            newPlayer.getCapability(PlayerCapabilityProvider.CAPABILITY).ifPresent(newCap -> {
                newCap.copyFrom(oldCap);
            });
        });

        oldPlayer.invalidateCaps();

        if (!newPlayer.level().isClientSide) {
            ImplantUtil.applyAttributes(newPlayer, true);
        }
    }

    @SubscribeEvent
    public static void playerJoinApplyImplantAttributes(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (!player.level().isClientSide) {
            ImplantUtil.applyAttributes(player, true);
        }
    }

    @SubscribeEvent
    public static void implantUnequip(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) return;

        Player player = event.getEntity();

        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() != Items.AIR
                || !player.isShiftKeyDown()) return;


        if (!CapabilityUtil.checkCapability(player)) return;

        PlayerCapability capability = CapabilityUtil.getCapability(player);

        System.out.println("right clicked on block");

        for (int i = 0; i < capability.implants.get().size(); i++) {
            ItemStack implant = capability.implants.get().get(i);
            if (implant.getItem() == Items.AIR) continue;

            System.out.println("implant: " + implant);
            System.out.println("added to inv: " + player.getInventory().add(implant.copy()));

            ImplantUtil.clearSlot(player, i);
        }
    }

    @SubscribeEvent
    public static void onImplantEquip(ImplantOnEquipEvent event) {
        ImplantUtil.applyAttributes(event.getEntity(), true);

        Player player = event.getEntity();
        ArrayList<ItemStack> implants = ImplantUtil.implants(player);
        for (ItemStack item : implants) {
            if (item.getItem() == Items.AIR) continue;
            ((ImplantItem) item.getItem()).onEquip(player, item);
        }
    }

    @SubscribeEvent
    public static void onImplantUnequip(ImplantOnUnequipEvent event) {
        ImplantUtil.applyAttributes(event.getEntity(), false);

        Player player = event.getEntity();
        ArrayList<ItemStack> implants = ImplantUtil.implants(player);
        for (ItemStack item : implants) {
            if (item.getItem() == Items.AIR) continue;
            ((ImplantItem) item.getItem()).onUnequip(player, item);
        }
    }

    @SubscribeEvent
    public static void onImplantTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        ArrayList<ItemStack> implants = ImplantUtil.implants(player);
        for (ItemStack item : implants) {
            if (item.getItem() == Items.AIR) continue;
            ((ImplantItem) item.getItem()).onTick(player, item);
        }
    }

    @SubscribeEvent
    public static void onImplantHit(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }
        for (ItemStack item : ImplantUtil.implants(player)) {
            if (item.getItem() instanceof ImplantItem implantItem) {
                implantItem.onHit(player, event.getEntity(), item);
            }
        }
    }

    @SubscribeEvent
    public static void onImplantHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        for (ItemStack item : ImplantUtil.implants(player)) {
            if (item.getItem() instanceof ImplantItem implantItem) {
                implantItem.onHurt(event.getSource().getEntity(), player, item);
            }
        }
    }

    @SubscribeEvent
    public static void onImplantKill(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }
        for (ItemStack item : ImplantUtil.implants(player)) {
            if (item.getItem() instanceof ImplantItem implantItem) {
                implantItem.onKill(player, event.getEntity(), item);
            }
        }
    }

    @SubscribeEvent
    public static void onImplantDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        for (ItemStack item : ImplantUtil.implants(player)) {
            if (item.getItem() instanceof ImplantItem implantItem) {
                implantItem.onDeath(event.getSource().getEntity(), player, item);
            }
        }
    }
}
