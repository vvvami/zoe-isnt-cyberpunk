package net.vami.zoe.item.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.ModTags;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

import java.text.DecimalFormat;
import java.util.*;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID, value = Dist.CLIENT , bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ImplantTooltip {
    private static final Map<AttributeModifier.Operation, String> OP_MAP = Map.of(
            AttributeModifier.Operation.ADDITION,
            "",

            AttributeModifier.Operation.MULTIPLY_BASE,
            "%",

            AttributeModifier.Operation.MULTIPLY_TOTAL,
            "%");

    private static final Map<TagKey<Item>, Component> RANK_MAP = Map.of(
            ModTags.Items.PRIMAL_RANK,
            Component.translatable("tag.zoe.primal_rank")
                    .withStyle(ChatFormatting.WHITE),

            ModTags.Items.NOVEL_RANK,
            Component.translatable("tag.zoe.novel_rank")
                    .withStyle(ChatFormatting.AQUA),

            ModTags.Items.PYRRHIC_RANK,
            Component.translatable("tag.zoe.pyrrhic_rank")
                    .withStyle(ChatFormatting.RED),

            ModTags.Items.CHAOS_RANK,
            Component.translatable("tag.zoe.chaos_rank")
                    .withStyle(ChatFormatting.LIGHT_PURPLE));

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        List<Component> tooltip = event.getToolTip();
        if (tooltip == null) return;

        Player player = event.getEntity();
        if (player == null) return;

        ItemStack stack = event.getItemStack().copy();
        if (!(stack.getItem() instanceof ImplantItem)) return;

        ResourceLocation key = ForgeRegistries.ITEMS.getKey(stack.getItem());

        Component lore = Component.literal("§7" // gray
                + Component.translatable("implant.zoe.lore." + key.getPath()).getString()); // implant lore

        float quality = ImplantUtil.getQuality(stack);
        String qualityStr = Component.literal("§6" // gold
                + Component.translatable("tooltip.implant.zoe.quality").getString() + "§f: " // quality
                + new DecimalFormat("##.##").format(quality)).getString(); // formatted quality number

        String qualityRank;
        String rankName;
        int ranking = 1;
        if (quality < 20) {
            qualityRank = "§c";
        } else if (quality < 40) {
            ranking = 2;
            qualityRank = "§e";
        } else if (quality < 70) {
            ranking = 3;
            qualityRank = "§a";
        } else if (quality < 100) {
            ranking = 4;
            qualityRank = "§b";
        } else {
            ranking = 5;
            qualityRank = "§d";
        }
        rankName = Component.translatable("tooltip.implant.zoe.quality_" + ranking).getString();
        qualityRank += "(" + rankName + ")";
        Component qualityComp = Component.literal(qualityStr)
                .append(" ")
                .append(qualityRank);

        ImplantData data = ((ImplantItem) stack.getItem()).data();

        Component humanity = Component.literal("§6"
                + Component.translatable("tooltip.implant.zoe.humanity").getString() + "§f: " // humanity
                + new DecimalFormat("##.##").format(data.humanityScaling())); // formatted humanity number

        Component attributes = Component.literal("§7" +Component.translatable("tooltip.implant.zoe.on_equip").getString() + ":");

        List<Component> attributeList = new ArrayList<>();

        // loops through every attribute and gets the name and the value together with the operation
        for (ImplantData.Attribute dataAttr : data.attributes()) {
            String op = OP_MAP.get(dataAttr.operation());

            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(dataAttr.attribute());
            String attributeName = Component.translatable(attribute.getDescriptionId()).getString();

            // getting the amplifier multiplied by our quality
            double amplifier = dataAttr.amplifier() * (quality / 100);

            // for 100% instead of 1% (because the percentages are according to floats 0-1 for 0-100%
            if (dataAttr.operation() != AttributeModifier.Operation.ADDITION) amplifier *= 100;
            String fullAmount = new DecimalFormat("##.##").format(amplifier);
            fullAmount = amplifier > 0 ? "+" + fullAmount : fullAmount;

            // difference between +5 and 5%, we don't want 5+ or %5 by accident
            String fullModifier = op.equals("+") ? op + fullAmount : fullAmount + op;

            // combine everything together for the full attribute
            String fullDisplay = "  §b» " + fullModifier + " " + attributeName;
            attributeList.add(Component.literal(fullDisplay));
        }

        Component implantRank = RANK_MAP.entrySet().stream()
                .filter(entry -> stack.is(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(RANK_MAP.get(ModTags.Items.PRIMAL_RANK));

        Component rank = Component.translatable("tooltip.implant.zoe.rank").withStyle(ChatFormatting.GOLD)
                .append("§f: ")
                .append(implantRank.copy());


        List<Component> tooltipCopy = new ArrayList<>(tooltip);
        tooltip.clear(); // clears the old tooltip details

        tooltip.add(tooltipCopy.get(0)); // item name
        tooltipCopy.remove(0); // removes it from the copy

        tooltip.add(rank); // rank (primal, novel, etc)
        tooltip.add(lore); // lore (info abt the implant)

        // if attributes are present, add 'em
        if (!attributeList.isEmpty()) {
            tooltip.add(Component.literal(" "));
            tooltip.add(attributes); // "On Equip:"
            tooltip.addAll(attributeList); // all the attributes n stuff
        }

        tooltip.add(Component.literal(" ")); // new line for separation

        tooltip.add(qualityComp); // implant quality
        tooltip.add(humanity); // implant humanity req

        tooltip.addAll(tooltipCopy); // all the rest of the original tooltip bs

    }
}
