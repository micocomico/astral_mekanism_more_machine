package ammm.registries;

import ammm.AMEMMTier;
import ammm.block.blockentity.base.AstralMekanismRecipeFactory;
import ammm.block.blockentity.factory.enchantedfactory.EFEnergizedSmelting;
import ammm.block.blockentity.machine.appliedmachine.*;
import ammm.block.blockentity.machine.astralmachine.AMAdsorptionSeparator;
import ammm.block.blockentity.machine.astralmachine.AMCrafter;
import ammm.block.blockentity.machine.astralmachine.AMPaintingMachine;
import ammm.block.blockentity.machine.enchantedmachine.*;
import ammm.block.container.factory.CFBase;
import ammm.block.container.machine.CMCrafter;
import ammm.block.container.machine.applied.ContainerAppliedReactionChamber;
import ammm.config.AMEMMConfig;
import astral_mekanism.AMEConstants;
import astral_mekanism.AMELang;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedAirCompressor;
import astral_mekanism.block.container.normalmachine.ContainerTransformer;
import astral_mekanism.block.shape.AMEBlockShapes;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.registration.BlockTypeMachine;
import astral_mekanism.registration.MachineDeferredRegister;
import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registration.RegistrationInterfaces;
import astral_mekanism.registries.AMEMachines;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import fixdol.mekanismelements.common.config.MSConfig;
import fixdol.mekanismelements.common.content.blocktype.MSBlockShapes;
import mekanism.api.Upgrade;
import mekanism.api.math.FloatingLong;
import mekanism.api.math.FloatingLongSupplier;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registries.MekanismSounds;
import net.minecraftforge.fml.loading.LoadingModList;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class AMEMMMachines {

    public static final FloatingLongSupplier MAX_SUPPLIER = () -> {
        return FloatingLong.MAX_VALUE;
    };

    public static final MachineDeferredRegister ESF = new MachineDeferredRegister(AMEConstants.MODID);
    public static final MachineDeferredRegister INV = new MachineDeferredRegister(AMEConstants.MODID);

    private static <BE extends AstralMekanismRecipeFactory<?, BE>, CONTAINER extends MekanismTileContainer<BE>> EnumMap<AMEMMTier, MachineRegistryObject<BE, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER, ItemBlockMachine>> registerAMEFactories(
            Function<AMEMMTier, String> nameBuilder,
            RegistrationInterfaces.BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            RegistrationInterfaces.ContainerConstructor<BE, CONTAINER> contConstructor,
            ILangEntry langEntry,
            Function<AMEMMTier, UnaryOperator<BlockTypeMachine.BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        EnumMap<AMEMMTier, MachineRegistryObject<BE, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER, ItemBlockMachine>> result = new EnumMap<>(
                AMEMMTier.class);
        for (AMEMMTier tier : AMEMMTier.values()) {
            result.put(tier, ESF.registerDefaultBlockItem(nameBuilder.apply(tier),
                    constructor, beClass, contConstructor, langEntry,
                    builder -> operator.apply(tier).apply(builder.with(new AttributeTier<>(tier)))));
        }
        return result;
    }

    public static MachineRegistryObject<AppliedOsmiumCompressor, ?, MekanismTileContainer<AppliedOsmiumCompressor>, ?> APPLIED_OSMIUM_COMPRESSOR;
    public static MachineRegistryObject<AppliedPurificationChamber, ?, MekanismTileContainer<AppliedPurificationChamber>, ?> APPLIED_PURIFICATION_CHAMBER;
    public static MachineRegistryObject<AppliedChemicalInjectionChamber, ?, MekanismTileContainer<AppliedChemicalInjectionChamber>, ?> APPLIED_CHEMICAL_INJECTION_CHAMBER;
    public static MachineRegistryObject<AppliedCombiner, ?, MekanismTileContainer<AppliedCombiner>, ?> APPLIED_COMBINER;
    public static MachineRegistryObject<AppliedMetallurgicInfuser, ?, MekanismTileContainer<AppliedMetallurgicInfuser>, ?> APPLIED_METALLURGIC_INFUSER;
    public static MachineRegistryObject<AppliedPrecisionSawmill, ?, MekanismTileContainer<AppliedPrecisionSawmill>, ?> APPLIED_PRECISION_SAWMILL;
    public static MachineRegistryObject<AppliedAlloyer, ?, MekanismTileContainer<AppliedAlloyer>, ?> APPLIED_ALLOYER;
    public static MachineRegistryObject<AppliedAntiprotonicNucleosynthesizer, ?, MekanismTileContainer<AppliedAntiprotonicNucleosynthesizer>, ?> APPLIED_ANTIPROTONIC_NUCLEOSYNTHESIZER;
    public static MachineRegistryObject<AppliedAPT, ?, MekanismTileContainer<AppliedAPT>, ?> APPLIED_APT;
    public static MachineRegistryObject<AppliedChemicalInfuser, ?, MekanismTileContainer<AppliedChemicalInfuser>, ?> APPLIED_CHEMICAL_INFUSER;
    public static MachineRegistryObject<AppliedChemicalWasher, ?, MekanismTileContainer<AppliedChemicalWasher>, ?> APPLIED_CHEMICAL_WASHER;
    public static MachineRegistryObject<AppliedDissolutionChamber, ?, MekanismTileContainer<AppliedDissolutionChamber>, ?> APPLIED_DISSOLUTION_CHAMBER;
    public static MachineRegistryObject<AppliedFluidInfuser, ?, MekanismTileContainer<AppliedFluidInfuser>, ?> APPLIED_FLUID_INFUSER;
    public static MachineRegistryObject<AppliedInfusingCondensentrator, ?, MekanismTileContainer<AppliedInfusingCondensentrator>, ?> APPLIED_INFUSING_CONDENSENTRATOR;
    public static MachineRegistryObject<AppliedMekanicalInscriber, ?, MekanismTileContainer<AppliedMekanicalInscriber>, ?> APPLIED_MEKANICAL_INSCRIBER;
    public static MachineRegistryObject<AppliedPaintingMachine, ?, MekanismTileContainer<AppliedPaintingMachine>, ?> APPLIED_PAINTING_MACHINE;
    public static MachineRegistryObject<AppliedPigmentMixer, ?, MekanismTileContainer<AppliedPigmentMixer>, ?> APPLIED_PIGMENT_MIXER;
    public static MachineRegistryObject<AppliedRadiationIrradiator, ?, MekanismTileContainer<AppliedRadiationIrradiator>, ?> APPLIED_RADIATION_IRRADIATOR;
    public static MachineRegistryObject<AppliedAdsorptionSeparator, ?, MekanismTileContainer<AppliedAdsorptionSeparator>, ?> APPLIED_ADSORPTION_SEPARATOR;
    public static MachineRegistryObject<AppliedChemixer, ?, MekanismTileContainer<AppliedChemixer>, ?> APPLIED_CHEMIXER;
    public static MachineRegistryObject<AppliedComposter, ?, MekanismTileContainer<AppliedComposter>, ?> APPLIED_COMPOSTER;
    public static MachineRegistryObject<AppliedMelter, ?, MekanismTileContainer<AppliedMelter>, ?> APPLIED_MELTER;
    public static MachineRegistryObject<AppliedPRC, ?, MekanismTileContainer<AppliedPRC>, ?> APPLIED_PRC;
    public static MachineRegistryObject<AppliedSolidificationChamber, ?, MekanismTileContainer<AppliedSolidificationChamber>, ?> APPLIED_SOLIDIFICATION_CHAMBER;
    public static MachineRegistryObject<AppliedFormulaicAssemblicator, ?, MekanismTileContainer<AppliedFormulaicAssemblicator>, ?> APPLIED_FORMULAIC_ASSEMBLICATOR;
    public static MachineRegistryObject<AppliedReactionChamber, ?, MekanismTileContainer<AppliedReactionChamber>, ?> APPLIED_REACTION_CHAMBER;
    public static MachineRegistryObject<AppliedTransformer, ?, MekanismTileContainer<AppliedTransformer>, ?> APPLIED_TRANSFORMER;
    public static MachineRegistryObject<AppliedGreenHouse, ?, MekanismTileContainer<AppliedGreenHouse>, ?> APPLIED_GREEN_HOUSE;
    public static MachineRegistryObject<AppliedCDM, ?, MekanismTileContainer<AppliedCDM>, ?> APPLIED_CDM;
    public static MachineRegistryObject<AppliedCncLathe, ?, MekanismTileContainer<AppliedCncLathe>, ?> APPLIED_CNC_LATHE;
    public static MachineRegistryObject<AppliedCncRollingMill, ?, MekanismTileContainer<AppliedCncRollingMill>, ?> APPLIED_CNC_ROLLING_MILL;
    public static MachineRegistryObject<AppliedCncStamper, ?, MekanismTileContainer<AppliedCncStamper>, ?> APPLIED_CNC_STAMPER;
    public static MachineRegistryObject<AppliedIOR, ?, MekanismTileContainer<AppliedIOR>, ?> APPLIED_IOR;
    public static MachineRegistryObject<AppliedNutritionalLiquifier, ?, MekanismTileContainer<AppliedNutritionalLiquifier>, ?> APPLIED_NUTRITIONAL_LIQUIFIER;
    public static MachineRegistryObject<AppliedPigmentExtractor, ?, MekanismTileContainer<AppliedPigmentExtractor>, ?> APPLIED_PIGMENT_EXTRACTOR;
    public static MachineRegistryObject<AppliedPlantingStation, ?, MekanismTileContainer<AppliedPlantingStation>, ?> APPLIED_PLANTING_STATION;
    public static MachineRegistryObject<AppliedRecycler, ?, MekanismTileContainer<AppliedRecycler>, ?> APPLIED_RECYCLER;
    public static MachineRegistryObject<AppliedGasConverter, ?, MekanismTileContainer<AppliedGasConverter>, ?> APPLIED_GAS_CONVERTER;
    public static MachineRegistryObject<AppliedGasSynthesizer, ?, MekanismTileContainer<AppliedGasSynthesizer>, ?> APPLIED_GAS_SYNTHESIZER;
    public static MachineRegistryObject<AppliedIAMRA, ?, MekanismTileContainer<AppliedIAMRA>, ?> APPLIED_IAMRA;
    public static MachineRegistryObject<AppliedInfuseSynthesizer, ?, MekanismTileContainer<AppliedInfuseSynthesizer>, ?> APPLIED_INFUSE_SYNTHESIZER;
    public static MachineRegistryObject<AppliedItemCompressor, ?, MekanismTileContainer<AppliedItemCompressor>, ?> APPLIED_ITEM_COMPRESSOR;
    public static MachineRegistryObject<AppliedItemUnzipper, ?, MekanismTileContainer<AppliedItemUnzipper>, ?> APPLIED_ITEM_UNZIPPER;
    public static MachineRegistryObject<AppliedMekanicalMatterCondenser, ?, MekanismTileContainer<AppliedMekanicalMatterCondenser>, ?> APPLIED_MEKANICAL_MATTER_CONDENSER;
    public static MachineRegistryObject<AppliedFluidAlloyer, ?, MekanismTileContainer<AppliedFluidAlloyer>, ?> APPLIED_FLUID_ALLOYER;
    public static MachineRegistryObject<AppliedFluidCombiner, ?, MekanismTileContainer<AppliedFluidCombiner>, ?> APPLIED_FLUID_COMBINER;

    public static MachineRegistryObject<AMAdsorptionSeparator, BlockTile.BlockTileModel<AMAdsorptionSeparator, BlockTypeMachine<AMAdsorptionSeparator>>,
            MekanismTileContainer<AMAdsorptionSeparator>, ItemBlockMachine> ASTRAL_ADSORPTION_SEPARATOR;
    public static MachineRegistryObject<AMPaintingMachine, BlockTile.BlockTileModel<AMPaintingMachine, BlockTypeMachine<AMPaintingMachine>>,
            MekanismTileContainer<AMPaintingMachine>, ItemBlockMachine> ASTRAL_PAINTING_MACHINE;
    public static MachineRegistryObject<AMCrafter, BlockTile.BlockTileModel<AMCrafter, BlockTypeMachine<AMCrafter>>,
            CMCrafter<AMCrafter>, ItemBlockMachine> ASTRAL_ESSENTIAL_CRAFTER;

    public static MachineRegistryObject<EMCrafter, BlockTile.BlockTileModel<EMCrafter, BlockTypeMachine<EMCrafter>>,
            CMCrafter<EMCrafter>, ItemBlockMachine> ENCHANTED_ESSENTIAL_CRAFTER;
    public static MachineRegistryObject<EMCombiner, BlockTile.BlockTileModel<EMCombiner, BlockTypeMachine<EMCombiner>>,
            MekanismTileContainer<EMCombiner>, ItemBlockMachine> ENCHANTED_COMBINER;
    public static MachineRegistryObject<EMComposter, BlockTile.BlockTileModel<EMComposter, BlockTypeMachine<EMComposter>>,
            MekanismTileContainer<EMComposter>, ItemBlockMachine> ENCHANTED_COMPOSTER;
    public static MachineRegistryObject<EMFluidInfuser, BlockTile.BlockTileModel<EMFluidInfuser, BlockTypeMachine<EMFluidInfuser>>,
            MekanismTileContainer<EMFluidInfuser>, ItemBlockMachine> ENCHANTED_FLUID_INFUSER;
    public static MachineRegistryObject<EMGNA, BlockTile.BlockTileModel<EMGNA, BlockTypeMachine<EMGNA>>,
            MekanismTileContainer<EMGNA>, ItemBlockMachine> ENCHANTED_GNA;
    public static MachineRegistryObject<EMMekanicalCharger, BlockTile.BlockTileModel<EMMekanicalCharger, BlockTypeMachine<EMMekanicalCharger>>,
            MekanismTileContainer<EMMekanicalCharger>, ItemBlockMachine> ENCHANTED_MEKANICAL_CHARGER;
    public static MachineRegistryObject<EMReactionChamber, BlockTile.BlockTileModel<EMReactionChamber, BlockTypeMachine<EMReactionChamber>>,
            MekanismTileContainer<EMReactionChamber>, ItemBlockMachine> ENCHANTED_REACTION_CHAMBER;
    public static MachineRegistryObject<EMTransformer, BlockTile.BlockTileModel<EMTransformer, BlockTypeMachine<EMTransformer>>,
            ContainerTransformer<EMTransformer>, ItemBlockMachine> ENCHANTED_TRANSFORMER;

    public static EnumMap<AMEMMTier, MachineRegistryObject<EFEnergizedSmelting, BlockTile.BlockTileModel<EFEnergizedSmelting, BlockTypeMachine<EFEnergizedSmelting>>,
            CFBase<EFEnergizedSmelting>, ItemBlockMachine>> ENCHANTED_ENERGIZED_SMELTING_FACTORIES;

    public static void AppliedMachines(){
        APPLIED_OSMIUM_COMPRESSOR = AMEMachines.MACHINES.registerSimple("applied_osmium_compressor", AppliedOsmiumCompressor::new, AppliedOsmiumCompressor.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE, BlockTypeMachine.BlockMachineBuilder::removeAttributeUpgrade);
        APPLIED_PURIFICATION_CHAMBER = AMEMachines.MACHINES.registerSimple("applied_purification_chamber", AppliedPurificationChamber::new, AppliedPurificationChamber.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_CHEMICAL_INJECTION_CHAMBER = AMEMachines.MACHINES.registerSimple("applied_chemical_injection_chamber", AppliedChemicalInjectionChamber::new, AppliedChemicalInjectionChamber.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_COMBINER = AMEMachines.MACHINES.registerSimple("applied_combiner", AppliedCombiner::new, AppliedCombiner.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_METALLURGIC_INFUSER = AMEMachines.MACHINES.registerSimple("applied_metallurgic_infuser", AppliedMetallurgicInfuser::new, AppliedMetallurgicInfuser.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_PRECISION_SAWMILL = AMEMachines.MACHINES.registerSimple("applied_precision_sawmill", AppliedPrecisionSawmill::new, AppliedPrecisionSawmill.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_ALLOYER = AMEMachines.MACHINES.registerSimple("applied_alloyer", AppliedAlloyer::new, AppliedAlloyer.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_ANTIPROTONIC_NUCLEOSYNTHESIZER = AMEMachines.MACHINES.registerSimple("applied_antiprotonic_nucleosynthesizer", AppliedAntiprotonicNucleosynthesizer::new, AppliedAntiprotonicNucleosynthesizer.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_APT = AMEMachines.MACHINES.registerSimple("applied_apt", AppliedAPT::new, AppliedAPT.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_CHEMICAL_INFUSER = AMEMachines.MACHINES.registerSimple("applied_chemical_infuser", AppliedChemicalInfuser::new, AppliedChemicalInfuser.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_CHEMICAL_WASHER = AMEMachines.MACHINES.registerSimple("applied_chemical_washer", AppliedChemicalWasher::new, AppliedChemicalWasher.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_DISSOLUTION_CHAMBER = AMEMachines.MACHINES.registerSimple("applied_dissolution_chamber", AppliedDissolutionChamber::new, AppliedDissolutionChamber.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_FLUID_INFUSER = AMEMachines.MACHINES.registerSimple("applied_fluid_infuser", AppliedFluidInfuser::new, AppliedFluidInfuser.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_INFUSING_CONDENSENTRATOR = AMEMachines.MACHINES.registerSimple("applied_infusing_condensentrator", AppliedInfusingCondensentrator::new, AppliedInfusingCondensentrator.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_MEKANICAL_INSCRIBER = AMEMachines.MACHINES.registerSimple("applied_mekanical_inscriber", AppliedMekanicalInscriber::new, AppliedMekanicalInscriber.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_PAINTING_MACHINE = AMEMachines.MACHINES.registerSimple("applied_painting_machine", AppliedPaintingMachine::new, AppliedPaintingMachine.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_PIGMENT_MIXER = AMEMachines.MACHINES.registerSimple("applied_pigment_mixer", AppliedPigmentMixer::new, AppliedPigmentMixer.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_RADIATION_IRRADIATOR = AMEMachines.MACHINES.registerSimple("applied_radiation_irradiator", AppliedRadiationIrradiator::new, AppliedRadiationIrradiator.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_ADSORPTION_SEPARATOR = AMEMachines.MACHINES.registerSimple("applied_adsorption_separator", AppliedAdsorptionSeparator::new, AppliedAdsorptionSeparator.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)).withCustomShape(MSBlockShapes.ADSORPTION_SEPARATOR));
        APPLIED_CHEMIXER = AMEMachines.MACHINES.registerSimple("applied_chemixer", AppliedChemixer::new, AppliedChemixer.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_COMPOSTER = AMEMachines.MACHINES.registerSimple("applied_composter", AppliedComposter::new, AppliedComposter.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.COBBLESTONE_SUPPLY.getValue())).withCustomShape(AMEBlockShapes.COMPOSTER));
        APPLIED_MELTER = AMEMachines.MACHINES.registerSimple("applied_thermalizer", AppliedMelter::new, AppliedMelter.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_PRC = AMEMachines.MACHINES.registerSimple("applied_prc", AppliedPRC::new, AppliedPRC.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_SOLIDIFICATION_CHAMBER = AMEMachines.MACHINES.registerSimple("applied_solidification_chamber", AppliedSolidificationChamber::new, AppliedSolidificationChamber.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_FORMULAIC_ASSEMBLICATOR = AMEMachines.MACHINES.registerSimple("applied_formulaic_assemblicator", AppliedFormulaicAssemblicator::new, AppliedFormulaicAssemblicator.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_REACTION_CHAMBER = AMEMachines.MACHINES.registerDefaultBlockItem("applied_reaction_chamber", AppliedReactionChamber::new, AppliedReactionChamber.class, ContainerAppliedReactionChamber::new,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)).withCustomShape(AMEBlockShapes.AAE_REACTION_CHAMBER));
        APPLIED_TRANSFORMER = AMEMachines.MACHINES.registerSimple("applied_transformer", AppliedTransformer::new, AppliedTransformer.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_GREEN_HOUSE = AMEMachines.MACHINES.registerSimple("applied_green_house", AppliedGreenHouse::new, AppliedGreenHouse.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_CDM = AMEMachines.MACHINES.registerSimple("applied_cdm", AppliedCDM::new, AppliedCDM.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_IOR = AMEMachines.MACHINES.registerSimple("applied_ior", AppliedIOR::new, AppliedIOR.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_NUTRITIONAL_LIQUIFIER = AMEMachines.MACHINES.registerSimple("applied_nutritional_liquifier", AppliedNutritionalLiquifier::new, AppliedNutritionalLiquifier.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_PIGMENT_EXTRACTOR = AMEMachines.MACHINES.registerSimple("applied_pigment_extractor", AppliedPigmentExtractor::new, AppliedPigmentExtractor.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_FLUID_COMBINER = AMEMachines.MACHINES.registerSimple("applied_fluid_combiner", AppliedFluidCombiner::new, AppliedFluidCombiner.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_FLUID_ALLOYER = AMEMachines.MACHINES.registerSimple("applied_fluid_alloyer", AppliedFluidAlloyer::new, AppliedFluidAlloyer.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        if (LoadingModList.get().getModFileById("mekmm") != null){
            APPLIED_CNC_LATHE = AMEMachines.MACHINES.registerSimple("applied_cnc_lathe", AppliedCncLathe::new, AppliedCncLathe.class,
                    AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
            APPLIED_CNC_ROLLING_MILL = AMEMachines.MACHINES.registerSimple("applied_cnc_rolling_mill", AppliedCncRollingMill::new, AppliedCncRollingMill.class,
                    AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
            APPLIED_CNC_STAMPER = AMEMachines.MACHINES.registerSimple("applied_cnc_stamper", AppliedCncStamper::new, AppliedCncStamper.class,
                    AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
            APPLIED_PLANTING_STATION = AMEMachines.MACHINES.registerSimple("applied_planting_station", AppliedPlantingStation::new, AppliedPlantingStation.class,
                    AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
            APPLIED_RECYCLER = AMEMachines.MACHINES.registerSimple("applied_recycler", AppliedRecycler::new, AppliedRecycler.class,
                    AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        }
        APPLIED_GAS_CONVERTER = AMEMachines.MACHINES.registerSimple("applied_gas_converter", AppliedGasConverter::new, AppliedGasConverter.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_GAS_SYNTHESIZER = AMEMachines.MACHINES.registerSimple("applied_gas_synthesizer", AppliedGasSynthesizer::new, AppliedGasSynthesizer.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_IAMRA = AMEMachines.MACHINES.registerSimple("applied_iamra", AppliedIAMRA::new, AppliedIAMRA.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_INFUSE_SYNTHESIZER = AMEMachines.MACHINES.registerSimple("applied_infuse_synthesizer", AppliedInfuseSynthesizer::new, AppliedInfuseSynthesizer.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_ITEM_COMPRESSOR = AMEMachines.MACHINES.registerSimple("applied_item_compressor", AppliedItemCompressor::new, AppliedItemCompressor.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_ITEM_UNZIPPER = AMEMachines.MACHINES.registerSimple("applied_item_unzipper", AppliedItemUnzipper::new, AppliedItemUnzipper.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
        APPLIED_MEKANICAL_MATTER_CONDENSER = AMEMachines.MACHINES.registerSimple("applied_mekanical_matter_condenser", AppliedMekanicalMatterCondenser::new, AppliedMekanicalMatterCondenser.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
    }
    
    public static void AstralMachines(){
        ASTRAL_ADSORPTION_SEPARATOR = AMEMachines.MACHINES.registerSimple("astral_adsorption_separator", AMAdsorptionSeparator::new, AMAdsorptionSeparator.class,
                AMELang.DESCRIPTION_ASTRAL_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING,Upgrade.ENERGY, AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                .withEnergyConfig(MSConfig.usageConfig.adsorptionSeparator,MAX_SUPPLIER).withSound(MekanismSounds.CHEMICAL_INFUSER).withCustomShape(MSBlockShapes.ADSORPTION_SEPARATOR));
        ASTRAL_PAINTING_MACHINE = AMEMachines.MACHINES.registerSimple("astral_painting_machine", AMPaintingMachine::new, AMPaintingMachine.class,
                AMELang.DESCRIPTION_ASTRAL_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                .withEnergyConfig(MekanismConfig.usage.paintingMachine,MAX_SUPPLIER).withSound(MekanismSounds.PAINTING_MACHINE));
        ASTRAL_ESSENTIAL_CRAFTER = AMEMachines.MACHINES.registerDefaultBlockItem("astral_crafter", AMCrafter::new, AMCrafter.class, CMCrafter<AMCrafter>::new,
                AMELang.DESCRIPTION_ESSENTIAL_CRAFTER,builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue(), Upgrade.ENERGY))
                .withEnergyConfig(AMEMMConfig.usage.astralCrafter, MAX_SUPPLIER));
    }

    public static void EnchantedMachines(){
        ENCHANTED_COMBINER = AMEMachines.MACHINES.registerSimple("enchanted_combiner", EMCombiner::new, EMCombiner.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,builder -> builder
                .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                .withEnergyConfig(() -> MekanismConfig.storage.combiner.get().multiply(200),() -> MekanismConfig.storage.combiner.get().multiply(12800))
                .withSound(MekanismSounds.COMBINER));
        ENCHANTED_COMPOSTER = AMEMachines.MACHINES.registerSimple("enchanted_composter", EMComposter::new, EMComposter.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,builder -> builder
                .changeAttributeUpgrade(EnumSet.of(Upgrade.SPEED,ExtraUpgrade.STACK,AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                .withCustomShape(AMEBlockShapes.COMPOSTER));
        ENCHANTED_FLUID_INFUSER = AMEMachines.MACHINES.registerSimple("enchanted_fluid_infuser", EMFluidInfuser::new, EMFluidInfuser.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,builder -> builder
                .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, ExtraUpgrade.STACK, AMEUpgrade.WATER_SUPPLY.getValue()))
                .withEnergyConfig(() -> AMEConfig.usage.fluidInfuser.get().multiply(200), () -> AMEConfig.storage.fluidInfuser.get().multiply(12800))
                .withSound(MekanismSounds.CHEMICAL_INFUSER));
        ENCHANTED_GNA = AMEMachines.MACHINES.registerSimple("enchanted_gna", EMGNA::new, EMGNA.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,builder -> builder
                .changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue(), AMEUpgrade.AIR_INTAKE.getValue(), Upgrade.SPEED, ExtraUpgrade.STACK)));
        ENCHANTED_MEKANICAL_CHARGER = AMEMachines.MACHINES.registerSimple("enchanted_mekanical_charger", EMMekanicalCharger::new, EMMekanicalCharger.class,AMELang.DESCRIPTION_ENCHANTED_MACHINE,builder -> builder
                .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                .withEnergyConfig(() -> AMEConfig.usage.mekanicalCherger.get().multiply(200), () -> AMEConfig.storage.mekanicalCherger.get().multiply(12800)));
        ENCHANTED_REACTION_CHAMBER = AMEMachines.MACHINES.registerSimple("enchanted_reaction_chamber", EMReactionChamber::new, EMReactionChamber.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,builder -> builder
                .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), Upgrade.SPEED, ExtraUpgrade.STACK, AMEUpgrade.WATER_SUPPLY.getValue()))
                .withEnergyConfig(() -> AMEConfig.usage.aaeReactionChamber.get().multiply(200), () -> AMEConfig.storage.aaeReactionChamber.get().multiply(12800))
                .withSound(MekanismSounds.PRECISION_SAWMILL).withCustomShape(AMEBlockShapes.AAE_REACTION_CHAMBER));
        ENCHANTED_TRANSFORMER = AMEMachines.MACHINES.registerDefaultBlockItem("enchanted_transformer", EMTransformer::new, EMTransformer.class,ContainerTransformer<EMTransformer>::new, AMELang.DESCRIPTION_ENCHANTED_MACHINE,builder -> builder
                .changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),Upgrade.SPEED, ExtraUpgrade.STACK, AMEUpgrade.WATER_SUPPLY.getValue()))
                .withEnergyConfig(() -> AMEConfig.usage.transformer.get().multiply(200), () -> AMEConfig.storage.transformer.get().multiply(12800)));
        ENCHANTED_ESSENTIAL_CRAFTER = AMEMachines.MACHINES.registerDefaultBlockItem("enchanted_crafter", EMCrafter::new, EMCrafter.class, CMCrafter<EMCrafter>::new, AMELang.DESCRIPTION_ESSENTIAL_CRAFTER, builder -> builder
                .changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue(), Upgrade.ENERGY, Upgrade.SPEED, ExtraUpgrade.STACK))
                .withEnergyConfig(() -> AMEConfig.usage.essentialCrafter.get().multiply(200),() -> AMEConfig.storage.essentialCrafter.get().multiply(12800)));
    }

    public static void EnchantedFactory(){
        ENCHANTED_ENERGIZED_SMELTING_FACTORIES = registerAMEFactories(t -> t.nameForEnchanted + "_enchanted_energized_smelting_factory",
                EFEnergizedSmelting::new, EFEnergizedSmelting.class, CFBase<EFEnergizedSmelting>::new, AMELang.DESCRIPTION_ENCHANTED_MACHINE,t -> builder -> builder
                .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.XP.getValue()))
                .withEnergyConfig(() -> MekanismConfig.usage.energizedSmelter.get().multiply(200),() -> MekanismConfig.storage.energizedSmelter.get().multiply(t.processes * 12800))
                .withSound(MekanismSounds.ENERGIZED_SMELTER));
    }

    public static final MachineRegistryObject<BEEnchantedAirCompressor, BlockTile.BlockTileModel<BEEnchantedAirCompressor, BlockTypeMachine<BEEnchantedAirCompressor>>, MekanismTileContainer<BEEnchantedAirCompressor>, ItemBlockMachine>
        EAC = INV.registerSimple("enchanted_air_compressor", BEEnchantedAirCompressor::new, BEEnchantedAirCompressor.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,
            builder -> builder.withEnergyConfig(MSConfig.usageConfig.airCompressor,() -> MSConfig.storageConfig.airCompressor.get().multiply(400)).withSound(MekanismSounds.CHEMICAL_INFUSER)
            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.SPEED, Upgrade.ENERGY,AMEUpgrade.RADIOACTIVE_SEALING.getValue(),ExtraUpgrade.STACK)));
}
