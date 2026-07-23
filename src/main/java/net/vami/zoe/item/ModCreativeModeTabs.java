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
import net.vami.zoe.block.ModBlocks;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ZoeIsntCyberpunk.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ZOE_MATERIALS =
            CREATIVE_MODE_TABS.register("zoe_materials",
                    () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.CARBONSTEEL.get()))
                    .title(Component.translatable("creativetab.zoe_materials"))
                    .displayItems((pParameters, pOutput) -> {

                    pOutput.accept(ModItems.CARBONSTEEL.get());
                    pOutput.accept(ModBlocks.CARBONSTEEL_BLOCK.get());
                    pOutput.accept(ModItems.REINFORCED_TIBIA.get().getDefaultInstance());
                    pOutput.accept(ModItems.POWERFIST.get().getDefaultInstance());
                    pOutput.accept(ModItems.SUBDERMAL_WEAVE.get().getDefaultInstance());
                    pOutput.accept(ModItems.DWARVENHANDE.get().getDefaultInstance());
                    pOutput.accept(ModItems.CORTEX_SUPPRESSOR.get().getDefaultInstance());
                    pOutput.accept(ModItems.SPIKED_KNUCKLES.get().getDefaultInstance());
                    pOutput.accept(ModItems.EARA.get().getDefaultInstance());
                    pOutput.accept(ModItems.THRUSTERS.get().getDefaultInstance());
                    pOutput.accept(ModItems.ROTOR_JOINTS.get().getDefaultInstance());
                    pOutput.accept(ModItems.OPTIC_CHITIN.get().getDefaultInstance());
                    pOutput.accept(ModItems.STEELED_JAW.get().getDefaultInstance());
                    pOutput.accept(ModItems.ARBITRARY_STOMACH.get().getDefaultInstance());
                    pOutput.accept(ModItems.LEGSAW.get().getDefaultInstance());
                    pOutput.accept(ModItems.BURNING_PALM.get().getDefaultInstance());
                    pOutput.accept(ModItems.WRISTPLATES.get().getDefaultInstance());
                    pOutput.accept(ModItems.TWIN_MOTOR.get().getDefaultInstance());
                    pOutput.accept(ModItems.PACEMAKER.get().getDefaultInstance());
                    pOutput.accept(ModItems.HUNTER_LENS.get().getDefaultInstance());
                    pOutput.accept(ModItems.PENDULOCK.get().getDefaultInstance());
                    pOutput.accept(ModItems.CELL_ACCELERANT.get().getDefaultInstance());
                    pOutput.accept(ModItems.DOUBLE_FLUX.get().getDefaultInstance());
                    pOutput.accept(ModItems.MARGET_EYE.get().getDefaultInstance());
                    pOutput.accept(ModItems.HEAVY_HAND.get().getDefaultInstance());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
