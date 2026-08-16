package ammm.registries;

import ammm.AMMMConstants;
import ammm.AMMMLang;
import ammm.block.blockentity.astralmachine.AstralCrafter;
import ammm.block.blockentity.enchantedfactory.EnchantedCrushingFactory;
import astral_mekanism.block.blockentity.base.BlockEntityRecipeFactory;
import ammm.block.blockentity.enchantedfactory.EnchantedEnergizedSmeltingFactory;
import ammm.block.blockentity.enchantedmachine.EnchantedCrafter;
import astral_mekanism.block.container.factory.ContainerAstralMekanismFactory;
import ammm.block.container.machine.ContainerCrafter;
import ammm.config.AMMMConfig;
import ammm.registration.BlockTypeMachine;
import ammm.registration.MachineDeferredRegister;
import ammm.registration.MachineRegistryObject;
import astral_mekanism.AMELang;
import astral_mekanism.AMETier;
import astral_mekanism.block.container.prefab.ContainerPagedMachine;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.registration.RegistrationInterfaces;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import mekanism.api.Upgrade;
import mekanism.api.math.FloatingLong;
import mekanism.api.math.FloatingLongSupplier;
import mekanism.api.text.ILangEntry;
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

    public static final MachineRegistryObject<AstralCrafter, BlockTileModel<AstralCrafter, BlockTypeMachine<AstralCrafter>>,
            ContainerCrafter<AstralCrafter>, ItemBlockMachine> ASTRAL_CRAFTER = MACHINES.registerDefaultBlockItem("astral_crafter",
                    AstralCrafter::new,
                    AstralCrafter.class,
                    ContainerCrafter<AstralCrafter>::new,
                    AMMMLang.DESCRIPTION_ASTRAL_CRAFTER,
                    builder -> builder
                            .withEnergyConfig(AMMMConfig.usage.astralCrafter, MAX_SUPPLIER)
                            .changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue())));

    public static final MachineRegistryObject<EnchantedCrafter, BlockTileModel<EnchantedCrafter, BlockTypeMachine<EnchantedCrafter>>,
            ContainerCrafter<EnchantedCrafter>, ItemBlockMachine> ENCHANTED_CRAFTER = MACHINES.registerDefaultBlockItem("enchanted_crafter",
                    EnchantedCrafter::new,
                    EnchantedCrafter.class,
                    ContainerCrafter<EnchantedCrafter>::new,
                    AMMMLang.DESCRIPTION_ENCHANTED_CRAFTER,
                    builder -> builder
                            .withEnergyConfig(() -> AMEConfig.usage.essentialCrafter.get().multiply(200)
                                    ,() -> AMEConfig.storage.essentialCrafter.get().multiply(12800))
                            .changeAttributeUpgrade(EnumSet.of(
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    Upgrade.ENERGY,Upgrade.SPEED,ExtraUpgrade.STACK)));

    public static final EnumMap<AMETier, MachineRegistryObject<EnchantedCrushingFactory, BlockTileModel<EnchantedCrushingFactory, BlockTypeMachine<EnchantedCrushingFactory>>,
            ContainerAstralMekanismFactory<EnchantedCrushingFactory>, ItemBlockMachine>> ENCHANTED_CRUSHING_FACTRIES = registerAMEFactories(
            t -> t.nameForNormal + "_enchanted_energized_smelting_factory",
            EnchantedCrushingFactory::new,
            EnchantedCrushingFactory.class,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE,
            t -> builder -> builder
                    .changeAttributeUpgrade(
                            EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                    .withSound(MekanismSounds.ENERGIZED_SMELTER)
                    .withEnergyConfig(() -> MekanismConfig.usage.crusher.get().multiply(200),
                            () -> MekanismConfig.storage.crusher.get().multiply(t.processes * 12800)));

    public static final EnumMap<AMETier, MachineRegistryObject<EnchantedEnergizedSmeltingFactory, BlockTileModel<EnchantedEnergizedSmeltingFactory, BlockTypeMachine<EnchantedEnergizedSmeltingFactory>>,
            ContainerAstralMekanismFactory<EnchantedEnergizedSmeltingFactory>, ItemBlockMachine>> ENCHANTED_ENERGIZED_SMELTING_FACTRIES = registerAMEFactories(
            t -> t.nameForNormal + "_enchanted_energized_smelting_factory",
            EnchantedEnergizedSmeltingFactory::new,
            EnchantedEnergizedSmeltingFactory.class,
            AMELang.DESCRIPTION_ENCHANTED_MACHINE,
            t -> builder -> builder
                    .changeAttributeUpgrade(
                            EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                    AMEUpgrade.XP.getValue()))
                    .withSound(MekanismSounds.ENERGIZED_SMELTER)
                    .withEnergyConfig(() -> MekanismConfig.usage.energizedSmelter.get().multiply(200),
                            () -> MekanismConfig.storage.energizedSmelter.get().multiply(t.processes * 12800)));

}
