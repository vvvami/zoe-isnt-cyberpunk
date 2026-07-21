package net.vami.zoe.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.implant.ImplantUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WebBlock.class)
public abstract class ImplantWebBlockMixin {

    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void twinMotor$noWebSlow(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (!(entity instanceof Player player)) return;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.TWIN_MOTOR.get());
        if (implant.isEmpty()) return;
        float quality = ImplantUtil.getQuality(implant);
        boolean removeSlow = quality / 100 > Math.random();

        if (removeSlow) {
            ci.cancel();
        }
    }
}
