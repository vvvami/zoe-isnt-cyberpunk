package net.vami.zoe.util;

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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.capability.CapUtil;
import net.vami.zoe.capability.PlayerCapability;
import net.vami.zoe.event.custom.ImplantOnEquipEvent;
import net.vami.zoe.event.custom.ImplantOnUnequipEvent;
import net.vami.zoe.item.custom.implants.ImplantItem;
import net.vami.zoe.network.ModPackets;
import net.vami.zoe.network.packet.SyncImplantsS2CPacket;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class ImplantUtil {
    public static final String IMPLANT_CONFIG_PATH = "/config/zoe/implants";

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
                oldImplantItem.onUnequip(player, oldStack);
            }
        }

        ItemStack storedStack = implant.copy();
        implantList.set(slot, storedStack);
        capability.implants.set(implantList);

        ImplantOnEquipEvent equipEvent = new ImplantOnEquipEvent(player, storedStack);
        MinecraftForge.EVENT_BUS.post(equipEvent);

        if (!equipEvent.isCanceled()) {
            newImplantItem.onEquip(player, storedStack);
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

            implantItem.onUnequip(player, implantStack);
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

    public static void syncImplants(ServerPlayer player) {
        if (!CapUtil.hasCapability(player)) return;

        PlayerCapability capability = CapUtil.getCap(player);

        List<ItemStack> implantsCopy = new ArrayList<>(capability.implants.get());

        ModPackets.sendToTrackingAndSelf(
                new SyncImplantsS2CPacket(player.getUUID(), implantsCopy),
                player
        );
    }

    public static File getFile(ImplantItem item, String path) {
            if (path == null) return new File("");

            File itemFile;
            itemFile = new File((FMLPaths.GAMEDIR.get().toString() + path),
                    File.separator +
                            ((ForgeRegistries.ITEMS.getKey(item).toString())
                                    .replace(":", "_") + ".json"));
            return itemFile;
    }

    public static void registerImplant(ImplantItem implant, JsonArray attributeArr, float baseHumanity) {
        boolean updateFile = false;
        JsonObject mainObj = new JsonObject();

        File implantFile = getFile(implant, IMPLANT_CONFIG_PATH);
        if (implantFile.exists()) {
            {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(implantFile));
                    StringBuilder jsonstringbuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        jsonstringbuilder.append(line);
                    }
                    bufferedReader.close();
                    mainObj = new Gson().fromJson(jsonstringbuilder.toString(), JsonObject.class);
                    updateFile = mainObj.get("update").getAsBoolean();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                implantFile.getParentFile().mkdirs();
                implantFile.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            updateFile = true;
        }
        if (updateFile) {
            mainObj.addProperty("enabled", true);

            if (attributeArr != null) {
                mainObj.add("attributes", attributeArr);
            }

            mainObj.addProperty("maxQuality", 0);
            mainObj.addProperty("humanityScaling", baseHumanity);
            mainObj.addProperty("update", true);
        }
        Gson builder = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fileWriter = new FileWriter(implantFile);
            fileWriter.write(builder.toJson(mainObj));
            fileWriter.close();
        } catch (IOException exception) {
            ZoeIsntCyberpunk.LOGGER.error("Failed to write implant file - ", exception);

        }

    }

    private static final Map<AttributeModifier.Operation, UUID> IMPLANT_ATTRIBUTE_UUIDS = Map.of(
            AttributeModifier.Operation.ADDITION,
            UUID.fromString("eecb3dee-1220-486b-a27e-320e71c32c1d"),

            AttributeModifier.Operation.MULTIPLY_BASE,
            UUID.fromString("5c407147-88de-4171-b166-b1d41f021a2b"),

            AttributeModifier.Operation.MULTIPLY_TOTAL,
            UUID.fromString("d20c6d93-d60f-48d7-a96d-b21d4fd7fdb7")
    );

    public static void applyAttributes(LivingEntity entity, boolean apply) {
        if (!CapUtil.hasCapability(entity)) return;

        removeImplantAttributes(entity);

        if (!apply) {
            entity.setHealth(entity.getHealth());
            return;
        }

        PlayerCapability capability = CapUtil.getCap(entity);

        Map<Attribute, EnumMap<AttributeModifier.Operation, Double>> bonuses = new HashMap<>();

        for (ItemStack implant : capability.implants.get()) {
            if (implant.isEmpty()) continue;
            if (!(implant.getItem() instanceof ImplantItem implantItem)) continue;

            File implantFile = ImplantUtil.getFile(
                    implantItem,
                    ImplantUtil.IMPLANT_CONFIG_PATH
            );

            if (!implantFile.isFile()) {
                ZoeIsntCyberpunk.LOGGER.error("Implant file does not exist: {}", implantFile.getName());
                continue;
            }

            JsonObject mainObj;

            try (BufferedReader reader = Files.newBufferedReader(
                    implantFile.toPath(),
                    StandardCharsets.UTF_8
            )) {
                mainObj = new Gson().fromJson(reader, JsonObject.class);
            } catch (IOException | JsonParseException e) {
                ZoeIsntCyberpunk.LOGGER.error("Failed to read implant file {}", implantFile.getName(), e);
                continue;
            }

            if (mainObj == null || !mainObj.has("attributes") || !mainObj.get("attributes").isJsonArray()) {
                continue;
            }

            JsonArray attributes = mainObj.getAsJsonArray("attributes");

            for (JsonElement element : attributes) {
                if (!element.isJsonObject()) continue;

                JsonObject attributeObj = element.getAsJsonObject();

                if (!attributeObj.has("attribute") || !attributeObj.has("amplifier")) {
                    continue;
                }

                ResourceLocation attributeId = ResourceLocation.tryParse(
                        attributeObj.get("attribute").getAsString()
                );

                if (attributeId == null) {
                    continue;
                }

                Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(attributeId);

                if (attribute == null || entity.getAttribute(attribute) == null) {
                    continue;
                }

                double amplifier = attributeObj.get("amplifier").getAsDouble();

                String modifierName = attributeObj.has("modifier")
                        ? attributeObj.get("modifier").getAsString()
                        : "percentage";

                AttributeModifier.Operation operation = getOperationFromString(modifierName);

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
                        "implant_" + ForgeRegistries.ATTRIBUTES.getKey(attribute) + "_" + operation.name().toLowerCase(Locale.ROOT),
                        amount,
                        operation
                ));
            }
        }

        entity.setHealth(entity.getHealth());
    }

    private static AttributeModifier.Operation getOperationFromString(String modifier) {
        if (modifier == null) {
            return AttributeModifier.Operation.MULTIPLY_TOTAL;
        }

        return switch (modifier.toLowerCase(Locale.ROOT)) {
            case "percentage", "percent" ->
                    AttributeModifier.Operation.MULTIPLY_TOTAL;

            case "addition", "add" ->
                    AttributeModifier.Operation.ADDITION;

            default -> {
                ZoeIsntCyberpunk.LOGGER.warn(
                        "Unknown attribute modifier '{}', defaulting to percentage",
                        modifier
                );
                yield AttributeModifier.Operation.MULTIPLY_TOTAL;
            }
        };
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

    private static void clampHealth(LivingEntity entity) {
        entity.setHealth(entity.getHealth());
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

        boolean allSame = Collections.frequency(implants, implants.get(0).getItem()) == implants.size();

        return allSame;
    }

    public static boolean hasImplants(ArrayList<ItemStack> implants) {
        return Collections.frequency(implants, implants.get(0).getItem()) != implants.size();
    }

    public static class Builder {

        public static JsonArray create(JsonObject ... args) {
            JsonArray array = new JsonArray();
            for (JsonObject arg : args) {
                array.add(arg);
            }
            return array;
        }

        public static JsonObject add(String attribute, double amplifier, String modifier)  {
            if (attribute == null)
                return new JsonObject();
            JsonObject attributeObj = new JsonObject();
            attributeObj.addProperty("attribute", attribute);
            attributeObj.addProperty("amplifier", amplifier);
            attributeObj.addProperty("modifier", modifier);
            return attributeObj;
        }
    }
}
