package ammm;

import ammm.block.blockentity.factory.astralfactory.*;
import ammm.block.blockentity.factory.enchantedfactory.*;
import ammm.block.blockentity.factory.normalfactory.*;
import ammm.block.blockentity.machine.astralmachine.AMAdsorptionSeparator;
import ammm.block.blockentity.machine.astralmachine.AMCrafter;
import ammm.block.blockentity.machine.astralmachine.AMPaintingMachine;
import ammm.block.blockentity.machine.enchantedmachine.*;
import ammm.block.gui.factory.*;
import ammm.block.gui.machine.GMCrafter;
import ammm.block.gui.machine.applied.*;
import ammm.registries.*;
import astral_mekanism.block.gui.basemachine.GuiAMEAdsorptionSeparator;
import astral_mekanism.block.gui.basemachine.GuiAMEPaintingMachine;
import astral_mekanism.block.gui.normalmachine.*;
import astral_mekanism.block.gui.prefab.GuiDoubleItemToItemRecipeMachine;
import astral_mekanism.block.gui.prefab.GuiGasToGasBlock;
import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registries.AMEMachines;
import com.mojang.logging.LogUtils;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.LoadingModList;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AMEMM.MODID)
public class AMEMM {

    // Define mod id in a common place for everything to reference
    public static final String MODID = AMEMMConstants.MODID;
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public AMEMM() {
        IEventBus modEventBus = FMLJavaModLoadingContext
                .get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        AMEMMMachines.ESF.register(modEventBus);
        AMEMMMachines.INV.register(modEventBus);
        AMEMMFactories.MACHINES.register(modEventBus);
        AMEMMCreativeTab.CREATIVE_TABS.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        initScreens();
    }

    private static void initScreens() {
        registerScreenMek(AMEMachines.APPLIED_SMELTER, GuiAppliedSmelter::new);
        registerScreenMek(AMEMMMachines.APPLIED_OSMIUM_COMPRESSOR, GuiAppliedOsmiumCompressor::new);
        registerScreenMek(AMEMMMachines.APPLIED_PURIFICATION_CHAMBER, GuiAppliedPurificationChamber::new);
        registerScreenMek(AMEMMMachines.APPLIED_CHEMICAL_INJECTION_CHAMBER, GuiAppliedChemicalInjectionChamber::new);
        registerScreenMek(AMEMMMachines.APPLIED_COMBINER, GuiAppliedCombiner::new);
        registerScreenMek(AMEMMMachines.APPLIED_METALLURGIC_INFUSER, GuiAppliedMetallurgicInfuser::new);
        registerScreenMek(AMEMMMachines.APPLIED_PRECISION_SAWMILL, GuiAppliedPrecisionSawmill::new);
        registerScreenMek(AMEMMMachines.APPLIED_ALLOYER, GuiAppliedAlloyer::new);
        registerScreenMek(AMEMMMachines.APPLIED_APT, GuiAppliedAPT::new);
        registerScreenMek(AMEMMMachines.APPLIED_ANTIPROTONIC_NUCLEOSYNTHESIZER, GuiAppliedAntiprotonicNucleosynthesizer::new);
        registerScreenMek(AMEMMMachines.APPLIED_CHEMICAL_INFUSER, GuiAppliedChemicalInfuser::new);
        registerScreenMek(AMEMMMachines.APPLIED_CHEMICAL_WASHER, GuiAppliedChemicalWasher::new);
        registerScreenMek(AMEMMMachines.APPLIED_DISSOLUTION_CHAMBER, GuiAppliedDissolutionChamber::new);
        registerScreenMek(AMEMMMachines.APPLIED_FLUID_INFUSER, GuiAppliedFluidInfuser::new);
        registerScreenMek(AMEMMMachines.APPLIED_INFUSING_CONDENSENTRATOR, GuiAppliedInfusingCondensentrator::new);
        registerScreenMek(AMEMMMachines.APPLIED_MEKANICAL_INSCRIBER, GuiAppliedMekanicalInscriber::new);
        registerScreenMek(AMEMMMachines.APPLIED_PAINTING_MACHINE, GuiAppliedPaintingMachine::new);
        registerScreenMek(AMEMMMachines.APPLIED_PIGMENT_MIXER, GuiAppliedPigmentMixer::new);
        registerScreenMek(AMEMMMachines.APPLIED_RADIATION_IRRADIATOR, GuiAppliedRadiationIrradiator::new);
        registerScreenMek(AMEMMMachines.APPLIED_ADSORPTION_SEPARATOR, GuiAppliedAdsorptionSeparator::new);
        registerScreenMek(AMEMMMachines.APPLIED_CHEMIXER, GuiAppliedChemixer::new);
        registerScreenMek(AMEMMMachines.APPLIED_COMPOSTER, GuiAppliedComposter::new);
        registerScreenMek(AMEMMMachines.APPLIED_MELTER, GuiAppliedMelter::new);
        registerScreenMek(AMEMMMachines.APPLIED_PRC, GuiAppliedPRC::new);
        registerScreenMek(AMEMMMachines.APPLIED_SOLIDIFICATION_CHAMBER, GuiAppliedSolidificationChamber::new);
        registerScreenMek(AMEMMMachines.APPLIED_FORMULAIC_ASSEMBLICATOR, GuiAppliedFormulaicAssemblicator::new);
        registerScreenMek(AMEMMMachines.APPLIED_REACTION_CHAMBER, GuiAppliedReactionChamber::new);
        registerScreenMek(AMEMMMachines.APPLIED_TRANSFORMER, GuiAppliedTransformer::new);
        registerScreenMek(AMEMMMachines.APPLIED_GREEN_HOUSE, GuiAppliedGreenHouse::new);
        registerScreenMek(AMEMMMachines.APPLIED_CDM, GuiAppliedCDM::new);
        registerScreenMek(AMEMMMachines.APPLIED_IOR, GuiAppliedIOR::new);
        registerScreenMek(AMEMMMachines.APPLIED_NUTRITIONAL_LIQUIFIER, GuiAppliedNutritionalLiquifier::new);
        registerScreenMek(AMEMMMachines.APPLIED_PIGMENT_EXTRACTOR, GuiAppliedPigmentExtractor::new);
        registerScreenMek(AMEMMMachines.APPLIED_FLUID_ALLOYER, GuiAppliedFluidAlloyer::new);
        registerScreenMek(AMEMMMachines.APPLIED_FLUID_COMBINER, GuiAppliedFluidCombiner::new);
        registerScreenMek(AMEMMMachines.APPLIED_CRAFTER, GuiAppliedCrafter::new);
        if (LoadingModList.get().getModFileById("mekmm") != null) {
            registerScreenMek(AMEMMMachines.APPLIED_CNC_LATHE, GuiAppliedCncLathe::new);
            registerScreenMek(AMEMMMachines.APPLIED_CNC_ROLLING_MILL, GuiAppliedCncRollingMill::new);
            registerScreenMek(AMEMMMachines.APPLIED_CNC_STAMPER, GuiAppliedCncStamper::new);
            registerScreenMek(AMEMMMachines.APPLIED_PLANTING_STATION, GuiAppliedPlantingStation::new);
            registerScreenMek(AMEMMMachines.APPLIED_RECYCLER, GuiAppliedRecycler::new);
        }
        registerScreenMek(AMEMMMachines.APPLIED_GAS_CONVERTER, GuiAppliedGasConverter::new);
        registerScreenMek(AMEMMMachines.APPLIED_GAS_SYNTHESIZER, GuiAppliedGasSynthesizer::new);
        registerScreenMek(AMEMMMachines.APPLIED_IAMRA, GuiAppliedIAMRA::new);
        registerScreenMek(AMEMMMachines.APPLIED_INFUSE_SYNTHESIZER, GuiAppliedInfuseSynthesizer::new);
        registerScreenMek(AMEMMMachines.APPLIED_ITEM_COMPRESSOR, GuiAppliedItemCompressor::new);
        registerScreenMek(AMEMMMachines.APPLIED_ITEM_UNZIPPER, GuiAppliedItemUnzipper::new);
        registerScreenMek(AMEMMMachines.APPLIED_MEKANICAL_MATTER_CONDENSER, GuiAppliedMekanicalMatterCondenser::new);
        if (LoadingModList.get().getModFileById("ame_dynamics") != null){
            registerScreenMek(AMEDMMMachines.APPLIED_SQUEEZER, GuiAppliedSqueezer::new);
            registerScreenMek(AMEDMMMachines.APPLIED_DRYING_BASIN, GuiAppliedDryingBasin::new);
        }
        if (LoadingModList.get().getModFileById("ame_the_valine") != null){
            registerScreenMek(AMETheValineMMMachines.APPLIED_VALINE_REACTOR, GuiAppliedValineReactor::new);
        }

        registerScreenMek(AMEMMMachines.ASTRAL_ADSORPTION_SEPARATOR, GuiAMEAdsorptionSeparator<AMAdsorptionSeparator>::new);
        registerScreenMek(AMEMMMachines.ASTRAL_PAINTING_MACHINE, GuiAMEPaintingMachine<AMPaintingMachine>::new);

        registerScreenMek(AMEMMMachines.ENCHANTED_COMBINER, GuiDoubleItemToItemRecipeMachine<EMCombiner>::new);
        registerScreenMek(AMEMMMachines.ENCHANTED_COMPOSTER, GuiMekanicalComposter<EMComposter>::new);
        registerScreenMek(AMEMMMachines.ENCHANTED_GNA, GuiGasToGasBlock<EMGNA>::new);
        registerScreenMek(AMEMMMachines.ENCHANTED_FLUID_INFUSER, GuiFluidInfuser<EMFluidInfuser>::new);
        registerScreenMek(AMEMMMachines.ENCHANTED_MEKANICAL_CHARGER, GuiMekanicalCharger<EMMekanicalCharger>::new);
        registerScreenMek(AMEMMMachines.ENCHANTED_REACTION_CHAMBER, GuiAAEReactionChamber<EMReactionChamber>::new);
        registerScreenMek(AMEMMMachines.ENCHANTED_TRANSFORMER, GuiTransformer<EMTransformer>::new);

        AMEMMMachines.ENCHANTED_ENERGIZED_SMELTING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFEnergizedSmelting<EFEnergizedSmelting>::new));

        registerScreenMek(AMEMMMachines.ASTRAL_ESSENTIAL_CRAFTER, GMCrafter<AMCrafter>::new);
        registerScreenMek(AMEMMMachines.ENCHANTED_ESSENTIAL_CRAFTER, GMCrafter<EMCrafter>::new);

        AMEMMFactories.ASTRAL_CRUSHING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFElectric<AFCrushing>::new));
        AMEMMFactories.ENCHANTED_CRUSHING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFElectric<EFCrushing>::new));
        AMEMMFactories.CRUSHING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFElectric<NFCrushing>::new));

        AMEMMFactories.ASTRAL_ENRICHING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFElectric<AFEnriching>::new));
        AMEMMFactories.ENCHANTED_ENRICHING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFElectric<EFEnriching>::new));
        AMEMMFactories.ENRICHING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFElectric<NFEnriching>::new));

        AMEMMFactories.ASTRAL_COMBINING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFCombining<AFCombining>::new));
        AMEMMFactories.ENCHANTED_COMBINING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFCombining<EFCombining>::new));
        AMEMMFactories.COMBINING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFCombining<NFCombining>::new));

        AMEMMFactories.ASTRAL_COMPRESSING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFAdvanced<AFCompressing>::new));
        AMEMMFactories.ENCHANTED_COMPRESSING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFAdvanced<EFCompressing>::new));
        AMEMMFactories.COMPRESSING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFAdvanced<NFCompressing>::new));

        AMEMMFactories.ASTRAL_INJECTING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFAdvanced<AFInjecting>::new));
        AMEMMFactories.ENCHANTED_INJECTING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFAdvanced<EFInjecting>::new));
        AMEMMFactories.INJECTING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFAdvanced<NFInjecting>::new));

        AMEMMFactories.ASTRAL_PURIFYING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFAdvanced<AFPurifying>::new));
        AMEMMFactories.ENCHANTED_PURIFYING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFAdvanced<EFPurifying>::new));
        AMEMMFactories.PURIFYING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFAdvanced<NFPurifying>::new));

        AMEMMFactories.ASTRAL_INFUSING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFInfusing<AFInfusing>::new));
        AMEMMFactories.ENCHANTED_INFUSING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFInfusing<EFInfusing>::new));
        AMEMMFactories.INFUSING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFInfusing<NFInfusing>::new));

        AMEMMFactories.ASTRAL_SAWING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFSawing<AFSawing>::new));
        AMEMMFactories.ENCHANTED_SAWING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFSawing<EFSawing>::new));
        AMEMMFactories.SAWING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFSawing<NFSawing>::new));
    }

    private static <BE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<BE>, U extends Screen & MenuAccess<CONTAINER>> void registerScreenMek(
            MachineRegistryObject<BE, ?, ? extends CONTAINER, ?> registryObject,
            ScreenConstructor<CONTAINER, U> constructor) {
        MenuScreens.register(registryObject.getContainer().get(), constructor);
    }
}