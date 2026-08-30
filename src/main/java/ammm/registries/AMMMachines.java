package ammm.registries;

import ammm.AMMMConstants;
import ammm.AMMMLang;
import ammm.block.blockentity.astralfactory.AFCombining;
import ammm.block.blockentity.astralfactory.AFCompressing;
import ammm.block.blockentity.astralfactory.AFCrushing;
import ammm.block.blockentity.astralfactory.AFEnriching;
import ammm.block.blockentity.astralmachine.AMCrafter;
import ammm.block.blockentity.base.MekanismRecipeFactory;
import ammm.block.blockentity.enchantedfactory.EFCombining;
import ammm.block.blockentity.enchantedfactory.EFCrushing;
import ammm.block.blockentity.enchantedfactory.EFEnergizedSmelting;
import ammm.block.blockentity.enchantedfactory.EFEnriching;
import ammm.block.blockentity.enchantedmachine.EMCombiner;
import ammm.block.blockentity.enchantedmachine.EMCrafter;
import ammm.block.blockentity.normalfactory.NFCombining;
import ammm.block.blockentity.normalfactory.NFCrushing;
import ammm.block.blockentity.normalfactory.NFEnriching;
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

    private static <BE extends MekanismRecipeFactory<?, BE, ?>> EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerAstralMekanismFactory<BE>, ItemBlockMachine>> registerFactories(
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
            t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.XP.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.energizedSmelter.get().multiply(200),() -> MekanismConfig.storage.energizedSmelter.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.ENERGIZED_SMELTER));

    public static final EnumMap<AMETier, MachineRegistryObject<AFCrushing, BlockTileModel<AFCrushing, BlockTypeMachine<AFCrushing>>,
            ContainerAstralMekanismFactory<AFCrushing>, ItemBlockMachine>> ASTRAL_CRUSHING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_crushing_factory", AFCrushing::new, AFCrushing.class, AMELang.DESCRIPTION_ASTRAL_MACHINE,
            t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.crusher,MAX_SUPPLIER)
                    .withSound(MekanismSounds.CRUSHER));

    public static final EnumMap<AMETier, MachineRegistryObject<EFCrushing, BlockTileModel<EFCrushing, BlockTypeMachine<EFCrushing>>,
            ContainerAstralMekanismFactory<EFCrushing>, ItemBlockMachine>> ENCHANTED_CRUSHING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_enchanted_crushing_factory", EFCrushing::new, EFCrushing.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,
            t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,  ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.crusher.get().multiply(200),() -> MekanismConfig.storage.crusher.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.CRUSHER));

    public static final EnumMap<AMETier, MachineRegistryObject<NFCrushing, BlockTileModel<NFCrushing, BlockTypeMachine<NFCrushing>>,
            ContainerAstralMekanismFactory<NFCrushing>, ItemBlockMachine>> CRUSHING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_crushing_factory", NFCrushing::new, NFCrushing.class, MekanismLang.DESCRIPTION_FACTORY,
            t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, ExtraUpgrade.STACK, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.crusher,() -> MekanismConfig.storage.crusher.get().multiply(t.processes))
                    .withSound(MekanismSounds.CRUSHER));

    public static final EnumMap<AMETier, MachineRegistryObject<AFEnriching, BlockTileModel<AFEnriching, BlockTypeMachine<AFEnriching>>,
            ContainerAstralMekanismFactory<AFEnriching>, ItemBlockMachine>> ASTRAL_ENRICHING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_enriching_factory", AFEnriching::new, AFEnriching.class, AMELang.DESCRIPTION_ASTRAL_MACHINE,
            t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.enrichmentChamber,MAX_SUPPLIER)
                    .withSound(MekanismSounds.ENRICHMENT_CHAMBER));

    public static final EnumMap<AMETier, MachineRegistryObject<EFEnriching, BlockTileModel<EFEnriching, BlockTypeMachine<EFEnriching>>,
            ContainerAstralMekanismFactory<EFEnriching>, ItemBlockMachine>> ENCHANTED_ENRICHING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_enchanted_enriching_factory", EFEnriching::new, EFEnriching.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,
            t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,  ExtraUpgrade.STACK,AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.enrichmentChamber.get().multiply(200),() -> MekanismConfig.storage.enrichmentChamber.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.ENRICHMENT_CHAMBER));

    public static final EnumMap<AMETier, MachineRegistryObject<NFEnriching, BlockTileModel<NFEnriching, BlockTypeMachine<NFEnriching>>,
            ContainerAstralMekanismFactory<NFEnriching>, ItemBlockMachine>> ENRICHING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_enriching_factory", NFEnriching::new, NFEnriching.class, MekanismLang.DESCRIPTION_FACTORY,
            t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, ExtraUpgrade.STACK,AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.enrichmentChamber,() -> MekanismConfig.storage.enrichmentChamber.get().multiply(t.processes))
                    .withSound(MekanismSounds.ENRICHMENT_CHAMBER));

    public static final EnumMap<AMETier, MachineRegistryObject<AFCombining, BlockTileModel<AFCombining, BlockTypeMachine<AFCombining>>,
            ContainerAstralMekanismFactory<AFCombining>, ItemBlockMachine>> ASTRAL_COMBINING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_combining_factory", AFCombining::new, AFCombining.class, AMELang.DESCRIPTION_ASTRAL_MACHINE,
            t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.combiner,MAX_SUPPLIER)
                    .withSound(MekanismSounds.COMBINER));

    public static final EnumMap<AMETier, MachineRegistryObject<EFCombining, BlockTileModel<EFCombining, BlockTypeMachine<EFCombining>>,
            ContainerAstralMekanismFactory<EFCombining>, ItemBlockMachine>> ENCHANTED_COMBINING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_enchanted_combining_factory", EFCombining::new, EFCombining.class, AMELang.DESCRIPTION_ENCHANTED_MACHINE,
            t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(() -> MekanismConfig.usage.combiner.get().multiply(200),() -> MekanismConfig.storage.combiner.get().multiply(t.processes * 12800))
                    .withSound(MekanismSounds.COMBINER));

    public static final EnumMap<AMETier, MachineRegistryObject<NFCombining, BlockTileModel<NFCombining, BlockTypeMachine<NFCombining>>,
            ContainerAstralMekanismFactory<NFCombining>, ItemBlockMachine>> COMBINING_FACTORIES = registerFactories(
            t -> t.nameForNormal + "_combining_factory",NFCombining::new,NFCombining.class,MekanismLang.DESCRIPTION_FACTORY,
            t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.combiner,() -> MekanismConfig.storage.enrichmentChamber.get().multiply(t.processes))
                    .withSound(MekanismSounds.COMBINER));

    public static final EnumMap<AMETier, MachineRegistryObject<AFCompressing, BlockTileModel<AFCompressing, BlockTypeMachine<AFCompressing>>,
            ContainerAstralMekanismFactory<AFCompressing>, ItemBlockMachine>> ASTRAL_COMPRESSING_FACTORIES = registerFactories(
            t -> t.nameForAstral + "_astral_compressing_factory", AFCompressing::new, AFCompressing.class, AMELang.DESCRIPTION_ASTRAL_MACHINE,
            t -> builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),AMEUpgrade.RADIOACTIVE_SEALING.getValue(), AMEUpgrade.AIR_INTAKE.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.osmiumCompressor,MAX_SUPPLIER)
                    .withSound(MekanismSounds.OSMIUM_COMPRESSOR));
}
