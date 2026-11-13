package ammm.mixin.ame;

import ame_dynamics.registries.AMEDMachines;
import ammm.registries.AMEDMMMachines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
