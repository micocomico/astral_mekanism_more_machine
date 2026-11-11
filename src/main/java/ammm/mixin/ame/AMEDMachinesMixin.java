package ammm.mixin.ame;

import ame_dynamics.registries.AMEDMachines;
import ammm.registries.AMEDMMMachines;
import ammm.registries.AMEMMMachines;
import astral_mekanism.AMETier;
import astral_mekanism.block.blockentity.base.BlockEntityRecipeFactory;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedAirCompressor;
import astral_mekanism.block.container.factory.ContainerAstralMekanismFactory;
import astral_mekanism.registration.BlockTypeMachine;
import astral_mekanism.registration.MachineDeferredRegister;
import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registration.RegistrationInterfaces;
import astral_mekanism.registries.AMEMachines;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumMap;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static ammm.registries.AMEMMMachines.ESF;

@Mixin(value = AMEDMachines.class, remap = false)
public class AMEDMachinesMixin {
    @Inject(method = "<clinit>",at = @At(value = "INVOKE", target = "Lastral_mekanism/registration/MachineDeferredRegister;registerSimple(Ljava/lang/String;Lastral_mekanism/registration/RegistrationInterfaces$BlockEntityConstructor;Ljava/lang/Class;Lmekanism/api/text/ILangEntry;Ljava/util/function/UnaryOperator;)Lastral_mekanism/registration/MachineRegistryObject;",ordinal = 3))
    private static void addSqueezer(CallbackInfo ci) {
        AMEDMMMachines.Squeezer();
    }

    @Inject(method = "<clinit>",at = @At(value = "TAIL"))
    private static void addDryingBasin(CallbackInfo ci) {
        AMEDMMMachines.DryingBasin();
    }
}
