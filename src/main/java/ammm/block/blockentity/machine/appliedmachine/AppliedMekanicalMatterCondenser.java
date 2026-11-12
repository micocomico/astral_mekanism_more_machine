package ammm.block.blockentity.machine.appliedmachine;

import ammm.block.blockentity.interf.applied.IAppliedDoubleToSingleMachine;
import appeng.api.config.Actionable;
import appeng.api.config.CondenserOutput;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import appeng.core.definitions.AEItems;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.block.blockentity.interf.applied.IAppliedSingleToSingleMachine;
import astral_mekanism.item.recipecard.ChemicalIngredientCardItem;
import astral_mekanism.item.recipecard.FluidIngredientCardItem;
import astral_mekanism.item.recipecard.ItemIngredientCardItem;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableEnum;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.tile.interfaces.IHasMode;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AppliedMekanicalMatterCondenser extends BEAppliedEnergizedMachine
        implements IAppliedDoubleToSingleMachine, IHasMode {

    private BasicInventorySlot cardSlotA;
    private BasicInventorySlot cardSlotB;
    private AEKey inputKeyA;
    private AEKey inputKeyB;
    private AEKey outputKey;
    private long inputAmountA;
    private long inputAmountB;
    private CondenserOutput mode = CondenserOutput.MATTER_BALLS;

    public AppliedMekanicalMatterCondenser(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, 0);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(
                cardSlotA = BasicInventorySlot.at(stack -> stack.getItem() instanceof FluidIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 55, 53));
        builder.addSlot(
                cardSlotB = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 73, 53));
        return builder.build();
    }

    private void removeRecipeInfo() {
        inputKeyA = null;
        inputKeyB = null;
        outputKey = null;
        inputAmountA = 0;
        inputAmountB = 0;
    }

    private void recalculateRecipeInfo() {
        if (cardSlotA.isEmpty() || cardSlotB.isEmpty()) {
            removeRecipeInfo();
            return;
        }
        ItemStack isA = cardSlotA.getStack();
        ItemStack isB = cardSlotB.getStack();
        if (isA.getItem() instanceof FluidIngredientCardItem cardItemA && isB.getItem() instanceof ItemIngredientCardItem cardItemB) {
            AEFluidKey keyA = cardItemA.getKey(isA);
            AEItemKey keyB = cardItemB.getKey(isB);
            if ((keyA != null || keyB != null) && hasLevel()) {
                if(mode == CondenserOutput.MATTER_BALLS){
                    inputKeyA = keyA;
                    inputKeyB = keyB;
                    inputAmountA = 32000;
                    inputAmountB = 256;
                    outputKey = AEItemKey.of(AEItems.MATTER_BALL);
                } else if(mode == CondenserOutput.SINGULARITY){
                    inputKeyA = keyA;
                    inputKeyB = keyB;
                    inputAmountA = 32000000;
                    inputAmountB = 256000;
                    outputKey = AEItemKey.of(AEItems.SINGULARITY);
                } else if(mode == CondenserOutput.TRASH){
                    inputKeyA = keyA;
                    inputKeyB = keyA;
                    inputAmountA = 1;
                    inputAmountB = 1;
                    outputKey = null;
                }
                return;
            }
        }
        removeRecipeInfo();
        return;
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        MEStorage storage = getMeStorage();
        if (MekanismUtils.canFunction(this) && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operationsA = 0;
            long operationsB = 0;
            if (inputKeyA != null) {
                operationsA = Math.min(
                        storage.extract(inputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountA,
                        outputKey != null ? storage.insert(outputKey, Long.MAX_VALUE, Actionable.SIMULATE, source) : Long.MAX_VALUE);
            }
            if (inputKeyB != null) {
                operationsB = Math.min(
                        storage.extract(inputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountB,
                        outputKey != null ? storage.insert(outputKey, Long.MAX_VALUE, Actionable.SIMULATE, source) : Long.MAX_VALUE);
            }
            if (operationsA + operationsB > 0) {
                if (operationsA > 0) storage.extract(inputKeyA, operationsA * inputAmountA, Actionable.MODULATE, source);
                if (operationsB > 0) storage.extract(inputKeyB, operationsB * inputAmountB, Actionable.MODULATE, source);
                if(outputKey != null) storage.insert(outputKey, operationsA + operationsB, Actionable.MODULATE, source);
                setActive(true);
                return;
            }
        }
        setActive(false);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableEnum.create(i -> CondenserOutput.values()[i], CondenserOutput.MATTER_BALLS,
                () -> mode, v -> mode = v));
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        try {
            mode = CondenserOutput.valueOf(nbt.getString("mode"));
        } catch (Exception e) {
            mode = CondenserOutput.MATTER_BALLS;
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putString("mode", mode.toString());
    }

    public void changeMode(boolean direction) {
        int p = direction ? 1 : -1;
        CondenserOutput[] values = CondenserOutput.values();
        mode = values[(mode.ordinal() + p) % values.length];
    }

    @Override
    public void nextMode() {
        changeMode(true);
    }

    @Override
    public void previousMode() {
        changeMode(false);
    }

    public AEKey getInputKeyA() {
        return inputKeyA;
    }

    public AEKey getInputKeyB() {
        return inputKeyB;
    }

    public AEKey getOutputKey() {
        return outputKey;
    }

    public CondenserOutput getMode() {
        return mode;
    }

}
