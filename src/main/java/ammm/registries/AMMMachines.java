package ammm.registries;

import ammm.AMMMConstants;
import ammm.AMMMLang;
import ammm.block.blockentity.astralfactory.*;
import ammm.block.blockentity.astralmachine.AMCrafter;
import ammm.block.blockentity.base.MekanismRecipeFactory;
import ammm.block.blockentity.enchantedfactory.*;
import ammm.block.blockentity.enchantedmachine.EMCombiner;
import ammm.block.blockentity.enchantedmachine.EMCrafter;
import ammm.block.blockentity.normalfactory.*;
import ammm.block.container.factory.CFSawing;
import ammm.block.container.machine.ContainerCrafter;
import ammm.config.AMMMConfig;
import ammm.registration.BlockTypeMachine;
import ammm.registration.MachineDeferredRegister;
import ammm.registration.MachineRegistryObject;
import astral_mekanism.AMELang;
import astral_mekanism.AMETier;
import astral_mekanism.block.blockentity.base.BlockEntityRecipeFactory;
import astral_mekanism.block.container.factory.ContainerAstralMekanismFactory;
import astral_mekanism.block.container.prefab.ContainerPagedMachine;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.registration.RegistrationInterfaces;
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

    private static <BE extends BlockEntityRecipeFactory<?, BE>> EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerAstralMekanismFactory<BE>, ItemBlockMachine>> registerAMEFactories(
            Function<AMETier, String> nameBuilder,
            RegistrationInterfaces.BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            ILangEntry langEntry,
            Function<AMETier, UnaryOperator<BlockTypeMachine.BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerAstralMekanismFactory<BE>, ItemBlockMachine>> result = new EnumMap<>(
                AMETier.class);
        for (AMETier tier : AMETier.values()) {
            result.put(tier, MACHINES.registerDefaultBlockItem(nameBuilder.apply(tier),
                    constructor, beClass, ContainerAstralMekanismFactory<BE>::new, langEntry,
                    builder -> operator.apply(tier).apply(builder.with(new AttributeTier<>(tier)))));
        }
        return result;
    }

    private static <BE extends MekanismRecipeFactory<?, BE, ?>, CONTAINER extends MekanismTileContainer<BE>> EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER, ItemBlockMachine>> registerFactories(
            Function<AMETier, String> nameBuilder,
            RegistrationInterfaces.BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            RegistrationInterfaces.ContainerConstructor<BE, CONTAINER> contConstructor,
            ILangEntry langEntry,
            Function<AMETier, UnaryOperator<BlockTypeMachine.BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, CONTAINER, ItemBlockMachine>> result = new EnumMap<>(
                AMETier.class);
        for (AMETier tier : AMETier.values()) {
            result.put(tier, MACHINES.registerDefaultBlockItem(nameBuilder.apply(tier),
                    constructor, beClass, contConstructor, langEntry,
                    builder -> operator.apply(tier).apply(builder.with(new AttributeTier<>(tier)))));
        }
        return result;
    }

    private static <BE extends TileEntityMekanism> EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerPagedMachine<BE>, ItemBlockMachine>> registerPagedMachines(
            Function<AMETier, String> nameBuilder,
            RegistrationInterfaces.BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            ILangEntry langEntry,
            Function<AMETier, UnaryOperator<BlockTypeMachine.BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerPagedMachine<BE>, ItemBlockMachine>> result = new EnumMap<>(
                AMETier.class);
        for (AMETier tier : AMETier.values()) {
            result.put(tier, MACHINES.registerDefaultBlockItem(
                    nameBuilder.apply(tier), constructor, beClass, ContainerPagedMachine<BE>::new, langEntry,
                    builder -> operator.apply(tier).apply(builder).with(new AttributeTier<>(tier))));
        }
        return result;
    }

    private static <BE extends TileEntityMekanism> EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ItemBlockMachine>> registerMachines(
            Function<AMETier, String> nameBuilder,
            RegistrationInterfaces.BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            ILangEntry langEntry,
            Function<AMETier, UnaryOperator<BlockTypeMachine.BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ItemBlockMachine>> result = new EnumMap<>(
                AMETier.class);
        for (AMETier tier : AMETier.values()) {
            result.put(tier, MACHINES.registerSimple(nameBuilder.apply(tier), constructor, beClass, langEntry,
                    builder -> operator.apply(tier).apply(builder).with(new AttributeTier<>(tier))));
        }
        return result;
    }

    public static final MachineDeferredRegister MACHINES = new MachineDeferredRegister(AMMMConstants.MODID);

    public static final MachineRegistryObject<AMCrafter, BlockTileModel<AMCrafter, BlockTypeMachine<AMCrafter>>,
            ContainerCrafter<AMCrafter>, ItemBlockMachine> ASTRAL_CRAFTER = MACHINES.
            registerDefaultBlockItem("astral_crafter", AMCrafter::new, AMCrafter.class, ContainerCrafter<AMCrafter>::new, AMMMLang.DESCRIPTION_ASTRAL_CRAFTER,
            builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue(),Upgrade.ENERGY))
                    .withEnergyConfig(AMMMConfig.usage.astralCrafter, MAX_SUPPLIER));


    public static final MachineRegistryObject<EMCrafter, BlockTileModel<EMCrafter, BlockTypeMachine<EMCrafter>>,
            ContainerCrafter<EMCrafter>, ItemBlockMachine> ENCHANTED_CRAFTER = MACHINES.
            registerDefaultBlockItem("enchanted_crafter", EMCrafter::new, EMCrafter.class, ContainerCrafter<EMCrafter>::new, AMMMLang.DESCRIPTION_ENCHANTED_CRAFTER,
            builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue(),Upgrade.ENERGY,Upgrade.SPEED,ExtraUpgrade.STACK))
                    .withEnergyConfig(() -> AMEConfig.usage.essentialCrafter.get().multiply(200),() -> AMEConfig.storage.essentialCrafter.get().multiply(12800)));

    public static final MachineRegistryObject<EMCombiner, BlockTileModel<EMCombiner, BlockTypeMachine<EMCombiner>>,
            MekanismTileContainer<EMCombiner>, ItemBlockMachine> ENCHANTED_COMBINER = MACHINES.
            registerSimple("enchanted_combiner", EMCombiner::new, EMCombiner.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,
            builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.storage.combiner.get().multiply(200),() -> MekanismConfig.storage.combiner.get().multiply(12800))
                    .withSound(MekanismSounds.COMBINER));


    public static final EnumMap<AMETier, MachineRegistryObject<EFEnergizedSmelting, BlockTileModel<EFEnergizedSmelting, BlockTypeMachine<EFEnergizedSmelting>>,
            ContainerAstralMekanismFactory<EFEnergizedSmelting>, ItemBlockMachine>> ENCHANTED_ENERGIZED_SMELTING_FACTORIES = registerAMEFactories(
            t -> t.nameForNormal + "_enchanted_energized_smelting_factory", EFEnergizedSmelting::new, EFEnergizedSmelting.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,
            t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.XP.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.energizedSmelter.get().multiply(200),
                            () -> MekanismConfig.storage.energizedSmelter.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.ENERGIZED_SMELTER));

    public static final EnumMap<AMETier, MachineRegistryObject<AFCrushing, BlockTileModel<AFCrushing, BlockTypeMachine<AFCrushing>>,
            ContainerAstralMekanismFactory<AFCrushing>, ItemBlockMachine>> ASTRAL_CRUSHING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_crushing_factory", AFCrushing::new, AFCrushing.class, ContainerAstralMekanismFactory<AFCrushing>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.crusher,MAX_SUPPLIER)
                    .withSound(MekanismSounds.CRUSHER));

    public static final EnumMap<AMETier, MachineRegistryObject<EFCrushing, BlockTileModel<EFCrushing, BlockTypeMachine<EFCrushing>>,
            ContainerAstralMekanismFactory<EFCrushing>, ItemBlockMachine>> ENCHANTED_CRUSHING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_enchanted_crushing_factory", EFCrushing::new, EFCrushing.class, ContainerAstralMekanismFactory<EFCrushing>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.crusher.get().multiply(200),
                            () -> MekanismConfig.storage.crusher.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.CRUSHER));

    public static final EnumMap<AMETier, MachineRegistryObject<NFCrushing, BlockTileModel<NFCrushing, BlockTypeMachine<NFCrushing>>,
            ContainerAstralMekanismFactory<NFCrushing>, ItemBlockMachine>> CRUSHING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_crushing_factory", NFCrushing::new, NFCrushing.class, ContainerAstralMekanismFactory<NFCrushing>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.crusher,() -> MekanismConfig.storage.crusher.get().multiply(t.processes))
                    .withSound(MekanismSounds.CRUSHER));

    public static final EnumMap<AMETier, MachineRegistryObject<AFEnriching, BlockTileModel<AFEnriching, BlockTypeMachine<AFEnriching>>,
            ContainerAstralMekanismFactory<AFEnriching>, ItemBlockMachine>> ASTRAL_ENRICHING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_enriching_factory", AFEnriching::new, AFEnriching.class, ContainerAstralMekanismFactory<AFEnriching>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.enrichmentChamber,MAX_SUPPLIER)
                    .withSound(MekanismSounds.ENRICHMENT_CHAMBER));

    public static final EnumMap<AMETier, MachineRegistryObject<EFEnriching, BlockTileModel<EFEnriching, BlockTypeMachine<EFEnriching>>,
            ContainerAstralMekanismFactory<EFEnriching>, ItemBlockMachine>> ENCHANTED_ENRICHING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_enchanted_enriching_factory", EFEnriching::new, EFEnriching.class, ContainerAstralMekanismFactory<EFEnriching>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK,AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.enrichmentChamber.get().multiply(200),
                            () -> MekanismConfig.storage.enrichmentChamber.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.ENRICHMENT_CHAMBER));

    public static final EnumMap<AMETier, MachineRegistryObject<NFEnriching, BlockTileModel<NFEnriching, BlockTypeMachine<NFEnriching>>,
            ContainerAstralMekanismFactory<NFEnriching>, ItemBlockMachine>> ENRICHING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_enriching_factory", NFEnriching::new, NFEnriching.class, ContainerAstralMekanismFactory<NFEnriching>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK,AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.enrichmentChamber,() -> MekanismConfig.storage.enrichmentChamber.get().multiply(t.processes))
                    .withSound(MekanismSounds.ENRICHMENT_CHAMBER));

    public static final EnumMap<AMETier, MachineRegistryObject<AFCombining, BlockTileModel<AFCombining, BlockTypeMachine<AFCombining>>,
            ContainerAstralMekanismFactory<AFCombining>, ItemBlockMachine>> ASTRAL_COMBINING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_combining_factory", AFCombining::new, AFCombining.class, ContainerAstralMekanismFactory<AFCombining>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.combiner,MAX_SUPPLIER)
                    .withSound(MekanismSounds.COMBINER));

    public static final EnumMap<AMETier, MachineRegistryObject<EFCombining, BlockTileModel<EFCombining, BlockTypeMachine<EFCombining>>,
            ContainerAstralMekanismFactory<EFCombining>, ItemBlockMachine>> ENCHANTED_COMBINING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_enchanted_combining_factory", EFCombining::new, EFCombining.class, ContainerAstralMekanismFactory<EFCombining>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.combiner.get().multiply(200),
                            () -> MekanismConfig.storage.combiner.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.COMBINER));

    public static final EnumMap<AMETier, MachineRegistryObject<NFCombining, BlockTileModel<NFCombining, BlockTypeMachine<NFCombining>>,
            ContainerAstralMekanismFactory<NFCombining>, ItemBlockMachine>> COMBINING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_combining_factory",NFCombining::new,NFCombining.class, ContainerAstralMekanismFactory<NFCombining>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.combiner,() -> MekanismConfig.storage.enrichmentChamber.get().multiply(t.processes))
                    .withSound(MekanismSounds.COMBINER));

    public static final EnumMap<AMETier, MachineRegistryObject<AFCompressing, BlockTileModel<AFCompressing, BlockTypeMachine<AFCompressing>>,
            ContainerAstralMekanismFactory<AFCompressing>, ItemBlockMachine>> ASTRAL_COMPRESSING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_compressing_factory", AFCompressing::new, AFCompressing.class, ContainerAstralMekanismFactory<AFCompressing>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue(), AMEUpgrade.AIR_INTAKE.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.osmiumCompressor,MAX_SUPPLIER)
                    .withSound(MekanismSounds.OSMIUM_COMPRESSOR));

    public static final EnumMap<AMETier, MachineRegistryObject<EFCompressing, BlockTileModel<EFCompressing, BlockTypeMachine<EFCompressing>>,
            ContainerAstralMekanismFactory<EFCompressing>, ItemBlockMachine>> ENCHANTED_COMPRESSING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_enchanted_compressing_factory", EFCompressing::new, EFCompressing.class, ContainerAstralMekanismFactory<EFCompressing>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue(), AMEUpgrade.AIR_INTAKE.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.osmiumCompressor.get().multiply(200),
                            () -> MekanismConfig.storage.osmiumCompressor.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.OSMIUM_COMPRESSOR));

    public static final EnumMap<AMETier, MachineRegistryObject<NFCompressing, BlockTileModel<NFCompressing, BlockTypeMachine<NFCompressing>>,
            ContainerAstralMekanismFactory<NFCompressing>, ItemBlockMachine>> COMPRESSING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_compressing_factory",NFCompressing::new, NFCompressing.class, ContainerAstralMekanismFactory<NFCompressing>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue(), AMEUpgrade.AIR_INTAKE.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.osmiumCompressor,() -> MekanismConfig.storage.osmiumCompressor.get().multiply(t.processes))
                    .withSound(MekanismSounds.OSMIUM_COMPRESSOR));

    public static final EnumMap<AMETier, MachineRegistryObject<AFPurifying, BlockTileModel<AFPurifying, BlockTypeMachine<AFPurifying>>,
            ContainerAstralMekanismFactory<AFPurifying>, ItemBlockMachine>> ASTRAL_PURIFYING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_purifying_factory", AFPurifying::new, AFPurifying.class, ContainerAstralMekanismFactory<AFPurifying>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.purificationChamber,MAX_SUPPLIER)
                    .withSound(MekanismSounds.PURIFICATION_CHAMBER));

    public static final EnumMap<AMETier, MachineRegistryObject<EFPurifying, BlockTileModel<EFPurifying, BlockTypeMachine<EFPurifying>>,
            ContainerAstralMekanismFactory<EFPurifying>, ItemBlockMachine>> ENCHANTED_PURIFYING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_enchanted_purifying_factory", EFPurifying::new, EFPurifying.class, ContainerAstralMekanismFactory<EFPurifying>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.purificationChamber.get().multiply(200),
                            () -> MekanismConfig.storage.purificationChamber.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.PURIFICATION_CHAMBER));

    public static final EnumMap<AMETier, MachineRegistryObject<NFPurifying, BlockTileModel<NFPurifying, BlockTypeMachine<NFPurifying>>,
            ContainerAstralMekanismFactory<NFPurifying>, ItemBlockMachine>> PURIFYING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_purifying_factory",NFPurifying::new, NFPurifying.class, ContainerAstralMekanismFactory<NFPurifying>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.purificationChamber,() -> MekanismConfig.storage.purificationChamber.get().multiply(t.processes))
                    .withSound(MekanismSounds.PURIFICATION_CHAMBER));

    public static final EnumMap<AMETier, MachineRegistryObject<AFInjecting, BlockTileModel<AFInjecting, BlockTypeMachine<AFInjecting>>,
            ContainerAstralMekanismFactory<AFInjecting>, ItemBlockMachine>> ASTRAL_INJECTING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_injecting_factory", AFInjecting::new, AFInjecting.class, ContainerAstralMekanismFactory<AFInjecting>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.chemicalInjectionChamber,MAX_SUPPLIER)
                    .withSound(MekanismSounds.PURIFICATION_CHAMBER));

    public static final EnumMap<AMETier, MachineRegistryObject<EFInjecting, BlockTileModel<EFInjecting, BlockTypeMachine<EFInjecting>>,
            ContainerAstralMekanismFactory<EFInjecting>, ItemBlockMachine>> ENCHANTED_INJECTING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_enchanted_injecting_factory", EFInjecting::new, EFInjecting.class, ContainerAstralMekanismFactory<EFInjecting>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.chemicalInjectionChamber.get().multiply(200),
                            () -> MekanismConfig.storage.chemicalInjectionChamber.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.PURIFICATION_CHAMBER));

    public static final EnumMap<AMETier, MachineRegistryObject<NFInjecting, BlockTileModel<NFInjecting, BlockTypeMachine<NFInjecting>>,
            ContainerAstralMekanismFactory<NFInjecting>, ItemBlockMachine>> INJECTING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_injecting_factory",NFInjecting::new, NFInjecting.class, ContainerAstralMekanismFactory<NFInjecting>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.chemicalInjectionChamber,() -> MekanismConfig.storage.chemicalInjectionChamber.get().multiply(t.processes))
                    .withSound(MekanismSounds.PURIFICATION_CHAMBER));

    public static final EnumMap<AMETier, MachineRegistryObject<AFInfusing, BlockTileModel<AFInfusing, BlockTypeMachine<AFInfusing>>,
            ContainerAstralMekanismFactory<AFInfusing>, ItemBlockMachine>> ASTRAL_INFUSING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_infusing_factory", AFInfusing::new, AFInfusing.class, ContainerAstralMekanismFactory<AFInfusing>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.metallurgicInfuser,MAX_SUPPLIER)
                    .withSound(MekanismSounds.METALLURGIC_INFUSER));

    public static final EnumMap<AMETier, MachineRegistryObject<EFInfusing, BlockTileModel<EFInfusing, BlockTypeMachine<EFInfusing>>,
            ContainerAstralMekanismFactory<EFInfusing>, ItemBlockMachine>> ENCHANTED_INFUSING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_enchanted_infusing_factory", EFInfusing::new, EFInfusing.class, ContainerAstralMekanismFactory<EFInfusing>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.metallurgicInfuser.get().multiply(200),
                            () -> MekanismConfig.storage.metallurgicInfuser.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.METALLURGIC_INFUSER));

    public static final EnumMap<AMETier, MachineRegistryObject<NFInfusing, BlockTileModel<NFInfusing, BlockTypeMachine<NFInfusing>>,
            ContainerAstralMekanismFactory<NFInfusing>, ItemBlockMachine>> INFUSING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_infusing_factory",NFInfusing::new, NFInfusing.class, ContainerAstralMekanismFactory<NFInfusing>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.metallurgicInfuser,() -> MekanismConfig.storage.metallurgicInfuser.get().multiply(t.processes))
                    .withSound(MekanismSounds.METALLURGIC_INFUSER));

    public static final EnumMap<AMETier, MachineRegistryObject<AFSawing, BlockTileModel<AFSawing, BlockTypeMachine<AFSawing>>,
            CFSawing<AFSawing>, ItemBlockMachine>> ASTRAL_SAWING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_sawing_factory", AFSawing::new, AFSawing.class, CFSawing<AFSawing>::new,
            AMELang.DESCRIPTION_ASTRAL_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.precisionSawmill,MAX_SUPPLIER)
                    .withSound(MekanismSounds.PRECISION_SAWMILL));

    public static final EnumMap<AMETier, MachineRegistryObject<EFSawing, BlockTileModel<EFSawing, BlockTypeMachine<EFSawing>>,
            CFSawing<EFSawing>, ItemBlockMachine>> ENCHANTED_SAWING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_enchanted_sawing_factory", EFSawing::new, EFSawing.class, CFSawing<EFSawing>::new,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.precisionSawmill.get().multiply(200),
                            () -> MekanismConfig.storage.precisionSawmill.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.PRECISION_SAWMILL));

    public static final EnumMap<AMETier, MachineRegistryObject<NFSawing, BlockTileModel<NFSawing, BlockTypeMachine<NFSawing>>,
            CFSawing<NFSawing>, ItemBlockMachine>> SAWING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_sawing_factory",NFSawing::new, NFSawing.class, CFSawing<NFSawing>::new,
            MekanismLang.DESCRIPTION_FACTORY, t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                            ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.precisionSawmill,() -> MekanismConfig.storage.precisionSawmill.get().multiply(t.processes))
                    .withSound(MekanismSounds.PRECISION_SAWMILL));
}
