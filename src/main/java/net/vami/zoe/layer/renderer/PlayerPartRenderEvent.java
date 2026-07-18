package net.vami.zoe.layer.renderer;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.layer.renderer.implant.PlayerModelPart;

import java.util.ArrayDeque;
import java.util.Deque;

@Mod.EventBusSubscriber(modid = ZoeIsntCyberpunk.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class PlayerPartRenderEvent {
    private static final Deque<ModelVisibilityState> STATES = new ArrayDeque<>();

    @SubscribeEvent
    public static void preRender(RenderPlayerEvent.Pre event) {
        AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();
        PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();

        STATES.push(ModelVisibilityState.capture(model));

        for (PlayerModelPart part :
                ClientImplantRenderState.getOverrides(player.getUUID())) {
            hidePart(model, part);
        }
    }

    @SubscribeEvent
    public static void postRender(RenderPlayerEvent.Post event) {
        if (!STATES.isEmpty()) {
            STATES.pop().restore();
        }
    }

    private static void hidePart(PlayerModel<AbstractClientPlayer> model, PlayerModelPart part) {
        switch (part) {
            case HEAD -> model.head.visible = model.hat.visible = false;
            case BODY -> model.body.visible = model.jacket.visible = false;
            case RIGHT_ARM -> model.rightArm.visible = model.rightSleeve.visible = false;
            case LEFT_ARM -> model.leftArm.visible = model.leftSleeve.visible = false;
            case RIGHT_LEG -> model.rightLeg.visible = model.rightPants.visible = false;
            case LEFT_LEG -> model.leftLeg.visible = model.leftPants.visible = false;
        }
    }

    private record ModelVisibilityState(
            PlayerModel<AbstractClientPlayer> model,
            boolean head, boolean hat,
            boolean body, boolean jacket,
            boolean rightArm, boolean rightSleeve,
            boolean leftArm, boolean leftSleeve,
            boolean rightLeg, boolean rightPants,
            boolean leftLeg, boolean leftPants) {
        private static ModelVisibilityState capture(PlayerModel<AbstractClientPlayer> model) {
            return new ModelVisibilityState(model,
                    model.head.visible, model.hat.visible,
                    model.body.visible, model.jacket.visible,
                    model.rightArm.visible, model.rightSleeve.visible,
                    model.leftArm.visible, model.leftSleeve.visible,
                    model.rightLeg.visible, model.rightPants.visible,
                    model.leftLeg.visible, model.leftPants.visible);
        }

        private void restore() {
            model.head.visible = head;
            model.hat.visible = hat;
            model.body.visible = body;
            model.jacket.visible = jacket;
            model.rightArm.visible = rightArm;
            model.rightSleeve.visible = rightSleeve;
            model.leftArm.visible = leftArm;
            model.leftSleeve.visible = leftSleeve;
            model.rightLeg.visible = rightLeg;
            model.rightPants.visible = rightPants;
            model.leftLeg.visible = leftLeg;
            model.leftPants.visible = leftPants;
        }
    }
}