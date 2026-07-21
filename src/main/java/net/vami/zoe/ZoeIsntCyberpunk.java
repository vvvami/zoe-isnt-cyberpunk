package net.vami.zoe;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vami.zoe.block.ModBlocks;
import net.vami.zoe.client.gui.ModMenuTypes;
import net.vami.zoe.client.gui.custom.ImplantScreen;
import net.vami.zoe.effect.ModEffects;
import net.vami.zoe.entity.ModEntities;
import net.vami.zoe.init.ModAttributes;
import net.vami.zoe.item.ModCreativeModeTabs;
import net.vami.zoe.item.ModItems;
import net.vami.zoe.network.ModPackets;
import org.slf4j.Logger;

@Mod(ZoeIsntCyberpunk.MOD_ID)
public class ZoeIsntCyberpunk {
    public static final String MOD_ID = "zoe";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ZoeIsntCyberpunk() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModAttributes.register(modEventBus);
        ModEntities.register(modEventBus);
        ModEffects.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModPackets::register);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.CARBONSTEEL);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.IMPLANT_MENU.get(), ImplantScreen::new);
        }
    }
}
