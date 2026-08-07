package ammm.registries;

import ammm.AMMMLang;
import ammm.block.blockentity.enchantedmachine.EnchantedCrafter;
import ammm.block.container.ContainerAstralCrafter;
import ammm.block.container.ContainerEnchantedCrafter;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.enums.AMEUpgrade;
import ammm.config.AMMMConfig;
import ammm.registration.BlockTypeMachine;
import ammm.registration.MachineRegistryObject;
import ammm.AMMMConstants;
import ammm.block.blockentity.astralmachine.AstralCrafter;
import ammm.registration.MachineDeferredRegister;
import mekanism.api.Upgrade;
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

    public static final MachineRegistryObject<EnchantedCrafter, BlockTileModel<EnchantedCrafter, BlockTypeMachine<EnchantedCrafter>>,
            ContainerEnchantedCrafter, ItemBlockMachine> ENCHANTED_CRAFTER = MACHINES.registerDefaultBlockItem("enchanted_crafter",
                    EnchantedCrafter::new,
                    EnchantedCrafter.class,
                    ContainerEnchantedCrafter::new,
                    AMMMLang.DESCRIPTION_ENCHANTED_CRAFTER,
                    builder -> builder
                            .withEnergyConfig(() -> AMEConfig.usage.essentialCrafter.get().multiply(200)
                                    ,() -> AMEConfig.storage.essentialCrafter.get().multiply(12800))
                            .changeAttributeUpgrade(EnumSet.of(
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    Upgrade.ENERGY,Upgrade.SPEED
                            )));
}
