package net.vami.zoe.entity.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.vami.zoe.entity.custom.VoidlingEntity;
import net.vami.zoe.entity.ModEntities;
import net.vami.zoe.network.packet.VoidlingPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ClientVoidlingRender {
    private static final Map<UUID, Ghost> GHOSTS = new HashMap<>();

    private ClientVoidlingRender() {
    }

    public static void accept(VoidlingPacket packet) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null) {
            return;
        }

        if (!packet.active()) {
            GHOSTS.remove(packet.uuid());
            return;
        }

        Ghost ghost = GHOSTS.computeIfAbsent(
                packet.uuid(),
                uuid -> createGhost(uuid)
        );

        if (ghost == null) {
            return;
        }

        ghost.previousPosition = ghost.currentPosition;
        ghost.currentPosition = new Vec3(
                packet.x(),
                packet.y(),
                packet.z()
        );

        ghost.previousYaw = ghost.currentYaw;
        ghost.currentYaw = packet.yaw();

        ghost.previousPitch = ghost.currentPitch;
        ghost.currentPitch = packet.pitch();

        ghost.previousBodyYaw = ghost.currentBodyYaw;
        ghost.currentBodyYaw = packet.bodyYaw();

        ghost.serverTickCount = packet.tickCount();

        if (!ghost.initialized) {
            ghost.previousPosition = ghost.currentPosition;
            ghost.previousYaw = ghost.currentYaw;
            ghost.previousPitch = ghost.currentPitch;
            ghost.previousBodyYaw = ghost.currentBodyYaw;
            ghost.initialized = true;
        }
    }

    private static Ghost createGhost(UUID uuid) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null) {
            return null;
        }

        VoidlingEntity entity = ModEntities.VOIDLING.get().create(
                minecraft.level
        );

        if (entity == null) {
            return null;
        }

        entity.setUUID(uuid);
        entity.noCulling = true;

        return new Ghost(entity);
    }

    public static Iterable<Ghost> ghosts() {
        return GHOSTS.values();
    }

    public static void clear() {
        GHOSTS.clear();
    }

    public static boolean isRealEntityTracked(UUID uuid) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null) {
            return false;
        }

        for (var entity : minecraft.level.entitiesForRendering()) {
            if (entity instanceof VoidlingEntity
                    && entity.getUUID().equals(uuid)
                    && !entity.isRemoved()) {
                return true;
            }
        }

        return false;
    }

    public static final class Ghost {
        private final VoidlingEntity entity;

        private Vec3 previousPosition = Vec3.ZERO;
        private Vec3 currentPosition = Vec3.ZERO;

        private float previousYaw;
        private float currentYaw;

        private float previousPitch;
        private float currentPitch;

        private float previousBodyYaw;
        private float currentBodyYaw;

        private int serverTickCount;
        private boolean initialized;

        private Ghost(VoidlingEntity entity) {
            this.entity = entity;
        }

        public VoidlingEntity entity() {
            return entity;
        }

        public UUID uuid() {
            return entity.getUUID();
        }

        public Vec3 interpolatedPosition(float partialTick) {
            return new Vec3(
                    Mth.lerp(
                            partialTick,
                            previousPosition.x,
                            currentPosition.x
                    ),
                    Mth.lerp(
                            partialTick,
                            previousPosition.y,
                            currentPosition.y
                    ),
                    Mth.lerp(
                            partialTick,
                            previousPosition.z,
                            currentPosition.z
                    )
            );
        }

        public float interpolatedYaw(float partialTick) {
            return Mth.rotLerp(
                    partialTick,
                    previousYaw,
                    currentYaw
            );
        }

        public float interpolatedPitch(float partialTick) {
            return Mth.lerp(
                    partialTick,
                    previousPitch,
                    currentPitch
            );
        }

        public float interpolatedBodyYaw(float partialTick) {
            return Mth.rotLerp(
                    partialTick,
                    previousBodyYaw,
                    currentBodyYaw
            );
        }

        public int serverTickCount() {
            return serverTickCount;
        }
    }
}