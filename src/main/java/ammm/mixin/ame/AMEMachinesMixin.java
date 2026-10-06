package ammm.mixin.ame;

import ammm.block.blockentity.mashine.others.AdvancedAirCompressor;
import ammm.registries.TabChangedMachines;
import astral_mekanism.AMELang;
import astral_mekanism.AMETier;
import astral_mekanism.block.blockentity.base.BlockEntityRecipeFactory;
import astral_mekanism.block.container.factory.ContainerAstralMekanismFactory;
import astral_mekanism.registration.BlockTypeMachine;
import astral_mekanism.registration.MachineDeferredRegister;
import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registration.RegistrationInterfaces;
import astral_mekanism.registries.AMEMachines;
import com.fxd927.mekanismelements.common.config.MSConfig;
import mekanism.api.Upgrade;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.tile.base.TileEntityMekanism;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static ammm.registries.TabChangedMachines.ESF;
import static astral_mekanism.registries.AMEMachines.MACHINES;
import static astral_mekanism.registries.AMEMachines.MAX_SUPPLIER;

@Mixin(value = AMEMachines.class, remap = false)
public class AMEMachinesMixin {
    @Inject(method = "<clinit>",at = @At(value = "INVOKE", target = "Lastral_mekanism/registration/MachineDeferredRegister;registerSimple(Ljava/lang/String;Lastral_mekanism/registration/RegistrationInterfaces$BlockEntityConstructor;Ljava/lang/Class;Lmekanism/api/text/ILangEntry;Ljava/util/function/UnaryOperator;)Lastral_mekanism/registration/MachineRegistryObject;",ordinal = 6))
    private static void addApplied(CallbackInfo ci) {
        TabChangedMachines.AppliedMachines();
    }
    @Inject(method = "<clinit>",at = @At(value = "INVOKE", target = "Lastral_mekanism/registration/MachineDeferredRegister;registerSimple(Ljava/lang/String;Lastral_mekanism/registration/RegistrationInterfaces$BlockEntityConstructor;Ljava/lang/Class;Lmekanism/api/text/ILangEntry;Ljava/util/function/UnaryOperator;)Lastral_mekanism/registration/MachineRegistryObject;",ordinal = 49))
    private static void addAstral(CallbackInfo ci) {
        TabChangedMachines.AstralMachines();
    }
    @Inject(method = "<clinit>",at = @At(value = "INVOKE", target = "Lastral_mekanism/registration/MachineDeferredRegister;registerSimple(Ljava/lang/String;Lastral_mekanism/registration/RegistrationInterfaces$BlockEntityConstructor;Ljava/lang/Class;Lmekanism/api/text/ILangEntry;Ljava/util/function/UnaryOperator;)Lastral_mekanism/registration/MachineRegistryObject;",ordinal = 82))
    private static void addEnchanted(CallbackInfo ci) {
        TabChangedMachines.EnchantedMachines();
    }

    @Redirect(method = "<clinit>",at = @At(value = "INVOKE", target = "Lastral_mekanism/registries/AMEMachines;registerFactories(Ljava/util/function/Function;Lastral_mekanism/registration/RegistrationInterfaces$BlockEntityConstructor;Ljava/lang/Class;Lmekanism/api/text/ILangEntry;Ljava/util/function/Function;)Ljava/util/EnumMap;",ordinal =0))
    private static <BE extends BlockEntityRecipeFactory<?, BE>> EnumMap<AMETier, MachineRegistryObject<BE, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerAstralMekanismFactory<BE>, ItemBlockMachine>> hoge(Function<AMETier, String> nameBuilder, RegistrationInterfaces.BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>> constructor, Class<BE> beClass, ILangEntry langEntry, Function<AMETier, UnaryOperator<BlockTypeMachine.BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        return astral_mekanism_more_machine$registerFactories(nameBuilder,constructor,beClass,langEntry,operator);
    }

    @Inject(method = "<clinit>",at = @At(value = "INVOKE", target = "Lastral_mekanism/registries/AMEMachines;registerFactories(Ljava/util/function/Function;Lastral_mekanism/registration/RegistrationInterfaces$BlockEntityConstructor;Ljava/lang/Class;Lmekanism/api/text/ILangEntry;Ljava/util/function/Function;)Ljava/util/EnumMap;",ordinal = 1))
    private static void addEF(CallbackInfo ci) {
        TabChangedMachines.EnchantedFactory();
    }

    @Redirect(method = "<clinit>",at = @At(value = "INVOKE", target = "Lastral_mekanism/registries/AMEMachines;registerFactories(Ljava/util/function/Function;Lastral_mekanism/registration/RegistrationInterfaces$BlockEntityConstructor;Ljava/lang/Class;Lmekanism/api/text/ILangEntry;Ljava/util/function/Function;)Ljava/util/EnumMap;",ordinal =1))
    private static <BE extends BlockEntityRecipeFactory<?, BE>> EnumMap<AMETier, MachineRegistryObject<BE, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerAstralMekanismFactory<BE>, ItemBlockMachine>> huga(Function<AMETier, String> nameBuilder, RegistrationInterfaces.BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>> constructor, Class<BE> beClass, ILangEntry langEntry, Function<AMETier, UnaryOperator<BlockTypeMachine.BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        return astral_mekanism_more_machine$registerFactories(nameBuilder,constructor,beClass,langEntry,operator);
    }

    @Redirect(method = "<clinit>",at = @At(value = "INVOKE", target = "Lastral_mekanism/registration/MachineDeferredRegister;registerSimple(Ljava/lang/String;Lastral_mekanism/registration/RegistrationInterfaces$BlockEntityConstructor;Ljava/lang/Class;Lmekanism/api/text/ILangEntry;Ljava/util/function/UnaryOperator;)Lastral_mekanism/registration/MachineRegistryObject;",ordinal = 57))
    private static <BE extends TileEntityMekanism> MachineRegistryObject<BE, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ItemBlockMachine> hege(MachineDeferredRegister instance, String name, RegistrationInterfaces.BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>> beConstructor, Class<BE> beClass, ILangEntry entry, UnaryOperator<BlockTypeMachine.BlockMachineBuilder<BlockTypeMachine<BE>, BE>> operator) {
        return null;
    }

    @Mutable
    @Final
    @Shadow
    public static MachineRegistryObject<AdvancedAirCompressor, BlockTile.BlockTileModel<AdvancedAirCompressor, BlockTypeMachine<AdvancedAirCompressor>>, MekanismTileContainer<AdvancedAirCompressor>, ItemBlockMachine> ENCHANTED_AIR_COMPRESSOR;

    @Inject(method = "<clinit>",at = @At(value = "INVOKE", target = "Lastral_mekanism/registration/MachineDeferredRegister;registerSimple(Ljava/lang/String;Lastral_mekanism/registration/RegistrationInterfaces$BlockEntityConstructor;Ljava/lang/Class;Lmekanism/api/text/ILangEntry;Ljava/util/function/UnaryOperator;)Lastral_mekanism/registration/MachineRegistryObject;",ordinal = 89))
    private static void hege(CallbackInfo ci){
        ENCHANTED_AIR_COMPRESSOR = MACHINES.registerSimple("advanced_air_compressor", AdvancedAirCompressor::new, AdvancedAirCompressor.class,
                AMELang.DESCRIPTION_ENCHANTED_MACHINE, (builder) -> ((BlockTypeMachine.BlockMachineBuilder)((BlockTypeMachine.BlockMachineBuilder)builder
                .withEnergyConfig(() -> MSConfig.usageConfig.airCompressor.get().multiply(Integer.MAX_VALUE), MAX_SUPPLIER))
                .withSound(MekanismSounds.CHEMICAL_INFUSER)).changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY)));
    }

    @Unique
    private static <BE extends BlockEntityRecipeFactory<?, BE>> EnumMap<AMETier, MachineRegistryObject<BE, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerAstralMekanismFactory<BE>, ItemBlockMachine>> astral_mekanism_more_machine$registerFactories(
            Function<AMETier, String> nameBuilder,
            RegistrationInterfaces.BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            ILangEntry langEntry,
            Function<AMETier, UnaryOperator<BlockTypeMachine.BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        EnumMap<AMETier, MachineRegistryObject<BE, BlockTile.BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerAstralMekanismFactory<BE>, ItemBlockMachine>> result = new EnumMap<>(
                AMETier.class);
        for (AMETier tier : AMETier.values()) {
            result.put(tier, ESF.registerDefaultBlockItem(nameBuilder.apply(tier),
                    constructor, beClass, ContainerAstralMekanismFactory<BE>::new, langEntry,
                    builder -> operator.apply(tier).apply(builder.with(new AttributeTier<>(tier)))));
        }
        return result;
    }
}
