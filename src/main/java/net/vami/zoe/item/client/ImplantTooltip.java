package net.vami.zoe.item.client;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID, value = Dist.CLIENT , bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ImplantTooltip {
    private static final Map<AttributeModifier.Operation, String> OP_MAP = Map.of(
            AttributeModifier.Operation.ADDITION,
            "",

            AttributeModifier.Operation.MULTIPLY_BASE,
            "%",

            AttributeModifier.Operation.MULTIPLY_TOTAL,
            "%");

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        List<Component> tooltip = event.getToolTip();
        if (tooltip == null) return;

        ItemStack stack = event.getItemStack().copy();
        if (!(stack.getItem() instanceof ImplantItem)) return;

        ResourceLocation key = ForgeRegistries.ITEMS.getKey(stack.getItem());

        Component lore = Component.literal("§7" // gray
                + Component.translatable("implant.zoe.lore." + key.getPath()).getString()); // implant lore

        float quality = ImplantUtil.getQuality(stack);
        String qualityStr = Component.literal("§6"
                + Component.translatable("implant.zoe.quality").getString() + ": " // quality
                + "§f" + new DecimalFormat("##.##").format(quality)).getString(); // formatted quality number

        String qualityRank = "";
        if (quality <= 10) {
            qualityRank = "§c(Junk)";
        } else if (quality < 40) {
            qualityRank = "§e(Poor)";
        } else if (quality < 70) {
            qualityRank = "§a(Decent)";
        } else if (quality < 100) {
            qualityRank = "§b(High)";
        } else {
            qualityRank = "§d(Perfect)";
        }

        qualityStr =  qualityStr + " " + qualityRank;
        Component qualityComp = Component.literal(qualityStr);

        ImplantData data = ((ImplantItem) stack.getItem()).data();

        Component humanity = Component.literal("§6"
                + Component.translatable("implant.zoe.humanity").getString() + ": " // humanity
                + "§f" + new DecimalFormat("##.##").format(data.humanityScaling())); // formatted humanity number

        Component attributes = Component.literal("§7" +Component.translatable("implant.zoe.on_equip").getString() + ":");

        List<Component> attributeList = new ArrayList<>();

        // loops through every attribute and gets the name and the value together with the operation
        for (ImplantData.Attribute dataAttr : data.attributes()) {
            String op = OP_MAP.get(dataAttr.modifier());

            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(dataAttr.attribute());
            String attributeName = Component.translatable(attribute.getDescriptionId()).getString();
            // getting the amplifier multiplied by our quality
            double amplifier = dataAttr.amplifier() * (quality / 100);
            // for 100% instead of 1% (because the percentages are according to floats 0-1 for 0-100%
            if (dataAttr.modifier() != AttributeModifier.Operation.ADDITION) amplifier *= 100;
            String fullAmount = new DecimalFormat("##.##").format(amplifier);
            fullAmount = amplifier > 0 ? "+" + fullAmount : fullAmount;
            // difference between +5 and 5%, we don't want 5+ or %5 by accident
            String fullModifier = op.equals("+") ? op + fullAmount : fullAmount + op;
            // combine everything together for the full attribute
            String fullDisplay = "  §b» " + fullModifier + " " + attributeName;
            attributeList.add(Component.literal(fullDisplay));
        }

        List<Component> tooltipCopy = new ArrayList<>(tooltip);
        tooltip.clear(); // clears the old tooltip details

        tooltip.add(tooltipCopy.get(0)); // item name
        tooltipCopy.remove(0); // removes it from the copy

        tooltip.add(lore); // lore

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
