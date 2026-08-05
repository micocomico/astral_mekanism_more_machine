package ammm.registries;

import ammm.AMMMConstants;
import ammm.AMMMLang;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registration.impl.CreativeTabRegistryObject;

public class AMMMCreativeTab {
    public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(
            AMMMConstants.MODID);

    public static final CreativeTabRegistryObject ASTRAL_MEKANISM_MORE_MACHINE_TAB = CREATIVE_TABS.register("astral_mekanism_more_machine_tab",
            AMMMLang.ITEM_GROUP, AMMMachines.ASTRAL_CRAFTER,
            builder -> builder.displayItems((displayParameters, output) -> {
                CreativeTabDeferredRegister.addToDisplay(AMMMachines.MACHINES.blockRegister, output);
            }));
}
