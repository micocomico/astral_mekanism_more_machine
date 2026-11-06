package ammm.registries;

import ammm.AMEMMConstants;
import ammm.AMEMMLang;
import astral_mekanism.registries.AMEItems;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registration.impl.CreativeTabRegistryObject;

public class AMEMMCreativeTab {
    public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(AMEMMConstants.MODID);

    public static final CreativeTabRegistryObject ASTRAL_MEKANISM_MORE_MACHINE_TAB = CREATIVE_TABS.register("astral_mekanism_more_machine_tab",
            AMEMMLang.FACTORIES_TAB_TITLE, AMEItems.STARDUST_ALLOY,
            builder -> builder.displayItems((displayParameters, output) -> {
                CreativeTabDeferredRegister.addToDisplay(AMEMMMachines.ESF.blockRegister, output);
                CreativeTabDeferredRegister.addToDisplay(AMEMMFactories.MACHINES.blockRegister, output);
            }));
}
