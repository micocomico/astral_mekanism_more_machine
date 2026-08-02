package astral_mekanism_more_machine.registries;

import astral_mekanism.AMELang;
import astral_mekanism.block.blockentity.normalmachine.BEAstralCrafter;
import astral_mekanism.block.container.normalmachine.ContainerAstralCrafter;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.registration.BlockTypeMachine;
import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism_more_machine.block.blockentity.astralmachine.AstralCrafter;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.item.block.machine.ItemBlockMachine;

import java.util.EnumSet;

import static astral_mekanism.registries.AMEMachines.MACHINES;

public class AMMMachines {
    public static final MachineRegistryObject<BEAstralCrafter, BlockTile.BlockTileModel<BEAstralCrafter, BlockTypeMachine<BEAstralCrafter>>,
            ContainerAstralCrafter, ItemBlockMachine> ASTRAL_CRAFTER = MACHINES.registerDefaultBlockItem("astral_crafter",
                    AstralCrafter::new,
                    BEAstralCrafter.class,
                    ContainerAstralCrafter::new,
                    AMELang.DESCRIPTION_ESSENTIAL_CRAFTER,
                    builder -> builder
                            .withEnergyConfig(AMEConfig.usage.essentialCrafter, AMEConfig.storage.essentialCrafter)
                            .changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue())));
}
