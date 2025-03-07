package net.vami.zoe.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.item.custom.implants.ReinforcedTibiaItem;

public class ModItems {
    
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ZoeIsntCyberpunk.MOD_ID);

    public static final RegistryObject<Item> CARBONSTEEL = ITEMS.register("carbonsteel",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<ReinforcedTibiaItem> REINFORCED_TIBIA = ITEMS.register("reinforced_tibia",
            () -> new ReinforcedTibiaItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
