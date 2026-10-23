package ammm.mixin.ame;

import astral_mekanism.AMEClient;
import astral_mekanism.registration.MachineRegistryObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AMEClient.class,remap = false)
public class AMEClientMixin {
    @WrapOperation(method = "initScreens",at = @At(value = "INVOKE", target = "Lastral_mekanism/AMEClient;registerScreenMek(Lastral_mekanism/registration/MachineRegistryObject;Lnet/minecraft/client/gui/screens/MenuScreens$ScreenConstructor;)V",ordinal = 59))
    private static <BE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<BE>, U extends Screen & MenuAccess<CONTAINER>> void registerScreenMek(MachineRegistryObject<BE, ?, ? extends CONTAINER, ?> registryObject, MenuScreens.ScreenConstructor<CONTAINER, U> constructor, Operation<Void> original) {

    }
}
