package ammm.mixin.ame;

import ame_dynamics.registries.AMEDMachines;
import ame_the_valine.registries.AMETheValineMachines;
import ammm.registries.AMEDMMMachines;
import ammm.registries.AMETheValineMMMachines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AMETheValineMachines.class, remap = false)
public class AMETheValineMachinesMixin {
    @Inject(method = "<clinit>",at = @At(value = "TAIL"))
    private static void addDryingBasin(CallbackInfo ci) {
        AMETheValineMMMachines.addValineReactor();
    }
}
