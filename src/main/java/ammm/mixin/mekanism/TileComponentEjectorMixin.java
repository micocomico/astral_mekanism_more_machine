package ammm.mixin.mekanism;

import ammm.EjectTransitRequest;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.text.EnumColor;
import mekanism.common.lib.inventory.TileTransitRequest;
import mekanism.common.lib.inventory.TransitRequest;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.ISlotInfo;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.transmitter.TileEntityLogisticalTransporterBase;
import mekanism.common.util.InventoryUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ammm.AMMM.LOGGER;

@Mixin(value = TileComponentEjector.class, remap = false)
public abstract class TileComponentEjectorMixin {

    @Shadow
    private int tickDelay;

    @Shadow
    @Final
    private TileEntityMekanism tile;

    @Shadow
    private EnumColor outputColor;


    @Inject(method = "outputItems", at = @At("HEAD"))
    private void OverclockedOutputItemsInject(ConfigInfo info, CallbackInfo ci) {
        for (DataType dataType : info.getSupportedDataTypes()) {
            if (!dataType.canOutput()) {
                continue;
            }
            ISlotInfo slotInfo = info.getSlotInfo(dataType);
            if (slotInfo instanceof InventorySlotInfo inventorySlotInfo) {
                Set<Direction> outputs = info.getSidesForData(dataType);
                if (!outputs.isEmpty()) {
                    for (IInventorySlot slot : inventorySlotInfo.getSlots()) {
                        EjectTransitRequest ejectMap = InventoryUtils.getEjectItemMap(new EjectTransitRequest(tile, outputs.iterator().next()), List.of(slot));
                        if (!ejectMap.isEmpty()) {
                            for (Direction side : outputs) {
                                BlockEntity target = WorldUtils.getTileEntity(tile.getLevel(), tile.getBlockPos().relative(side));
                                if (target != null) {
                                    ejectMap.side = side;
                                    TransitRequest.TransitResponse response;
                                    if (target instanceof TileEntityLogisticalTransporterBase transporter) {
                                        response = transporter.getTransmitter().insert(tile, ejectMap, outputColor, true, 0);
                                    } else {
                                        response = ejectMap.addToInventory(target, side, 0, false);
                                    }
                                    if (!response.isEmpty()) {
                                        response.useAll();
                                        if (ejectMap.isEmpty()) {
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        tickDelay = 0;
    }

}
