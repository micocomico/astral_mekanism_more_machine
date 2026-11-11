package ammm.registries;

import ame_dynamics.registries.AMEDMachines;
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

public class AMEDMMMachines {
    public static MachineRegistryObject<AppliedDryingBasin, ?, MekanismTileContainer<AppliedDryingBasin>, ?> APPLIED_DRYING_BASIN;
    public static MachineRegistryObject<AppliedSqueezer, ?, MekanismTileContainer<AppliedSqueezer>, ?> APPLIED_SQUEEZER;

    public static void Squeezer(){
        APPLIED_SQUEEZER = AMEDMachines.MACHINES.registerSimple("applied_squeezer", AppliedSqueezer::new, AppliedSqueezer.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
    }

    public static void DryingBasin(){
        APPLIED_DRYING_BASIN = AMEDMachines.MACHINES.registerSimple("applied_drying_basin", AppliedDryingBasin::new, AppliedDryingBasin.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)).withCustomShape(AMEDMachines.SHAPES));
    }
}
