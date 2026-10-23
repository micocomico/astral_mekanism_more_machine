package ammm.registries;

import ammm.AMMMTier;
import ammm.block.blockentity.base.AstralMekanismRecipeFactory;
import ammm.block.blockentity.factory.enchantedfactory.EFEnergizedSmelting;
import ammm.block.blockentity.mashine.appliedmachine.*;
import ammm.block.blockentity.mashine.astralmachine.AMAdsorptionSeparator;
import ammm.block.blockentity.mashine.astralmachine.AMCrafter;
import ammm.block.blockentity.mashine.astralmachine.AMPaintingMachine;
import ammm.block.blockentity.mashine.enchantedmachine.*;
import ammm.block.container.factory.CFBase;
import ammm.block.container.machine.ContainerCrafter;
import ammm.config.AMMMConfig;
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

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TabChangedMachines {

    public static final FloatingLongSupplier MAX_SUPPLIER = () -> {
        return FloatingLong.MAX_VALUE;
    };

    public static final MachineDeferredRegister ESF = new MachineDeferredRegister(AMEConstants.MODID);
    public static final MachineDeferredRegister INV = new MachineDeferredRegister(AMEConstants.MODID);

    private static <BE extends AstralMekanismRecipeFactory<?, BE>, CONTAINER extends MekanismTileContainer<BE>> EnumMap<AMMMTier, MachineRegistryObject<BE, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER, ItemBlockMachine>> registerAMEFactories(
            Function<AMMMTier, String> nameBuilder,
            RegistrationInterfaces.BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            RegistrationInterfaces.ContainerConstructor<BE, CONTAINER> contConstructor,
            ILangEntry langEntry,
            Function<AMMMTier, UnaryOperator<BlockTypeMachine.BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        EnumMap<AMMMTier, MachineRegistryObject<BE, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER, ItemBlockMachine>> result = new EnumMap<>(
                AMMMTier.class);
        for (AMMMTier tier : AMMMTier.values()) {
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

    public static MachineRegistryObject<AMAdsorptionSeparator, BlockTile.BlockTileModel<AMAdsorptionSeparator, BlockTypeMachine<AMAdsorptionSeparator>>,
            MekanismTileContainer<AMAdsorptionSeparator>, ItemBlockMachine> ASTRAL_ADSORPTION_SEPARATOR;
    public static MachineRegistryObject<AMPaintingMachine, BlockTile.BlockTileModel<AMPaintingMachine, BlockTypeMachine<AMPaintingMachine>>,
            MekanismTileContainer<AMPaintingMachine>, ItemBlockMachine> ASTRAL_PAINTING_MACHINE;
    public static MachineRegistryObject<AMCrafter, BlockTile.BlockTileModel<AMCrafter, BlockTypeMachine<AMCrafter>>,
            ContainerCrafter<AMCrafter>, ItemBlockMachine> ASTRAL_ESSENTIAL_CRAFTER;

    public static MachineRegistryObject<EMCrafter, BlockTile.BlockTileModel<EMCrafter, BlockTypeMachine<EMCrafter>>,
            ContainerCrafter<EMCrafter>, ItemBlockMachine> ENCHANTED_ESSENTIAL_CRAFTER;
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

    public static EnumMap<AMMMTier, MachineRegistryObject<EFEnergizedSmelting, BlockTile.BlockTileModel<EFEnergizedSmelting, BlockTypeMachine<EFEnergizedSmelting>>,
            CFBase<EFEnergizedSmelting>, ItemBlockMachine>> ENCHANTED_ENERGIZED_SMELTING_FACTORIES;

    public static void AppliedMachines(){
        APPLIED_OSMIUM_COMPRESSOR = AMEMachines.MACHINES.registerSimple("applied_osmium_compressor", AppliedOsmiumCompressor::new, AppliedOsmiumCompressor.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
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
    }
    
    public static void AstralMachines(){
        ASTRAL_ADSORPTION_SEPARATOR = AMEMachines.MACHINES.registerSimple("astral_adsorption_separator", AMAdsorptionSeparator::new, AMAdsorptionSeparator.class,
                AMELang.DESCRIPTION_ASTRAL_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING,Upgrade.ENERGY, AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                .withEnergyConfig(MSConfig.usageConfig.adsorptionSeparator,MAX_SUPPLIER).withSound(MekanismSounds.CHEMICAL_INFUSER).withCustomShape(MSBlockShapes.ADSORPTION_SEPARATOR));
        ASTRAL_PAINTING_MACHINE = AMEMachines.MACHINES.registerSimple("astral_painting_machine", AMPaintingMachine::new, AMPaintingMachine.class,
                AMELang.DESCRIPTION_ASTRAL_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                .withEnergyConfig(MekanismConfig.usage.paintingMachine,MAX_SUPPLIER).withSound(MekanismSounds.PAINTING_MACHINE));
        ASTRAL_ESSENTIAL_CRAFTER = AMEMachines.MACHINES.registerDefaultBlockItem("astral_essential_crafter", AMCrafter::new, AMCrafter.class, ContainerCrafter<AMCrafter>::new,
                AMELang.DESCRIPTION_ESSENTIAL_CRAFTER,builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue(), Upgrade.ENERGY))
                .withEnergyConfig(AMMMConfig.usage.astralCrafter, MAX_SUPPLIER));
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
        ENCHANTED_ESSENTIAL_CRAFTER = AMEMachines.MACHINES.registerDefaultBlockItem("enchanted_essential_crafter", EMCrafter::new, EMCrafter.class, ContainerCrafter<EMCrafter>::new, AMELang.DESCRIPTION_ESSENTIAL_CRAFTER,builder -> builder
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
