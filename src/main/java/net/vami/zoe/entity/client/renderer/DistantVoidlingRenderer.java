package net.vami.zoe.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.entity.custom.VoidlingEntity;

@Mod.EventBusSubscriber(
        modid = ZoeIsntCyberpunk.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public final class DistantVoidlingRenderer {
    private DistantVoidlingRenderer() {
    }

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {
        if (event.getStage()
                != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null
                || minecraft.player == null) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        Camera camera = event.getCamera();
        Vec3 cameraPosition = camera.getPosition();

        EntityRenderDispatcher dispatcher =
                minecraft.getEntityRenderDispatcher();

        MultiBufferSource.BufferSource buffers =
                minecraft.renderBuffers().bufferSource();

        float partialTick = event.getPartialTick();

        for (ClientVoidlingRender.Ghost ghost
                : ClientVoidlingRender.ghosts()) {

            /*
             * When the real entity is being tracked, its normal renderer
             * handles it. Do not also render the ghost.
             */
            if (ClientVoidlingRender.isRealEntityTracked(ghost.uuid())) {
                continue;
            }

            VoidlingEntity entity = ghost.entity();

            Vec3 worldPosition =
                    ghost.interpolatedPosition(partialTick);

            float yaw = ghost.interpolatedYaw(partialTick);
            float pitch = ghost.interpolatedPitch(partialTick);
            float bodyYaw =
                    ghost.interpolatedBodyYaw(partialTick);

            updateGhostEntity(
                    entity,
                    worldPosition,
                    yaw,
                    pitch,
                    bodyYaw,
                    ghost.serverTickCount()
            );

            double relativeX =
                    worldPosition.x - cameraPosition.x;
            double relativeY =
                    worldPosition.y - cameraPosition.y;
            double relativeZ =
                    worldPosition.z - cameraPosition.z;

            poseStack.pushPose();

            /*
             * Directly invokes your registered VoidlingRenderer.
             *
             * The x/y/z parameters are relative to the camera, just as
             * vanilla passes entity coordinates during normal rendering.
             */
            dispatcher.render(
                    entity,
                    relativeX,
                    relativeY,
                    relativeZ,
                    yaw,
                    partialTick,
                    poseStack,
                    buffers,
                    LightTexture.FULL_BRIGHT
            );

            poseStack.popPose();
        }

        /*
         * Submit everything added to the shared entity buffer.
         */
        buffers.endBatch();
    }

    private static void updateGhostEntity(
            VoidlingEntity entity,
            Vec3 position,
            float yaw,
            float pitch,
            float bodyYaw,
            int serverTickCount
    ) {
        /*
         * Save old state so animations and renderer interpolation work.
         */
        entity.xo = entity.getX();
        entity.yo = entity.getY();
        entity.zo = entity.getZ();

        entity.xOld = entity.getX();
        entity.yOld = entity.getY();
        entity.zOld = entity.getZ();

        entity.yRotO = entity.getYRot();
        entity.xRotO = entity.getXRot();

        entity.yBodyRotO = entity.yBodyRot;
        entity.yHeadRotO = entity.yHeadRot;

        /*
         * Apply the synchronized state.
         */
        entity.setPos(
                position.x,
                position.y,
                position.z
        );

        entity.setYRot(yaw);
        entity.setXRot(pitch);

        entity.yBodyRot = bodyYaw;
        entity.yHeadRot = yaw;

        entity.tickCount = serverTickCount;
    }

    @SubscribeEvent
    public static void unloadLevel(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            ClientVoidlingRender.clear();
        }
    }
}