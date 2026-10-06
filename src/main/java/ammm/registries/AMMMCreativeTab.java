package ammm.registries;

import ammm.AMMMConstants;
import ammm.AMMMLang;
import astral_mekanism.registries.AMEItems;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registration.impl.CreativeTabRegistryObject;

public class AMMMCreativeTab {
    public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(AMMMConstants.MODID);

    public static final CreativeTabRegistryObject ASTRAL_MEKANISM_MORE_MACHINE_TAB = CREATIVE_TABS.register("astral_mekanism_more_machine_tab",
            AMMMLang.TABTITLE, AMEItems.STARDUST_ALLOY,
            builder -> builder.displayItems((displayParameters, output) -> {
                CreativeTabDeferredRegister.addToDisplay(TabChangedMachines.ESF.blockRegister, output);
                CreativeTabDeferredRegister.addToDisplay(AMMMachines.MACHINES.blockRegister, output);
            }));
}
