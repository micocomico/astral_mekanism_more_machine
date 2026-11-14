package ammm.block.blockentity.machine.appliedmachine;

import ame_the_valine.recipe.ValineReactorBaseRecipe;
import ame_the_valine.registries.AMETheValineRecipeTypes;
import ammm.block.blockentity.interf.applied.IAppliedDoubleToDoubleMachine;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.item.recipecard.ChemicalIngredientCardItem;
import astral_mekanism.util.AMEKeyUtils;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AppliedValineReactor extends BEAppliedEnergizedMachine
        implements IAppliedDoubleToDoubleMachine {

    private BasicInventorySlot cardSlotA;
    private BasicInventorySlot cardSlotB;
    private AEKey inputKeyA;
    private AEKey inputKeyB;
    private AEKey outputKeyA;
    private AEKey outputKeyB;
    private long inputAmountA;
    private long inputAmountB;
    private long outputAmountA;
    private long outputAmountB;
    private long outputAmountC;

    public AppliedValineReactor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, 0);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(
                cardSlotA = BasicInventorySlot.at(stack -> stack.getItem() instanceof ChemicalIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 55, 53));
        builder.addSlot(
                cardSlotB = BasicInventorySlot.at(stack -> stack.getItem() instanceof ChemicalIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 73, 53));
        return builder.build();
    }

    private void removeRecipeInfo() {
        inputKeyA = null;
        inputKeyB = null;
        outputKeyA = null;
        outputKeyB = null;
        inputAmountA = 0;
        inputAmountB = 0;
        outputAmountA = 0;
        outputAmountB = 0;
        outputAmountC = 0;
    }

    private void recalculateRecipeInfo() {
        if (cardSlotA.isEmpty() || cardSlotB.isEmpty()) {
            removeRecipeInfo();
            return;
        }
        ItemStack isA = cardSlotA.getStack();
        ItemStack isB = cardSlotB.getStack();
        if (isA.getItem() instanceof ChemicalIngredientCardItem cardItemA && isB.getItem() instanceof ChemicalIngredientCardItem cardItemB) {
            MekanismKey keyA = cardItemA.getKey(isA);
            MekanismKey keyB = cardItemB.getKey(isB);
            if (keyA != null && keyB != null && hasLevel()) {
                GasStack stackA = AMEKeyUtils.getGas(keyA);
                GasStack stackB = AMEKeyUtils.getGas(keyB);
                stackA.setAmount(Long.MAX_VALUE);
                stackB.setAmount(Long.MAX_VALUE);
                level.getRecipeManager().getAllRecipesFor(getRecipeType().getRecipeType())
                        .stream().filter(r -> r.test(stackA,stackB)).findFirst()
                        .ifPresentOrElse(r -> {
                            inputKeyA = keyA;
                            inputKeyB = keyB;
                            inputAmountA = r.getInputA().getNeededAmount(stackA);
                            inputAmountB = r.getInputB().getNeededAmount(stackB);
                            GasStack outputA = r.getOutputA();
                            GasStack outputB = r.getOutputB();
                            outputKeyA = MekanismKey.of(outputA);
                            outputKeyB = MekanismKey.of(outputB);
                            outputAmountA = outputA.getAmount();
                            outputAmountB = outputB.getAmount();
                            outputAmountC = r.getOutputEnergy().divideToLong(MekanismConfig.general.forgeConversionRate.get());
                        }, this::removeRecipeInfo);
                return;
            }
        }
        removeRecipeInfo();
        return;
    }

    protected IMekanismRecipeTypeProvider<ValineReactorBaseRecipe, ?> getRecipeType(){
        return AMETheValineRecipeTypes.VALINE_REACTOR;
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        MEStorage storage = getMeStorage();
        if (MekanismUtils.canFunction(this) && inputAmountA > 0 && inputAmountB > 0 && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = Math.min(
                    Math.min(storage.extract(inputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountA,
                             storage.extract(inputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountB),
                    Math.min(storage.insert(outputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountA,
                             storage.insert(outputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountB));
            operations = Math.min(operations, storage.insert(this.feKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountC);
            if (operations > 0) {
                storage.extract(inputKeyA, operations * inputAmountA, Actionable.MODULATE, source);
                storage.extract(inputKeyB, operations * inputAmountB, Actionable.MODULATE, source);
                storage.insert(outputKeyA, operations * outputAmountA, Actionable.MODULATE, source);
                storage.insert(outputKeyB, operations * outputAmountB, Actionable.MODULATE, source);
                storage.insert(this.feKey, operations * outputAmountC, Actionable.MODULATE, source);
                setActive(true);
                return;
            }
        }
        setActive(false);
    }

    public AEKey getInputKeyA() {
        return inputKeyA;
    }

    public AEKey getInputKeyB() {
        return inputKeyB;
    }

    public AEKey getOutputKeyA() {
        return outputKeyA;
    }

    public AEKey getOutputKeyB() {
        return outputKeyB;
    }
}
