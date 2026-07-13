package net.vami.zoe.event.custom;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class ImplantOnUnequipEvent extends PlayerEvent {
    private ItemStack stack;

    public ImplantOnUnequipEvent(Player player, ItemStack stack) {
        super(player);
        this.stack = stack;
    }

    public ItemStack getImplant() {
        return stack;
    }

    public Item getItem() {
        return stack.getItem();
    }
}
