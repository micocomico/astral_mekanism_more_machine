package ammm.registries;

import ame_dynamics.registries.AMEDMachines;
import ame_the_valine.registries.AMETheValineMachines;
import ammm.block.blockentity.machine.appliedmachine.AppliedDryingBasin;
import ammm.block.blockentity.machine.appliedmachine.AppliedValineReactor;
import astral_mekanism.AMELang;
import astral_mekanism.registration.MachineRegistryObject;
import mekanism.api.Upgrade;
import mekanism.common.inventory.container.tile.MekanismTileContainer;

import java.util.EnumSet;

public class AMETheValineMMMachines {
    public static MachineRegistryObject<AppliedValineReactor, ?, MekanismTileContainer<AppliedValineReactor>, ?> APPLIED_VALINE_REACTOR;

    public static void addValineReactor(){
        APPLIED_VALINE_REACTOR = AMETheValineMachines.MACHINES.registerSimple("applied_valine_reactor", AppliedValineReactor::new, AppliedValineReactor.class,
                AMELang.DESCRIPTION_APPLIED_MACHINE,builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY)));
    }
}
