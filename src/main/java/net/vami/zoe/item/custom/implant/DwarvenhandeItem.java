package net.vami.zoe.item.custom.implant;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

import java.util.Map;

public class DwarvenhandeItem extends ImplantItem {
    public DwarvenhandeItem(Properties pProperties) {
        super(pProperties);
    }

    public static final Map<TagKey<Block>, Integer> QUALITY_REQUIREMENTS =
            Map.of(BlockTags.NEEDS_STONE_TOOL, 21,
                    BlockTags.NEEDS_IRON_TOOL, 41,
                    BlockTags.NEEDS_DIAMOND_TOOL, 61);

    public static int getRequiredQuality(BlockState state) {
        return QUALITY_REQUIREMENTS.entrySet()
                .stream()
                .filter(entry -> state.is(entry.getKey()))
                .mapToInt(Map.Entry::getValue)
                .max()
                .orElse(1);
    }

    public static boolean canMineWithFist(Player player, BlockState state, float quality) {
        quality = Mth.clamp(quality, 1, 100);

        return player.getMainHandItem().isEmpty()
                && state.is(BlockTags.MINEABLE_WITH_PICKAXE)
                && quality >= getRequiredQuality(state);
    }

    @Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static final class DwarvenhandeEvents {

        @SubscribeEvent
        public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
            Player player = event.getEntity();
            BlockState state = event.getTargetBlock();

            ItemStack item =  ImplantUtil.getImplant(player, ModItems.DWARVENHANDE.get());
            if (item.isEmpty()) return;
            float quality = ImplantUtil.getQuality(item);

            if (canMineWithFist(player, state, quality)) {
                event.setCanHarvest(true);
            }
        }

        @SubscribeEvent
        public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();
            BlockState state = event.getState();

            ItemStack item =  ImplantUtil.getImplant(player, ModItems.DWARVENHANDE.get());
            if (item.isEmpty()) return;
            float quality = ImplantUtil.getQuality(item);

            if (canMineWithFist(player, state, quality)) {
                event.setNewSpeed(event.getNewSpeed() * 2.0F);
            }
        }
    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
