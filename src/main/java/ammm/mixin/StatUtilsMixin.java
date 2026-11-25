package ammm.mixin;

import mekanism.api.math.MathUtils;
import mekanism.common.util.StatUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StatUtils.class,remap = false)
public class StatUtilsMixin {
    @Inject(method = "inversePoisson",at = @At("HEAD"), cancellable = true)
    private static void inversePoisson(double mean, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(MathUtils.clampToInt(mean));
    }
}
