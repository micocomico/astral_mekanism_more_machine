package ammm.registries;

import ame_dynamics.registries.AMEDMachines;
import ammm.block.blockentity.machine.appliedmachine.AppliedDryingBasin;
import ammm.block.blockentity.machine.appliedmachine.AppliedSqueezer;
import astral_mekanism.AMELang;
import astral_mekanism.registration.MachineRegistryObject;
import mekanism.api.Upgrade;
import mekanism.common.inventory.container.tile.MekanismTileContainer;

import java.util.EnumSet;

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
