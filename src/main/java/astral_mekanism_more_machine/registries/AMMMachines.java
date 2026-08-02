package astral_mekanism_more_machine.registries;

import astral_mekanism.AMELang;
import astral_mekanism_more_machine.block.container.normalmachine.ContainerAstralCrafter;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism_more_machine.registration.BlockTypeMachine;
import astral_mekanism_more_machine.registration.MachineRegistryObject;
import astral_mekanism_more_machine.AMMMConstants;
import astral_mekanism_more_machine.block.blockentity.normalmachine.AstralCrafter;
import astral_mekanism_more_machine.registration.MachineDeferredRegister;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.item.block.machine.ItemBlockMachine;

import java.util.EnumSet;

public class AMMMachines {

    public static final MachineDeferredRegister MACHINES = new MachineDeferredRegister(AMMMConstants.MODID);

    public static final MachineRegistryObject<AstralCrafter, BlockTile.BlockTileModel<AstralCrafter, BlockTypeMachine<AstralCrafter>>,
            ContainerAstralCrafter, ItemBlockMachine> ASTRAL_CRAFTER = MACHINES.registerDefaultBlockItem("astral_crafter",
                    AstralCrafter::new,
                    AstralCrafter.class,
                    ContainerAstralCrafter::new,
                    AMELang.DESCRIPTION_ESSENTIAL_CRAFTER,
                    builder -> builder
                            .withEnergyConfig(AMEConfig.usage.essentialCrafter, AMEConfig.storage.essentialCrafter)
                            .changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue())));
}
