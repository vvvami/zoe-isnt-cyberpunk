package net.vami.zoe.util.implant;

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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.capability.CapUtil;
import net.vami.zoe.capability.PlayerCapability;
import net.vami.zoe.event.custom.ImplantOnEquipEvent;
import net.vami.zoe.event.custom.ImplantOnUnequipEvent;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.network.ModPackets;
import net.vami.zoe.network.packet.SyncImplantsS2CPacket;

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

        for (ItemStack itemStack : capability.implants.get()) {
            if (itemStack.getItem() == item) {
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static List<ItemStack> getImplants(Player player, Item item) {
        ArrayList<ItemStack> implantList = new ArrayList<>();

        if (!CapUtil.hasCapability(player)) return implantList;
        PlayerCapability capability = CapUtil.getCap(player);

        for (ItemStack itemStack : capability.implants.get()) {
            if (itemStack.getItem() == item) {
                implantList.add(itemStack);
            }
        }
        return implantList;
    }

    public static int count(Player player, Item item) {
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
            resetHealth(entity);
            return;
        }

        if (!CapUtil.hasCapability(entity)) return;

        PlayerCapability capability = CapUtil.getCap(entity);

        Map<Attribute, EnumMap<AttributeModifier.Operation, Double>> bonuses = collectAttributeBonuses(entity, capability.implants.get());

        applyAttributeBonuses(entity, bonuses);
        resetHealth(entity);
    }

    private static Map<Attribute, EnumMap<AttributeModifier.Operation, Double>> collectAttributeBonuses(LivingEntity entity, Iterable<ItemStack> implants) {
        Map<Attribute, EnumMap<AttributeModifier.Operation, Double>> bonuses = new HashMap<>();

        for (ItemStack implant : implants) {
            collectImplantBonuses(entity, implant, bonuses);
        }

        return bonuses;
    }

    public static void collectImplantBonuses(LivingEntity entity, ItemStack implant, Map<Attribute, EnumMap<AttributeModifier.Operation, Double>> bonuses) {
        if (implant.isEmpty()) return;
        if (!(implant.getItem() instanceof ImplantItem implantItem)) return;

        ImplantData implantData = readImplantData(implantItem);
        if (implantData == null) return;

        double qualityMultiplier = ImplantUtil.getQuality(implant) / 100;

        for (ImplantData.Attribute attributeData : implantData.attributes()) {
            collectAttributeBonus(entity, attributeData, qualityMultiplier, bonuses);
        }
    }

    public static Map<Attribute, EnumMap<AttributeModifier.Operation, Double>>
    collectImplantBonuses(LivingEntity entity, ItemStack implant) {

        Map<Attribute, EnumMap<AttributeModifier.Operation, Double>> bonuses =
                new LinkedHashMap<>();

        collectImplantBonuses(entity, implant, bonuses);

        return bonuses;
    }

    private static ImplantData readImplantData(ImplantItem implantItem) {
        Path path = ImplantConfig.getPath(implantItem);
        ImplantData implantData = ImplantConfig.read(path);

        if (implantData == ImplantData.DEFAULT) {
            ZoeIsntCyberpunk.LOGGER.error(
                    "Implant file does not exist: {}",
                    implantItem.getDescriptionId()
            );

            return null;
        }

        return implantData;
    }

    private static void collectAttributeBonus(LivingEntity entity, ImplantData.Attribute attributeData, double qualityMultiplier, Map<Attribute, EnumMap<AttributeModifier.Operation, Double>> bonuses) {
        Attribute attribute = resolveAttribute(entity, attributeData.attribute());
        if (attribute == null) return;

        double amount = attributeData.amplifier() * qualityMultiplier;
        if (amount == 0) return;

        addBonus(bonuses, attribute, attributeData.operation(), amount);
    }

    public static Attribute resolveAttribute(LivingEntity entity, ResourceLocation attributeId) {
        if (attributeId == null) return null;

        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(attributeId);

        if (attribute == null || entity.getAttribute(attribute) == null) return null;

        return attribute;
    }

    private static void addBonus(Map<Attribute, EnumMap<AttributeModifier.Operation, Double>> bonuses, Attribute attribute, AttributeModifier.Operation operation, double amount) {
        bonuses.computeIfAbsent(
                        attribute,
                        ignored -> new EnumMap<>(AttributeModifier.Operation.class))
                .merge(operation, amount, Double::sum);
    }

    private static void applyAttributeBonuses(LivingEntity entity, Map<Attribute, EnumMap<AttributeModifier.Operation, Double>> bonuses) {
        for (Map.Entry<Attribute, EnumMap<AttributeModifier.Operation, Double>> attributeEntry : bonuses.entrySet()) {

            AttributeInstance instance = entity.getAttribute(attributeEntry.getKey());

            if (instance == null) continue;

            applyOperationBonuses(instance, attributeEntry.getKey(), attributeEntry.getValue());
        }
    }

    public static void applyOperationBonuses(AttributeInstance instance, Attribute attribute, EnumMap<AttributeModifier.Operation, Double> operationBonuses) {
        for (Map.Entry<AttributeModifier.Operation, Double> operationEntry : operationBonuses.entrySet()) {
            AttributeModifier.Operation operation = operationEntry.getKey();
            double amount = operationEntry.getValue();

            if (amount == 0) continue;

            instance.addTransientModifier(createImplantModifier(attribute, operation, amount));
        }
    }

    public static AttributeModifier createImplantModifier(Attribute attribute, AttributeModifier.Operation operation, double amount) {
        UUID uuid = IMPLANT_ATTRIBUTE_UUIDS.get(operation);

        return new AttributeModifier(uuid, createModifierName(attribute, operation), amount, operation);
    }

    private static String createModifierName(Attribute attribute, AttributeModifier.Operation operation) {
        ResourceLocation attributeId = ForgeRegistries.ATTRIBUTES.getKey(attribute);

        return "implant_" + attributeId + "_" + operation.name().toLowerCase();
    }

    private static void resetHealth(LivingEntity entity) {
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

    public static double calculateAttributePreview(AttributeInstance instance, EnumMap<AttributeModifier.Operation, Double> previewBonuses) {
        double baseValue = instance.getBaseValue();

        double addition = 0d;
        double multiplyBase = 0d;
        double multiplyTotal = 1d;

        for (AttributeModifier modifier : instance.getModifiers()) {
            switch (modifier.getOperation()) {
                case ADDITION ->
                        addition += modifier.getAmount();

                case MULTIPLY_BASE ->
                        multiplyBase += modifier.getAmount();

                case MULTIPLY_TOTAL ->
                        multiplyTotal *= 1d + modifier.getAmount();
            }
        }

        addition += previewBonuses.getOrDefault(
                AttributeModifier.Operation.ADDITION,
                0d);

        multiplyBase += previewBonuses.getOrDefault(
                AttributeModifier.Operation.MULTIPLY_BASE,
                0d);

        multiplyTotal *= 1d + previewBonuses.getOrDefault(
                AttributeModifier.Operation.MULTIPLY_TOTAL,
                0d);

        double afterAddition = baseValue + addition;

        double afterMultiplyBase =
                afterAddition + afterAddition * multiplyBase;

        double finalValue =
                afterMultiplyBase * multiplyTotal;

        return instance.getAttribute().sanitizeValue(finalValue);
    }
}
