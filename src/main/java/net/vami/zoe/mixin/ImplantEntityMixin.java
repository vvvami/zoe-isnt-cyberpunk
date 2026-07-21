package net.vami.zoe.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.extensions.IForgeEntity;
import net.minecraftforge.fluids.FluidType;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.implant.ImplantUtil;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class ImplantEntityMixin implements IForgeEntity {


    // for twin motor no fluid pushing
    @Override
    public boolean isPushedByFluid(FluidType fluidType) {
        Entity entity = (Entity) (Object) this;

        if (entity instanceof Player player
                && !ImplantUtil.getImplant(player, ModItems.TWIN_MOTOR.get()).isEmpty()) {
            return false;
        }

        return fluidType.canPushEntity(entity);
    }
}