package net.vami.zoe.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.util.implant.ImplantUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public class ImplantPlayerMixin {
    @ModifyVariable(method = "causeFoodExhaustion", at = @At("HEAD"), argsOnly = true)
    private float arbitraryStomach$reduceExhaust(float exhaustion) {
        Player player = (Player)(Object)this;

        ItemStack implant = ImplantUtil.getImplant(player, ModItems.ARBITRARY_STOMACH.get());
        if (implant.isEmpty()) return exhaustion;

        float quality = ImplantUtil.getQuality(implant);

        float multiplier = 1f - quality / 150f;

        return exhaustion * multiplier;
    }
}
