package net.vami.zoe.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vami.zoe.ZoeIsntCyberpunk;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ZoeIsntCyberpunk.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ZOE_MATERIALS =
            CREATIVE_MODE_TABS.register("zoe_materials",
                    () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.CARBONSTEEL.get()))
                    .title(Component.translatable("creativetab.zoe_materials"))
                    .displayItems((pParameters, pOutput) -> {

                    pOutput.accept(ModItems.CARBONSTEEL.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
