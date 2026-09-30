package ammm.registries;

import ammm.AMMMConstants;
import ammm.AMMMTier;
import ammm.block.blockentity.base.AstralMekanismRecipeFactory;
import ammm.block.blockentity.base.MekanismRecipeFactory;
import ammm.block.blockentity.factory.astralfactory.*;
import ammm.block.blockentity.factory.enchantedfactory.*;
import ammm.block.blockentity.factory.normalfactory.*;
import ammm.block.blockentity.mashine.appliedmachine.*;
import ammm.block.blockentity.mashine.astralmachine.AMAdsorptionSeparator;
import ammm.block.blockentity.mashine.astralmachine.AMAirCompressor;
import ammm.block.blockentity.mashine.astralmachine.AMCrafter;
import ammm.block.blockentity.mashine.astralmachine.AMPaintingMachine;
import ammm.block.blockentity.mashine.enchantedmachine.*;
import ammm.block.container.factory.CFBase;
import ammm.block.container.factory.CFSawing;
import ammm.block.container.machine.ContainerCrafter;
import ammm.config.AMMMConfig;
import astral_mekanism.AMELang;
import astral_mekanism.block.container.normalmachine.ContainerTransformer;
import astral_mekanism.block.container.prefab.ContainerPagedMachine;
import astral_mekanism.block.shape.AMEBlockShapes;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.registration.BlockTypeMachine;
import astral_mekanism.registration.MachineDeferredRegister;
import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registration.RegistrationInterfaces;
import astral_mekanism.registries.AMEMachines;
import com.fxd927.mekanismelements.common.config.MSConfig;
import com.fxd927.mekanismelements.common.content.blocktype.MSBlockShapes;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import mekanism.api.Upgrade;
import mekanism.api.math.FloatingLong;
import mekanism.api.math.FloatingLongSupplier;
import mekanism.api.text.ILangEntry;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.tile.base.TileEntityMekanism;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class AMMMachines {

    public static final FloatingLongSupplier MAX_SUPPLIER = () -> {
        return FloatingLong.MAX_VALUE;
    };

    private static <BE extends AstralMekanismRecipeFactory<?, BE>, CONTAINER extends MekanismTileContainer<BE>> EnumMap<AMMMTier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER, ItemBlockMachine>> registerAMEFactories(
            Function<AMMMTier, String> nameBuilder,
            RegistrationInterfaces.BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            RegistrationInterfaces.ContainerConstructor<BE, CONTAINER> contConstructor,
            ILangEntry langEntry,
            Function<AMMMTier, UnaryOperator<BlockTypeMachine.BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        EnumMap<AMMMTier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER, ItemBlockMachine>> result = new EnumMap<>(
                AMMMTier.class);
        for (AMMMTier tier : AMMMTier.values()) {
            result.put(tier, MACHINES.registerDefaultBlockItem(nameBuilder.apply(tier),
                    constructor, beClass, contConstructor, langEntry,
                    builder -> operator.apply(tier).apply(builder.with(new AttributeTier<>(tier)))));
        }
        return result;
    }

    private static <BE extends MekanismRecipeFactory<?, BE, ?>, CONTAINER extends MekanismTileContainer<BE>> EnumMap<AMMMTier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER, ItemBlockMachine>> registerFactories(
            Function<AMMMTier, String> nameBuilder,
            RegistrationInterfaces.BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            RegistrationInterfaces.ContainerConstructor<BE, CONTAINER> contConstructor,
            ILangEntry langEntry,
            Function<AMMMTier, UnaryOperator<BlockTypeMachine.BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        EnumMap<AMMMTier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER, ItemBlockMachine>> result = new EnumMap<>(
                AMMMTier.class);
        for (AMMMTier tier : AMMMTier.values()) {
            result.put(tier, MACHINES.registerDefaultBlockItem(nameBuilder.apply(tier),
                    constructor, beClass, contConstructor, langEntry,
                    builder -> operator.apply(tier).apply(builder.with(new AttributeTier<>(tier)))));
        }
        return result;
    }

    private static <BE extends TileEntityMekanism> EnumMap<AMMMTier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerPagedMachine<BE>, ItemBlockMachine>> registerPagedMachines(
            Function<AMMMTier, String> nameBuilder,
            RegistrationInterfaces.BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            ILangEntry langEntry,
            Function<AMMMTier, UnaryOperator<BlockTypeMachine.BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        EnumMap<AMMMTier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerPagedMachine<BE>, ItemBlockMachine>> result = new EnumMap<>(
                AMMMTier.class);
        for (AMMMTier tier : AMMMTier.values()) {
            result.put(tier, MACHINES.registerDefaultBlockItem(
                    nameBuilder.apply(tier), constructor, beClass, ContainerPagedMachine<BE>::new, langEntry,
                    builder -> operator.apply(tier).apply(builder).with(new AttributeTier<>(tier))));
        }
        return result;
    }

    private static <BE extends TileEntityMekanism> EnumMap<AMMMTier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ItemBlockMachine>> registerMachines(
            Function<AMMMTier, String> nameBuilder,
            RegistrationInterfaces.BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            ILangEntry langEntry,
            Function<AMMMTier, UnaryOperator<BlockTypeMachine.BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        EnumMap<AMMMTier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ItemBlockMachine>> result = new EnumMap<>(
                AMMMTier.class);
        for (AMMMTier tier : AMMMTier.values()) {
            result.put(tier, MACHINES.registerSimple(nameBuilder.apply(tier), constructor, beClass, langEntry,
                    builder -> operator.apply(tier).apply(builder).with(new AttributeTier<>(tier))));
        }
        return result;
    }

    public static final MachineDeferredRegister MACHINES = new MachineDeferredRegister(AMMMConstants.MODID);
    public static final MachineDeferredRegister TEST_MACHINES = new MachineDeferredRegister(AMMMConstants.MODID);

    public static final MachineRegistryObject<AppliedOsmiumCompressor, ?, MekanismTileContainer<AppliedOsmiumCompressor>, ?> APPLIED_OSMIUM_COMPRESSOR = AMEMachines.MACHINES
            .registerSimple("applied_osmium_compressor", AppliedOsmiumCompressor::new, AppliedOsmiumCompressor.class, AMELang.DESCRIPTION_APPLIED_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));

    public static final MachineRegistryObject<AppliedPurificationChamber, ?, MekanismTileContainer<AppliedPurificationChamber>, ?> APPLIED_PURIFICATION_CHAMBER = AMEMachines.MACHINES
            .registerSimple("applied_purification_chamber", AppliedPurificationChamber::new, AppliedPurificationChamber.class, AMELang.DESCRIPTION_APPLIED_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));

    public static final MachineRegistryObject<AppliedChemicalInjectionChamber, ?, MekanismTileContainer<AppliedChemicalInjectionChamber>, ?> APPLIED_CHEMICAL_INJECTION_CHAMBER = AMEMachines.MACHINES
            .registerSimple("applied_chemical_injection_chamber", AppliedChemicalInjectionChamber::new, AppliedChemicalInjectionChamber.class, AMELang.DESCRIPTION_APPLIED_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));

    public static final MachineRegistryObject<AppliedCombiner, ?, MekanismTileContainer<AppliedCombiner>, ?> APPLIED_COMBINER = AMEMachines.MACHINES
            .registerSimple("applied_combiner", AppliedCombiner::new, AppliedCombiner.class, AMELang.DESCRIPTION_APPLIED_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));

    public static final MachineRegistryObject<AppliedMetallurgicInfuser, ?, MekanismTileContainer<AppliedMetallurgicInfuser>, ?> APPLIED_METALLURGIC_INFUSER = AMEMachines.MACHINES
            .registerSimple("applied_metallurgic_infuser", AppliedMetallurgicInfuser::new, AppliedMetallurgicInfuser.class, AMELang.DESCRIPTION_APPLIED_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));

    public static final MachineRegistryObject<AppliedPrecisionSawmill, ?, MekanismTileContainer<AppliedPrecisionSawmill>, ?> APPLIED_PRECISION_SAWMILL = AMEMachines.MACHINES
            .registerSimple("applied_precision_sawmill", AppliedPrecisionSawmill::new, AppliedPrecisionSawmill.class, AMELang.DESCRIPTION_APPLIED_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));

    public static final MachineRegistryObject<AMAdsorptionSeparator, BlockTileModel<AMAdsorptionSeparator, BlockTypeMachine<AMAdsorptionSeparator>>,
            MekanismTileContainer<AMAdsorptionSeparator>, ItemBlockMachine> ASTRAL_ADSORPTION_SEPARATOR = AMEMachines.MACHINES
            .registerSimple("astral_adsorption_separator", AMAdsorptionSeparator::new, AMAdsorptionSeparator.class, AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING,Upgrade.ENERGY,AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                            .withEnergyConfig(MSConfig.usageConfig.adsorptionSeparator,MAX_SUPPLIER)
                            .withSound(MekanismSounds.CHEMICAL_INFUSER).withCustomShape(MSBlockShapes.ADSORPTION_SEPARATOR));

    public static final MachineRegistryObject<AMAirCompressor, BlockTileModel<AMAirCompressor, BlockTypeMachine<AMAirCompressor>>,
            MekanismTileContainer<AMAirCompressor>, ItemBlockMachine> ASTRAL_AIR_COMPRESSOR = AMEMachines.MACHINES
            .registerSimple("astral_air_compressor", AMAirCompressor::new, AMAirCompressor.class, AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.RADIOACTIVE_SEALING.getValue(), ExtraUpgrade.STACK))
                            .withEnergyConfig(MSConfig.usageConfig.airCompressor,MAX_SUPPLIER).withSound(MekanismSounds.CHEMICAL_INFUSER));

    public static final MachineRegistryObject<AMPaintingMachine, BlockTileModel<AMPaintingMachine, BlockTypeMachine<AMPaintingMachine>>,
            MekanismTileContainer<AMPaintingMachine>, ItemBlockMachine> ASTRAL_PAINTING_MACHINE = AMEMachines.MACHINES
            .registerSimple("astral_painting_machine", AMPaintingMachine::new, AMPaintingMachine.class, AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                            .withEnergyConfig(MekanismConfig.usage.paintingMachine,MAX_SUPPLIER).withSound(MekanismSounds.PAINTING_MACHINE));

    public static final MachineRegistryObject<AMCrafter, BlockTileModel<AMCrafter, BlockTypeMachine<AMCrafter>>,
            ContainerCrafter<AMCrafter>, ItemBlockMachine> ASTRAL_ESSENTIAL_CRAFTER = AMEMachines.MACHINES
            .registerDefaultBlockItem("astral_essential_crafter", AMCrafter::new, AMCrafter.class, ContainerCrafter<AMCrafter>::new, AMELang.DESCRIPTION_ESSENTIAL_CRAFTER,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue(), Upgrade.ENERGY))
                            .withEnergyConfig(AMMMConfig.usage.astralCrafter, MAX_SUPPLIER));

    public static final MachineRegistryObject<EMCombiner, BlockTileModel<EMCombiner, BlockTypeMachine<EMCombiner>>,
            MekanismTileContainer<EMCombiner>, ItemBlockMachine> ENCHANTED_COMBINER = AMEMachines.MACHINES
            .registerSimple("enchanted_combiner", EMCombiner::new, EMCombiner.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                            .withEnergyConfig(() -> MekanismConfig.storage.combiner.get().multiply(200),() -> MekanismConfig.storage.combiner.get().multiply(12800))
                            .withSound(MekanismSounds.COMBINER));

    public static final MachineRegistryObject<EMComposter, BlockTileModel<EMComposter, BlockTypeMachine<EMComposter>>,
            MekanismTileContainer<EMComposter>, ItemBlockMachine> ENCHANTED_COMPOSTER = AMEMachines.MACHINES
            .registerSimple("enchanted_composter", EMComposter::new, EMComposter.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.SPEED,ExtraUpgrade.STACK,AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                            .withCustomShape(AMEBlockShapes.COMPOSTER));

    public static final MachineRegistryObject<EMCrafter, BlockTileModel<EMCrafter, BlockTypeMachine<EMCrafter>>,
            ContainerCrafter<EMCrafter>, ItemBlockMachine> ENCHANTED_ESSENTIAL_CRAFTER = AMEMachines.MACHINES
            .registerDefaultBlockItem("enchanted_essential_crafter", EMCrafter::new, EMCrafter.class, ContainerCrafter<EMCrafter>::new, AMELang.DESCRIPTION_ESSENTIAL_CRAFTER,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue(), Upgrade.ENERGY, Upgrade.SPEED, ExtraUpgrade.STACK))
                            .withEnergyConfig(() -> AMEConfig.usage.essentialCrafter.get().multiply(200),() -> AMEConfig.storage.essentialCrafter.get().multiply(12800)));

    public static final MachineRegistryObject<EMFluidInfuser, BlockTileModel<EMFluidInfuser, BlockTypeMachine<EMFluidInfuser>>,
            MekanismTileContainer<EMFluidInfuser>, ItemBlockMachine> ENCHANTED_FLUID_INFUSER = AMEMachines.MACHINES
            .registerSimple("enchanted_fluid_infuser", EMFluidInfuser::new, EMFluidInfuser.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, ExtraUpgrade.STACK, AMEUpgrade.WATER_SUPPLY.getValue()))
                            .withEnergyConfig(() -> AMEConfig.usage.fluidInfuser.get().multiply(200), () -> AMEConfig.storage.fluidInfuser.get().multiply(12800)).withSound(MekanismSounds.CHEMICAL_INFUSER));

    public static final MachineRegistryObject<EMGNA, BlockTileModel<EMGNA, BlockTypeMachine<EMGNA>>,
            MekanismTileContainer<EMGNA>, ItemBlockMachine> ENCHANTED_GNA = AMEMachines.MACHINES
            .registerSimple("enchanted_gna", EMGNA::new, EMGNA.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue(), AMEUpgrade.AIR_INTAKE.getValue(), Upgrade.SPEED, ExtraUpgrade.STACK)));

    public static final MachineRegistryObject<EMMekanicalCharger, BlockTileModel<EMMekanicalCharger, BlockTypeMachine<EMMekanicalCharger>>,
            MekanismTileContainer<EMMekanicalCharger>, ItemBlockMachine> ENCHANTED_MEKANICAL_CHARGER = AMEMachines.MACHINES
            .registerSimple("enchanted_mekanical_charger", EMMekanicalCharger::new, EMMekanicalCharger.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                            .withEnergyConfig(() -> AMEConfig.usage.mekanicalCherger.get().multiply(200), () -> AMEConfig.storage.mekanicalCherger.get().multiply(12800)));

    public static final MachineRegistryObject<EMReactionChamber, BlockTileModel<EMReactionChamber, BlockTypeMachine<EMReactionChamber>>,
            MekanismTileContainer<EMReactionChamber>, ItemBlockMachine> ENCHANTED_REACTION_CHAMBER = AMEMachines.MACHINES
            .registerSimple("enchanted_reaction_chamber", EMReactionChamber::new, EMReactionChamber.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), Upgrade.SPEED, ExtraUpgrade.STACK, AMEUpgrade.WATER_SUPPLY.getValue()))
                            .withEnergyConfig(() -> AMEConfig.usage.aaeReactionChamber.get().multiply(200), () -> AMEConfig.storage.aaeReactionChamber.get().multiply(12800))
                            .withSound(MekanismSounds.PRECISION_SAWMILL).withCustomShape(AMEBlockShapes.AAE_REACTION_CHAMBER));

    public static final MachineRegistryObject<EMTransformer, BlockTileModel<EMTransformer, BlockTypeMachine<EMTransformer>>,
            ContainerTransformer<EMTransformer>, ItemBlockMachine> ENCHANTED_TRANSFORMER = AMEMachines.MACHINES
            .registerDefaultBlockItem("enchanted_transformer", EMTransformer::new, EMTransformer.class, ContainerTransformer<EMTransformer>::new, AMELang.DESCRIPTION_ENCHANTED_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), Upgrade.SPEED, ExtraUpgrade.STACK, AMEUpgrade.WATER_SUPPLY.getValue()))
                            .withEnergyConfig(() -> AMEConfig.usage.transformer.get().multiply(200), () -> AMEConfig.storage.transformer.get().multiply(12800)));

    public static final EnumMap<AMMMTier, MachineRegistryObject<EFEnergizedSmelting, BlockTileModel<EFEnergizedSmelting, BlockTypeMachine<EFEnergizedSmelting>>,
            CFBase<EFEnergizedSmelting>, ItemBlockMachine>> ENCHANTED_ENERGIZED_SMELTING_FACTORIES = registerAMEFactories(
            t -> t.nameForEnchanted + "_enchanted_energized_smelting_factory", EFEnergizedSmelting::new, EFEnergizedSmelting.class, CFBase<EFEnergizedSmelting>::new, AMELang.DESCRIPTION_ENCHANTED_MACHINE,
            t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.XP.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.energizedSmelter.get().multiply(200),
                            () -> MekanismConfig.storage.energizedSmelter.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.ENERGIZED_SMELTER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<AFCrushing, BlockTileModel<AFCrushing, BlockTypeMachine<AFCrushing>>,
            CFBase<AFCrushing>, ItemBlockMachine>> ASTRAL_CRUSHING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_crushing_factory", AFCrushing::new, AFCrushing.class, CFBase<AFCrushing>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.crusher,MAX_SUPPLIER)
                    .withSound(MekanismSounds.CRUSHER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<EFCrushing, BlockTileModel<EFCrushing, BlockTypeMachine<EFCrushing>>,
            CFBase<EFCrushing>, ItemBlockMachine>> ENCHANTED_CRUSHING_FACTORIES = registerFactories(
            t -> t.nameForEnchanted + "_enchanted_crushing_factory", EFCrushing::new, EFCrushing.class, CFBase<EFCrushing>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.crusher.get().multiply(200),
                            () -> MekanismConfig.storage.crusher.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.CRUSHER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<NFCrushing, BlockTileModel<NFCrushing, BlockTypeMachine<NFCrushing>>,
            CFBase<NFCrushing>, ItemBlockMachine>> CRUSHING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_crushing_factory", NFCrushing::new, NFCrushing.class, CFBase<NFCrushing>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.crusher,() -> MekanismConfig.storage.crusher.get().multiply(t.processes))
                    .withSound(MekanismSounds.CRUSHER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<AFEnriching, BlockTileModel<AFEnriching, BlockTypeMachine<AFEnriching>>,
            CFBase<AFEnriching>, ItemBlockMachine>> ASTRAL_ENRICHING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_enriching_factory", AFEnriching::new, AFEnriching.class, CFBase<AFEnriching>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.enrichmentChamber,MAX_SUPPLIER)
                    .withSound(MekanismSounds.ENRICHMENT_CHAMBER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<EFEnriching, BlockTileModel<EFEnriching, BlockTypeMachine<EFEnriching>>,
            CFBase<EFEnriching>, ItemBlockMachine>> ENCHANTED_ENRICHING_FACTORIES = registerFactories(
            t -> t.nameForEnchanted + "_enchanted_enriching_factory", EFEnriching::new, EFEnriching.class, CFBase<EFEnriching>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK,AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.enrichmentChamber.get().multiply(200),
                            () -> MekanismConfig.storage.enrichmentChamber.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.ENRICHMENT_CHAMBER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<NFEnriching, BlockTileModel<NFEnriching, BlockTypeMachine<NFEnriching>>,
            CFBase<NFEnriching>, ItemBlockMachine>> ENRICHING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_enriching_factory", NFEnriching::new, NFEnriching.class, CFBase<NFEnriching>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK,AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.enrichmentChamber,() -> MekanismConfig.storage.enrichmentChamber.get().multiply(t.processes))
                    .withSound(MekanismSounds.ENRICHMENT_CHAMBER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<AFCombining, BlockTileModel<AFCombining, BlockTypeMachine<AFCombining>>,
            CFBase<AFCombining>, ItemBlockMachine>> ASTRAL_COMBINING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_combining_factory", AFCombining::new, AFCombining.class, CFBase<AFCombining>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.combiner,MAX_SUPPLIER)
                    .withSound(MekanismSounds.COMBINER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<EFCombining, BlockTileModel<EFCombining, BlockTypeMachine<EFCombining>>,
            CFBase<EFCombining>, ItemBlockMachine>> ENCHANTED_COMBINING_FACTORIES = registerFactories(
            t -> t.nameForEnchanted + "_enchanted_combining_factory", EFCombining::new, EFCombining.class, CFBase<EFCombining>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.combiner.get().multiply(200),
                            () -> MekanismConfig.storage.combiner.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.COMBINER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<NFCombining, BlockTileModel<NFCombining, BlockTypeMachine<NFCombining>>,
            CFBase<NFCombining>, ItemBlockMachine>> COMBINING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_combining_factory",NFCombining::new,NFCombining.class, CFBase<NFCombining>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.combiner,() -> MekanismConfig.storage.enrichmentChamber.get().multiply(t.processes))
                    .withSound(MekanismSounds.COMBINER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<AFCompressing, BlockTileModel<AFCompressing, BlockTypeMachine<AFCompressing>>,
            CFBase<AFCompressing>, ItemBlockMachine>> ASTRAL_COMPRESSING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_compressing_factory", AFCompressing::new, AFCompressing.class, CFBase<AFCompressing>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue(), AMEUpgrade.AIR_INTAKE.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.osmiumCompressor,MAX_SUPPLIER)
                    .withSound(MekanismSounds.OSMIUM_COMPRESSOR));

    public static final EnumMap<AMMMTier, MachineRegistryObject<EFCompressing, BlockTileModel<EFCompressing, BlockTypeMachine<EFCompressing>>,
            CFBase<EFCompressing>, ItemBlockMachine>> ENCHANTED_COMPRESSING_FACTORIES = registerFactories(
            t -> t.nameForEnchanted + "_enchanted_compressing_factory", EFCompressing::new, EFCompressing.class, CFBase<EFCompressing>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue(), AMEUpgrade.AIR_INTAKE.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.osmiumCompressor.get().multiply(200),
                            () -> MekanismConfig.storage.osmiumCompressor.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.OSMIUM_COMPRESSOR));

    public static final EnumMap<AMMMTier, MachineRegistryObject<NFCompressing, BlockTileModel<NFCompressing, BlockTypeMachine<NFCompressing>>,
            CFBase<NFCompressing>, ItemBlockMachine>> COMPRESSING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_compressing_factory",NFCompressing::new, NFCompressing.class, CFBase<NFCompressing>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue(), AMEUpgrade.AIR_INTAKE.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.osmiumCompressor,() -> MekanismConfig.storage.osmiumCompressor.get().multiply(t.processes))
                    .withSound(MekanismSounds.OSMIUM_COMPRESSOR));

    public static final EnumMap<AMMMTier, MachineRegistryObject<AFPurifying, BlockTileModel<AFPurifying, BlockTypeMachine<AFPurifying>>,
            CFBase<AFPurifying>, ItemBlockMachine>> ASTRAL_PURIFYING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_purifying_factory", AFPurifying::new, AFPurifying.class, CFBase<AFPurifying>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.purificationChamber,MAX_SUPPLIER)
                    .withSound(MekanismSounds.PURIFICATION_CHAMBER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<EFPurifying, BlockTileModel<EFPurifying, BlockTypeMachine<EFPurifying>>,
            CFBase<EFPurifying>, ItemBlockMachine>> ENCHANTED_PURIFYING_FACTORIES = registerFactories(
            t -> t.nameForEnchanted + "_enchanted_purifying_factory", EFPurifying::new, EFPurifying.class, CFBase<EFPurifying>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.purificationChamber.get().multiply(200),
                            () -> MekanismConfig.storage.purificationChamber.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.PURIFICATION_CHAMBER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<NFPurifying, BlockTileModel<NFPurifying, BlockTypeMachine<NFPurifying>>,
            CFBase<NFPurifying>, ItemBlockMachine>> PURIFYING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_purifying_factory",NFPurifying::new, NFPurifying.class, CFBase<NFPurifying>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.purificationChamber,() -> MekanismConfig.storage.purificationChamber.get().multiply(t.processes))
                    .withSound(MekanismSounds.PURIFICATION_CHAMBER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<AFInjecting, BlockTileModel<AFInjecting, BlockTypeMachine<AFInjecting>>,
            CFBase<AFInjecting>, ItemBlockMachine>> ASTRAL_INJECTING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_injecting_factory", AFInjecting::new, AFInjecting.class, CFBase<AFInjecting>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.chemicalInjectionChamber,MAX_SUPPLIER)
                    .withSound(MekanismSounds.CHEMICAL_INJECTION_CHAMBER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<EFInjecting, BlockTileModel<EFInjecting, BlockTypeMachine<EFInjecting>>,
            CFBase<EFInjecting>, ItemBlockMachine>> ENCHANTED_INJECTING_FACTORIES = registerFactories(
            t -> t.nameForEnchanted + "_enchanted_injecting_factory", EFInjecting::new, EFInjecting.class, CFBase<EFInjecting>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.chemicalInjectionChamber.get().multiply(200),
                            () -> MekanismConfig.storage.chemicalInjectionChamber.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.CHEMICAL_INJECTION_CHAMBER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<NFInjecting, BlockTileModel<NFInjecting, BlockTypeMachine<NFInjecting>>,
            CFBase<NFInjecting>, ItemBlockMachine>> INJECTING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_injecting_factory",NFInjecting::new, NFInjecting.class, CFBase<NFInjecting>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.chemicalInjectionChamber,() -> MekanismConfig.storage.chemicalInjectionChamber.get().multiply(t.processes))
                    .withSound(MekanismSounds.CHEMICAL_INJECTION_CHAMBER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<AFInfusing, BlockTileModel<AFInfusing, BlockTypeMachine<AFInfusing>>,
            CFBase<AFInfusing>, ItemBlockMachine>> ASTRAL_INFUSING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_infusing_factory", AFInfusing::new, AFInfusing.class, CFBase<AFInfusing>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.metallurgicInfuser,MAX_SUPPLIER)
                    .withSound(MekanismSounds.METALLURGIC_INFUSER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<EFInfusing, BlockTileModel<EFInfusing, BlockTypeMachine<EFInfusing>>,
            CFBase<EFInfusing>, ItemBlockMachine>> ENCHANTED_INFUSING_FACTORIES = registerFactories(
            t -> t.nameForEnchanted + "_enchanted_infusing_factory", EFInfusing::new, EFInfusing.class, CFBase<EFInfusing>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.metallurgicInfuser.get().multiply(200),
                            () -> MekanismConfig.storage.metallurgicInfuser.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.METALLURGIC_INFUSER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<NFInfusing, BlockTileModel<NFInfusing, BlockTypeMachine<NFInfusing>>,
            CFBase<NFInfusing>, ItemBlockMachine>> INFUSING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_infusing_factory",NFInfusing::new, NFInfusing.class, CFBase<NFInfusing>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.metallurgicInfuser,() -> MekanismConfig.storage.metallurgicInfuser.get().multiply(t.processes))
                    .withSound(MekanismSounds.METALLURGIC_INFUSER));

    public static final EnumMap<AMMMTier, MachineRegistryObject<AFSawing, BlockTileModel<AFSawing, BlockTypeMachine<AFSawing>>,
            CFSawing<AFSawing>, ItemBlockMachine>> ASTRAL_SAWING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_sawing_factory", AFSawing::new, AFSawing.class, CFSawing<AFSawing>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.precisionSawmill,MAX_SUPPLIER)
                    .withSound(MekanismSounds.PRECISION_SAWMILL));

    public static final EnumMap<AMMMTier, MachineRegistryObject<EFSawing, BlockTileModel<EFSawing, BlockTypeMachine<EFSawing>>,
            CFSawing<EFSawing>, ItemBlockMachine>> ENCHANTED_SAWING_FACTORIES = registerFactories(
            t -> t.nameForEnchanted + "_enchanted_sawing_factory", EFSawing::new, EFSawing.class, CFSawing<EFSawing>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.precisionSawmill.get().multiply(200),
                            () -> MekanismConfig.storage.precisionSawmill.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.PRECISION_SAWMILL));

    public static final EnumMap<AMMMTier, MachineRegistryObject<NFSawing, BlockTileModel<NFSawing, BlockTypeMachine<NFSawing>>,
            CFSawing<NFSawing>, ItemBlockMachine>> SAWING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_sawing_factory",NFSawing::new, NFSawing.class, CFSawing<NFSawing>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.precisionSawmill,() -> MekanismConfig.storage.precisionSawmill.get().multiply(t.processes))
                    .withSound(MekanismSounds.PRECISION_SAWMILL));
}
