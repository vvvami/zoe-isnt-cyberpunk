package net.vami.zoe.item.custom.implant;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ImplantOnEquipEvent;
import net.vami.zoe.event.custom.ImplantOnUnequipEvent;
import net.vami.zoe.item.custom.ImplantItem;
import net.vami.zoe.layer.renderer.implant.PlayerModelPart;
import net.vami.zoe.layer.renderer.implant.PlayerPartOverrideProvider;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;

import java.util.EnumSet;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LegsawItem extends ImplantItem implements PlayerPartOverrideProvider {
    public LegsawItem(Properties pProperties) {
        super(pProperties);

    }

    /** has functionality in {@link net.vami.zoe.mixin.ImplantLivingEntityMixin}*/

    @Override
    public ImplantData data() {
        return ImplantData.build(10f,
                ImplantData.add(Attributes.ATTACK_DAMAGE,
                        5,
                        AttributeModifier.Operation.ADDITION),

                ImplantData.add(Attributes.MOVEMENT_SPEED,
                        -0.25,
                        AttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    @Override
    public void addParts(int implantCount, EnumSet<PlayerModelPart> parts) {
        if (implantCount >= 1) {
            parts.add(PlayerModelPart.RIGHT_LEG);
        }

        if (implantCount >= 2) {
            parts.add(PlayerModelPart.LEFT_LEG);
        }
    }
}
