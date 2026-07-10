package net.vami.zoe.util.implant;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.item.custom.ImplantItem;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImplantConfig {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    // this makes for %appdata%/.minecraft/config/zoe/implants/
    private static final Path IMPLANT_CONFIG_DIR = FMLPaths.CONFIGDIR.get()
            .resolve("zoe")
            .resolve("implants");

    public static Path getPath(ImplantItem item) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);

        if (itemId == null) {
            throw new IllegalStateException("Implant item is not registered: " + item);
        }

        String fileName = itemId.toString().replace(":", "_") + ".json";

        return IMPLANT_CONFIG_DIR.resolve(fileName);
    }

    public static void register(ImplantItem implant, ImplantData data) {
        Path path = getPath(implant);

        if (!Files.exists(path)) {
            write(path, data);
        }

        ImplantData existingData = read(path);

        if (existingData.update()) {
            write(path, data);
        }
    }

    public static ImplantData read(Path path) {
        try (Reader reader = Files.newBufferedReader(path)) {
            JsonElement json = JsonParser.parseReader(reader);

            return ImplantData.CODEC
                    .parse(JsonOps.INSTANCE, json)
                    .resultOrPartial(error ->
                            ZoeIsntCyberpunk.LOGGER.error(
                                    "Failed to parse implant config {}: {}",
                                    path,
                                    error
                            )
                    )
                    .orElse(ImplantData.DEFAULT);

        } catch (IOException exception) {
            ZoeIsntCyberpunk.LOGGER.error(
                    "Failed to read implant config {}",
                    path,
                    exception
            );

            return ImplantData.DEFAULT;
        }
    }

    public static void write(Path path, ImplantData data) {
        ImplantData.CODEC
                .encodeStart(JsonOps.INSTANCE, data)
                .resultOrPartial(error ->
                        ZoeIsntCyberpunk.LOGGER.error(
                                "Failed to encode implant config {}: {}",
                                path,
                                error
                        )
                )
                .ifPresent(json -> writeJson(path, json));
    }

    private static void writeJson(Path path, JsonElement json) {
        try {
            Files.createDirectories(path.getParent());

            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(json, writer);
            }

        } catch (IOException exception) {
            ZoeIsntCyberpunk.LOGGER.error(
                    "Failed to write implant config {}",
                    path,
                    exception
            );
        }
    }




}
