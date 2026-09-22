package ammm.block.blockentity.appliedmachine;

import ammm.block.blockentity.interf.applied.IAppliedDoubleToSingleMachine;
import ammm.block.blockentity.interf.applied.IAppliedSingleToDoubleMachine;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.item.recipecard.ChemicalIngredientCardItem;
import astral_mekanism.item.recipecard.ItemIngredientCardItem;
import astral_mekanism.recipes.cachedRecipe.FormulizedSawingCachedRecipe;
import astral_mekanism.util.AMEKeyUtils;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.SawmillRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AppliedPrecisionSawmill extends BEAppliedEnergizedMachine
        implements IAppliedSingleToDoubleMachine {

    private BasicInventorySlot cardSlot;
    private AEKey inputKey;
    private AEKey outputKeyA;
    private AEKey outputKeyB;
    private long inputAmount;
    private long outputAmountA;
    private long outputAmountB;

    public AppliedPrecisionSawmill(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, MekanismConfig.usage.precisionSawmill.get().multiply(200)
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(
                cardSlot = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 64, 53));
        return builder.build();
    }

    private void removeRecipeInfo() {
        inputKey = null;
        outputKeyA = null;
        outputKeyB = null;
        inputAmount = 0;
        outputAmountA = 0;
        outputAmountB = 0;
    }

    private void recalculateRecipeInfo() {
        if (cardSlot.isEmpty()) {
            removeRecipeInfo();
            return;
        }
        ItemStack is = cardSlot.getStack();
        if (is.getItem() instanceof ItemIngredientCardItem cardItem) {
            AEItemKey key = cardItem.getKey(is);
            if (key != null && hasLevel()) {
                ItemStack stack = key.toStack(0x3fffffff);
                level.getRecipeManager().getAllRecipesFor(getRecipeType().getRecipeType())
                        .stream().filter(r -> r.test(stack)).findFirst()
                        .ifPresentOrElse(r -> {
                            double chance = r.getSecondaryChance();
                            long multi = chance > 0 ? (long) Math.ceil(1 / r.getSecondaryChance()) : 1;
                            inputKey = key;
                            inputAmount = r.getInput().getNeededAmount(stack) * multi;
                            SawmillRecipe.ChanceOutput output = r.getOutput(stack);
                            outputKeyA = AEItemKey.of(output.getMainOutput());
                            outputKeyB = AEItemKey.of(output.getMaxSecondaryOutput());
                            outputAmountA = output.getMainOutput().getCount() * multi;
                            outputAmountB = output.getMaxSecondaryOutput().getCount();
                        }, this::removeRecipeInfo);
                return;
            }
        }
        removeRecipeInfo();
        return;
    }

    public IMekanismRecipeTypeProvider<SawmillRecipe, InputRecipeCache.SingleItem<SawmillRecipe>> getRecipeType() {
        return MekanismRecipeType.SAWING;
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        MEStorage storage = getMeStorage();
        if (MekanismUtils.canFunction(this) && inputAmount > 0 && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = Math.min(Math.min(
                    storage.extract(inputKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmount,
                    storage.insert(outputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountA),
                    outputKeyB != null ? storage.insert(outputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountB : Long.MAX_VALUE);
            operations = Math.min(operations, getSupportableOperations(storage, source));
            if (operations > 0) {
                storage.extract(inputKey, operations * inputAmount, Actionable.MODULATE, source);
                storage.insert(outputKeyA, operations * outputAmountA, Actionable.MODULATE, source);
                storage.insert(outputKeyB, operations * outputAmountB, Actionable.MODULATE, source);
                consumeEnergy(storage, source, operations);
                setActive(true);
                return;
            }
        }
        setActive(false);
    }

    public AEKey getInputKey() {
        return inputKey;
    }

    public AEKey getOutputKeyA() {
        return outputKeyA;
    }

    public AEKey getOutputKeyB() {
        return outputKeyB;
    }

}
