package net.vami.zoe.event;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.layer.renderer.implant.ClientImplantRenderState;

import java.util.ArrayDeque;
import java.util.Deque;

@Mod.EventBusSubscriber(
        modid = ZoeIsntCyberpunk.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public final class PlayerBodyPartRenderEvent {

    /*
     * Stores the original visibility values for each active player render.
     * The Deque allows nested renders to be restored in the correct order.
     */
    private static final Deque<HiddenPartState> STATES =
            new ArrayDeque<>();

    private PlayerBodyPartRenderEvent() {
    }

    @SubscribeEvent
    public static void beforePlayerRender(RenderPlayerEvent.Pre event) {
        AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();

        PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();

        ResourceLocation legsawId = ForgeRegistries.ITEMS.getKey(ModItems.LEGSAW.get());

        int legsawCount = ClientImplantRenderState.getLayerCount(player.getUUID(), legsawId);

        boolean hideRightLeg = legsawCount >= 1;
        boolean hideLeftLeg = legsawCount >= 2;

        STATES.push(new HiddenPartState(
                model,
                hideRightLeg,
                hideLeftLeg,
                model.rightLeg.visible,
                model.rightPants.visible,
                model.leftLeg.visible,
                model.leftPants.visible
        ));

        if (hideRightLeg) {
            model.rightLeg.visible = false;
            model.rightPants.visible = false;
        }

        if (hideLeftLeg) {
            model.leftLeg.visible = false;
            model.leftPants.visible = false;
        }
    }

    @SubscribeEvent
    public static void afterPlayerRender(RenderPlayerEvent.Post event) {
        if (STATES.isEmpty()) {
            return;
        }

        HiddenPartState state = STATES.pop();

        if (state.hiddenRLeg()) {
            state.model().rightLeg.visible =
                    state.oRLegVisible();

            state.model().rightPants.visible =
                    state.oRPantsVisible();
        }

        if (state.hiddenLLeg()) {
            state.model().leftLeg.visible =
                    state.oLLegVisible();

            state.model().leftPants.visible =
                    state.oLPantsVisible();
        }
    }

    private record HiddenPartState(
            PlayerModel<AbstractClientPlayer> model,

            boolean hiddenRLeg,
            boolean hiddenLLeg,

            boolean oRLegVisible,
            boolean oRPantsVisible,

            boolean oLLegVisible,
            boolean oLPantsVisible) {
    }
}