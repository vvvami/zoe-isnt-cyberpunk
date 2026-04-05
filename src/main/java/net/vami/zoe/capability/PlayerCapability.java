package net.vami.zoe.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class PlayerCapability {
    public CapabilityContainer<ArrayList<ItemStack>> implants
            = new CapabilityContainer<>(new ArrayList<>(17));
    // If you want to add a new capability, Add it to copyfrom, saveNBTdata and LoadNBTdata
    // These only fire when the capabilities are registered in mod events

    public void copyFrom(PlayerCapability source){
        this.implants = source.implants;
    }

    public void SaveNBTData(CompoundTag nbt) {
        int i = 0;
        for (ItemStack implant : this.implants.get()) {
            if (implant == null) {
                implant = ItemStack.EMPTY;
            }
            nbt.put("implant" + i, implant.save(new CompoundTag()));
            i++;
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        int i = 0;
        ArrayList<ItemStack> implantList = new ArrayList<>();
        for (ItemStack implant : this.implants.get()) {
            implantList.add(ItemStack.of(nbt.getCompound("implant" + i)));
            i++;
        }
        implants.set(implantList);
    }
}
