package ammm.mixin.mekanism;

import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumMap;
import java.util.Map;

@Mixin(value = TileComponentEjector.class, remap = false)
public abstract class TileComponentEjectorMixin {
    @Shadow
    @Final
    @Mutable
    private int tickDelay;

    @Shadow
    private final Map<TransmissionType, ConfigInfo> configInfo =  new EnumMap<>(TransmissionType.class);

    @Shadow
    public abstract boolean isEjecting(ConfigInfo info, TransmissionType type);

    @Shadow
    protected abstract void outputItems(ConfigInfo info);

    @Shadow
    protected abstract void eject(TransmissionType type, ConfigInfo info);

    @Inject(method = "tickServer", at = @At("HEAD"))
    private void OverclockedOutputItemsInject(CallbackInfo ci) {
        for (Map.Entry<TransmissionType, ConfigInfo> entry : configInfo.entrySet()) {
            TransmissionType type = entry.getKey();
            ConfigInfo info = entry.getValue();
            if (isEjecting(info, type)) {
                if (type == TransmissionType.ITEM) {
                    for (int i = 0; i < 255; i++) {
                        outputItems(info);
                    }
                } else if (type != TransmissionType.HEAT) {
                    eject(type, info);
                }
            }
        }
    }

}
