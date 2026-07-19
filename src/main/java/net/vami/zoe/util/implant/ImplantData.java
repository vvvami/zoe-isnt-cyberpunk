package net.vami.zoe.util.implant;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.ZoeIsntCyberpunk;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record ImplantData(boolean enabled,
                          List<Attribute> attributes,
                          int maxQuality,
                          float humanityScaling,
                          boolean update) {

    public static final ImplantData DEFAULT = new ImplantData(
            false,
            List.of(),
            -1,
            0f,
            false
    );

    public static final Codec<ImplantData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL
                            .optionalFieldOf("enabled")
                            .xmap(optional -> optional.orElse(true), Optional::of)
                            .forGetter(ImplantData::enabled),

                    Attribute.CODEC
                            .listOf()
                            .optionalFieldOf("attributes")
                            .xmap(optional -> optional.orElse(List.of()), Optional::of)
                            .forGetter(ImplantData::attributes),

                    Codec.INT
                            .optionalFieldOf("maxQuality")
                            .xmap(optional -> optional.orElse(0), Optional::of)
                            .forGetter(ImplantData::maxQuality),

                    Codec.FLOAT
                            .optionalFieldOf("humanityScaling")
                            .xmap(optional -> optional.orElse(1f), Optional::of)
                            .forGetter(ImplantData::humanityScaling),

                    Codec.BOOL
                            .optionalFieldOf("update")
                            .xmap(optional -> optional.orElse(true), Optional::of)
                            .forGetter(ImplantData::update)
            ).apply(instance, ImplantData::new)
    );

    public static ImplantData build(List<Attribute> attributes, float baseHumanity) {
        return new ImplantData(true, attributes, 0, baseHumanity, true);
    }

    public static ImplantData build(float baseHumanity, Attribute ... attributes) {
        return new ImplantData(true, Arrays.stream(attributes).toList(), 0, baseHumanity, true);
    }

    public static ImplantData build(float baseHumanity) {
        return new ImplantData(true, List.of(), 0, baseHumanity, true);
    }

    public static ImplantData.Attribute add(net.minecraft.world.entity.ai.attributes.Attribute attribute, double amount, AttributeModifier.Operation operation) {
        return ImplantData.Attribute.build(attribute, amount, operation);
    }

    public static List<ImplantData.Attribute> attributes(ImplantData.Attribute ... attributes) {
        return Arrays.stream(attributes).toList();
    }


    // for the attribute array dumbassery
    public record Attribute(
            ResourceLocation attribute,
            double amplifier,
            AttributeModifier.Operation operation
    ) {
        private static final Codec<Attribute> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ResourceLocation.CODEC
                                .fieldOf("attribute")
                                .forGetter(Attribute::attribute),

                        Codec.DOUBLE
                                .fieldOf("amplifier")
                                .forGetter(Attribute::amplifier),

                        Modifier.CODEC
                                .fieldOf("operation")
                                .forGetter(Attribute::operation)
                ).apply(instance, Attribute::new)
        );

        public static ImplantData.Attribute build(ResourceLocation attribute, double amplifier, AttributeModifier.Operation modifier) {
            return new ImplantData.Attribute(attribute, amplifier, modifier);
        }

        public static ImplantData.Attribute build(net.minecraft.world.entity.ai.attributes.Attribute attribute, double amplifier, AttributeModifier.Operation modifier) {
            ResourceLocation res = ForgeRegistries.ATTRIBUTES.getKey(attribute);
            return new ImplantData.Attribute(res, amplifier, modifier);
        }


        // shitty operation type interpreter
        public static class Modifier {
            public static final Codec<AttributeModifier.Operation> CODEC = Codec.STRING.comapFlatMap(
                    Modifier::decode,
                    Modifier::encode
            );

            private static DataResult<AttributeModifier.Operation> decode(String name) {
                return switch (name.toLowerCase()) {
                    case "addition", "add", "flat" ->
                            DataResult.success(AttributeModifier.Operation.ADDITION);

                    case "multiply_base", "base_percentage", "base_percent", "base" ->
                            DataResult.success(AttributeModifier.Operation.MULTIPLY_BASE);

                    case "percentage", "percent", "multiply_total", "total_percentage", "total_percent" ->
                            DataResult.success(AttributeModifier.Operation.MULTIPLY_TOTAL);

                    default -> DataResult.error(() -> "Unknown attribute operation operation: " + name);
                };
            }

            private static String encode(AttributeModifier.Operation operation) {
                return switch (operation) {
                    case ADDITION -> "addition";
                    case MULTIPLY_BASE -> "base";
                    case MULTIPLY_TOTAL -> "percentage";
                };
            }
        }
    }

    // writes the implant JSON config file
    public static class Register {
        private static final Gson GSON = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        public static void write(Path path, ImplantData data) {
            ImplantData.CODEC
                    .encodeStart(JsonOps.INSTANCE, data)
                    .resultOrPartial(error ->
                            ZoeIsntCyberpunk.LOGGER.error(
                                    "Failed to write implant json {}: {}",
                                    path,
                                    error
                            )
                    )
                    .ifPresent(json -> writeJson(path, json));
        }

        private static void writeJson(Path path, JsonElement json) {
            try {
                Files.createDirectories(path.getParent());

                try (java.io.Writer writer = Files.newBufferedWriter(path)) {
                    GSON.toJson(json, writer);
                }

            } catch (IOException exception) {
                ZoeIsntCyberpunk.LOGGER.error(
                        "Failed to write implant json: {}",
                        path,
                        exception
                );
            }
        }
    }

    // loads implant data from the JSON
    public static class Loader {

        public static ImplantData read(Path path) {
            try (Reader reader = Files.newBufferedReader(path)) {
                JsonElement json = JsonParser.parseReader(reader);

                return ImplantData.CODEC
                        .parse(JsonOps.INSTANCE, json)
                        .resultOrPartial(error ->
                                ZoeIsntCyberpunk.LOGGER.error(
                                        "Failed to read implant data from {}: {}",
                                        path,
                                        error
                                )
                        )
                        .orElse(ImplantData.DEFAULT);

            } catch (IOException exception) {
                ZoeIsntCyberpunk.LOGGER.error(
                        "Failed to open implant data file: {}",
                        path,
                        exception
                );

                return ImplantData.DEFAULT;
            }
        }
    }
}
