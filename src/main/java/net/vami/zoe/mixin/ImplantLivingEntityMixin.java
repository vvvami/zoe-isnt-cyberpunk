package net.vami.zoe.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.implant.ImplantUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class ImplantLivingEntityMixin {
    @Shadow
    protected ItemStack useItem;
    @Shadow protected int useItemRemaining;

    @Inject(method = "startUsingItem", at = @At("TAIL"))
    private void steelJaw$eatFast(InteractionHand hand, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        ItemStack stack = this.useItem;

        if (stack.isEmpty()) return;
        if (!stack.isEdible()) return;
        if (!(self instanceof Player player)) return;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.STEELED_JAW.get());
        if (implant.isEmpty()) return;

        float quality = ImplantUtil.getQuality(implant);

        float calc = Math.max(1f, quality / 5f);

        this.useItemRemaining = Math.max(1,
                Mth.ceil(stack.getUseDuration() - calc));
    }

    @Inject(method = "shouldDiscardFriction", at = @At("RETURN"), cancellable = true)
    private void legsaw$noFriction(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) return;

        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.onGround()) return;

        if (!(entity instanceof Player player)) return;

        if (ImplantUtil.count(player, ModItems.LEGSAW.get()) >= 2) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "handleOnClimbable", at = @At("RETURN"), cancellable = true)
    private void twinMotor$noVineSlow(Vec3 originalMotion, CallbackInfoReturnable<Vec3> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!(entity instanceof Player player)) return;

        BlockState state = entity.level()
                .getBlockState(entity.blockPosition());

        if (!isVine(state)) return;

        if (ImplantUtil.getImplant(player, ModItems.TWIN_MOTOR.get()).isEmpty()) return;


        Vec3 vanillaResult = cir.getReturnValue();

        cir.setReturnValue(new Vec3(
                originalMotion.x,
                vanillaResult.y,
                originalMotion.z));
    }

    @Unique private static boolean isVine(BlockState state) {
        return state.is(Blocks.VINE)
                || state.is(Blocks.CAVE_VINES)
                || state.is(Blocks.CAVE_VINES_PLANT)
                || state.is(Blocks.TWISTING_VINES)
                || state.is(Blocks.TWISTING_VINES_PLANT)
                || state.is(Blocks.WEEPING_VINES)
                || state.is(Blocks.WEEPING_VINES_PLANT);
    }
}
