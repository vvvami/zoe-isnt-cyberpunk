package net.vami.zoe.util.implant;

import com.google.gson.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.capability.CapUtil;
import net.vami.zoe.capability.PlayerCapability;
import net.vami.zoe.event.custom.ImplantOnEquipEvent;
import net.vami.zoe.event.custom.ImplantOnUnequipEvent;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.network.ModPackets;
import net.vami.zoe.network.packet.SyncImplantsS2CPacket;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class ImplantUtil {
    public static final String QUALITY_TAG = "zQuality";

    public static void setSlot(Player player, ItemStack implant, int slot) {
        if (implant.isEmpty()) return;
        if (!(implant.getItem() instanceof ImplantItem newImplantItem)) return;

        PlayerCapability capability = CapUtil.getCap(player);

        ArrayList<ItemStack> implantList = capability.implants.get();

        ItemStack oldStack = implantList.get(slot);

        if (oldStack.getItem() instanceof ImplantItem oldImplantItem) {
            ImplantOnUnequipEvent unequipEvent = new ImplantOnUnequipEvent(player, oldStack);
            MinecraftForge.EVENT_BUS.post(unequipEvent);

            if (!unequipEvent.isCanceled()) {
                oldImplantItem.onUnequip(unequipEvent);
            }
        }

        ItemStack storedStack = implant.copy();
        implantList.set(slot, storedStack);
        capability.implants.set(implantList);

        ImplantOnEquipEvent equipEvent = new ImplantOnEquipEvent(player, storedStack);
        MinecraftForge.EVENT_BUS.post(equipEvent);

        if (!equipEvent.isCanceled()) {
            newImplantItem.onEquip(equipEvent);
        }
    }

    public static void clearSlot(Player player, int slot) {
        PlayerCapability capability = CapUtil.getCap(player);

        ArrayList<ItemStack> implantList = capability.implants.get();

        ItemStack implantStack = implantList.get(slot);

        ImplantOnUnequipEvent IUE = new ImplantOnUnequipEvent(player, implantStack);
        MinecraftForge.EVENT_BUS.post(IUE);

        if (!IUE.isCanceled()
                && implantStack.getItem() instanceof ImplantItem implantItem) {

            implantItem.onUnequip(IUE);
        }

        implantList.set(slot, ItemStack.EMPTY);
        ImplantUtil.set(implantList, capability);

        // Sync implant unequipping to client
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        ImplantUtil.syncImplants(serverPlayer);
    }

    public static ItemStack getImplant(Player player, Item item) {
        if (!CapUtil.hasCapability(player)) return ItemStack.EMPTY;

        PlayerCapability capability = CapUtil.getCap(player);
        ArrayList<ItemStack> implantList = capability.implants.get();

        for (ItemStack itemStack : implantList) {
            if (itemStack.getItem() == item) {
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static int implantCount(Player player, Item item) {
        if (!CapUtil.hasCapability(player)) return 0;
        PlayerCapability capability = CapUtil.getCap(player);
        ArrayList<ItemStack> implantList = capability.implants.get();
        return (int) implantList.stream()
                .filter(stack -> stack.getItem() == item)
                .count();
    }

    public static ItemStack getSlot(int slot, PlayerCapability capability) {
        return capability.implants.get().get(slot);
    }

    public static ItemStack getSlot(Player player, int slot) {
        PlayerCapability capability = CapUtil.getCap(player);
        return capability.implants.get().get(slot);
    }

    public static ArrayList<ItemStack> implants(Player player) {
        if (CapUtil.hasCapability(player)) {
            PlayerCapability capability = CapUtil.getCap(player);
            return capability.implants.get();
        }
        return new ArrayList<>(Collections.nCopies(20, ItemStack.EMPTY));
    }

    public static void set(ArrayList<ItemStack> implants, PlayerCapability capability) {
        capability.implants.set(implants);
    }

    // syncs implants to the client
    public static void syncImplants(ServerPlayer player) {
        if (!CapUtil.hasCapability(player)) return;

        PlayerCapability capability = CapUtil.getCap(player);

        List<ItemStack> implantsCopy = new ArrayList<>(capability.implants.get());

        ModPackets.sendToTrackingAndSelf(new SyncImplantsS2CPacket(player.getUUID(), implantsCopy),
                player);
    }

    // every operation needs its own uuid or they wont function properly
    // there needa be differentiation between "ADDITION" and "MULTIPLY BASE" for example
    private static final Map<AttributeModifier.Operation, UUID> IMPLANT_ATTRIBUTE_UUIDS = Map.of(
            AttributeModifier.Operation.ADDITION,
            UUID.fromString("eecb3dee-1220-486b-a27e-320e71c32c1d"),

            AttributeModifier.Operation.MULTIPLY_BASE,
            UUID.fromString("5c407147-88de-4171-b166-b1d41f021a2b"),

            AttributeModifier.Operation.MULTIPLY_TOTAL,
            UUID.fromString("d20c6d93-d60f-48d7-a96d-b21d4fd7fdb7")
    );

    public static void applyAttributes(LivingEntity entity, boolean apply) {
        removeImplantAttributes(entity);

        if (!apply) {
            entity.setHealth(entity.getHealth());
            return;
        }

        if (!CapUtil.hasCapability(entity)) return;
        PlayerCapability capability = CapUtil.getCap(entity);

        // this creates a map like this
        // MOVEMENT_SPEED:
        // ADDITION        -> 0.2
        // MULTIPLY_TOTAL  -> 0.15
        Map<Attribute, EnumMap<AttributeModifier.Operation, Double>> bonuses = new HashMap<>();

        for (ItemStack implant : capability.implants.get()) {
            if (implant.isEmpty()) continue;
            if (!(implant.getItem() instanceof ImplantItem implantItem)) continue;

            Path path = ImplantConfig.getPath(implantItem);
            ImplantData implantData = ImplantConfig.read(path);

            if (implantData == ImplantData.DEFAULT) {
                ZoeIsntCyberpunk.LOGGER.error("Implant file does not exist: {}", implantItem.getDescriptionId());
                continue;
            }

            List<ImplantData.Attribute> attributes = implantData.attributes();

            for (ImplantData.Attribute attr : attributes) {


                ResourceLocation attributeId = attr.attribute();

                if (attributeId == null) {
                    continue;
                }

                Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(attributeId);

                if (attribute == null || entity.getAttribute(attribute) == null) {
                    continue;
                }

                double amplifier = attr.amplifier();

                AttributeModifier.Operation operation = attr.modifier();

                double quality = ImplantUtil.getQuality(implant);

                double amount = amplifier * (quality / 100);

                bonuses
                        .computeIfAbsent(attribute, a -> new EnumMap<>(AttributeModifier.Operation.class))
                        .merge(operation, amount, Double::sum);
            }
        }

        for (Map.Entry<Attribute, EnumMap<AttributeModifier.Operation, Double>> attributeEntry : bonuses.entrySet()) {
            Attribute attribute = attributeEntry.getKey();
            AttributeInstance instance = entity.getAttribute(attribute);

            if (instance == null) continue;

            for (Map.Entry<AttributeModifier.Operation, Double> operationEntry : attributeEntry.getValue().entrySet()) {
                AttributeModifier.Operation operation = operationEntry.getKey();
                double amount = operationEntry.getValue();

                if (amount == 0) continue;

                UUID uuid = IMPLANT_ATTRIBUTE_UUIDS.get(operation);

                instance.addTransientModifier(new AttributeModifier(
                        uuid,
                        "implant_" + ForgeRegistries.ATTRIBUTES.getKey(attribute) + "_" + operation.name().toLowerCase(),
                        amount,
                        operation
                ));
            }
        }

        entity.setHealth(entity.getHealth());
    }

    private static void removeImplantAttributes(LivingEntity entity) {
        for (Attribute attribute : ForgeRegistries.ATTRIBUTES.getValues()) {
            AttributeInstance instance = entity.getAttribute(attribute);
            if (instance == null) continue;

            for (UUID uuid : IMPLANT_ATTRIBUTE_UUIDS.values()) {
                if (instance.getModifier(uuid) != null) {
                    instance.removeModifier(uuid);
                }
            }
        }
    }

    public static float getQuality(ItemStack implant) {
        CompoundTag tag = implant.getTag();

        if (tag == null) return 0;

        if (tag.contains(QUALITY_TAG))
            return tag.getFloat(QUALITY_TAG);

        return 0;
    }

    public static boolean hasImplants(Player player) {
        if (!CapUtil.hasCapability(player)) return false;

        PlayerCapability capability = CapUtil.getCap(player);

        ArrayList<ItemStack> implants = capability.implants.get();

        return hasImplants(implants);
    }

    public static boolean hasImplants(ArrayList<ItemStack> implants) {
        for (ItemStack implant : implants) {
            if (!implant.isEmpty()) return true;
        }
        return false;
    }
}
