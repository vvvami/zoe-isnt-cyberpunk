package net.vami.zoe.gui.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vami.zoe.gui.ModMenuTypes;
import org.jetbrains.annotations.Nullable;

public class ImplantMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final Player player;
    private static MenuType<ImplantMenu> menuType;
    private final CraftingContainer implantSlots = new TransientCraftingContainer(this, 1, 20);

    public ImplantMenu(int containerID, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerID, playerInventory, ContainerLevelAccess.NULL);
    }

    public ImplantMenu(int containerID, Inventory pInventory, ContainerLevelAccess access) {
        super(ModMenuTypes.IMPLANT_MENU.get(), containerID);
        this.access = access;
        this.player = pInventory.player;

        addPlayerInventory(pInventory);
        addPlayerHotbar(pInventory);

        Slot implant0 = new Slot(implantSlots, 0, 8, 18);
        Slot implant1 = new Slot(implantSlots, 0, 8, 18);
        Slot implant2 = new Slot(implantSlots, 0, 8, 18);
        Slot implant3 = new Slot(implantSlots, 0, 8, 18);
        Slot implant4 = new Slot(implantSlots, 0, 8, 18);
        Slot implant5 = new Slot(implantSlots, 0, 8, 18);
        Slot implant6 = new Slot(implantSlots, 0, 8, 18);
        Slot implant7 = new Slot(implantSlots, 0, 8, 18);
        Slot implant8 = new Slot(implantSlots, 0, 8, 18);
        Slot implant9 = new Slot(implantSlots, 0, 8, 18);
        Slot implant10 = new Slot(implantSlots, 0, 8, 18);
        Slot implant11 = new Slot(implantSlots, 0, 8, 18);
        Slot implant12 = new Slot(implantSlots, 0, 8, 18);
        Slot implant13 = new Slot(implantSlots, 0, 8, 18);
        Slot implant14 = new Slot(implantSlots, 0, 8, 18);
        Slot implant15 = new Slot(implantSlots, 0, 8, 18);
        Slot implant16 = new Slot(implantSlots, 0, 8, 18);
        Slot implant17 = new Slot(implantSlots, 0, 8, 18);
        Slot implant18 = new Slot(implantSlots, 0, 8, 18);
        Slot implant19 = new Slot(implantSlots, 0, 8, 18);

        this.addSlot(implant0);
        this.addSlot(implant1);
        this.addSlot(implant2);
        this.addSlot(implant3);
        this.addSlot(implant4);
        this.addSlot(implant5);
        this.addSlot(implant6);
        this.addSlot(implant7);
        this.addSlot(implant8);
        this.addSlot(implant9);
        this.addSlot(implant10);
        this.addSlot(implant11);
        this.addSlot(implant12);
        this.addSlot(implant13);
        this.addSlot(implant14);
        this.addSlot(implant15);
        this.addSlot(implant16);
        this.addSlot(implant17);
        this.addSlot(implant18);
        this.addSlot(implant19);
    }


    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 20;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
