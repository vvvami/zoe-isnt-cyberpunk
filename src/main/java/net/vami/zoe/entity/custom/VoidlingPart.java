package net.vami.zoe.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;

public class VoidlingPart extends PartEntity<VoidlingEntity> {
    private final VoidlingEntity parent;
    public final String name;
    private final EntityDimensions size;
    private final Vec3 offset;

    public VoidlingPart(VoidlingEntity parent, String name, float width, float height, double x, double y, double z) {
        super(parent);
        this.parent = parent;
        this.name = name;
        this.size = EntityDimensions.scalable(
                width * VoidlingEntity.SCALE,
                height * VoidlingEntity.SCALE);

        this.offset = new Vec3(x, y, z);
        this.refreshDimensions();
    }

    public VoidlingPart(VoidlingEntity parent, String name, float width, float height) {
        this(parent, name, width, height, 0.0D, 0.0D, 0.0D);
    }

    public Vec3 getOffset() {
        return this.offset;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.size;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isRemoved()) {
            return false;
        }

        return this.parent.hurtPart(this, source, amount);
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || this.getParent() == entity;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return this.getParent().getPickResult();
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException("Multipart entity parts are not spawned separately");
    }
}
