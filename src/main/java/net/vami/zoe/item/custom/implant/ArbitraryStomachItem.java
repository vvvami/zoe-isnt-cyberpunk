package net.vami.zoe.item.custom.implant;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArbitraryStomachItem extends ImplantItem {
    public ArbitraryStomachItem(Properties pProperties) {
        super(pProperties);

    }

    // makes food give more food value and nutrition
    // makes exhaustion up to 50% less -> ImplantPlayerMixin


    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        ItemStack food = event.getItem();

        FoodProperties properties = food.getFoodProperties(player);
        if (properties == null) return;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.ARBITRARY_STOMACH.get());
        if (implant.isEmpty()) return;

        float quality = Mth.clamp(ImplantUtil.getQuality(implant), 1f, 100f);

        float bonus = quality / 100f;

        // grants bonus food based on the original food's properties
        int bonusFood = Math.max(1, Mth.ceil(properties.getNutrition() * bonus));

        // applies the food bonus directly
        player.getFoodData().eat(bonusFood, properties.getSaturationModifier() * bonus);
    }

    @Override
    public ImplantData data() {
        return ImplantData.build(10f);
    }
}
