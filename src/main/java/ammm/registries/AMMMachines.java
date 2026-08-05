package ammm.registries;

import ammm.AMMMLang;
import ammm.block.container.normalmachine.ContainerAstralCrafter;
import astral_mekanism.enums.AMEUpgrade;
import ammm.config.AMMMConfig;
import ammm.registration.BlockTypeMachine;
import ammm.registration.MachineRegistryObject;
import ammm.AMMMConstants;
import ammm.block.blockentity.normalmachine.AstralCrafter;
import ammm.registration.MachineDeferredRegister;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.item.block.machine.ItemBlockMachine;

import java.util.EnumSet;

public class AMMMachines {

    public static final MachineDeferredRegister MACHINES = new MachineDeferredRegister(AMMMConstants.MODID);

    public static final MachineRegistryObject<AstralCrafter, BlockTileModel<AstralCrafter, BlockTypeMachine<AstralCrafter>>,
            ContainerAstralCrafter, ItemBlockMachine> ASTRAL_CRAFTER = MACHINES.registerDefaultBlockItem("astral_crafter",
                    AstralCrafter::new,
                    AstralCrafter.class,
                    ContainerAstralCrafter::new,
                    AMMMLang.DESCRIPTION_ASTRAL_CRAFTER,
                    builder -> builder
                            .withEnergyConfig(AMMMConfig.usage.astralCrafter, AMMMConfig.storage.astralCrafter)
                            .changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue())));
}
