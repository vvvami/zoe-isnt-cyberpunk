package net.vami.zoe.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.item.custom.implant.*;

public class ModItems {
    
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ZoeIsntCyberpunk.MOD_ID);

    public static final RegistryObject<Item> CARBONSTEEL = ITEMS.register("carbonsteel",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<ReinforcedTibiaItem> REINFORCED_TIBIA = ITEMS.register("reinforced_tibia",
            () -> new ReinforcedTibiaItem(new Item.Properties()));

    public static final RegistryObject<SpikedKnucklesItem> SPIKED_KNUCKLES = ITEMS.register("spiked_knuckles",
            () -> new SpikedKnucklesItem(new Item.Properties()));

    public static final RegistryObject<PowerfistItem> POWERFIST = ITEMS.register("powerfist",
            () -> new PowerfistItem(new Item.Properties()));

    public static final RegistryObject<SubdermalWeaveItem> SUBDERMAL_WEAVE = ITEMS.register("subdermal_weave",
            () -> new SubdermalWeaveItem(new Item.Properties()));

    public static final RegistryObject<ThrustersItem> THRUSTERS = ITEMS.register("thrusters",
            () -> new ThrustersItem(new Item.Properties()));

    public static final RegistryObject<CortexSuppressorItem> CORTEX_SUPPRESSOR = ITEMS.register("cortex_suppressor",
            () -> new CortexSuppressorItem(new Item.Properties()));

    public static final RegistryObject<EaraItem> EARA = ITEMS.register("eara",
            () -> new EaraItem(new Item.Properties()));

    public static final RegistryObject<DwarvenhandeItem> DWARVENHANDE = ITEMS.register("dwarvenhande",
            () -> new DwarvenhandeItem(new Item.Properties()));

    public static final RegistryObject<RotorJointsItem> ROTOR_JOINTS = ITEMS.register("rotor_joints",
            () -> new RotorJointsItem(new Item.Properties()));

    public static final RegistryObject<OpticChitinItem> OPTIC_CHITIN = ITEMS.register("optic_chitin",
            () -> new OpticChitinItem(new Item.Properties()));

    public static final RegistryObject<SteeledJawItem> STEELED_JAW = ITEMS.register("steeled_jaw",
            () -> new SteeledJawItem(new Item.Properties()));

    public static final RegistryObject<ArbitraryStomachItem> ARBITRARY_STOMACH = ITEMS.register("arbitrary_stomach",
            () -> new ArbitraryStomachItem(new Item.Properties()));

    public static final RegistryObject<LegsawItem> LEGSAW = ITEMS.register("legsaw",
            () -> new LegsawItem(new Item.Properties()));

    public static final RegistryObject<BurningPalmItem> BURNING_PALM = ITEMS.register("burning_palm",
            () -> new BurningPalmItem(new Item.Properties()));

    public static final RegistryObject<WristplatesItem> WRISTPLATES = ITEMS.register("wristplates",
            () -> new WristplatesItem(new Item.Properties()));

    public static final RegistryObject<TwinMotorItem> TWIN_MOTOR = ITEMS.register("twin_motor",
            () -> new TwinMotorItem(new Item.Properties()));

    public static final RegistryObject<PacemakerItem> PACEMAKER = ITEMS.register("pacemaker",
            () -> new PacemakerItem(new Item.Properties()));

    public static final RegistryObject<HunterLensItem> HUNTER_LENS = ITEMS.register("hunter_lens",
            () -> new HunterLensItem(new Item.Properties()));

    public static final RegistryObject<PendulockItem> PENDULOCK = ITEMS.register("pendulock",
            () -> new PendulockItem(new Item.Properties()));

    public static final RegistryObject<CellAccelerantItem> CELL_ACCELERANT = ITEMS.register("cell_accelerant",
            () -> new CellAccelerantItem(new Item.Properties()));

    public static final RegistryObject<DoubleFluxItem> DOUBLE_FLUX = ITEMS.register("double_flux",
            () -> new DoubleFluxItem(new Item.Properties()));

    public static final RegistryObject<MargetEyeItem> MARGET_EYE = ITEMS.register("marget_eye",
            () -> new MargetEyeItem(new Item.Properties()));

    public static final RegistryObject<HeavyHandItem> HEAVY_HAND = ITEMS.register("heavy_hand",
            () -> new HeavyHandItem(new Item.Properties()));

    public static final RegistryObject<WiredTendonsItem> WIRED_TENDONS = ITEMS.register("wired_tendons",
            () -> new WiredTendonsItem(new Item.Properties()));

    public static final RegistryObject<SecondHeartItem> SECOND_HEART = ITEMS.register("second_heart",
            () -> new SecondHeartItem(new Item.Properties()));

    public static final RegistryObject<MountainShatteringLaceItem> MOUNTAIN_SHATTERING_LACE =
            ITEMS.register("mountain_shattering_lace",
            () -> new MountainShatteringLaceItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
