package net.vami.zoe.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

public class PlayerCapability {

    public CapabilityContainer<ArrayList<ItemStack>> implants
            = new CapabilityContainer<>(new ArrayList<>(Collections.nCopies(20, ItemStack.EMPTY)));
    // If you want to add a new capability, Add it to copyfrom, saveNBTdata and LoadNBTdata
    // These only fire when the capabilities are registered in mod events

    private static final int IMPLANT_SLOTS = 20;

    public void copyFrom(PlayerCapability source) {
        ArrayList<ItemStack> copied = new ArrayList<>();

        for (int i = 0; i < IMPLANT_SLOTS; i++) {
            ItemStack stack = source.implants.get().get(i);
            copied.add(stack == null ? ItemStack.EMPTY : stack.copy());
        }

        this.implants.set(copied);
    }

    public void SaveNBTData(CompoundTag nbt) {
        for (int i = 0; i < IMPLANT_SLOTS; i++) {
            ItemStack stack = this.implants.get().get(i);

            if (stack == null) {
                stack = ItemStack.EMPTY;
            }

            nbt.put("implant" + i, stack.save(new CompoundTag()));
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        ArrayList<ItemStack> implantList = new ArrayList<>();

        for (int i = 0; i < IMPLANT_SLOTS; i++) {
            implantList.add(ItemStack.of(nbt.getCompound("implant" + i)));
        }

        this.implants.set(implantList);
    }

}
