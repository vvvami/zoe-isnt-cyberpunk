package net.vami.zoe.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.vami.zoe.ZoeIsntCyberpunk;

public class ModTags {
    public static class Items {

        public static final TagKey<Item> IS_IMPLANT = tag("is_implant");
        public static final TagKey<Item> CEREBRUM_IMPLANT = tag("cerebrum_implant");
        public static final TagKey<Item> OCULAR_IMPLANT = tag("ocular_implant");
        public static final TagKey<Item> SKELETON_IMPLANT = tag("skeleton_implant");
        public static final TagKey<Item> TORSO_IMPLANT = tag("torso_implant");
        public static final TagKey<Item> SKIN_IMPLANT = tag("skin_implant");
        public static final TagKey<Item> BLOOD_IMPLANT = tag("blood_implant");
        public static final TagKey<Item> ARMS_IMPLANT = tag("arms_implant");
        public static final TagKey<Item> LEGS_IMPLANT = tag("legs_implant");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(ZoeIsntCyberpunk.MOD_ID, name));
        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }
    }

    public static class Blocks {

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(ZoeIsntCyberpunk.MOD_ID, name));
        }

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }
    }

    public static class DamageTypes {

        public static final TagKey<DamageType> IS_MELEE = tag("is_melee");


        private static TagKey<DamageType> tag(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ZoeIsntCyberpunk.MOD_ID, name));
        }

        private static TagKey<DamageType> forgeTag(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("forge", name));
        }
    }
}
