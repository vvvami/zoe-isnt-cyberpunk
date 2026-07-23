package net.vami.zoe.event;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.capability.CapUtil;
import net.vami.zoe.capability.PlayerCapability;
import net.vami.zoe.capability.PlayerCapabilityProvider;
import net.vami.zoe.event.custom.*;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.network.ModPackets;
import net.vami.zoe.network.packet.SyncImplantsS2CPacket;
import net.vami.zoe.util.implant.ImplantConfig;
import net.vami.zoe.util.implant.ImplantUtil;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID)
public class ModImplantEvents {

    @Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID,
            bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class Registry {
        @SubscribeEvent
        public static void registerImplantConfigs(FMLLoadCompleteEvent event) {
            for (Item item : ForgeRegistries.ITEMS.getValues()) {
                if (item instanceof ImplantItem implantItem) {
                    ImplantConfig.register(implantItem, implantItem.data()); ;
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
    public static void playerRespawnApplyImplantAttributes(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();

        if (!player.level().isClientSide) {
            ImplantUtil.applyAttributes(player, true);
            player.setHealth(player.getMaxHealth());
        }
    }

    @SubscribeEvent
    public static void implantUnequip(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) return;

        Player player = event.getEntity();

        if (!player.getMainHandItem().isEmpty()
                || !player.isShiftKeyDown()) return;


        if (!CapUtil.hasCapability(player)) return;

        PlayerCapability capability = CapUtil.getCap(player);

        System.out.println("right clicked on block");

        for (int i = 0; i < capability.implants.get().size(); i++) {
            ItemStack implant = capability.implants.get().get(i);
            if (implant.isEmpty()) continue;

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
            if (item.isEmpty()) continue;
            ((ImplantItem) item.getItem()).onEquip(event);
        }

        if (!(player instanceof ServerPlayer serverPlayer)) return;
        ImplantUtil.syncImplants(serverPlayer);
    }

    @SubscribeEvent
    public static void onImplantUnequip(ImplantOnUnequipEvent event) {
        ImplantUtil.applyAttributes(event.getEntity(), false);

        Player player = event.getEntity();

        ArrayList<ItemStack> implants = ImplantUtil.implants(player);
        for (ItemStack item : implants) {
            if (item.isEmpty()) continue;
            ((ImplantItem) item.getItem()).onUnequip(event);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ImplantUtil.syncImplants(player);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ImplantUtil.syncImplants(player);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ImplantUtil.syncImplants(player);
    }

    // Renderer event
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getEntity() instanceof ServerPlayer watcher)) return;
        if (!(event.getTarget() instanceof ServerPlayer target)) return;

        if (!CapUtil.hasCapability(target)) return;

        PlayerCapability capability = CapUtil.getCap(target);

        List<ItemStack> implantsCopy = new ArrayList<>(capability.implants.get());

        ModPackets.sendToClient(
                new SyncImplantsS2CPacket(target.getUUID(), implantsCopy),
                watcher
        );
    }

//    @SubscribeEvent
//    public static void onImplantTick(TickEvent.PlayerTickEvent event) {
//        Player player = event.player;
//
//        ArrayList<ItemStack> implants = ImplantUtil.implants(player);
//
//        if (!ImplantUtil.hasImplants(implants)) return;
//
//        for (ItemStack item : implants) {
//            if (!(item.getItem() instanceof ImplantItem)) continue;
//
//            ((ImplantItem) item.getItem()).onTick(player, item);
//        }
//    }

    @SubscribeEvent
    public static void onImplantHit(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player)) return;
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (item instanceof ImplantItem) {
                ((ImplantItem) item).onHit(event);
            }
        }
    }

    @SubscribeEvent
    public static void onImplantHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (item instanceof ImplantItem) {
                ((ImplantItem) item).onHurt(event);
            }
        }
    }

    @SubscribeEvent
    public static void onImplantKill(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        ArrayList<ItemStack> implants = ImplantUtil.implants(player);

        if (!ImplantUtil.hasImplants(implants)) return;

        for (ItemStack item : implants) {
            if (!(item.getItem() instanceof ImplantItem)) continue;

            ImplantOnKillEvent IKE = new ImplantOnKillEvent
                    (item, event.getEntity(), event.getSource());
            MinecraftForge.EVENT_BUS.post(IKE);

            if (IKE.isCanceled()) {
                event.setCanceled(true);
                return;
            }
        }
    }

    @SubscribeEvent
    public static void onImplantDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ArrayList<ItemStack> implants = ImplantUtil.implants(player);

        if (!ImplantUtil.hasImplants(implants)) return;

        for (ItemStack item : implants) {
            if (item.getItem() instanceof ImplantItem) {

                ImplantOnDeathEvent IDE = new ImplantOnDeathEvent
                        (item, player, event.getSource());
                MinecraftForge.EVENT_BUS.post(IDE);

                if (IDE.isCanceled()) {
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }
}
