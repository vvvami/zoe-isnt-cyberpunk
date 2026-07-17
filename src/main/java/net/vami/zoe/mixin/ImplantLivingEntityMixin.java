package net.vami.zoe.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.implant.ImplantUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class ImplantLivingEntityMixin {
    @Shadow
    protected ItemStack useItem;
    @Shadow protected int useItemRemaining;

    @Inject(method = "startUsingItem", at = @At("TAIL"))
    private void prevail$chomp(InteractionHand hand, CallbackInfo ci) {
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
}
